package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Création de la partie de l'interface graphique qui affiche les tas de tuiles ainsi que la tuile à poser
 *
 *  @author Cyriac Philippe (360553)
 *  @author Vladislav Yarkovoy (362242)
 */
public abstract class DecksUI {
    public static void create(
            ObservableValue<Tile> tileToPlace,
            ObservableValue<Integer> normalTilesLeft,
            ObservableValue<Integer> menhirTilesLeft,
            ObservableValue<String> message,
            Consumer<Occupant> occupantConsumer) {

        //
        // Normal tiles display
        //
        StackPane normalTilesPane = new StackPane();

        // On a besoin de mettre à jour le texte qui affiche le nombre de tuiles restantes dans la pile
        Text normalTilesText = new Text();
        ObservableValue<String> normalTilesCount = normalTilesLeft.map(Object::toString);
        normalTilesText.textProperty().bind(normalTilesCount);

        ImageView normalTilesImageView = new ImageView();
        normalTilesImageView.setId("NORMAL");

        normalTilesPane.getChildren().add(normalTilesText);
        normalTilesPane.getChildren().add(normalTilesImageView);

        //
        // Menhir tiles display
        //
        StackPane menhirTilesPane = new StackPane();

        // On a besoin de mettre à jour le texte qui affiche le nombre de tuiles restantes dans la pile
        Text menhirTilesText = new Text();
        ObservableValue<String> menhirTilesCount = menhirTilesLeft.map(Object::toString);
        menhirTilesText.textProperty().bind(menhirTilesCount);

        ImageView menhirTilesImageView = new ImageView();
        menhirTilesImageView.setId("MENHIR");

        menhirTilesPane.getChildren().add(menhirTilesText);
        menhirTilesPane.getChildren().add(menhirTilesImageView);

        //
        // Display of both tile types
        //
        HBox tileCountBox = new HBox();
        tileCountBox.getChildren().add(normalTilesPane);
        tileCountBox.getChildren().add(menhirTilesPane);

        //
        // Tile to place display
        //
        StackPane tileToPlacePane = new StackPane();
        tileToPlacePane.setId("next-tile");

        Text tileToPlaceText = new Text();
        tileToPlaceText.setWrappingWidth(0.8);
        ObservableValue<Boolean> messageIsNull = message.map(Objects::isNull);
        tileToPlaceText.visibleProperty().bind(messageIsNull);

        ImageView tileToPlaceImageView = new ImageView();

        tileToPlacePane.getChildren().add(tileToPlaceText);
        tileToPlacePane.getChildren().add(tileToPlaceImageView);

        //
        // Global Decks display
        //
        VBox decksBox = new VBox();
        decksBox.getStyleClass().add("decks.css");
        decksBox.getChildren().add(tileCountBox);
        decksBox.getChildren().add(tileToPlacePane);
    }
}
