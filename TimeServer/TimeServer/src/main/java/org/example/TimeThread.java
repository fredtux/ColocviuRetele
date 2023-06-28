package org.example;

import java.io.*;
import java.net.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

// Inspirat din QuoteServerThread.java din Laboratorul 8
public class TimeThread extends Thread {

    protected DatagramSocket socket = null;

    public TimeThread() throws IOException {
        this("TimeThread");
    }

    public TimeThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(7070);

    }

    public void run() {

        while (true) {
            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                // figure out response
                String dString = dtf.format(now);

                buf = dString.getBytes();

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //socket.close();
    }
}
