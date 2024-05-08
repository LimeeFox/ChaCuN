package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Partition de zone
 *
 * @param areas
 *         ensemble d'aires données
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */

public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {

    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    public ZonePartition() {
        this(new HashSet<>());
    }

    /**
     * Aire contenant une zone demandée
     *
     * @param zone
     *         zone qui doit contenir l'aire recherchée
     * @return l'air qui contient la zone demandée
     * @throws IllegalArgumentException
     *         si aucune des aires ne contient la zone demandée
     */
    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)) {
                return area;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Bâtisseur de partition de zone
     *
     * @param <Z>
     *         le type de la zone en construction
     */
    public static final class Builder<Z extends Zone> {
        private final Set<Area<Z>> builderAreas;

        public Builder(ZonePartition<Z> partition) {
            this.builderAreas = new HashSet<>(partition.areas());
        }

        /**
         * Ajout d'une nouvelle aire inoccupée, constitué d'une zone donnée et d'un nombre de connections ouvertes
         * donné, à la partition en cours de construction
         *
         * @param zone
         *         zone constituant l'aire inoccupée
         * @param openConnections
         *         nombre de connections ouvertes de l'aire inoccupée
         */
        public void addSingleton(Z zone, int openConnections) {
            builderAreas.add(new Area<>(Set.of(zone), List.of(), openConnections));
        }

        /**
         * Ajout d'un occupant d'une couleur donnée à l'aire contenant une zone donnée
         *
         * @param zone
         *         zone à laquelle un occupant sera ajouter
         * @param color
         *         couleur de l'occupant à ajouter si aucune aire de la partition
         *         en construction ne contient la zone donnée
         * @throws IllegalArgumentException
         *         si la zone n'appartient pas à une aire de la partition, ou si l'aire est déjà occupée
         */
        public void addInitialOccupant(Z zone, PlayerColor color) {
            Area<Z> occupiedArea = areaContaining(zone).withInitialOccupant(color);
            builderAreas.remove(areaContaining(zone));
            builderAreas.add(occupiedArea);
        }

        /**
         * Suppression d'un occupant d'une couleur donnée de l'aire contenant une zone donnée
         *
         * @param zone
         *         zone à laquelle l'occupant sera supprimé
         * @param color
         *         couleur de l'occupant qui sera supprimé
         * @throws IllegalArgumentException
         *         si aucune aire de la partition en construction ne contient la zone donnée,
         *         ou si elle n'est pas occupée par au moins un occupant de la couleur donnée
         */
        public void removeOccupant(Z zone, PlayerColor color) {
            Area<Z> unoccupiedArea = areaContaining(zone).withoutOccupant(color);
            builderAreas.remove(areaContaining(zone));
            builderAreas.add(unoccupiedArea);
        }

        /**
         * Suppression de tous les occupants d'une aire donnée
         *
         * @param area
         *         aire dont la totalité des occupants seront supprimés
         * @throws IllegalArgumentException
         *         si l'aire ne fait pas partie de la partition
         */
        public void removeAllOccupantsOf(Area<Z> area) {
            Preconditions.checkArgument(builderAreas.contains(area));
            builderAreas.remove(area);
            builderAreas.add(area.withoutOccupants());
        }

        /**
         * Connecte les aires contenant les zones zone1 et zone2 pour en faire une plus grande aire
         *
         * @param zone1
         *         zone contenue dans la première aire
         * @param zone2
         *         zone contenue dans la deuxième aire
         * @throws IllegalArgumentException
         *         si l'une des deux zones n'appartient pas à une aire de la partition
         */
        public void union(Z zone1, Z zone2) {
            Area<Z> area1 = areaContaining(zone1);
            Area<Z> area2 = areaContaining(zone2);

            builderAreas.remove(area1);
            if (!area1.equals(area2)) {
                builderAreas.remove(area2);
            }

            builderAreas.add(area1.connectTo(area2));
        }

        private Area<Z> areaContaining(Z zone) {
            for (Area<Z> area : builderAreas) {
                if (area.zones().contains(zone)) {
                    return area;
                }
            }
            throw new IllegalArgumentException();
        }

        /**
         * Finaliser la construction d'une ZonePartition<Z>
         *
         * @return la ZonePartition<Z> finalisée
         */
        public ZonePartition<Z> build() {
            return new ZonePartition<Z>(builderAreas);
        }
    }
}