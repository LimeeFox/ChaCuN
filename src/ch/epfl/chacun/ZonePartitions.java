package ch.epfl.chacun;

import java.util.Objects;

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
         * connecte les deux bords de tuiles donnés, en connectant entre elles les aires correspondantes
         * ou lève IllegalArgumentException si les deux bords ne sont pas de la même sorte
         *
         * @param tile
         */
        public void addTile(Tile tile) {

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
