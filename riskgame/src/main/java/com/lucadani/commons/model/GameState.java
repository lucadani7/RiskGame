package com.lucadani.commons.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Player> playersList;
    private Map<Integer, Territory> territoryMap; // Key = territory ID
    private int currentPlayerId; // the current player's turn
    private boolean gameEnded;
    private int winnerId; // if the game finishes, we'll have a winner
}
