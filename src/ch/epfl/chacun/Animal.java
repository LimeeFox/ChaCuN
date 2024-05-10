package ch.epfl.chacun;

/**
 * Animal avec un identifié sur le plateau
 *
 * @param id
 *         identifiant général de l'animal
 * @param kind
 *         type de l'animal : mammouth, auroch, cerf, smilodon
 * @author Vladislav Yarkovoy (362242)
 * @author Cyriac Philippe (360553)
 */
public record Animal(int id, Kind kind) {
    public enum Kind {
        MAMMOTH,
        AUROCHS,
        DEER,
        TIGER
    }

    /**
     * Identifiant de la case de l'animal
     *
     * @return identifiant de la case sur laquelle se trouve l'animal
     */
    public int tileId() {
        return Zone.tileId(Math.floorDiv(id, 10));
    }
}
