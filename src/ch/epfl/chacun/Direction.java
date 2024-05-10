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
     * Applique une rotation à la direction
     *
     * @param rotation
     *          rotation que l'on souhaite appliquer à la direction this
     * @return la direction résultant de la rotation appliquée
     */
    public Direction rotated(Rotation rotation) {
        return ALL.get((ordinal() + rotation.quarterTurnsCW()) % COUNT);
    }

    /**
     * Inverse de la direction
     *
     * @return direction inverse à this
     */
    public Direction opposite() {
        return this.rotated(Rotation.HALF_TURN);
    }
}
