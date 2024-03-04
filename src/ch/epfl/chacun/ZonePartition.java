package ch.epfl.chacun;

import java.util.Set;

/**
 * Partition de zone
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 *
 * @param Set<Area<Z>>
 */

public record ZonePartition(Set<Area<Z>>) {

    public ZonePartition {
        this.Set<Area<Z>> = Set.copyOf(Set<Area<Z>>);
    }

}
