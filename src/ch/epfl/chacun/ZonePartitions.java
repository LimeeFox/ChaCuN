package ch.epfl.chacun;

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

    public static final class Builder {

        private final ZonePartition.Builder<Zone.Forest> forestBuilder = new ZonePartition.Builder<>(new ZonePartition<>());
        private final ZonePartition.Builder<Zone.Meadow> meaedowBuilder = new ZonePartition.Builder<>(new ZonePartition<>());
        private final ZonePartition.Builder<Zone.River> riverBuilder = new ZonePartition.Builder<>(new ZonePartition<>());
        private final ZonePartition.Builder<Zone.Water> riverSystemBuilder = new ZonePartition.Builder<>(new ZonePartition<>());

        Builder(ZonePartitions initial) {
           // help
        }

        public void addTile(Tile tile) {

        }

        /**
         * Connecte les deux bords de tuiles donnés, en connectant entre elles les aires correspondantes
         *
         * @param s1
         * @param s2
         */
        public void connectSides(TileSide s1, TileSide s2) {
            Preconditions.checkArgument(s1.isSameKindAs(s2));

            switch (s1) {
                case TileSide.Meadow(Zone.Meadow m1)
                        when s2 instanceof TileSide.Meadow(Zone.Meadow m2) ->
                    // Connecter les deux zones meadow
                case TileSide.Forest(Zone.Forest m1)
                        when s2 instanceof TileSide.Forest(Zone.Forest m2) ->
                    // Connecter les deux zones foret
                case TileSide.River(Zone.River m1)
                        when s2 instanceof TileSide.River(Zone.River m2) ->
                    // Connecter les deux zones riviere
                default ->
                // … lève une exception
            }
        }

        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone) {

        }

        public void removePawn(PlayerColor player, Zone occupiedZone) {
            occupiedZone.
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

        public ZonePartitions build() {
            return new ZonePartitions();
        }
    }



}
