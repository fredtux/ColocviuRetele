package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// Inspirat din QuoteClient.java din Laboratorul 8
public class Main {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java TimeClient.java <hostname>");
            return;
        }

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();

            // send request
            byte[] buf = new byte[256];
            InetAddress address = InetAddress.getByName(args[0]);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 7070);
            socket.send(packet);

            // get response
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            // display response
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Time right now: " + received);

            socket.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }

    }
}