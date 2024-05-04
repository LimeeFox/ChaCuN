package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Tiles.TILES;

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
     * @param highlightedTiles
     * @param tileRotates
     * @param tileMoves
     * @param occupantConsumer
     * @return
     */
    public static Node create(int scope,
                              ObservableValue<GameState> currentGameState,
                              ObservableValue<Rotation> tileRotation,
                              ObservableValue<Set<Occupant>> visibleOccupants,
                              ObservableValue<Set<Integer>> highlightedTiles,
                              Consumer<Rotation> tileRotates,
                              Consumer<Pos> tileMoves,
                              Consumer<Occupant> occupantConsumer) {
        // La portée doit toujours être strictement positive
        Preconditions.checkArgument(scope > 0);

        // Table associant les identifiants des tuiles du jeu à leur image
        // Les images de chaque tuile sont ainsi toutes chargées
        final Map<Integer, Image> cache = getCache();
        final Image marker = new Image("marker.png");

        // Image de fond "vide"
        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));

        GridPane boardGridPane = new GridPane();
        boardGridPane.setId("board-grid");

        ObservableValue<Board> board = currentGameState.map(GameState::board);
        // Rotation de la tuile à poser
        ObservableValue<Integer> rotation = tileRotation.map(Rotation::degreesCW);
        for (int x = -scope; x <= scope; x++) {
            for (int y = -scope; y <= scope; y++) {
                final Pos pos = new Pos(x, y);
                //ObservableValue<PlacedTile> currentTile = board.map(b -> b.tileAt(pos)); // todo @deprecated

                Group group = new Group();
                ImageView tileFace = new ImageView();

                tileFace.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileFace.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);

                // Gérer tous les changements pour chaque case du tableau
                currentGameState.addListener((o, oldGameState, newGameState) -> {
                    GameState.Action nextAction = newGameState.nextAction();
                    PlacedTile tile = board.getValue().tileAt(pos); //fixme i changed it, it used to be newGameState.board(), maybe any bug that arises in the future can be fixed here
                    PlayerColor placer = newGameState.currentPlayer();

                    // L'image de la tuile (soit "vide", soit celle de la tuile)
                    tileFace.setImage(tile == null ? emptyTileImage : cache.get(tile.id()));
                    veil(group, nextAction == GameState.Action.PLACE_TILE &&
                            board.getValue().insertionPositions().contains(pos) && placer != null ?
                            ColorMap.fillColor(placer) : Color.TRANSPARENT);

                    // Gérer les occupants
                    /*
                    if (tile != null) {
                        for (Occupant occupant : tile.potentialOccupants()) {
                            // On a besoin de créer un occupant pour CHAQUE joueur, qu'on va en premier temps, cacher,
                            // et en deuxième temps, le refaire apparaître au cas oû la tuile se fait occuper
                            for (PlayerColor playerColor : newGameState.players()) {
                                System.out.println(STR."occupant: \{occupant.kind()}");
                                SVGPath occupantIcon = (SVGPath) Icon.newFor(playerColor, occupant.kind());
                                occupantIcon.setId(STR."\{occupant.kind().toString().toLowerCase()}_\{occupant.zoneId()}");
                                occupantIcon.setVisible(false);

                                // Gérer la rotation de l'occupant. La rotation doit être inversée pour les occupants
                                occupantIcon.rotateProperty().bind(rotation.map(r -> - r));

                                occupantIcon.setOnMouseClicked(event -> {
                                    if (event.getButton() == MouseButton.PRIMARY) {
                                        occupantConsumer.accept(occupant);
                                        occupantIcon.setVisible(true);
                                    }
                                });

                                group.getChildren().add(occupantIcon);
                            }
                        }

                        // Gérer les jetons d'annulation
                        tile.meadowZones().forEach(meadowZone -> meadowZone.animals().forEach(animal -> {
                            ImageView cancellationToken = new ImageView();
                            cancellationToken.setImage(marker);
                            cancellationToken.setId(STR."marker_\{animal.id()}");
                            cancellationToken.getStyleClass().add("marker");

                            ObservableValue<Boolean> isCancelled = board
                                    .map(b -> b.cancelledAnimals().contains(animal));
                            cancellationToken.visibleProperty().bind(isCancelled);

                            group.getChildren().add(cancellationToken);
                        }));
                    }
                    */

                    /* fixme deprecated
                    // Mise en évidence de la case si elle fait partie de la frange
                    // Si la case fait partie de la frange mais quelle n'est pas survolée par le curseur de la souris,
                    // alors elle est recouverte d'un voile de la couleur du joueur courant
                    if (nextAction != GameState.Action.PLACE_TILE && placer != null
                            && board.getValue().insertionPositions().contains(pos)) {
                        tileFace.setImage(emptyTileImage);
                        veil(group, ColorMap.fillColor(placer));
                    }

                     */

                    // Gérer l'affichage de la tuile si elle est prête à être posée
                    // C-à-d, la souris survole une case de frange
                    tileFace.setOnMouseEntered(event -> {
                        if (board.getValue().insertionPositions().contains(pos) && nextAction == GameState.Action.PLACE_TILE) {
                            Tile tileToPlace = newGameState.tileToPlace();
                            // Si la case contient une tuile, et que certaines tuiles sont mises en évidence
                            // mais pas celle de la case, alors elle est recouverte d'un voile noir qui l'assombrit
                            if (board.getValue()
                                    .canAddTile(new PlacedTile(tileToPlace, placer, tileRotation.getValue(), pos))) {
                                veil(group, Color.TRANSPARENT);
                            } else {
                                // Si la case fait partie de la frange, que le curseur de la souris la survole
                                // et que la tuile courante, avec sa rotation actuelle, ne peut pas y être placée,
                                // alors elle est recouverte d'un voile blanc
                                veil(group, Color.WHITE);
                            }
                            tileFace.setImage(cache.get(tileToPlace.id())); // fixme aaa help
                            group.rotateProperty().set(rotation.getValue());
                        }
                        System.out.println(STR."enter");
                    });

                    tileFace.setOnMouseExited(event -> {
                        if (board.getValue().insertionPositions().contains(pos) && placer != null && nextAction == GameState.Action.PLACE_TILE) {
                            //tileFace.setImage(emptyTileImage);
                            veil(group, ColorMap.fillColor(placer));
                            System.out.println(STR."exit");
                        }
                    });



                    //System.out.println(STR."tile: \{tile}");
                    //System.out.println(STR."tile to place: \{newGameState.tileToPlace()}");
                });

                highlightedTiles.addListener((o, oldValue, newValue) -> {
                    if (newValue.isEmpty() || !oldValue.contains(board.getValue().tileAt(pos).id()))
                        veil(group, Color.TRANSPARENT);
                    else if (! newValue.contains(board.getValue().tileAt(pos).id()))
                        veil(group, Color.BLACK);
                });

                // Gérer la rotation de la tuile
                // group.rotateProperty().bind(rotation); todo deprecated (cuz every cell will be roated at the same time in that case lmao)
                group.getChildren().add(tileFace);
                boardGridPane.add(group, x + scope, y + scope);
            }
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("board-scroll-pane");
        scrollPane.getStylesheets().add("board.css");
        scrollPane.setContent(boardGridPane);

        return scrollPane;
    }

    /**
     * Méthode d'aide qui permet de générer les images de toutes les tuiles du jeu //todo demander a cyriac ou il l'a volé, je refuse de croire que c'est lui qui l'a ecrit lol
     *
     * @return une cache d'images contenant les images de chaque tuile du jeu
     */
    //todo make sure we do show the images of tiles ar the right size
    private static Map<Integer, Image> getCache() {
        Map<Integer, Image> cache = new HashMap<>();
        for (int i = 0; i < TILES.size(); i++) {
            // Chemin vers l'image de tuile
            String imagePath = STR."resources/256/\{i}.jpg";

            // Chargement de l'image
            File imageFile = new File(imagePath);
            Image image = new Image(imageFile.toURI().toString());

            // Ajout de l'image à la "cache"
            cache.put(i, image);
        }
        return cache;
    }

    /**
     * Méthode d'aide qui permet de gérer les voiles
     *
     * @param group groupe
     * @param color couleur de l'avant plan
     */
    private static void veil(Group group, Color color) {
        ColorInput veil = new ColorInput();
        veil.setHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        veil.setWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        veil.setPaint(color);

        Blend blend = new Blend(BlendMode.SRC_OVER);
        blend.setOpacity(0.5);
        blend.setTopInput(veil);

        group.setEffect(blend);
    }
}
