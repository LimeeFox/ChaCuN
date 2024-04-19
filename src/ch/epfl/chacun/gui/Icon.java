package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;

/**
 * Classe qui permet de dessiner des icones comme PION ou HUTTE
 */
public abstract class Icon {
    /**
     * Obtenir une forme en fonction de l'occupant et de la couleur de son joueur
     *
     * @param playerColor
     *         la couleur du joueur auquel appartient l'occupant
     * @param kind
     *         le type de l'occupant
     * @return une icone de couleur et contour correspondante au joueur
     */
    public static Node newFor(PlayerColor playerColor, Occupant.Kind kind) {
        SVGPath svg = new SVGPath();

        switch (kind) {
            case PAWN -> svg.setContent("M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L 12 -2 L 12 -4 L 6 -6"
                    + "L 6 -10 L 0 -10 L -2 -4 L -6 -2 L -8 -10 L -12 -10 L -8 6 Z");
            case HUT -> svg.setContent("M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z");
        }

        svg.setFill(ColorMap.fillColor(playerColor));
        svg.setStroke(ColorMap.strokeColor(playerColor));

        return svg;
    }
}
