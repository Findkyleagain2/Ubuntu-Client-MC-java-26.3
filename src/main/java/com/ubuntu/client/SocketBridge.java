package com.ubuntu.client;

import java.net.*;
import java.io.*;

public class SocketBridge extends Thread {
    public void run() {
        try (ServerSocket server = new ServerSocket(9999)) {
            while (true) {
                try (Socket client = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                    String cmd = in.readLine();
                    if ("AIM".equals(cmd)) { /* Toggle Logic */ }
                }
            }
        } catch (IOException e) {
            System.err.println("Socket Error: " + e.getMessage());
        }
    }
}