package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Interface graphique qui affiche le plateau de jeu
 *
 * @author Cyriac Philippe (360553)
 */
public class BoardUI {
    /**
     *
     * @param scope
     * @param currentGameState
     * @param tileRotation
     * @param visibleOccupants
     * @param tiles
     * @param tileRotates
     * @param tileMoves
     * @param occupantConsumer
     * @return
     */
    public static Node create(int scope, GameState currentGameState, Rotation tileRotation,
                              Set<Occupant> visibleOccupants, Set<Integer> tiles,
                              Consumer<Rotation> tileRotates, Consumer<Pos> tileMoves,
                              Consumer<Occupant> occupantConsumer) {
        // La portée doit toujours être strictement positive
        Preconditions.checkArgument(scope > 0);

        // Table associant les identifiants des tuiles du jeu à leur image
        // Les images de chaque tuile sont ainsi toutes chargées
        Map<Integer, Image> cache = getCache();

        //todo might have to define this somewhere else, but i doubt it
        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));

        GridPane boardGridPane = new GridPane();
        for (int x = -scope; x < scope; x++) {
            for (int y = -scope; y < scope; y++) {
                Pos position = new Pos(x, y);
                Board currentBoard = currentGameState.board();

                PlacedTile placedTile = currentBoard.tileAt(position);

                // Si la position x,y que l'on observe contient une tuile, on l'affiche
                if (placedTile != null) {
                    boardGridPane.add(new ImageView(cache.get(placedTile.id())),
                            x + scope, y+ scope);
                }
                    // Sinon on affiche un case "vide" (presque blanche)
                else {
                        boardGridPane.add(new ImageView(emptyTileImage), x + scope, y + scope);
                }
            }
        }
        return boardGridPane;
    }

    /**
     *
     * @return
     */
    private static Map<Integer, Image> getCache() {
        Map<Integer, Image> cache = new HashMap<>();
        for (int i = 0; i < 95; i++) {
            // Chemin vers l'image de tuile
            //todo check if we need to get the images for board tile from 256 or 512
            String imagePath = "C:\\Users\\cyria\\OneDrive\\Documents\\GITHUB\\POOP\\ChaCuN\\resources\\256\\" +
                    STR."\{i}.jpg";

            // Chargement de l'image
            File imageFile = new File(imagePath);
            Image image = new Image(imageFile.toURI().toString());

            // Ajout de l'image à la "cache"
            cache.put(i, image);
        }
        return cache;
    }
}
