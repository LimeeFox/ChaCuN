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

import java.util.function.Consumer;

/**
 * Création de la partie de l'interface graphique qui affiche les tas de tuiles ainsi que la tuile à poser
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */
public abstract class DecksUI {
    /**
     * Constructeur de DecksUI
     *
     * @param tileToPlace
     *         la tuile à placer
     * @param normalTilesLeft
     *         le nombre de tuiles restantes dans la pile des tuiles "normales"
     * @param menhirTilesLeft
     *         le nombre de tuiles restantes dans la pile des tuiles "menhir"
     * @param message
     *         texte à afficher à la place de la tuile à placer
     * @param occupantConsumer
     *         gestionnaire d'événements destiné à être appelé lorsque le joueur courant signale
     *         qu'il ne désire pas de poser ou reprendre un occupant, en cliquant sur @message
     * @return l'interface graphique des piles de tuiles avec un aperçu de la tuile à poser
     */
    private static final double DECKS_SCALE = 0.5;
    private static final double PREVIEW_SCALE = 0.8;

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
        createPane(normalTilesPane, "NORMAL", normalTilesLeft);

        //
        // Menhir tiles display
        //
        StackPane menhirTilesPane = new StackPane();

        // On a besoin de mettre à jour le texte qui affiche le nombre de tuiles restantes dans la pile
        createPane(menhirTilesPane, "MENHIR", menhirTilesLeft);

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
        tileToPlaceImageView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        tileToPlaceImageView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        ObservableValue<Image> tileToPlaceImage = tileToPlace.map(Tile::id).map(ImageLoader::largeImageForTile);
        tileToPlaceImageView.imageProperty().bind(tileToPlaceImage);

        // On modifie le texte en fonction du message à afficher
        occupantInfoText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE * PREVIEW_SCALE);
        occupantInfoText.textProperty().bind(message);

        // Si il y a un message à afficher, c'est que le joueur peut executer (ou pas) une action en cliquant sur
        // le texte
        ObservableValue<Boolean> messageIsNull = message.map(String::isEmpty);
        tileToPlaceImageView.visibleProperty().bind(messageIsNull);

        occupantInfoText.setOnMouseClicked(e -> {
            if (!messageIsNull.getValue()) {
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

    private static void createPane(StackPane pane, String type, ObservableValue<Integer> tilesLeft) {
        Text tilesText = new Text();
        ObservableValue<String> tileCount = tilesLeft.map(Object::toString);
        tilesText.textProperty().bind(tileCount);

        ImageView tilesImageView = new ImageView();
        tilesImageView.setId(type);
        Image menhirImage = new Image(STR."/256/\{type}.jpg");
        tilesImageView.setImage(menhirImage);
        tilesImageView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE * DECKS_SCALE);
        tilesImageView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE * DECKS_SCALE);

        pane.getChildren().add(tilesImageView);
        pane.getChildren().add(tilesText);
    }
}
