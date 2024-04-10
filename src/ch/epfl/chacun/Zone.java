package ch.epfl.chacun;

import java.util.List;

/**
 * Interface pour les zones d'une tuile
 *
 * @author Vladislav Yarkovoy (362242)
 */
public sealed interface Zone {

    /**
     * Tous les types de pouvoirs spéciaux qui existent pour les zones
     */
    enum SpecialPower {
        SHAMAN,
        LOGBOAT,
        HUNTING_TRAP,
        PIT_TRAP,
        WILD_FIRE,
        RAFT
    }

    /**
     * Obtention de l'ID de la tuile
     *
     * @param zoneId
     *         identifiant de la zone sur le plateau de jeu
     * @return l'identité de la tuile, prête à être assignée à une tuile
     */
    static int tileId(int zoneId) {
        return Math.floorDiv(zoneId, 10);
    }

    /**
     * Obtention de l'ID locale
     *
     * @param zoneId
     *         identifiant de la zone sur le plateau de jeu
     * @return l'identité de la zone, prête à être assignée à une zone
     */
    static int localId(int zoneId) {
        return zoneId % 10;
    }

    int id();

    default int tileId() {
        return tileId(id());
    }

    default int localId() {
        return localId(id());
    }

    default SpecialPower specialPower() {
        return null;
    }

    /**
     * Zone de forêt dans laquelle on peut (ou pas) trouver des menhirs
     *
     * @param id
     *         identifiant de la zone
     * @param kind
     *         type de la forêt
     */
    record Forest(int id, Kind kind) implements Zone {

        // les types de forêts différentes qui existent dans le jeu
        public enum Kind {
            PLAIN,
            WITH_MENHIR,
            WITH_MUSHROOMS
        }
    }

    /**
     * Zone de type pré dans laquelle on peut (ou pas) trouver des animaux
     *
     * @param id
     *         identifiant de la zone
     * @param animals
     *         liste d'animaux de la zone
     * @param specialPower
     *         pouvoir spécial de la zone
     */
    record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone {

        public Meadow {
            animals = List.copyOf(animals);
        }

        public SpecialPower SpecialPower() {
            return specialPower;
        } //todo is this even useful? ask assistant
    }

    /**
     * Zone aquatique qui peut contenir 1 ou 2 poissons
     */
    sealed interface Water extends Zone {
        int fishCount();
    }

    /**
     * Zone du lac qui peut posséder 0 ou plusieurs poissons, et qui peut (ou pas) contenir des pouvoirs spéciaux
     *
     * @param id
     *         identifiant du zone
     * @param fishCount
     *         nombre de poissons dans le lac
     * @param specialPower
     *         pouvoir spécial de la zone
     */
    record Lake(int id, int fishCount, SpecialPower specialPower) implements Zone, Zone.Water {
        //todo why is this empty? it could have SpecialPower() like lake but i dont see the point
    }

    /**
     * Zone de rivière qui peut posséder 0 ou plusieurs poissons, et qui peut (ou pas) faire partie d'un lac
     *
     * @param id
     *         identifiant de la zone
     * @param fishCount
     *         nombre de poissons dans la rivière
     * @param lake
     *         lac connecté à la rivière (ou null si il n'y en a aucun)
     */
    record River(int id, int fishCount, Lake lake) implements Zone, Zone.Water {

        /**
         * Déterminer si la rivière est connectée à un lac
         *
         * @return true ssi la rivière est connectée à un lac
         */
        public boolean hasLake() {
            return lake != null;
        }
    }
}
