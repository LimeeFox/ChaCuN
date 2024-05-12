package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
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
        L'interface graphique de droite
        */
        // La Node d'Actions et des Piles du jeu
        VBox decksAndActions = new VBox();
        //Node Actions = todo merge actionsUI
        Node Decks = DecksUI.create();
        //decksAndActions.getChildren().add(Actions);
        decksAndActions.getChildren().add(Decks);

        // La Node de l'interface des joueurs et l'interface du tableau de messages
        Node Players = PlayersUI.create();
        Node MessageBoard = MessageBoardUI.create();

        // La racine de la partie droite de l'interface graphique principale du jeu
        BorderPane sideUI = new BorderPane();
        sideUI.getChildren().add(Players);
        sideUI.getChildren().add(MessageBoard);

        /*
        L'interface graphique de centre
        */
        //Node Board = todo merge BoardUI

        /*
        Mise en commun de toutes les interfaces
        */
        BorderPane root = new BorderPane();

        root.getChildren().add(sideUI);
        //root.getChildren().add(Board);


        Scene scene = new Scene(root, 250, 400);
        stage.setTitle("ChaCuN");
        stage.setScene(scene);
        stage.setWidth(1440);
        stage.setHeight(1080);
        stage.show();
    }
}
