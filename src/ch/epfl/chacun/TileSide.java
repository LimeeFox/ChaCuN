package ch.epfl.chacun;

import java.util.List;

/**
 * Interface pour un bord de tuile
 *
 * @author Vladislav Yarkovoy (362242)
 * @author Cyriac Philippe (360553)
 */
public sealed interface TileSide {

    /**
     * Les zones qui touchent le bord représenté par this
     */
    List<Zone> zones();

    /**
     * Méthode qui vérifie si un bord de tuile adjacente est du même type que this
     *
     * @param that
     *         bord de tuile voisine
     */
    boolean isSameKindAs(TileSide that);

    /**
     * Bord de tuile de type forêt
     *
     * @param forest
     *         zone forêt qui touche le bord
     */
    record Forest(Zone.Forest forest) implements TileSide {

        /**
         * Les zones qui touchent le bord représenté par this
         *
         * @return liste des zones en bord de tuile
         */
        @Override
        public List<Zone> zones() {
            return List.of(forest);
        }

        /**
         * Vérifie si un bord de tuile est de type forêt
         *
         * @param that
         *         un bord de tuile voisin à la zone this
         * @return si le bord de tuile est de type forêt
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.Forest;
        }
    }

    /**
     * Bord de tuile de type pré
     *
     * @param meadow
     *         zone pré qui touche un bord
     */
    record Meadow(Zone.Meadow meadow) implements TileSide {

        /**
         * Les zones qui touchent le bord représenté par this
         *
         * @return liste des zones en bord de tuile
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow);
        }

        /**
         * Vérifie si un bord de tuile est de type pré
         *
         * @param that
         *         un bord de tuile voisin à la zone this
         * @return si le bord de tuile est de type pré
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.Meadow;
        }
    }

    /**
     * Un bord de tuile de type rivière
     *
     * @param meadow1
     *         première zone pré qui entoure la rivière et touche le bord
     * @param river
     *         zone rivière qui touche le bord
     * @param meadow2
     *         seconde zone pré qui entoure la rivière et touche le bord
     */
    record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide {

        /**
         * Les zones qui touchent le bord représenté par this
         *
         * @return liste des zones en bord de tuile
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        /**
         * Vérifie si un bord de tuile est de type river
         *
         * @param that
         *         bord de tuile voisin
         * @return si le bord de tuile est de type rivière
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof TileSide.River;
        }
    }
}
