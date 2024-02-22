package ch.epfl.chacun;

import java.util.List;

/**
 * Une direction des quatre points cardinaux
 *
 * @author Cyriac Philippe (360553)
 */
public enum Direction {

    // Directions cardinales, dans l'ordre horaire
    N,
    E,
    S,
    W;

    // Liste des directions cardinales
    public static final List<Direction> ALL = List.of(Direction.values());

    // Nombre total de directions cardinales: 4
    public static final int COUNT = ALL.size();

    /**
     * Direction après une rotation
     *
     * @return rotatedDirection
     *          nouvelle direction obtenue par rotation
     */
    public Direction rotates(Rotation rotation) {
        Direction rotatedDirection;
        int rotatedDirectionIndex = ordinal() + rotation.quarterTurnsCW();
        rotatedDirection = ALL.get(rotatedDirectionIndex);
        return rotatedDirection;
    }

    /**
     * Inverse de la direction
     * @return ALL.get((ordinal()+COUNT/2)%COUNT)
     *          direction inverse de this
     */
    public Direction opposite() {
        Direction oppositeDirection = ALL.get((ordinal() + COUNT / 2) % COUNT);
        return oppositeDirection;
    }
}
