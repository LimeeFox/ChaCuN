package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Interface graphique qui affiche le plateau de jeu
 *
 * @author Cyriac Philippe (360553)
 */
public class BoardUI {
    public static Node create(int scope, GameState currentGameState, Rotation tileRotation,
                              Set<Occupant> visibleOccupants, Set<Integer> tiles,
                              Consumer<Rotation> tileRotates, Consumer<Pos> tileMoves,
                              Consumer<Occupant> occupantConsumer) {
        // La portée doit toujours être strictement positive
        Preconditions.checkArgument(scope > 0);

        GridPane boardGridPane = new GridPane();
        for (int x = -scope; x < scope; x++) {
            for (int y = -scope; y < scope; y++) {
                //todo, two cases :
                //todo 1, we do have a tile to add
                //todo 2, the tile to add is null (what to do then?)
                ImageView imageView = new ImageView(ImageLoader.normalImageForTile(currentGameState.board()
                        .tileAt(new Pos(x, y)).tile().id()));
                boardGridPane.add(imageView, x + scope, y+ scope);
            }
        }

        //todo return the right type of node once it has been modified accordingly
        return null;
    }
}
