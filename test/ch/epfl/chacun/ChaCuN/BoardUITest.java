import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Rotation;
import ch.epfl.chacun.gui.BoardUI;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;

import java.util.Set;

public final class BoardUITest extends Application {
    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        var playerNames = /* comme à l'étape 8 */;
        var playerColors = /* comme à l'étape 8 */;
        var tileDecks = /* comme à l'étape 8 */;
        var textMaker = /* comme à l'étape 8 */;
        var gameState = /* comme à l'étape 8 */;

        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.<Occupant>of());
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        var gameStateO = new SimpleObjectProperty<>(gameState);
        var boardNode = BoardUI
                .create(1,
                        gameStateO,
                        tileToPlaceRotationP,
                        visibleOccupantsP,
                        highlightedTilesP,
                        r -> System.out.println("Rotate: " + r),
                        t -> System.out.println("Place: " + t),
                        o -> System.out.println("Select: " + o));

        gameStateO.set(gameStateO.get().withStartingTilePlaced());

        var rootNode = new BorderPane(boardNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}
