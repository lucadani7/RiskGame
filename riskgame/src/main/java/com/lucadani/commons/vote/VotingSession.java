package com.lucadani.commons.vote;

import com.lucadani.commons.enums.VoteOption;
import com.lucadani.commons.enums.VoteOutcome;
import com.lucadani.commons.model.Player;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingSession {
    @Getter
    private final List<Player> eligiblePlayers;
    @Getter
    private final Map<Integer, VoteOption> votes = new HashMap<>();
    private final Map<Integer, Instant> deadlines = new HashMap<>();
    private final Instant startTime;

    public VotingSession(List<Player> players, Duration timeoutDuration) {
        eligiblePlayers = players;
        startTime = Instant.now();
        players.forEach(p -> deadlines.put(p.getId(), startTime.plus(timeoutDuration)));
    }

    private List<Integer> getIdsByOption(VoteOption option) {
        return votes.entrySet().stream()
                .filter(e -> e.getValue() == option)
                .map(Map.Entry::getKey)
                .toList();
    }

    public void vote(int playerId, VoteOption option) {
        if (Instant.now().isBefore(deadlines.get(playerId))) {
            votes.put(playerId, option);
        }
    }

    public VoteResult getResult() {
        // if a player didn't vote, it means he wants to surrender
        eligiblePlayers.stream().filter(p -> !votes.containsKey(p.getId())).forEach(p -> votes.put(p.getId(), VoteOption.SURRENDER));

        int majority = eligiblePlayers.size() / 2 + 1;
        long extra = votes.values().stream().filter(v -> v == VoteOption.EXTRA_TIME).count();
        long surrender = eligiblePlayers.size() - extra;
        VoteOutcome outcome = extra >= majority ? VoteOutcome.EXTRA_TURN : (surrender >= majority) ? VoteOutcome.ACCEPTED_DRAW : VoteOutcome.FORCED_DRAW;
        return new VoteResult(
                eligiblePlayers.size(),
                votes.size(),
                extra,
                surrender,
                getIdsByOption(VoteOption.EXTRA_TIME),
                getIdsByOption(VoteOption.SURRENDER),
                outcome
        );
    }
}
