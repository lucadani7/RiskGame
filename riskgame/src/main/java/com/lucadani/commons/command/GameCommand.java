package com.lucadani.commons.command;

import com.lucadani.commons.enums.CommandType;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameCommand implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private CommandType type;
    private int sourceTerritoryId;   // it might be useless for END_TURN
    private int targetTerritoryId;   // used for ATTACK și MOVE
    private int units;               // count of units (ex: we send three units)
    private int playerId;            // who issues the command
}
