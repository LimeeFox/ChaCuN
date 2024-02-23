package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Interface pour un bord de tuile
 *
 * @author Cyriac Philippe (360553)
 */
public sealed interface TileSide {

    /**
     * Les zones qui touchent le bord représenté par this
     */
    public abstract List<Zone> zones();

    /**
     * Vérifié si un bord de tuile et du même type que this
     *
     * @param that
     *          bord de tuile voisin
     */
    public abstract boolean isSameKindAs(TileSide that);

    /**
     * Bord de tuile de type forêt
     * 
     * @param forest
     *          zone forêt qui touche le bord
     */
    public record Forest(Zone.Forest forest) implements TileSide {

        /**
         * Les zones qui touchent le bord représenté par this
         *
         * @return zoneList
         *          liste des zones en bord de tuile
         */
        @Override
        public List<Zone> zones() {
            List<Zone> zoneList = List.of(forest);
            return zoneList;
        }

        /**
         * Vérifie si un bord de tuile est de type forêt
         *
         * @param that
         *          un bord de tuile voisin à la zone this
         * @return sameKind
         *          si le bord de tuile est de type forêt
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            boolean sameKind = that instanceof TileSide.Forest;
            return sameKind;
        }
    }

    /**
     * Bord de tuile de type pré
     * 
     * @param meadow
     *          zone pré qui touche un bord
     */
    public record Meadow(Zone.Meadow meadow) implements TileSide {

        /**
         * Les zones qui touchent le bord représenté par this
         *
         * @return zoneList
         *          liste des zones en bord de tuile
         */
        @Override
        public List<Zone> zones() {
            List<Zone> zoneList = List.of(meadow);
            return zoneList;
        }

        /**
         * Vérifie si un bord de tuile est de type pré
         *
         * @param that
         *          un bord de tuile voisin à la zone this
         * @return sameKind
         *          si le bord de tuile est de type pré
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            boolean sameKind = that instanceof TileSide.Meadow;
            return sameKind;
        }
    }

    /**
     * Un bord de tuile de type rivière
     * 
     * @param meadow1
     *          première zone pré qui entoure la rivière et touche le bord
     * @param river
     *          zone rivière qui touche le bord
     * @param meadow2
     *          seconde zone pré qui entoure la rivière et touche le bord
     */
    public record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2)
            implements TileSide {

        /**
         * Les zones qui touchent le bord représenté par this
         * 
         * @return zoneList
         *          liste des zones en bord de tuile
         */
        @Override
        public List<Zone> zones() {
            List<Zone> zoneList = List.of(meadow1, river, meadow2);
            return zoneList;
        }

        /**
         * Vérifie si un bord de tuile est de type river
         *
         * @param that
         *          bord de tuile voisin
         * @return sameKind
         *          si le bord de tuile est de type rivière
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            boolean sameKind = that instanceof TileSide.River;
            return sameKind;
        }
    }
}
