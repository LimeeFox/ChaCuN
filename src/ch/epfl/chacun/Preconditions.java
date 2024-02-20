package ch.epfl.chacun;

/**
 * Preconditions
 *
 * @author Cyriac Philippe (36553)
 */
public final class Preconditions {

    /**
     * Classe non-instantiable qui offre la possibilité
     * de vérifier si la précondition pour certaines
     * méthodes et valide. En d'autres termes, si
     * l'on peut éxécuter la méthode sans erreur.
     */

    private Preconditions() {}


    /**
     * Vérifie si la condition d'éxécution est validé.
     *
     * @param condition
     *          condition d'éxécution
     *
     * @throws IllegalArgumentException
     *          si la condition est fausse
     */
    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }
}