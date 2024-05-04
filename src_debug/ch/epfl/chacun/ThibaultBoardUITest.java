package ch.epfl.chacun;

import ch.epfl.chacun.gui.*;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ThibaultBoardUITest extends Application{
    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        var playerNames = Map.of(
                PlayerColor.RED, "Thibault",
                PlayerColor.BLUE, "Adrien",
                PlayerColor.GREEN, "Dylan");
        var playerColors = playerNames.keySet().stream().sorted().toList();

        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        var startDeck = tilesByKind.get(Tile.Kind.START);
        var normalDeck = tilesByKind.get(Tile.Kind.NORMAL);
        var menhirDeck = tilesByKind.get(Tile.Kind.MENHIR);

        Collections.shuffle(normalDeck);
        Collections.shuffle(menhirDeck);

        var tileDecks = new TileDecks(startDeck, normalDeck, menhirDeck);

        var textMaker = new TextMakerFr(playerNames);

        var gameState0 =
                GameState.initial(playerColors,
                        tileDecks,
                        textMaker);

        var gameState1 = gameState0.withStartingTilePlaced();

        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.<Occupant>of());
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        var gameState = new SimpleObjectProperty<>(gameState0);


        var boardNode = BoardUI
                .create(12,
                        gameState,
                        tileToPlaceRotationP,
                        visibleOccupantsP,
                        highlightedTilesP,
                        r -> {
                            System.out.println("Rotate: " + r);
                            tileToPlaceRotationP.set(tileToPlaceRotationP.get().add(r));
                        },
                        t -> {
                            System.out.println("Place: " + t);
                            gameState.set(gameState.get().withPlacedTile(new PlacedTile(gameState.get().tileToPlace(), gameState.get().currentPlayer(),tileToPlaceRotationP.get(), t)));
                            if(gameState.get().nextAction() == GameState.Action.OCCUPY_TILE){
                                Set<Occupant> newVisibleOccupants = gameState.get().board().occupants();
                                newVisibleOccupants.addAll(gameState.get().lastTilePotentialOccupants());
                                visibleOccupantsP.set(newVisibleOccupants);
                            }
                        },
                        o -> {
                            System.out.println("Select: " + o);
                            if(gameState.get().nextAction() == GameState.Action.OCCUPY_TILE)
                                gameState.set(gameState.get().withNewOccupant(o));
                            else
                                gameState.set(gameState.get().withOccupantRemoved(o));
                            visibleOccupantsP.set(gameState.get().board().occupants());
                        });

        gameState.set(gameState.get().withStartingTilePlaced());

        var rootNode = new BorderPane(boardNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}