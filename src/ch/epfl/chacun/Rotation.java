package ch.epfl.chacun;

import java.util.List;

/**
 * Une rotation de tuile.
 *
 * @author Cyriac Philippe (360553)
 */

public enum Rotation {
    //Toutes les rotations possibles pour une tuile.
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT;

    //List des rotations énumérées ci-dessus.
    public static final List<Rotation> ALL = List.of(Rotation.values());
    // Nombre total de rotation possible. Soit la size de ALL.
    public static final int COUNT = ALL.size();

    /**
     * Ajout d'un rotation that à la rotation this
     *
     * @param that
     *         rotation ajoutée au récepteur
     * @return la rotation une fois que this et that sont additionnés
     */
    public Rotation add(Rotation that) {
        return ALL.get((this.ordinal() + that.quarterTurnsCW()) % COUNT);
    }

    /**
     * Negation de la rotation
     *
     * @return rotation négative par rapport à la rotation initiale
     */
    public Rotation negated() {
        return ALL.get((4 - ordinal()) % COUNT);
    }

    /**
     * Nombre de quarts de tour dans le sens horaire
     *
     * @return indice de la rotation selon l'ordre d'énumération
     */
    public int quarterTurnsCW() {
        return ordinal();
    }

    /**
     * Nombre de degrés dans le sens horaire
     *
     * @return le nombre de quarts de tour multiplié par le nombre de degrés dans un quart
     */
    public int degreesCW() {
        return quarterTurnsCW() * 90;
    }
}