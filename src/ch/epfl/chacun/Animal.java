package ch.epfl.chacun;

/**
 * Animal avec un identifié sur le plateau
 *
 * @author Cyriac Philippe (360553)
 *
 * @param id
 *          identifiant général de l'animal
 * @param kind
 *          type de l'animal : mammouth, auroch,
 *          cerf, smilodon
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
     * @return animalTileId
     *          identifiant de la case sur laquelle se trouve l'animal
     */
    public int tileId() {
        int animalTileId = Math.floorDiv(id, 100);
        return animalTileId;
    }
}
