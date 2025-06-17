package com.lucadani.elimination;

import com.lucadani.commons.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SurvivalDecision implements SurvivalDecisionCollector {
    @Override
    public Map<Integer, Boolean> collectDecisions(List<Player> players, int threshold) {
        Map<Integer, Boolean> decisions = new HashMap<>();
        for (Player player : players) {
            if (!player.isEligibleToPlay()) {
                continue;
            }
            System.out.printf("[SURVIVAL PROMPT] %s has less than %d points.%n", player.getName(), threshold);
            String answer;
            do {
                System.out.printf("[SURVIVAL CHOICE] Do you want to continue playing, %s? (YES / NO): ", player.getName());
                answer = new Scanner(System.in).nextLine().trim();
            } while (!(answer.equalsIgnoreCase("YES") || answer.equalsIgnoreCase("NO")));
            decisions.put(player.getId(), answer.equalsIgnoreCase("YES"));
        }
        return decisions;
    }
}
