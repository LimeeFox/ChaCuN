package ch.epfl.chacun;

/**
 * Classe Preconditions qui offre la possibilité de vérifier si la précondition pour certaines méthodes et valide.
 * En d'autres termes, si l'on peut exécuter la méthode sans erreurs.
 *
 * @author Vladislav Yarkovoy (362242)
 * @author Cyriac Philippe (360553)
 */
public final class Preconditions {
    private Preconditions() {
    }

    /**
     * Vérifie si la condition execution est validé.
     *
     * @param mustBeTrue
     *         condition pour continuer correctement le programme
     * @throws IllegalArgumentException si la condition est fausse
     */
    public static void checkArgument(boolean mustBeTrue) {
        if (!mustBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}