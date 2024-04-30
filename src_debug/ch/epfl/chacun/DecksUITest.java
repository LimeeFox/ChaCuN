package ch.epfl.chacun;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ch.epfl.chacun.gui.DecksUI;


public class DecksUITest extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Dummy values for testing
        SimpleObjectProperty<Tile> observableTile = new SimpleObjectProperty<>(Tiles.TILES.get(10));
        SimpleObjectProperty<Integer> remainingNormalTiles = new SimpleObjectProperty<>(10);
        SimpleObjectProperty<Integer> remainingMenhirTiles = new SimpleObjectProperty<>(5);
        SimpleObjectProperty<String> textToDisplay = new SimpleObjectProperty<>("Testing text display");





        Occupant dummyOccupant = new Occupant(Occupant.Kind.PAWN,10);



        Node testDeckUI = DecksUI.create(observableTile,remainingNormalTiles,remainingMenhirTiles,textToDisplay,
                occupant -> {
                    System.out.println("Text was clicked. Occupant: " + dummyOccupant.kind().toString());
                });


        textToDisplay.set("test");
        observableTile.set(Tiles.TILES.get(19));
        textToDisplay.set("");
        //textToDisplay.set("");
        observableTile.set(Tiles.TILES.get(35));

        StackPane test = new StackPane(testDeckUI);



        Scene scene = new Scene(test, 250, 400);
        primaryStage.setTitle("DecksUI Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
