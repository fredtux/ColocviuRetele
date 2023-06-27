import java.io.*;
import java.net.*;
import java.util.*;

public class TimeClient {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
             System.out.println("Usage: java TimeClient.java <hostname>");
             return;
        }

            // get a datagram socket
        DatagramSocket socket = new DatagramSocket();

            // send request
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName(args[0]);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
    
            // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

	    // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Time right now: " + received);
    
        socket.close();
    }
}
