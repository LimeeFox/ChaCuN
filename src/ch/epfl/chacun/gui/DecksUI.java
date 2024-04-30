package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
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
    public static Node create(
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
        Image normalImage = new Image(STR."256/NORMAL.jpg");
        normalTilesImageView.setImage(normalImage);
        normalTilesImageView.setFitHeight(normalImage.getHeight() * 0.5);
        normalTilesImageView.setFitWidth(normalImage.getWidth() * 0.5);

        normalTilesPane.getChildren().add(normalTilesImageView);
        normalTilesPane.getChildren().add(normalTilesText);

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
        Image menhirImage = new Image(STR."256/MENHIR.jpg");
        menhirTilesImageView.setImage(menhirImage);
        menhirTilesImageView.setFitHeight(menhirImage.getHeight() * 0.5);
        menhirTilesImageView.setFitWidth(menhirImage.getWidth() * 0.5);

        menhirTilesPane.getChildren().add(menhirTilesImageView);
        menhirTilesPane.getChildren().add(menhirTilesText);

        //
        // Display of both tile types
        //
        HBox tileCountBox = new HBox();
        tileCountBox.setId("decks");
        tileCountBox.getChildren().add(normalTilesPane);
        tileCountBox.getChildren().add(menhirTilesPane);

        //
        // Tile to place display
        //
        StackPane tileToPlacePane = new StackPane();
        tileToPlacePane.setId("next-tile");

        Text occupantInfoText = new Text();

        // On modifie l'image en fonction de la tuile à placer
        ImageView tileToPlaceImageView = new ImageView();
        ObservableValue<Image> tileToPlaceImage = tileToPlace.map(Tile::id).map(ImageLoader::normalImageForTile);
        tileToPlaceImageView.imageProperty().bind(tileToPlaceImage);

        occupantInfoText.setWrappingWidth(tileToPlaceImageView.getImage().getWidth() * 0.8);
        occupantInfoText.textProperty().bind(message);

        ObservableValue<Boolean> messageIsNull = message.map(String::isEmpty);
        tileToPlaceImageView.visibleProperty().bind(messageIsNull);

        occupantInfoText.setOnMouseClicked(e -> {
            if (!occupantInfoText.getText().isEmpty()) {
                occupantConsumer.accept(null);
            }
        });

        tileToPlacePane.getChildren().add(tileToPlaceImageView);
        tileToPlacePane.getChildren().add(occupantInfoText);

        //
        // Global Decks display
        //
        VBox decksBox = new VBox();
        decksBox.getStylesheets().add("decks.css");
        decksBox.getChildren().add(tileCountBox);
        decksBox.getChildren().add(tileToPlacePane);

        return decksBox;
    }
}
