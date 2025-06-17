package com.lucadani.commons.model;

import com.lucadani.commons.enums.PlayerState;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private boolean isAI;
    private int totalTerritories;
    private int totalUnits;
    private PlayerState state = PlayerState.ACTIVE;

    public boolean isEligibleToPlay() {
        return state == PlayerState.ACTIVE; // if a player is neither disconnected nor eliminated, he can play
    }

    public boolean isAliveButOffline() {
        return state == PlayerState.DISCONNECTED; // the player is not eliminated, but he doesn't play anymore, so the next player or AI can replace him
    }

    public boolean isInactive() {
        return isAliveButOffline() || isOutOfGameForever();
    }

    public boolean isOutOfGameForever() {
        return isDefeated() || hasQuitTheGame(); // a player who has been eliminated by the game or has quit cannot play anymore in the current game session
    }

    public boolean isDefeated() {
        return state == PlayerState.ELIMINATED; // the player has been eliminated by the game, but he can stay until the game will be over, if he wants
    }

    public boolean hasQuitTheGame() {
        return state == PlayerState.QUIT; // the player hasn't accepted the defeat, so he has decided to quit the game
    }

    public boolean wasDroppedBetweenTurns() {
        return hasQuitTheGame() && !isDefeated();
    }

    public boolean isWinner() {
        return state == PlayerState.WINNER; // the player won the game
    }
}
