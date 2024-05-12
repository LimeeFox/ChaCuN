package ch.epfl.chacun.gui;

import javafx.scene.image.Image;

/**
 * Classe qui offre des méthodes permettantes de charger des images des tuiles
 *
 * @author Vladislav Yarkovoy (362242)
 * @author Cyriac Philippe (360553)
 */
public abstract class ImageLoader {

    public static final int LARGE_TILE_PIXEL_SIZE = 512; //Taille des grandes tuiles
    public static final int LARGE_TILE_FIT_SIZE = 256; //Taille d'affichage des grandes tuiles
    public static final int NORMAL_TILE_PIXEL_SIZE = 256; //Taille des tuiles normales
    public static final int NORMAL_TILE_FIT_SIZE = 128; //Taille d'affichage des tuiles normales
    public static final int MARKER_PIXEL_SIZE = 96; //Taille du marqueur
    public static final int MARKER_FIT_SIZE = 48; //Taille d'affichage du marqueur

    /**
     * @param tileId
     *         l'identifiant de la tuile
     * @return une image de la tuile qui corréspond à @tileId @todo check si l'utilisation de cette methode gere elle-meme le cas ou tileId serait invalide
     * Obtenir une tuile selon son @tileId de taille 256px
     */
    public static Image normalImageForTile(int tileId) {
        return new Image(STR."\{NORMAL_TILE_PIXEL_SIZE}/\{String.format("%02d", tileId)}.jpg");

    }

    /**
     * @param tileId
     *         l'identifiant de la tuile
     * @return une image de la tuile qui corréspond à @tileId @todo check si l'utilisation de cette methode gere elle-meme le cas ou tileId serait invalide
     * Obtenir une tuile selon son @tileId de taille 512px
     */
    public static Image largeImageForTile(int tileId) {
        return new Image(STR."\{LARGE_TILE_PIXEL_SIZE}/\{String.format("%02d", tileId)}.jpg");
    }
}
