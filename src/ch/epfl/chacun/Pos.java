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
     *          position obtenue après translation de x et de y
     */
    public Pos translated(int dX, int dY) {
        Pos translatedPos = new Pos(this.x()
                + dX, this.y() + dY);
        return translatedPos;
    }

    /*
    public Pos neighbour(Direction direction) {

    }
    */
}
