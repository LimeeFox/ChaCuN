package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        /*
        L'initialisation des joueurs ainsi que de la graine du jeu
         */
        Map<PlayerColor, String> players = new HashMap<>();
        List<String> playerNames = getParameters().getUnnamed();

        // todo check if others did it like that too, cuz this feels too complicated (bazooka squirrel)
        final Iterator<PlayerColor> colorIterator = Arrays.asList(PlayerColor.values()).iterator();
        playerNames.forEach(name -> {
            //if (colorIterator.hasNext()) { // fixme dans la consigne, ils disent que par soucis de simplicité on peut juste laisser le programme planter si il y a trop de joueurs, ducoup j'enleve ce if mais je le garde au cas ou
            players.put(colorIterator.next(), name);
            //}
        });

        Map<String, String> seedArgument = getParameters().getNamed();
        TextMakerFr textMakerFr = new TextMakerFr(players);

        /*
        Mélange de tuiles
         */
        final long seed = Long.parseUnsignedLong(seedArgument.get("seed"));
        final List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tiles, RandomGeneratorFactory.getDefault().create(seed));

        Map<Tile.Kind, List<Tile>> decks = tiles.stream().collect(Collectors.groupingBy(Tile::kind));
        TileDecks tileDecks =
                new TileDecks(decks.get(Tile.Kind.START), decks.get(Tile.Kind.NORMAL), decks.get(Tile.Kind.MENHIR));

        /*
        Les valeurs observables à créer
         */
        // L'état du jeu
        ObjectProperty<GameState> gameState = new SimpleObjectProperty<>(
                GameState.initial(players.keySet().stream().toList(), tileDecks, textMakerFr));

        // La rotation des tuiles
        ObjectProperty<Rotation> tileRotation = new SimpleObjectProperty<>(Rotation.NONE);

        // Les occupants
        ObjectProperty<Set<Occupant>> visibleOccupants = new SimpleObjectProperty<>(Set.of());

        // Les tuiles à mettre en évidence
        ObjectProperty<Set<Integer>> highlightedTiles = new SimpleObjectProperty<>(Set.of());

        // La tuile à placer sur le plateau
        ObservableValue<Tile> tileToPlace = gameState.map(GameState::tileToPlace); // gs -> gs.tileDecks().normalTiles().getFirst() //fixme GameState::tileToPlace, in the meantime i did smth hacky but i wanna kms

        // Le nombre de tuiles restantes dans les piles
        ObservableValue<Integer> normalTilesLeft = gameState.map(g -> g.tileDecks().normalTiles().size());
        ObservableValue<Integer> menhirTilesLeft = gameState.map(g -> g.tileDecks().menhirTiles().size());

        // Le message à afficher
        ObjectProperty<String> message = new SimpleObjectProperty<>("test"); // todo update this with either TextMakerFR.clickToOccupy and TextMakerFR.clickToUnoccupy

        // un autre truc de messageboard que jai pas trop compris todo rewrite comment lmao
        ObservableValue<List<MessageBoard.Message>> messages = gameState.map(g -> g.messageBoard().messages());

        // Les identités de toutes les tuiles
        ObjectProperty<Set<Integer>> tileIdsToHighlight = new SimpleObjectProperty<>(Set.of());

        /*
        L'interface graphique de droite
        */
        // La Node d'Actions et des Piles du jeu
        VBox decksAndActions = new VBox();
        //Node Actions = todo merge actionsUI
        // fixme System.out.println(tileToPlace.getValue());
        Node Decks = DecksUI.create(tileToPlace, normalTilesLeft, menhirTilesLeft, message,
                occupant -> gameState.getValue().withNewOccupant(occupant));
        //decksAndActions.getChildren().add(Actions); todo merge ActionsUI
        decksAndActions.getChildren().add(Decks);

        // La Node de l'interface des joueurs et l'interface du tableau de messages
        Node Players = PlayersUI.create(gameState, textMakerFr);
        Node MessageBoard = MessageBoardUI.create(messages, tileIdsToHighlight);

        // La racine de la partie droite de l'interface graphique principale du jeu
        BorderPane sideUI = new BorderPane();
        sideUI.setTop(Players);
        sideUI.setCenter(MessageBoard);
        sideUI.setBottom(decksAndActions);

        /*
        L'interface graphique de centre
        */
        Node Board = BoardUI
                .create(
                        ch.epfl.chacun.Board.REACH,
                        gameState,
                        tileRotation,
                        visibleOccupants,
                        highlightedTiles,
                        rotation -> {
                            tileRotation.set(tileRotation.get().add(rotation));
                        },
                        move -> {
                            GameState gs = gameState.getValue();
                            gameState.set(gs.withPlacedTile(new PlacedTile(tileToPlace.getValue(), gs.currentPlayer(), tileRotation.getValue(), move)));
                        },
                        occupant -> {
                            GameState gs = gameState.getValue();
                            GameState.Action nextAction = gs.nextAction();

                            if (nextAction == GameState.Action.OCCUPY_TILE) {
                                gameState.set(gs.withNewOccupant(occupant));
                            } else if (nextAction == GameState.Action.RETAKE_PAWN
                                    && occupant.kind() == Occupant.Kind.PAWN) {
                                gameState.set(gs.withOccupantRemoved(occupant));
                            }

                            visibleOccupants.set(gs.board().occupants());
                        });

        /*
        Mise en commun de toutes les interfaces
        */
        BorderPane root = new BorderPane();

        root.setRight(sideUI);
        root.setCenter(Board);


        Scene scene = new Scene(root, 250, 400);
        stage.setTitle("ChaCuN");
        stage.setScene(scene);
        stage.setWidth(1440);
        stage.setHeight(1080);

        // Une fois le graphe de scène construit, son contenu est modifié pour contenir le résultat de l'application
        // de la méthode withStartingTilePlaced à cet état initial
        gameState.set(gameState.getValue().withStartingTilePlaced());

        stage.show();
    }
}
