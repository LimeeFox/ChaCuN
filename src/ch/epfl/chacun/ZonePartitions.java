package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Vladislav Yarkovoy (362242)
 *
 * @param forests
 * @param meadows
 * @param rivers
 * @param riverSystems
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.Meadow> meadows, ZonePartition<Zone.River> rivers, ZonePartition<Zone.Water> riverSystems) {

    public static final ZonePartitions EMPTY = new ZonePartitions(new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>());

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
         */
        public void addTile(Tile tile) {
            // Ici le problème c'est que nous devons calculer le nombre de connexions ouvertes de
            // chaque zone. Ce qu'on va faire, c'est itérer sur tout les TileSide (côtés de la tuile) [1]
            // et regarder quelles sont les zones qui appartiennent à ce côté [2]. Pour chaque telle zone, nous allons
            // incrémenter le nombre de connexion ouvertes de 1. [3]
            int[] nbOpenings = new int[10];

            // On va parcourir d'abord toutes les zones sur les côtés de la tuile [1]
            final Set<Zone.Lake> countedLakes = new HashSet<>();
            for (Zone sideZone : tile.sideZones()) {
                for (TileSide tileSide : tile.sides()) {
                    if (tileSide.zones().contains(sideZone)) { // [2]
                        nbOpenings[sideZone.localId()] += 1; // [3]

                        // N'oublions pas les lacs
                        if (sideZone instanceof Zone.River river && river.hasLake()) {
                            Zone.Lake lake = river.lake();
                            if (countedLakes.contains(lake)) continue;

                            countedLakes.add(lake);
                            nbOpenings[river.lake().localId()] += 1;
                        }
                    }
                }
                // Ajouter les zones en tant qu'aires, inoccupées, à nos partitions
                switch(sideZone) {
                    case Zone.Forest forest -> forestBuilder.addSingleton(forest, nbOpenings[forest.localId()]);
                    case Zone.Meadow meadow -> meadowBuilder.addSingleton(meadow, nbOpenings[meadow.localId()]);
                    case Zone.River river -> {
                        if (river.hasLake()) {
                            final Zone.Lake lake = river.lake();
                            riverSystemBuilder.addSingleton(river, nbOpenings[river.localId()] + 1);
                            riverSystemBuilder.addSingleton(lake, nbOpenings[lake.localId()]);
                            riverSystemBuilder.union(river, lake); // Avant de creer l'aire hydrographique, il faut d'abord ajouter les zones river et son lac séparément (conception de union())
                        } else {
                            riverSystemBuilder.addSingleton(river, nbOpenings[river.localId()]);
                        }
                        riverBuilder.addSingleton(river, nbOpenings[river.localId()]);
                    }
                    default -> {}
                }
            }
        }

        /**
         * Connecte les deux bords de tuiles donnés, en connectant entre elles les aires correspondantes
         *
         * @param s1
         * @param s2
         */
        public void connectSides(TileSide s1, TileSide s2){
            Preconditions.checkArgument(s1.isSameKindAs(s2));

            switch (s1) {
                // Connecter les deux zones forêt
                case TileSide.Meadow(Zone.Meadow m1) when s2 instanceof TileSide.Meadow(Zone.Meadow m2):
                    meadowBuilder.union(m1, m2);
                    break;
                    // Connecter les deux zones meadow
                case TileSide.Forest(Zone.Forest m1) when s2 instanceof TileSide.Forest(Zone.Forest m2):
                    forestBuilder.union(m1, m2);
                    break;
                    // Connecter les deux zones rivière
                case TileSide.River riverTile1 when s2 instanceof TileSide.River riverTile2:
                    riverBuilder.union(riverTile1.river(), riverTile2.river());
                    break;
                default:
                    break;
            }
        }

        /**
         * ajoute un occupant initial, de la sorte donnée et appartenant au joueur donné, à l'aire contenant la zone donnée
         * ou lève IllegalArgumentException si la sorte d'occupant donnée ne peut pas occuper une zone de la sorte donnée
         *
         * @param player
         * @param occupantKind
         * @param occupiedZone
         * @throws IllegalArgumentException si la sorte d'occupant donnée ne peut pas occuper une zone de la sorte donnée
         */
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone) throws IllegalArgumentException {
            switch (occupantKind) {
                case PAWN:
                    switch (occupiedZone) {
                        case Zone.Meadow m1:
                            meadowBuilder.addInitialOccupant(m1, player);
                            break;
                        case Zone.Forest m1:
                            forestBuilder.addInitialOccupant(m1, player);
                            break;
                        case Zone.Water m1:
                            if (m1 instanceof Zone.River river) {
                                riverBuilder.addInitialOccupant(river, player);
                                break;
                            }
                            riverSystemBuilder.addInitialOccupant(m1, player);
                            break;
                    }
                    break;
                case HUT:
                    if (Objects.requireNonNull(occupiedZone) instanceof Zone.Water m1) {
                        riverSystemBuilder.addInitialOccupant(m1, player);
                    } else {
                        throw new IllegalArgumentException();
                    }
                    break;
            }
        }

        /**
         * supprime un occupant (un pion) appartenant au joueur donné de l'aire contenant la zone donnée
         * ou lève IllegalArgumentException si la zone est un lac
         *
         * @param player
         * @param occupiedZone
         */
        public void removePawn(PlayerColor player, Zone occupiedZone) {
            Preconditions.checkArgument(!(occupiedZone instanceof Zone.Lake));

            switch (occupiedZone) {
                case Zone.Meadow m1:
                    meadowBuilder.removeOccupant(m1, player);
                    break;
                case Zone.Forest m1:
                    forestBuilder.removeOccupant(m1, player);
                    break;
                case Zone.River m1:
                    riverBuilder.removeOccupant(m1, player);
                    break;
                default:
                    break;
            }
        }

        /**
         * Supprimer tous les occupents de la forêt donnée
         *
         * @param forest
         */
        public void clearGatherers(Area<Zone.Forest> forest) {
            forest.occupants().clear();
        }

        /**
         * Supprimer tous les occupents de la rivière donnée
         *
         * @param river
         */
        public void clearFishers(Area<Zone.River> river) {
            river.occupants().clear();
        }

        /**
         * Finaliser la construction d'une ZonePartitions
         *
         * @return la ZonePartitions finalisée
         */
        public ZonePartitions build() {
            return new ZonePartitions(forestBuilder.build(), meadowBuilder.build(), riverBuilder.build(), riverSystemBuilder.build());
        }
    }
}
