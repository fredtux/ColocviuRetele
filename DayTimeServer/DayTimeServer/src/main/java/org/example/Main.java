package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        while (true) {
            try (
                    //cream socket (pentru adresa 0.0.0.0) cu portul dat ca parametru la lansarea serverrlui
                    ServerSocket serverSocket =
                            new ServerSocket(4040);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out =
                            new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String dayTimeString;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now;
                now = LocalDateTime.now();
                dayTimeString = dtf.format(now);
                out.println(dayTimeString);
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port 4040 or listening for a connection");
                System.out.println(e.getMessage());
            }
        }

    }
}