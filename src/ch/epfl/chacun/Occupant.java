package ch.epfl.chacun;

import java.util.Objects;

/**
 * Occupant d'une tuile identifiée
 *
 * @author Cyriac Philippe (360553)
 *
 * @param kind
 *          type d'occupant : pion, hutte
 * @param zoneId
 *          identifiant de la zone
 */
public record Occupant(Kind kind, int zoneId) {

    // Types d'occupants
    public enum Kind {
        PAWN,
        HUT
    }

    // Constructeur compact
    public Occupant {
        Objects.requireNonNull(kind);
        Preconditions.checkArgument(zoneId >= 0);
    }

    /**
     * Conte les occupants d'une même sorte que possède un joueur
     *
     * @param kind
     *          type d'occupants
     * @return occupantsOfKind
     *          nombre total d'occupants du même type que possède
     *          un joueur
     */
    public static int occupantsCount(Kind kind) {
        int occupantsOfKind = 0;
        if (kind.equals(Kind.PAWN)) {
            occupantsOfKind = 5;
        } else {
            occupantsOfKind = 3;
        }
        return occupantsOfKind;
    }
}
