package com.lucadani.penalties;

import com.lucadani.commons.enums.PenaltyType;
import com.lucadani.commons.enums.PlayerState;
import com.lucadani.commons.model.Player;
import com.lucadani.commons.score.ScoreManager;
import com.lucadani.commons.score.ScoreManagerInterface;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class PenaltyService {
    private final ScoreManagerInterface scoreManager = new ScoreManager();
    private final PenaltyHistory penaltyHistory;

    private void applyMoralPenalty(Player player, int totalTurns, int currentTurn) {
        if (player.isDefeated() || player.isWinner()) {
            return;
        }
        if (!player.hasQuitTheGame()) {
            return;
        }
        Predicate<Integer> isEliminationPhase = value -> value % 5 == 1 && value > 1;
        if (isEliminationPhase.test(currentTurn)) {
            return;
        }
        int penaltyScore = totalTurns * 50 * (-1);
        scoreManager.setScore(player.getId(), penaltyScore);
        penaltyHistory.incrementPenalty(player, PenaltyType.MORAL);
        System.out.printf("[MORAL PENALTY] %s has quit the game before elimination phase. Final score: %d%n points.", player.getName(), penaltyScore);
    }

    private void applyTacticalPenalty(Player player) {
        if (!player.isEligibleToPlay()) {
            return;
        }
        scoreManager.subtractPoints(player.getId(), 25);
        penaltyHistory.incrementPenalty(player, PenaltyType.TACTICAL);
        System.out.printf("[TACTICAL PENALTY] -25 points for: %s%n, due to inactivity.", player.getName());
    }

    private void applyCumulativePenalty(Player player) {
        if (!player.isEligibleToPlay()) {
            return;
        }
        int strikes = penaltyHistory.getCountPenalty(player, PenaltyType.CUMULATIVE) + 1;
        penaltyHistory.incrementPenalty(player, PenaltyType.CUMULATIVE);
        switch (strikes) {
            case 1 -> {
                scoreManager.subtractPoints(player.getId(), 10);
                System.out.printf("[CUMULATIVE PENALTY] First cumulative penalty. -10 points for: %s." +
                        " At three cumulative penalties in a row, %s%n will be eliminated and his score will be wiped.", player.getName(), player.getName());
            }
            case 2 -> {
                scoreManager.subtractPoints(player.getId(), 20);
                System.out.printf("[CUMULATIVE PENALTY] Second cumulative penalty. -20 points for: %s." +
                        " At three cumulative penalties in a row, %s%n will be eliminated and his score will be wiped.", player.getName(), player.getName());
            }
            case 3 -> {
                scoreManager.wipeScoreAndEliminate(player.getId(), player);
                System.out.printf("[CUMULATIVE PENALTY] Third cumulative penalty. %s has been eliminated by the game. Final score: %d%n points.", player.getName(), 0);
            }
        }
    }

    public void applySurvivalPenalty(Player player, boolean acceptToContinuePlaying) {
        if (!player.isEligibleToPlay()) {
            return;
        }
        if (!acceptToContinuePlaying) {
            player.setState(PlayerState.ELIMINATED);
            System.out.printf("[SURVIVAL ELIMINATION] %s has decided to withdraw from the game. Final score: %d%n points.", player.getName(), scoreManager.getScore(player.getId()));
        } else {
            int oldScore = scoreManager.getScore(player.getId());
            if (oldScore == 0) {
                player.setState(PlayerState.ELIMINATED);
                System.out.printf("[SURVIVAL PENALTY] %s has been eliminated by the game, because he has got no points. Final score: %d%n points.", player.getName(), 0);
                return;
            }
            int reducedScore = oldScore / 2;
            scoreManager.setScore(player.getId(), reducedScore);
            penaltyHistory.incrementPenalty(player, PenaltyType.SURVIVAL);
            System.out.printf("[SURVIVAL PENALTY] %s has accepted to continue playing. His score has been reduced from %d to %d%n points.", player.getName(), oldScore, reducedScore);
        }
    }

    public void applyPenalty(Player player, PenaltyType type, int totalTurns, int currentTurn, boolean acceptToContinuePlaying) {
        switch (type) {
            case TACTICAL -> applyTacticalPenalty(player);
            case CUMULATIVE -> applyCumulativePenalty(player);
            case MORAL -> applyMoralPenalty(player, totalTurns, currentTurn);
            case SURVIVAL -> applySurvivalPenalty(player, acceptToContinuePlaying);
        }
    }
}
