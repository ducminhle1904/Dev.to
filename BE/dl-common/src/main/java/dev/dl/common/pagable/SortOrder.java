package dev.dl.common.pagable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuppressWarnings("ALL")
public class SortOrder {
    private String property;
    private Direction direction;

    public SortOrder(String property) {
        this(property, Direction.ASC);
    }

}
