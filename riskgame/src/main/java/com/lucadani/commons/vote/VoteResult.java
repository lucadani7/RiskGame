package com.lucadani.commons.vote;

import com.lucadani.commons.enums.VoteOutcome;

import java.util.List;

public record VoteResult(int totalPlayers,
                         int totalVotesReceived,
                         long countExtraTime,
                         long countSurrender,
                         List<Integer> votersExtraTime,
                         List<Integer> votersSurrender,
                         VoteOutcome outcome) {
}
