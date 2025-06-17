package com.lucadani.elimination;

import com.lucadani.commons.model.Player;
import com.lucadani.commons.score.ScoreManager;
import com.lucadani.commons.score.ScoreManagerInterface;
import com.lucadani.penalties.PenaltyService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class SurvivalManager {
    private final ScoreManagerInterface scoreManager = new ScoreManager();
    private final PenaltyService penaltyService;

    private static final Predicate<Integer> IS_ELIMINATION_TURN = turn -> turn > 1 && turn % 5 == 1;
    private static final Function<Integer, Integer> GET_ELIMINATION_INDEX = turn -> turn / 5;
    private static final Function<Integer, Integer> GET_THRESHOLD = turn -> 25 * GET_ELIMINATION_INDEX.apply(turn) + 5;

    public boolean isEliminationTurn(int turn) {
        return IS_ELIMINATION_TURN.test(turn);
    }

    public int getThreshold(int turn) {
        return GET_THRESHOLD.apply(turn);
    }

    public void evaluate(List<Player> players, int currentTurn, Map<Integer, Boolean> survivalDecisions) {
        if (!isEliminationTurn(currentTurn)) {
            return;
        }
        int threshold = getThreshold(currentTurn);
        for (Player player : players) {
            if (!player.isEligibleToPlay()) {
                continue;
            }
            int score = scoreManager.getScore(player.getId());
            if (score < threshold) {
                penaltyService.applySurvivalPenalty(player, survivalDecisions.getOrDefault(player.getId(), false));
            } else {
                scoreManager.setScore(player.getId(), score * 2);
                System.out.printf("[SURVIVAL BONUS] %s had %d ≥ %d. Score doubled to %d%n.", player.getName(), score, threshold, score * 2);
            }
        }
    }
}
