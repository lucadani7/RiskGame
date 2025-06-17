package com.lucadani.penalties;

import com.lucadani.commons.enums.PenaltyType;
import com.lucadani.commons.model.Player;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PenaltyHistory {
    private final Map<Integer, EnumMap<PenaltyType, Integer>> data = new HashMap<>();

    // checking if the player has no penalizations
    public boolean isPlayerUnpenalized(Player player) {
        return getSummary(player).values().stream().allMatch(v -> v == 0);
    }

    public Map<PenaltyType, Integer> getSummary(Player player) {
        return data.getOrDefault(player.getId(), new EnumMap<>(PenaltyType.class));
    }

    public int getCountPenalty(Player player, PenaltyType type) {
        return data.getOrDefault(player.getId(), new EnumMap<>(PenaltyType.class)).getOrDefault(type, 0);
    }

    public void incrementPenalty(Player player, PenaltyType type) {
        data.computeIfAbsent(player.getId(), __ -> new EnumMap<>(PenaltyType.class)).merge(type, 1, Integer::sum);
    }
}
