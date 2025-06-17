package com.lucadani.commons.score;

import com.lucadani.commons.enums.PlayerState;
import com.lucadani.commons.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreManager implements ScoreManagerInterface {
    private final Map<Integer, Integer> scores = new HashMap<>();
    private final Map<Integer, List<Integer>> scoreHistory = new HashMap<>();
    private final Map<Integer, Integer> penaltyCount = new HashMap<>();

    @Override
    public void registerPlayer(int playerId) {
        scores.put(playerId, 0);
        scoreHistory.put(playerId, new ArrayList<>(List.of(0)));
        penaltyCount.put(playerId, 0);
    }

    @Override
    public void addPoints(int playerId, int amount) {
        scores.merge(playerId, amount, Integer::sum);
        appendHistory(playerId);
    }

    @Override
    public void subtractPoints(int playerId, int amount) {
        scores.merge(playerId, -amount, Integer::sum);
        incrementPenaltyCount(playerId);
        appendHistory(playerId);
    }

    @Override
    public void wipeScoreAndEliminate(int playerId, Player player) {
        scores.put(playerId, 0);
        appendHistory(playerId);
        player.setState(PlayerState.ELIMINATED);
        incrementPenaltyCount(playerId);
    }

    @Override
    public int getScore(int playerId) {
        return scores.getOrDefault(playerId, 0);
    }

    @Override
    public void setScore(int playerId, int score) {
        scores.put(playerId, score);
        appendHistory(playerId);
    }

    @Override
    public int getScoreAtTurn(int playerId, int turnIndex) {
        List<Integer> history = scoreHistory.getOrDefault(playerId, List.of(0));
        return turnIndex < history.size() ? history.get(turnIndex) : history.getLast();
    }

    @Override
    public int getPenaltyCount(int playerId) {
        return penaltyCount.getOrDefault(playerId, 0);
    }

    @Override
    public List<Player> getTopScorers(List<Player> players) {
        int maxScore = players.stream().mapToInt(p -> getScore(p.getId())).max().orElse(0);
        return players.stream().filter(p -> getScore(p.getId()) == maxScore).toList();
    }

    @Override
    public boolean areAllEqual(List<Player> players) {
        return players.stream().map(p -> getScore(p.getId())).distinct().count() == 1;
    }

    @Override
    public void resetScore(int playerId) {
        scores.put(playerId, 0);
        scoreHistory.get(playerId).clear();
        scoreHistory.get(playerId).add(0);
        penaltyCount.put(playerId, 0);
    }

    @Override
    public void appendHistory(int playerId) {
        scoreHistory.get(playerId).add(scores.get(playerId));
    }

    @Override
    public void incrementPenaltyCount(int playerId) {
        penaltyCount.merge(playerId, 1, Integer::sum);
    }
}
