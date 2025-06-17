package com.lucadani.commons.room;

import com.lucadani.commons.command.GameCommand;
import com.lucadani.commons.model.GameState;
import com.lucadani.commons.model.Player;
import com.lucadani.commons.enums.PlayerState;
import com.lucadani.commons.model.Territory;
import com.lucadani.server.handler.ClientConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom {
    private final int maxPlayers;
    private final Map<Integer, ClientConnection> players = new ConcurrentHashMap<>();
    private final GameState gameState = new GameState();

    public GameRoom(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        gameState.setGameEnded(false);
        gameState.setWinnerId(-1);
    }

    // register a new player
    public synchronized boolean addPlayer(int playerId, ClientConnection conn) {
        if (players.size() >= maxPlayers) {
            return false;
        }
        Player p = new Player(playerId, "Player " + playerId, false, 0, 0, PlayerState.ACTIVE);
        gameState.getPlayersList().add(p);
        players.put(playerId, conn);

        if (players.size() == maxPlayers) {
            // start the game
            // initialize the maps, share the territories and unities
            List<Territory> territoriesList = generateTerritories();
            Map<Integer, Territory> map = new HashMap<>();
            for (Territory territory : territoriesList) {
                map.put(territory.getId(), territory);
            }
            gameState.setTerritoryMap(map);
            gameState.setCurrentPlayerId(gameState.getPlayersList().getFirst().getId());
            broadcastState();
        }
        return true;
    }

    public void handleCommand(GameCommand command) {
        System.out.println("GameRoom handles command: " + command);
        nextTurn();
        broadcastState();
    }

    private void nextTurn() {
        List<Player> players = gameState.getPlayersList();
        int total = players.size();
        int idx = -1;

        // looking for the index of the current player
        for (Player player : players) {
            if (player.getId() == gameState.getCurrentPlayerId()) {
                idx = players.indexOf(player);
                break;
            }
        }

        for (int offset = 1; offset <= total; ++offset) {
            int nextIdx = (idx + offset) % total;
            Player nextPlayer = players.get(nextIdx);
            if (nextPlayer.isEligibleToPlay()) {
                gameState.setCurrentPlayerId(nextPlayer.getId());
                return;
            }
        }
    }

    private List<Territory> generateTerritories() {
        List<Territory> territories = new ArrayList<>();
        for (int i = 1; i <= 6; ++i) {
            territories.add(new Territory(i, "T" + i, -1, 5, List.of())); // dummy territories
        }
        return territories;
    }

    private void broadcastState() {
        for (var entry : players.entrySet()) {
            try {
                entry.getValue().sendGameState(gameState);
            } catch (Exception e) {
                System.err.println("Error while sending the game state to the player: " + entry.getKey());
            }
        }
    }
}
