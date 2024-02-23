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
    public  static final int COUNT = ALL.size();

    /**
     * Ajout d'un rotation that à la rotation this
     *
     * @param that
     *          rotation ajoutée au récepteur
     * @return All.get(addTurnCW)
     *          la rotation une fois que this et that sont additionnés
     */
    public Rotation add(Rotation that) {
        int addTurnCW = (this.quarterTurnsCW() + that.quarterTurnsCW())%4;
        return ALL.get(addTurnCW);
    }

    /**
     * Negation de la rotation
     *
     * @return negationRotation
     *          rotation négative par rapport à la rotation initiale
     */
    public Rotation negated() {
        int negationIndex = (4 - ordinal()) % COUNT;
        Rotation negationRotation = ALL.get(negationIndex);
        return negationRotation;
    }

    /**
     * Nombre de quarts de tour dans le sens horaire
     *
     * @return ordinal()
     *          indice de la rotation selon l'ordre d'énumération
     */
    public int quarterTurnsCW() {
        return ordinal();
    }

    /**
     * Nombre de degrés dans le sens horaire
     *
     * @return quarterTurnsCW()*90
     *      le nombre de quarts de tour multiplié par
     *      le nombre de degrés dans un quart
     */
    public int degreesCW() {
        return quarterTurnsCW()*90;
    }
}