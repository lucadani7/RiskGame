package com.lucadani.server;

import com.lucadani.server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerMain {
    private static final int PORT = 9090;
    private static final AtomicInteger PLAYER_ID_GENERATOR = new AtomicInteger(1);

    public static void main(String[] args) {
        System.out.println("RiskGame server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                int playerId = PLAYER_ID_GENERATOR.getAndIncrement();
                System.out.println("New player connected with ID " + playerId);
                ClientHandler handler = new ClientHandler(socket, playerId);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
