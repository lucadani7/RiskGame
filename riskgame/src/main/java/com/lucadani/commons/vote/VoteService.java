package com.lucadani.commons.vote;

import com.lucadani.commons.enums.VoteOption;
import com.lucadani.commons.enums.VoteOutcome;
import com.lucadani.commons.model.Player;
import com.lucadani.commons.score.ScoreManager;

import java.time.Duration;
import java.util.List;

public class VoteService {
    // starting a new session vote
    public VotingSession startSession(List<Player> eligiblePlayers, Duration timeout) {
        return new VotingSession(eligiblePlayers, timeout);
    }

    // register player's vote
    public void castVote(VotingSession session, int playerId, VoteOption option) {
        session.vote(playerId, option);
    }

    // condition for voting to be allowed: there must be at least three eligible players and there must exist flawless equality of points between them
    public boolean isVotingPhaseAllowed(List<Player> eligiblePlayers) {
        return eligiblePlayers.size() > 2 && new ScoreManager().areAllEqual(eligiblePlayers);
    }

    // conclude voting and return
    public VoteResult concludeVoting(VotingSession session) {
        return isVotingPhaseAllowed(session.getEligiblePlayers()) ? session.getResult() : new VoteResult(
                session.getEligiblePlayers().size(),
                session.getVotes().size(),
                0, 0,
                List.of(), List.of(),
                VoteOutcome.VOTING_NOT_ALLOWED
        );
    }
}
