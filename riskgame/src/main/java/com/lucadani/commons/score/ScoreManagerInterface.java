package com.lucadani.commons.score;

import com.lucadani.commons.model.Player;

import java.util.List;

public interface ScoreManagerInterface {
    void registerPlayer(int playerId);
    void addPoints(int playerId, int amount);
    void subtractPoints(int playerId, int amount);
    void wipeScoreAndEliminate(int playerId, Player player);
    int getScore(int playerId);
    void setScore(int playerId, int score);
    int getScoreAtTurn(int playerId, int turnIndex);
    int getPenaltyCount(int playerId);
    List<Player> getTopScorers(List<Player> players);
    boolean areAllEqual(List<Player> players);
    void resetScore(int playerId);
    void appendHistory(int playerId);
    void incrementPenaltyCount(int playerId);
}
