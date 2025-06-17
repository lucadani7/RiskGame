package com.lucadani.commons.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Territory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int ownerId;
    private int unitCount;
    private List<Integer> neighborIds; // IDs of neighbor territories
}
