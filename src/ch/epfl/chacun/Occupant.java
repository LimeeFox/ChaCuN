package ch.epfl.chacun;

import java.util.Objects;

/**
 * Occupant d'une tuile identifiée
 *
 * @param kind
 *         type d'occupant : pion, hutte
 * @param zoneId
 *         identifiant de la zone
 * @author Cyriac Philippe (360553)
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
     * Compte les occupants d'une sorte spécifiée (pion ou hutte) que possède
     * un joueur, par défaut
     *
     * @param kind
     *         type d'occupants à compter
     * @return nombre total d'occupants du même type que possède un joueur
     */
    public static int occupantsCount(Kind kind) {
        return kind.equals(Kind.PAWN) ? 5 : 3;
    }
}
