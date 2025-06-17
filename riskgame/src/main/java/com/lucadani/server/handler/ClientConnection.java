package com.lucadani.server.handler;

import com.lucadani.commons.model.GameState;

import java.io.IOException;

public interface ClientConnection {
    void sendGameState(GameState state) throws IOException;
}
