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
     * Créé une Node selon le graphe suivant: <a href="https://cs108.epfl.ch/p/i/players-sg;32.png">...</a>
     *
     * @param currentGameState
     *         l'état actuel du jeu
     * @param textMaker
     *         l'outil qui servira à afficher les messages sur le tableau de messages
     * @return la @Node de l'interface graphique qui contient les joueurs
     */
    public static Node create(ObservableValue<GameState> currentGameState, TextMaker textMaker) {
        // Creation de nodes pour chaque joueur
        Map<PlayerColor, TextFlow> playerNodes = new TreeMap<>();
        GameState gameState = currentGameState.getValue();

        for (PlayerColor playerColor : gameState.players()) {
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
            playerNodes.put(playerColor, player);
        }

        VBox playersBox = new VBox();
        playersBox.setId("players");
        playersBox.getStylesheets().add("players.css");
        playersBox.getChildren().addAll(playerNodes.values());

        // Listener pour mettre à jour le joueur courant lors des changements d'état du jeu
        ObservableValue<PlayerColor> currentPlayer0 = currentGameState.map(GameState::currentPlayer);
        currentPlayer0.addListener((observable, oldPlayer, newPlayer) -> {
            // Enlever le highlight du joueur précedant pour le mettre sur le joueur courant
            System.out.println(playerNodes);
            playerNodes.get(oldPlayer).getStyleClass().remove("current");
            playerNodes.get(newPlayer).getStyleClass().add("current");
        });

        // Listener pour mettre à jour les pions des joueurs lors des changements d'état du jeu
        ObservableValue<Integer> freeHutsCount = currentGameState
                .map(gs -> gs.freeOccupantsCount(gs.currentPlayer(), Occupant.Kind.HUT));
        ObservableValue<Integer> freePawnsCount = currentGameState
                .map(gs -> gs.freeOccupantsCount(gs.currentPlayer(), Occupant.Kind.PAWN));

        // On itère sur tous les joueurs pour obtenir leur liste de nodes "occupants" //todo potentially optimize this, similarly to adrien (I told him how to do it btw)
        for (PlayerColor playerColor : gameState.players()) {
            TextFlow player = playerNodes.get(playerColor);
            Set<SVGPath> occupants = player.getChildren().stream()
                    .filter(child -> child instanceof SVGPath)
                    .map(child -> (SVGPath) child)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            // On saute les n premiers occupants non placés afin de rendre ceux aux extrêmes droites opaques
            occupants.stream()
                    .skip(freeHutsCount.getValue())
                    .limit(3 - freeHutsCount.getValue())
                    .forEach(svg -> svg.opacityProperty().bind(Bindings.createDoubleBinding(() -> 0.1)));

            // Pareil mais pour les pions
            occupants.stream().skip(freeHutsCount.getValue() + freePawnsCount.getValue()).forEach(svg -> {
                svg.opacityProperty().bind(Bindings.createDoubleBinding(() -> 0.1));
            });
        }

        return playersBox;
    }
}
