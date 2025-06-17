package com.lucadani.orchestrator;

import com.lucadani.commons.model.Player;
import com.lucadani.commons.score.ScoreManager;
import com.lucadani.elimination.SurvivalDecision;
import com.lucadani.elimination.SurvivalManager;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class GameOrchestrator {
    private final List<Player> players;
    private final SurvivalManager survivalManager;

    public void startTurn(int currentTurn) {
        System.out.printf("%n--- TURN %d STARTED --- %n", currentTurn);
        if (survivalManager.isEliminationTurn(currentTurn)) {
            int threshold = survivalManager.getThreshold(currentTurn);
            Map<Integer, Boolean> survivalDecisions = new SurvivalDecision().collectDecisions(players, threshold);
            survivalManager.evaluate(players, currentTurn, survivalDecisions);
        } else {
            for (Player p : players) {
                if (p.isEligibleToPlay()) {
                    System.out.printf("[REWARD] %s gains 10 points for participation%n", p.getName());
                    new ScoreManager().addPoints(p.getId(), 10);
                }
            }
        }
    }
}
