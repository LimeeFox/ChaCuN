package ch.epfl.chacun;

/**
 * Position d'une case sur le plateau de jeu
 *
 * @author Cyriac Philippe (360553)
 *
 * @param x
 * @param y
 */
public record Pos(int x, int y) {
    public final static Pos ORIGIN = new Pos(0, 0);

    /**
     * Translation de la position
     *
     * @param dX
     *          difference de coordonnée x
     * @param dY
     *          difference de coordonnée y
     * @return translatedPos
     *          position obtenue après translation
     */
    public Pos translated(int dX, int dY) {
        Pos translatedPos = new Pos(this.x()
                + dX, this.y() + dY);
        return translatedPos;
    }

    /**
     * Tuile voisine
     *
     * @param direction
     *          direction du voisin par rapport à this
     * @return neighbourPosition
     *          position de la tuile voisine
     */
    public Pos neighbour(Direction direction) {
        Pos neighbourPosition = this;
        switch (direction) {
            case N -> neighbourPosition = translated(0, -1);

            case E -> neighbourPosition = translated(-1, 0);

            case S -> neighbourPosition = translated(0, 1);

            case W -> neighbourPosition = translated(1, 0);
        }
        return neighbourPosition;
    }
}
