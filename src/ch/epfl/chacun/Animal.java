package ch.epfl.chacun;

/**
 * Animal avec un identifié sur le plateau
 *
 * @author Cyriac Philippe (360553)
 *
 * @param id
 *          identifiant de l'animal
 * @param kind
 *          type de l'animal : mammouth, auroch,
 *          cerf, smilodon
 */
public record Animal(int id, Kind kind) {
    enum Kind {
        MAMMOTH,
        AUROCHS,
        DEER,
        TIGER
    }

    /**
     * Identifiant de la case de l'animal
     *
     * @return
     */
    public int tileId() {
        //necessite Zone pour la completion
        int animalTileId = id;

    }
}
