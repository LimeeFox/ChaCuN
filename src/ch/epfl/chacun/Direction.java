package ch.epfl.chacun;

import java.util.List;

/**
 * Une direction des quatre points cardinaux
 *
 * @author Cyriac Philippe (360553)
 */
public enum Direction {
    N,
    E,
    S,
    W;

    private final static List<Direction> ALL = List.of(Direction.values());
    private final static int COUNT = ALL.size();

    /**
     * Direction après une rotation
     *
     * @return TBD
     */
    public Direction rotates( ) {
        int rotatedDirectionIndex = ordinal() + ();
        return null;
    }

    /**
     * Inverse de la direction
     * @return ALL.get((ordinal()+COUNT/2)%COUNT)
     *          direction inverse de this
     */
    public Direction opposite() {
        return ALL.get((ordinal()+COUNT/2)%COUNT);
    }
}
