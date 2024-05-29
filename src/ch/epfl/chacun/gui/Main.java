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
        Map<PlayerColor, String> players = new TreeMap<>();
        List<String> playerNames = getParameters().getUnnamed();


        final Iterator<PlayerColor> colorIterator = Arrays.asList(PlayerColor.values()).iterator();
        playerNames.forEach(name -> players.put(colorIterator.next(), name));

        Map<String, String> seedArgument = getParameters().getNamed();
        TextMakerFr textMakerFr = new TextMakerFr(players);

        /*
        Mélange de tuiles
         */
        final long seed = Long.parseUnsignedLong(seedArgument.get("seed"));
        final List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tiles, RandomGeneratorFactory.getDefault().create(seed));

        final List<Tile> tiless = new ArrayList<>(Tiles.TILES.stream().filter(tile -> ((tile.kind() == Tile.Kind.NORMAL && tile.id() % 4 == 0 ||
                        (tile.kind() == Tile.Kind.MENHIR && (tile.id() == 88)) || tile.kind() == Tile.Kind.START)))
                .toList());

        Map<Tile.Kind, List<Tile>> decks = tiless.stream().collect(Collectors.groupingBy(Tile::kind));
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

        // Les occupants qui doivent s'afficher (ceux à placer ainsi que ceux déjà placés)
        ObservableValue<Set<Occupant>> visibleOccupants = gameState.map(gs -> {
            Set<Occupant> occupants = new HashSet<>();

            if (gs.nextAction() == GameState.Action.OCCUPY_TILE) {
                occupants.addAll(gs.lastTilePotentialOccupants());
            } else {
                occupants.removeAll(gs.lastTilePotentialOccupants());
            }

            occupants.addAll(gs.board().occupants());
            return occupants;
        });

        // Les tuiles à mettre en évidence
        ObjectProperty<Set<Integer>> highlightedTiles = new SimpleObjectProperty<>(Set.of());

        // La tuile à placer sur le plateau
        ObservableValue<Tile> tileToPlace = gameState.map(GameState::tileToPlace);

        // Le nombre de tuiles restantes dans les piles
        ObservableValue<Integer> normalTilesLeft = gameState.map(g -> g.tileDecks().normalTiles().size());
        ObservableValue<Integer> menhirTilesLeft = gameState.map(g -> g.tileDecks().menhirTiles().size());

        // Le message à afficher
        ObservableValue<String> message = gameState.map(g -> {
            if (g.tileToPlace() == null) {
                GameState.Action action = g.nextAction();
                if (action == GameState.Action.OCCUPY_TILE) {
                    return textMakerFr.clickToOccupy();
                } else if (action == GameState.Action.RETAKE_PAWN) {
                    return textMakerFr.clickToUnoccupy();
                }
            }
            return "";
        });

        // un autre truc de MessageBoard que jai pas trop compris todo rewrite comment lmao
        ObservableValue<List<MessageBoard.Message>> messages = gameState.map(g -> g.messageBoard().messages());

        // Liste chronologique des actions encodées en base 32 de la partie
        ObjectProperty<List<String>> base32Codes = new SimpleObjectProperty<>(List.of());

        /*
        L'interface graphique de droite
        */
        // La Node d'Actions et des Piles du jeu
        VBox decksAndActions = new VBox();

        // Interface graphique des codes en base 32 pour le jeu à distance
        Node Actions = ActionUI.create(base32Codes, handler -> {
            try {
                updateStateAndCodes(Objects.requireNonNull(ActionEncoder.decodeAndApply(gameState.getValue(), handler)),
                        gameState,
                        base32Codes);
            } catch (Exception _) {}
        });

        Node Decks = DecksUI.create(tileToPlace, normalTilesLeft, menhirTilesLeft, message,
                occupant -> occupantConsumer(gameState, base32Codes, occupant));
        decksAndActions.getChildren().add(Actions);
        decksAndActions.getChildren().add(Decks);

        // La Node de l'interface des joueurs et l'interface du tableau de messages
        Node Players = PlayersUI.create(gameState, textMakerFr);
        Node MessageBoard = MessageBoardUI.create(messages, highlightedTiles);

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
                        rotation -> tileRotation.set(tileRotation.get().add(rotation)),
                        pos -> {
                            GameState currentGameState = gameState.getValue();

                            updateStateAndCodes(ActionEncoder.withPlacedTile(currentGameState,
                                    new PlacedTile(tileToPlace.getValue(),
                                            currentGameState.currentPlayer(),
                                            tileRotation.getValue(),
                                            pos)),
                                    gameState,
                                    base32Codes);

                            if (gameState.getValue().nextAction() == GameState.Action.OCCUPY_TILE) {
                                Set<Occupant> newVisibleOccupants = gameState.getValue().board().occupants();
                                newVisibleOccupants.addAll(gameState.getValue().lastTilePotentialOccupants());
                            }
                        },
                        occupant -> occupantConsumer(gameState, base32Codes, occupant));

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

    /**
     * Méthode d'aide qui permet de réutiliser le géstionnaire d'événements en lien avec l'occupant, vu que c'est le
     * même pour l'interface graphique de la pile, puis celle du plateau de jeu
     *
     * @param gameState
     *         l'état du jeu actuel, qui est à modifier selon les critères de la méthode
     * @param occupant
     *         l'occupant qui est à mettre sur le plateau ou à y retirer.
     */
    private void occupantConsumer(ObjectProperty<GameState> gameState,
                                  ObjectProperty<List<String>> base32Codes,
                                  Occupant occupant) {
        GameState currentGameState = gameState.getValue();
        GameState.Action nextAction = currentGameState.nextAction();

        if (nextAction == GameState.Action.OCCUPY_TILE) {
            updateStateAndCodes(ActionEncoder.withNewOccupant(currentGameState, occupant), gameState, base32Codes);

        } else if (nextAction == GameState.Action.RETAKE_PAWN
                && (occupant == null || occupant.kind() == Occupant.Kind.PAWN)) {
            updateStateAndCodes(ActionEncoder.withOccupantRemoved(currentGameState, occupant), gameState, base32Codes);
        }
    }

    private void updateStateAndCodes(ActionEncoder.StateAction action,
                                     ObjectProperty<GameState> state,
                                     ObjectProperty<List<String>> base32Codes
                                     ) {
        List<String> codes = new ArrayList<>(base32Codes.getValue());

        codes.add(action.base32Code());
        base32Codes.setValue(codes);

        state.set(action.gameState());
    }
}
