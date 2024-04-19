package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMaker;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.HashSet;
import java.util.Set;

import static java.lang.StringTemplate.STR;

/**
 *
 */
public abstract class PlayersUI {

    /**
     * Créé une Node selon le graphe suivant: <a href="https://cs108.epfl.ch/p/i/players-sg;32.png">...</a>
     *
     * @param currentGameState
     *         l'état actuel du jeu
     * @param textMaker
     * @return la @Node de l'interface graphique qui contient les joueurs
     */
    public static Node create(ObservableValue<GameState> currentGameState, TextMaker textMaker) {

        // Creation de nodes pour chaque joueurs
        Set<TextFlow> playerNodes = new HashSet<>();

        TextFlow currentPlayer = new TextFlow();
        currentPlayer.getStyleClass().add("player");
        currentPlayer.getStyleClass().add("current");
        playerNodes.add(currentPlayer);

        for (PlayerColor playerColor : currentGameState.getValue().players()) {
            // On a besoin d'un cercle
            Circle circle = new Circle();
            circle.setRadius(5);
            circle.setFill(ColorMap.fillColor(playerColor));

            // On a besoin du nom du joueur et ses points
            Text playerInfo = new Text(STR." \{textMaker.playerName(playerColor)} : todo:pts \n}"); //todo points

            // On a besoin de ses occupants
            SVGPath svgHut1 = (SVGPath) Icon.newFor(playerColor, Occupant.Kind.HUT);
            SVGPath svgHut2 = (SVGPath) Icon.newFor(playerColor, Occupant.Kind.HUT);
            SVGPath svgHut3 = (SVGPath) Icon.newFor(playerColor, Occupant.Kind.HUT);

            Text occupantSpaces = new Text("   ");

            SVGPath svgPawn1 = (SVGPath) Icon.newFor(playerColor, Occupant.Kind.PAWN);
            SVGPath svgPawn2 = (SVGPath) Icon.newFor(playerColor, Occupant.Kind.PAWN);
            SVGPath svgPawn3 = (SVGPath) Icon.newFor(playerColor, Occupant.Kind.PAWN);
            SVGPath svgPawn4 = (SVGPath) Icon.newFor(playerColor, Occupant.Kind.PAWN);
            SVGPath svgPawn5 = (SVGPath) Icon.newFor(playerColor, Occupant.Kind.PAWN);

            // On ajoute tout
            TextFlow player = new TextFlow(circle, playerInfo, svgHut1, svgHut2, svgHut3, occupantSpaces, svgPawn1,
                    svgPawn2, svgPawn3, svgPawn4, svgPawn5);
            player.getStyleClass().add("player");
            playerNodes.add(player);
        }

        VBox playersBox = new VBox();
        playersBox.setId("players");
        playersBox.setStyle("/resources/players.css");

        for (Node node : playerNodes) {
            playersBox.getChildren().add(node);
        }

        return playersBox;
    }
}
