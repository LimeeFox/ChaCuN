package ch.epfl.chacun;

import java.util.List;

/**
 * @param forests
 *         les partitions de forêts initiales
 * @param meadows
 *         les partitions de prés initiales
 * @param rivers
 *         les partitions de rivières initiales
 * @param riverSystems
 *         les partitions des systèmes hydrographiques initiales
 * @author Vladislav Yarkovoy (362242)
 * @author Cyriac Philippe (360553)
 */
public record ZonePartitions(
        ZonePartition<Zone.Forest> forests,
        ZonePartition<Zone.Meadow> meadows,
        ZonePartition<Zone.River> rivers,
        ZonePartition<Zone.Water> riverSystems) {

    public static final ZonePartitions EMPTY = new ZonePartitions(new ZonePartition<>(),
            new ZonePartition<>(),
            new ZonePartition<>(),
            new ZonePartition<>());

    /**
     * Classe bâtisseur de ZonePartitions
     */
    public static final class Builder {

        private final ZonePartition.Builder<Zone.Forest> forestBuilder;
        private final ZonePartition.Builder<Zone.Meadow> meadowBuilder;
        private final ZonePartition.Builder<Zone.River> riverBuilder;
        private final ZonePartition.Builder<Zone.Water> riverSystemBuilder;

        public Builder(ZonePartitions initial) {
            forestBuilder = new ZonePartition.Builder<>(initial.forests);
            meadowBuilder = new ZonePartition.Builder<>(initial.meadows);
            riverBuilder = new ZonePartition.Builder<>(initial.rivers);
            riverSystemBuilder = new ZonePartition.Builder<>(initial.riverSystems);
        }

        /**
         * Ajoute aux quatre partitions les aires correspondant aux zones de la tuile donnée
         *
         * @param tile
         *         la tuile que l'on souhaite ajouter à la partition
         */
        //todo ask about better way of calculating nbOpenings
        public void addTile(Tile tile) {
            // Le défi ici, c'est de calculer le nombre de connexions ouvertes de chaque zone.
            // Ce qu'on va faire, c'est itérer sur tous les TileSide (côtés de la tuile) [1]
            // et regarder quelles sont les zones qui appartiennent à ce côté [2]. Pour chaque telle zone, nous allons
            // incrémenter le nombre de connexions ouvertes de 1. [3]
            int[] nbOpenings = new int[10];

            // On va parcourir d'abord toutes les zones sur les côtés de la tuile [1]
                for (TileSide tileSide : tile.sides()) {
                    for (Zone sideZone : tileSide.zones()) {
                            nbOpenings[sideZone.localId()] += 1; // [3]

                            // N'oublions pas les lacs
                            if (sideZone instanceof Zone.River river && river.hasLake()) {
                                nbOpenings[river.localId()] += 1;
                                nbOpenings[river.lake().localId()] += 1;
                            }
                        }
                    }
            // Ajouter les zones en tant qu'aires, inoccupées, à nos partitions
            for (Zone sideZone : tile.sideZones()) {
                switch (sideZone) {
                    case Zone.Forest forest -> forestBuilder.addSingleton(forest, nbOpenings[forest.localId()]);
                    case Zone.Meadow meadow -> meadowBuilder.addSingleton(meadow, nbOpenings[meadow.localId()]);
                    case Zone.River river -> {
                        if (river.hasLake()) {
                            riverSystemBuilder.addSingleton(river.lake(), nbOpenings[river.lake().localId()]);
                            riverBuilder.addSingleton(river, nbOpenings[river.localId()] - 1);
                        } else {
                            riverBuilder.addSingleton(river, nbOpenings[river.localId()]);
                        }
                        riverSystemBuilder.addSingleton(river, nbOpenings[river.localId()]);
                    }
                    default -> {
                    }
                }
            }

            for (Zone zone : tile.sideZones()) {
                if (zone instanceof Zone.River river && river.hasLake()) {
                    riverSystemBuilder.union(river, river.lake());
                }
            }
        }

        /**
         * Connecte les deux bords de tuiles donnés, en connectant entre elles les aires correspondantes
         *
         * @param s1
         *         le premier TileSide à connecter avec @s2
         * @param s2
         *         le deuxième TileSide à connecter avec @s1
         * @throws IllegalArgumentException
         *         si les deux bords ne sont pas de la même sorte
         */
        public void connectSides(TileSide s1, TileSide s2) {
            switch (s1) {
                // Connecter les deux zones forêt
                case TileSide.Forest(Zone.Forest m1) when s2 instanceof TileSide.Forest(Zone.Forest m2) ->
                        forestBuilder.union(m1, m2);
                // Connecter les deux zones meadow
                case TileSide.Meadow(Zone.Meadow m1) when s2 instanceof TileSide.Meadow(Zone.Meadow m2) ->
                        meadowBuilder.union(m1, m2);
                // Connecter les deux zones rivière
                case TileSide.River riverTile1 when s2 instanceof TileSide.River riverTile2 -> {
                    riverBuilder.union(riverTile1.river(), riverTile2.river());
                    riverSystemBuilder.union(riverTile1.river(), riverTile2.river());
                    meadowBuilder.union(riverTile1.meadow1(), riverTile2.meadow2());
                    meadowBuilder.union(riverTile1.meadow2(), riverTile2.meadow1());
                }
                default -> Preconditions.checkArgument(s1.isSameKindAs(s2));
            }
        }

        /**
         * Ajoute un occupant initial, de la sorte donnée et appartenant au joueur donné,
         * à l'aire contenant la zone donnée
         *
         * @param player
         *         joueur auquel appartient l'occupant
         * @param occupantKind
         *         le type de l'occupent à ajouter à la partition
         * @param occupiedZone
         *         la zone où l'occupant sera placé
         * @throws IllegalArgumentException
         *         si la sorte d'occupant donnée ne peut pas occuper une zone de la sorte donnée
         */
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone)
                throws IllegalArgumentException {
            switch (occupiedZone) {
                case Zone.Forest forest when occupantKind == Occupant.Kind.PAWN ->
                        forestBuilder.addInitialOccupant(forest, player);
                case Zone.Meadow meadow when occupantKind == Occupant.Kind.PAWN ->
                        meadowBuilder.addInitialOccupant(meadow, player);
                case Zone.River river when occupantKind == Occupant.Kind.PAWN ->
                        riverBuilder.addInitialOccupant(river, player);
                case Zone.Water water when occupantKind == Occupant.Kind.HUT ->
                        riverSystemBuilder.addInitialOccupant(water, player);
                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Supprime un occupant (un pion) appartenant au joueur donné de l'aire contenant la zone donnée
         *
         * @param player
         *         le joueur auquel appartient le pion
         * @param occupiedZone
         *         la zone occupée à laquelle le pion doit être supprimé
         * @throws IllegalArgumentException
         *         si la zone est un lac
         */
        public void removePawn(PlayerColor player, Zone occupiedZone) {
            Preconditions.checkArgument(!(occupiedZone instanceof Zone.Lake));

            switch (occupiedZone) {
                case Zone.Meadow m1 -> meadowBuilder.removeOccupant(m1, player);
                case Zone.Forest m1 -> forestBuilder.removeOccupant(m1, player);
                case Zone.River m1 -> riverBuilder.removeOccupant(m1, player);
                default -> {}
            }
        }

        /**
         * Supprimer tous les occupents de la forêt donnée
         *
         * @param forest
         *         aire de forêt dans laquelle tous les occupants sont à enlever
         */
        public void clearGatherers(Area<Zone.Forest> forest) {
            forestBuilder.removeAllOccupantsOf(forest);
        }

        /**
         * Supprimer tous les occupents de la rivière donnée
         *
         * @param river
         *         aire de rivières dans laquelle tous les occupants sont à enlever
         */
        public void clearFishers(Area<Zone.River> river) {
            riverBuilder.removeAllOccupantsOf(river);
        }

        /**
         * Finaliser la construction d'une ZonePartitions
         *
         * @return la ZonePartitions finalisée
         */
        public ZonePartitions build() {
            return new ZonePartitions(forestBuilder.build(), meadowBuilder.build(), riverBuilder.build(),
                    riverSystemBuilder.build());
        }
    }
}
