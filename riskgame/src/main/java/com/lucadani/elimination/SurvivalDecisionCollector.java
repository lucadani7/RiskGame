package com.lucadani.elimination;

import com.lucadani.commons.model.Player;

import java.util.List;
import java.util.Map;

public interface SurvivalDecisionCollector {
    Map<Integer, Boolean> collectDecisions(List<Player> players, int threshold);
}
