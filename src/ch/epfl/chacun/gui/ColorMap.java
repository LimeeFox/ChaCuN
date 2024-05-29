package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;

/**
 * Classe qui permet d'associer chaque couleur PlayerColor à une couleur
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */
public abstract class ColorMap {
    /**
     * Obtenir la couleur de remplissage en fonction de @playerColor
     *
     * @param playerColor
     *         couleur du joueur à convertir
     * @return couleur du type Color pour le remplissage de formes
     */
    public static Color fillColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case BLUE -> Color.BLUE;
            case RED -> Color.RED;
            case GREEN -> Color.LIME;
            case YELLOW -> Color.YELLOW;
            case PURPLE -> Color.PURPLE;
        };
    }

    /**
     * Obtenir la couleur du contour en fonction de @playerColor
     *
     * @param playerColor
     *         couleur du joueur à convertir
     * @return couleur du type Color pour le contour des formes
     */
    public static Color strokeColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case YELLOW, GREEN -> Color.WHITE.deriveColor(0, 1, 0.6, 1);
            default -> Color.WHITE;
        };
    }
}
