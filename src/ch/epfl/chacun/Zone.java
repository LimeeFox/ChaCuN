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
        RAFT;
    }

    /**
     * Obtention de l'ID de la tuile
     *
     * @param zoneId
     * @return l'identité de la tuile, prête à être assignée à une tuile
     */
    static int tileId(int zoneId) {
        return zoneId * 10;
    }

    /**
     * Obtention de l'ID locale
     *
     * @param zoneId
     * @return l'identité de la zone, prête à être assignée à une zone
     */
    static int localId(int zoneId) {
        return tileId(zoneId) * 10;
    }

    int id();

    int tileId();

    int localId();

    default SpecialPower specialPower() {
        return null;
    }

    /**
     * Zone de forêt dans laquelle on peut (ou pas) trouver des menhirs
     */
    final record Forest(int zoneId, Kind kind) implements Zone {

        // les types de forêts différentes qui existent dans le jeu
        enum Kind {
            PLAIN,
            WITH_MENHIR,
            WITH_MUSHROOMS;
        }

        @Override
        public int id() {
            return 0; // @todo il y a qqch a faire la mais ils disent pas trop encore quoi. Ou sinon il faut que je relise xd
        }

        @Override
        public int tileId() {
            return Zone.tileId(zoneId) + id();
        }

        @Override
        public int localId() {
            return tileId(); // @todo il y a qqch a faire la mais ils disent pas trop encore quoi. Ou sinon il faut que je relise xd
        }
    }

    /**
     * Zone de pré dans laquelle on peut (ou pas) trouver des animaux
     *
     * @param zoneId
     * @param animals
     * @param specialPower
     */
    record Meadow(int zoneId, ArrayList<Animal> animals, SpecialPower specialPower) implements Zone {

        @Override
        public int id() {
            return 0;
        }

        @Override
        public int tileId() {
            return 0;
        }

        @Override
        public int localId() {
            return 0;
        }

        public SpecialPower SpecialPower() {
            return specialPower;
        }
    }

    /**
     * Zone aquatique qui peut contenir 1 ou 2 poissons
     */
    record Water() implements Zone {
        @Override
        public int id() {
            return 0;
        }

        @Override
        public int tileId() {
            return 0;
        }

        @Override
        public int localId() {
            return 0;
        }
    }

    /**
     * Zone de rivière qui peut posséder 0 ou plusieurs poissons, et qui peut (ou pas) faire partie d'un lac
     *
     * @param zoneid
     * @param fishCount
     * @param lake
     */
    record River(int zoneid, int fishCount, Lake lake) implements Zone {

        public boolean hasLake() {
            return lake != null;
        }

        @Override
        public int id() {
            return 0;
        }

        @Override
        public int tileId() {
            return 0;
        }

        @Override
        public int localId() {
            return 0;
        }
    }
}
