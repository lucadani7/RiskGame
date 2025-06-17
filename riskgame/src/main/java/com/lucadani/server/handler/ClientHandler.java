package com.lucadani.server.handler;

import com.lucadani.commons.command.GameCommand;
import com.lucadani.commons.model.GameState;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final int playerId;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private volatile boolean running = true;

    public ClientHandler(Socket socket, int playerId) {
        this.socket = socket;
        this.playerId = playerId;
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.printf("ClientHandler started for player %s on thread %s%n", playerId, Thread.currentThread().getName());

            while (running) {
                Object obj = in.readObject();
                if (obj instanceof GameCommand command) {
                    System.out.printf("Received: %s command from player %s%n", command, playerId);
                    GameState dummyState = new GameState();
                    out.writeObject(dummyState);
                    out.flush();
                }
            }
        } catch (EOFException | SocketException e) {
            System.out.println("Player " + playerId + " disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.printf("Error for player %s: %s%n", playerId, e.getMessage());
        } finally {
            // we are trying to close connection
            try {
                running = false;
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                socket.close();
            } catch (IOException e) {
                System.err.printf("Error while closing connection to %s: %s%n", playerId, e.getMessage());
            }
        }
    }
}
