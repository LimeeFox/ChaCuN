package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.*;
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
        final Map<Integer, Image> cache = new HashMap<>();
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

                Group group = new Group();
                ImageView tileFace = new ImageView();

                tileFace.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileFace.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);

                // Gérer tous les changements pour chaque case du tableau
                currentGameState.addListener((o, oldGameState, newGameState) -> {
                    GameState.Action nextAction = newGameState.nextAction();
                    PlacedTile tile = board.getValue().tileAt(pos);
                    PlayerColor placer = newGameState.currentPlayer();

                    // L'image de la tuile (soit "vide", soit celle de la tuile)
                    tileFace.setImage(tile == null
                            ? emptyTileImage : cache.computeIfAbsent(tile.id(), ImageLoader::normalImageForTile));
                    veil(group, nextAction == GameState.Action.PLACE_TILE &&
                            board.getValue().insertionPositions().contains(pos) && placer != null ?
                            ColorMap.fillColor(placer) : Color.TRANSPARENT);

                    // Gérer les occupants
                    if (tile != null && tile.kind() != Tile.Kind.START) {
                        for (Occupant occupant : tile.potentialOccupants()) {
                            // On a besoin de créer un occupant pour CHAQUE joueur, qu'on va en premier temps, cacher,
                            // et en deuxième temps, le refaire apparaître au cas oû la tuile se fait occuper
                            SVGPath occupantIcon = (SVGPath) Icon
                                    .newFor(board.getValue().tileAt(pos).placer(), occupant.kind());
                            occupantIcon.setId(STR."\{occupant.kind().toString().toLowerCase()}_\{occupant.zoneId()}");

                            occupantIcon.visibleProperty().bind(visibleOccupants
                                    .map(occupants -> occupants.contains(occupant)));

                            // Gérer la rotation de l'occupant. La rotation doit être inversée pour les occupants
                            occupantIcon.setRotate(-group.getRotate());

                            occupantIcon.setOnMouseClicked(event -> {
                                if (event.getButton() == MouseButton.PRIMARY) {
                                    //&& visibleOccupants.getValue().contains(occupant)) {
                                    occupantConsumer.accept(occupant);
                                }
                            });

                            group.getChildren().add(occupantIcon);
                            /* fixme deprecated
                            for (PlayerColor playerColor : newGameState.players()) {
                                SVGPath occupantIcon = (SVGPath) Icon.newFor(playerColor, occupant.kind());
                                occupantIcon.setId(STR."\{occupant.kind().toString().toLowerCase()}_\{occupant.zoneId()}");

                                occupantIcon.visibleProperty().bind(visibleOccupants
                                        .map(occupants -> occupants.contains(occupant)));

                                // Gérer la rotation de l'occupant. La rotation doit être inversée pour les occupants
                                occupantIcon.setRotate(-group.getRotate());

                                occupantIcon.setOnMouseClicked(event -> {
                                    if (event.getButton() == MouseButton.PRIMARY) {
                                            //&& visibleOccupants.getValue().contains(occupant)) {
                                        occupantConsumer.accept(occupant);
                                    }
                                });

                                group.getChildren().add(occupantIcon);
                            }

                             */
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

                    // Gérer l'affichage de la tuile si elle est prête à être posée
                    // C-à-d, la souris survole une case de frange
                    /* todo experimental feature that uses one listener for hovering instead of two
                    (even though in the source code implementation, they use the other two to define this one lol)
                    tileFace.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isHovering) -> {
                        Tile tileToPlace = newGameState.tileToPlace();
                        if (board.getValue().insertionPositions().contains(pos) && nextAction == GameState.Action.PLACE_TILE) {
                            if (isHovering) {
                                // Si la case contient une tuile, et que certaines tuiles sont mises en évidence
                                // mais pas celle de la case, alors elle est recouverte d'un voile noir qui l'assombrit
                                if (board.getValue().canAddTile(new PlacedTile(tileToPlace, placer, tileRotation.getValue(), pos))) {
                                    veil(group, Color.TRANSPARENT);
                                } else {
                                    // Si la case fait partie de la frange, que le curseur de la souris la survole
                                    // et que la tuile courante, avec sa rotation actuelle, ne peut pas y être placée,
                                    // alors elle est recouverte d'un voile blanc
                                    veil(group, Color.WHITE);
                                }
                                tileFace.setImage(cache.get(tileToPlace.id()));
                                group.rotateProperty().set(tileRotation.getValue().degreesCW());
                            } else {
                                // This is equivalent to setOnMouseExited
                                if (placer != null) {
                                    tileFace.setImage(emptyTileImage);
                                    veil(group, ColorMap.fillColor(placer));
                                }
                            }
                        }
                    });

                     */

                    tileFace.setOnMouseEntered(event -> {
                        if (board.getValue().insertionPositions().contains(pos) && nextAction == GameState.Action.PLACE_TILE) {
                            Tile tileToPlace = newGameState.tileToPlace(); //todo we're using tileToPlace many times
                            fringeCheck(newGameState.board(), group, newGameState, placer, tileRotation.getValue(), pos); //todo check if we can use board instead of newGameState
                            tileFace.setImage(cache.computeIfAbsent(tileToPlace.id(), ImageLoader::normalImageForTile));
                            group.rotateProperty().set(rotation.getValue());
                        }
                    });

                    tileFace.setOnMouseExited(event -> {
                        if (board.getValue().insertionPositions().contains(pos) && placer != null
                                && nextAction == GameState.Action.PLACE_TILE) {
                            tileFace.setImage(emptyTileImage);
                            veil(group, ColorMap.fillColor(placer));
                        }
                    });

                    // Gérer la rotation de la tuile
                    group.setOnMouseClicked(event -> {
                        if (board.getValue().insertionPositions().contains(pos)
                                && nextAction == GameState.Action.PLACE_TILE) {
                            // Si c'est un click droit, alors tourner la tuile dans le sens anti-horaire
                            // Ou dans le sens horaire si le bouton ALT (Option sur MacOS) est appuyée
                            if (event.getButton() == MouseButton.SECONDARY) {
                                tileRotates.accept(event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT);
                                fringeCheck(newGameState.board(), group, newGameState, placer, tileRotation.getValue(), pos);
                                group.setRotate(tileRotation.getValue().degreesCW());
                            // Si c'est un click gauche, alors poser la tuile si cela est permis
                            } else if (event.getButton() == MouseButton.PRIMARY) {
                                final PlacedTile tileToPlace =
                                        new PlacedTile(newGameState.tileToPlace(), placer, tileRotation.getValue(), pos);
                                if (board.getValue().canAddTile(tileToPlace)) {
                                    tileMoves.accept(pos);
                                    tileFace.setImage(cache.computeIfAbsent(tileToPlace.id(), ImageLoader::normalImageForTile));
                                }
                            }
                        }
                    });
                });

                // todo when I was writing this method, only God and I knew wtf it did. Now only God does. help pls idk wtf this shit does
                highlightedTiles.addListener((o, oldValue, newValue) -> {
                    if (newValue.isEmpty() || !oldValue.contains(board.getValue().tileAt(pos).id()))
                        veil(group, Color.TRANSPARENT);
                    // Si la case contient une tuile, et que certaines tuiles sont mises en évidence
                    // mais pas celle de la case, alors elle est recouverte d'un voile noir qui l'assombrit
                    else if (!newValue.contains(board.getValue().tileAt(pos).id()))
                        veil(group, Color.BLACK);
                });

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

    private static void fringeCheck(Board board,
                                    Group group,
                                    GameState gameState,
                                    PlayerColor placer,
                                    Rotation rotation,
                                    Pos pos) {
        Tile tileToPlace = gameState.tileToPlace();
        // Si le joueur parcours la case avec sa souris, on fait un preview de la tuile à cette case
        if (board.canAddTile(new PlacedTile(tileToPlace, placer, rotation, pos))) {
            veil(group, Color.TRANSPARENT);
        } else {
            // Si la case fait partie de la frange, que le curseur de la souris la survole
            // et que la tuile courante, avec sa rotation actuelle, ne peut pas y être placée,
            // alors elle est recouverte d'un voile blanc
            veil(group, Color.WHITE);
        }
    }
}
