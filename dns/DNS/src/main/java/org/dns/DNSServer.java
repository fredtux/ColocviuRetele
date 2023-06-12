package org.dns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

public class DNSServer {
    private ObjectMapper mapper;
    private TypeFactory typeFactory;
    private ArrayList<Object> zones;
    private DatagramSocket dnsSocket;
    private Lock lock = new ReentrantLock();

    public DNSServer(){
        // Initialize variables
        this.mapper = new ObjectMapper();
        this.typeFactory = mapper.getTypeFactory();
        this.zones = new ArrayList<>();

        // Read zones from resource zones.json
        try {
            // Zone file source of inspiration: https://github.com/howCodeORG/howDNS
            this.zones = this.mapper.readValue(getClass().getResourceAsStream("/zones.json"), typeFactory.constructCollectionType(ArrayList.class, Object.class));
        } catch(Exception e){
            System.out.println("Can't read zones: " + e);
            System.exit(1);
        }

        // DNS Server
        try {
            // Start socket on port 53 accept 10 connections from 0.0.0.0
            this.dnsSocket = new DatagramSocket(53, InetAddress.getByName("0.0.0.0"));
            byte[] buffer = new byte[65536];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            System.out.println("DNS Server started on port 53");

            while(true){
                // Receive DNS requests
                this.dnsSocket.receive(request);

                // Start new thread to process request
                new Thread(() -> {
                    this.processRequest(request);
                }).start();
            }
        } catch(Exception e){
            System.out.println("Can't open server socket: " + e);
            System.exit(1);
        }
    }

    private void processRequest(DatagramPacket request){
        // Handle DNS requests
        byte[] requestData = request.getData();
        int requestDataLength = request.getLength();

        // DNS Response
        try{
            byte[] responseData = this.handleDNSData(requestData, requestDataLength, request, false);
            if(responseData == null)
                responseData = this.handleDNSData(requestData, requestDataLength, request, true);

            DatagramPacket response = new DatagramPacket(responseData, responseData.length, request.getAddress(), request.getPort());

            // Send DNS response
            try{
                lock.lock();
                this.dnsSocket.send(response);
                lock.unlock();
            } catch(Exception e){
                System.out.println("Can't send response: " + e);
            }
        } catch(Exception e){
            System.out.println("Can't handle DNS request: " + e);
        } finally {
            System.out.println("Killing child...");
        }
    }

    private byte[] handleDNSData(byte[] requestData, int requestDataLength, DatagramPacket request, boolean unfound) throws Exception{
        DNSRequest dnsRequest = new DNSRequest(requestData, requestDataLength);
        String domain = dnsRequest.getDomain();
        if(unfound){
            return this.craftDNSPacket(requestData, requestDataLength, request, null, domain);
        }
        System.out.println("Searching for domain: " + domain);

        // Check if domain is in zones
        for(Object zone : this.zones){
            Zone z = this.mapper.convertValue(zone, Zone.class);
            if(domain.equals(z.$origin)){
                if(z.a != null){
                    return this.craftDNSPacket(requestData, requestDataLength, request, z, domain);
                }
            }
        }

        return null;
    }

    private byte[] craftDNSPacket(byte[] requestData, int requestDataLength, DatagramPacket request, Zone z, String domain) throws Exception{
        // Create a new DNS message
        Message dnsMessage = new Message();

        // Set the ID field of the DNS header
        Header header = dnsMessage.getHeader();

        // Create the question section
        String domainName = domain;
        int recordType = Type.A;
        int recordClass = DClass.IN;
        Record question = Record.newRecord(Name.fromString(domainName), recordType, recordClass);
        dnsMessage.addRecord(question, Section.QUESTION);

        // Create the answer section
        String ipAddress;
        if(z == null) {
            ipAddress = "0.0.0.0";
            header.setRcode(Rcode.NXDOMAIN);
        }
        else {
            ipAddress = z.a.get(0).value;
        }
        int ttl = 3600;
        InetAddress addr = InetAddress.getByName(ipAddress);
        Record answer = Record.newRecord(Name.fromString(domainName), Type.A, recordClass, ttl, addr.getAddress());
        dnsMessage.addRecord(answer, Section.ANSWER);

        // Make dnsMessage a response
        header.setFlag(Flags.QR);

        // Add the request id to dnsMessage
        header.setID(new Header(requestData).getID());

        // Convert the DNS message to a byte array
        byte[] dnsPacket = dnsMessage.toWire();

        return dnsPacket;
    }
}
