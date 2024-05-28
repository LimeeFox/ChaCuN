package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.StringTemplate.STR;

/**
 * Classe qui gère l'interface graphique du joueur
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */
public abstract class PlayersUI {
    /**
     * Créé une intérface graphique qui contient les joueurs et leur statut du jeu (occupants libres, points, etc...)
     *
     * @param currentGameState
     *         l'état actuel du jeu
     * @param textMaker
     *         l'outil qui servira à afficher les messages sur le tableau de messages
     * @return la @Node de l'interface graphique qui contient les joueurs
     */
    public static Node create(ObservableValue<GameState> currentGameState, TextMaker textMaker) {
        // Quelques constantes utiles
        final int HUTS = Occupant.occupantsCount(Occupant.Kind.HUT);
        final int PAWNS = Occupant.occupantsCount(Occupant.Kind.PAWN);

        // La racine de l'interface entière des joueurs
        VBox playersBox = new VBox();
        playersBox.setId("players");
        playersBox.getStylesheets().add("players.css");

        // Creation de nodes pour chaque joueur
        GameState gameState = currentGameState.getValue();
        Set<PlayerColor> p = new TreeSet<>(gameState.players());
        for (PlayerColor playerColor : p) {
            // On a besoin d'un cercle pour indiquer la couleur du joueur aux utilisateurs
            Circle circle = new Circle();
            circle.setRadius(5);
            circle.setFill(ColorMap.fillColor(playerColor));

            // On a besoin du nom du joueur et ses points
            ObservableValue<Map<PlayerColor, Integer>> points0 = currentGameState
                    .map(GameState::messageBoard)
                    .map(MessageBoard::points);
            // Valeur observable contenant le texte des points
            // du joueur de couleur `p` (p.ex. "Dalia : 5 points")
            ObservableValue<String> pointsTextO = points0.map(pointsMap ->
                    STR." \{textMaker.playerName(playerColor)} : "
                            + STR."\{pointsMap.getOrDefault(playerColor, 0)} points \n");

            // Nom et les points du joueur de couleur `p` (mis à jour automatiquement plus loin dans le code!)
            Text pointsText = new Text();
            pointsText.textProperty().bind(pointsTextO);

            // Le TextFlow final du joueur
            TextFlow player = new TextFlow(circle, pointsText);
            player.getStyleClass().add("player");

            // On a besoin de ses huttes
            for (int i = 0; i < Occupant.occupantsCount(Occupant.Kind.HUT); i++) {
                player.getChildren().add(Icon.newFor(playerColor, Occupant.Kind.HUT));
            }

            // Espace pour la séparation visuelle
            player.getChildren().add(new Text("   "));

            // On a besoin de ses pions
            for (int i = 0; i < Occupant.occupantsCount(Occupant.Kind.PAWN); i++) {
                player.getChildren().add(Icon.newFor(playerColor, Occupant.Kind.PAWN));
            }

            // Pour l'initialisation (qui se fait seulement une fois, tout au début de la partie)
            // On aimerait déterminer et mettre en évidence le joueur courant (le premier à jouer)
            if (gameState.currentPlayer() == playerColor) {
                player.getStyleClass().add("current");
            }

            // Auditeur pour mettre à jour le joueur courant lors des changements d'état du jeu
            ObservableValue<PlayerColor> currentPlayer = currentGameState.map(GameState::currentPlayer);
            currentPlayer.addListener((observable, oldPlayer, newPlayer) -> {
                // Enlever la mise en évidence du joueur précedant pour que le nouveau joueur courant le remplace
                if (playerColor == newPlayer) {
                    player.getStyleClass().add("current");
                } else if (playerColor == oldPlayer) {
                    player.getStyleClass().remove("current");
                }
            });

            // Auditeur pour mettre à jour les pions des joueurs lors des changements d'état du jeu
            ObservableValue<Integer> freeHutsCount = currentGameState
                    .map(gs -> gs.freeOccupantsCount(playerColor, Occupant.Kind.HUT));
            ObservableValue<Integer> freePawnsCount = currentGameState
                    .map(gs -> gs.freeOccupantsCount(playerColor, Occupant.Kind.PAWN));

            List<SVGPath> occupants = player.getChildren().stream()
                    .filter(child -> child instanceof SVGPath)
                    .map(child -> (SVGPath) child)
                    .collect(Collectors.toCollection(LinkedList::new));

            // On saute les n premiers occupants non placés afin de rendre ceux aux extrêmes droites opaques
            for (int i = 0 ; i < HUTS ; i++) {
                final int index = i;
                occupants.get(i).opacityProperty().bind(freeHutsCount.map(freeHuts -> {
                    if (index < freeHuts) {
                        return 1.0;
                    }
                    return 0.1;
                }));
            }

            for (int i = HUTS ; i < HUTS + PAWNS ; i++) {
                final int index = i;
                occupants.get(i).opacityProperty().bind(freePawnsCount.map(freePawns -> {
                    if (index < freePawns + HUTS) {
                        return 1.0;
                    }
                    return 0.1;
                }));
            }

            playersBox.getChildren().add(player);
        }

        return playersBox;
    }
}
