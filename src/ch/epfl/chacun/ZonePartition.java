package ch.epfl.chacun;

import java.util.Set;

/**
 * Partition de zone
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 *
 * @param areas
 *          ensemble d'aires données
 */

public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {

    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    // TODO: 04/03/2024 secondary constructeur

    /**
     * Aire contenant une zone demandée
     *
     * @param zone
     *          zone qui doit contenir l'aire recherchée
     * @return area
     *          l'air qui contient la zone demandée
     * @throws IllegalArgumentException
     *          si aucune des aires ne contient la zone demandée
     */
    Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)) {
                return area;
            }
        }
        throw new IllegalArgumentException();
    }
}
