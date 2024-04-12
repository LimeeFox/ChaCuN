package ch.epfl.chacun;

/**
 * Position d'une case sur le plateau de jeu
 *
 * @param x
 *         la composante EST-OUEST (croissant à l'EST et décroissant à l'OUEST)
 * @param y
 *         la composante SUD-NORD (croissant au SUD et décroissnt au NORD)
 * @author Cyriac Philippe (360553)
 */
public record Pos(int x, int y) {
    public static final Pos ORIGIN = new Pos(0, 0);

    /**
     * Translation de la position
     *
     * @param dX
     *         difference de coordonnée x
     * @param dY
     *         difference de coordonnée y
     * @return position obtenue après translation
     */
    public Pos translated(int dX, int dY) {
        Pos translatedPos = new Pos(this.x()
                + dX, this.y() + dY);
        return translatedPos;
    }

    /**
     * Trouver la tuile voisine en fonction de la direction donnée
     *
     * @param direction
     *         direction du voisin par rapport à this
     * @return position de la tuile voisine
     */
    public Pos neighbor(Direction direction) {
        Pos neighbourPosition = this;
        switch (direction) {
            case N -> neighbourPosition = translated(0, -1);
            case E -> neighbourPosition = translated(1, 0);
            case S -> neighbourPosition = translated(0, 1);
            case W -> neighbourPosition = translated(-1, 0);
        }
        return neighbourPosition;
    }
}
