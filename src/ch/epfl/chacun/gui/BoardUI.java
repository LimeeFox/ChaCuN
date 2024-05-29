package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
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
 * @author Vladislav Yarkovoy (362242)
 */
public class BoardUI {
    /**
     * @param scope
     *         portée
     * @param gameState
     *         l'état du jeu
     * @param tileRotation
     *         la rotation à appliquer à la tuile à placer
     * @param visibleOccupants
     *         l'ensemble des occupants visibles
     * @param highlightedTiles
     *         l'ensemble des identifiants des tuiles mises en évidence
     * @param tileRotates
     *         un gestionnaire prenant une valeur de type Rotation, à appeler lorsque le joueur courant
     *         désire effectuer une rotation de la tuile à placer, c.-à-d. qu'il effectue un clic droit
     *         sur une case de la frange
     * @param tileMoves
     *         un gestionnaire prenant une valeur de type Pos, à appeler lorsque le joueur courant désire
     *         poser la tuile à placer, c.-à-d. qu'il effectue un clic gauche sur une case de la frange
     * @param occupantConsumer
     *         un gestionnaire prenant une valeur de type Occupant, à appeler lorsque le joueur courant
     *         sélectionne un occupant, c.-à-d. qu'il clique sur l'un d'entre eux
     * @return La Node qui contient le plateau du jeu en forme de grille avec des cases correspondant à des tuiles
     */
    public static Node create(int scope,
                              ObservableValue<GameState> gameState,
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

        ObservableValue<Board> board = gameState.map(GameState::board);
        ObservableValue<Tile> tileToPlace = gameState.map(GameState::tileToPlace);
        ObservableValue<GameState.Action> nextAction = gameState.map(GameState::nextAction);
        ObservableValue<PlayerColor> currentPlayer = gameState.map(GameState::currentPlayer);
        // On parcourt toutes les cases possibles du plateau graphique afin d'y installer des tuiles au fur et à mesure
        // du jeu.
        for (int x = -scope; x <= scope; x++) {
            for (int y = -scope; y <= scope; y++) {
                final Pos pos = new Pos(x, y);
                ObservableValue<PlacedTile> tile = board.map(b -> b.tileAt(pos));

                Group group = new Group();
                ImageView tileFace = new ImageView();

                // Création du container avec toutes les propriétés nécessaires à la création d'une tuile
                ObservableValue<CellData> cell = Bindings.createObjectBinding(
                        () -> {
                            PlacedTile currentTile = tile.getValue();
                            GameState.Action currentAction = nextAction.getValue();
                            Board currentBoard = board.getValue();

                            Image image = currentTile == null
                                    ? emptyTileImage
                                    : cache.computeIfAbsent(currentTile.id(), ImageLoader::normalImageForTile);

                            Rotation currentRotation = currentTile == null
                                    ? tileRotation.getValue() : currentTile.rotation();

                            PlayerColor placer = currentPlayer.getValue();
                            Color veilColour = null;
                            // Ici, on gère la frange
                            if (currentBoard.insertionPositions().contains(pos)
                                    && currentAction == GameState.Action.PLACE_TILE) {
                                if (placer != null) veilColour = ColorMap.fillColor(placer);

                                // Si la tuile se fait survoler avec la souris, alors on fait un aperçu de la tuile
                                // à placer avec une opacité qui dépend de si on peut la placer où non.
                                if (tileFace.isHover()) {
                                    image = cache.computeIfAbsent(tileToPlace.getValue().id(),
                                            ImageLoader::normalImageForTile);

                                    PlacedTile tileToPlace0 = new PlacedTile(tileToPlace.getValue(),
                                            placer,
                                            currentRotation,
                                            pos);
                                    veilColour = currentBoard.canAddTile(tileToPlace0) ? null : Color.WHITE;
                                }
                            }

                            // Si l'utilisateur survole le texte du tableau d'affichage qui parle d'une zone fermée,
                            // La zone en question doit être mise en évidence
                            if (currentTile != null
                                    && !highlightedTiles.getValue().isEmpty()
                                    && !highlightedTiles.getValue().contains(currentTile.id())) {
                                veilColour = Color.BLACK;
                            }

                            return new CellData(image, currentRotation, veilColour);
                        },
                        tileRotation,
                        board,
                        tileToPlace,
                        nextAction,
                        highlightedTiles,
                        tileFace.hoverProperty());

                tileFace.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileFace.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileFace.imageProperty().bind(cell.map(CellData::image));

                // Manipulation de la rotation de la tuile
                group.rotateProperty().bind(cell.map(CellData::rotation).map(Rotation::degreesCW));

                // Manipulation de la voile de la tuile
                ColorInput veil = new ColorInput();
                veil.setHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                veil.setWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                veil.paintProperty().bind(cell.map(CellData::veilColour));
                Blend blend = new Blend(BlendMode.SRC_OVER);
                blend.setOpacity(0.5);
                blend.setTopInput(veil);

                group.effectProperty().bind(cell.map(CellData::veilColour).map(color -> color != null ? blend : null));

                /*
                Manipulation des occupants et les jetons d'annulation
                 */
                tile.addListener((o, oldValue, newValue) -> {
                    if (newValue == null) return;

                    if (newValue.kind() != Tile.Kind.START) {
                        PlayerColor placer = tile.getValue().placer();
                        for (Occupant occupant : newValue.potentialOccupants()) {
                            // On a besoin de créer un occupant pour CHAQUE joueur, qu'on va en premier temps, cacher,
                            // et en deuxième temps, le refaire apparaître au cas oû la tuile se fait occuper
                            SVGPath occupantIcon = (SVGPath) Icon
                                    .newFor(placer, occupant.kind());
                            occupantIcon.setId(STR."\{occupant.kind().toString().toLowerCase()}_\{occupant.zoneId()}");

                            occupantIcon.visibleProperty()
                                    .bind(visibleOccupants.map(occupants -> occupants.contains(occupant)));

                            // Gérer la rotation de l'occupant. La rotation doit être inversée pour les occupants
                            occupantIcon.rotateProperty()
                                    .bind(cell.map(CellData::rotation).map(rotation -> -rotation.degreesCW()));

                            // Auditeur qui va gérer les clics sur les occupants
                            occupantIcon.setOnMouseClicked(event -> {
                                if (event.getButton() == MouseButton.PRIMARY
                                        && event.isStillSincePress()
                                        && (gameState.getValue().lastTilePotentialOccupants().contains(occupant)
                                        || nextAction.getValue() == GameState.Action.RETAKE_PAWN)) {
                                    if (placer != null
                                            && occupantIcon
                                            .fillProperty().getValue().equals(ColorMap.fillColor(placer))) {
                                        occupantConsumer.accept(occupant);
                                    }
                                }
                            });

                            group.getChildren().add(occupantIcon);
                        }
                    }


                    // Gérer les jetons d'annulation
                    newValue.meadowZones().forEach(meadowZone -> meadowZone.animals().forEach(animal -> {
                        ImageView cancellationToken = new ImageView();
                        cancellationToken.setImage(marker);
                        cancellationToken.setId(STR."marker_\{animal.id()}");
                        cancellationToken.getStyleClass().add("marker");
                        cancellationToken.setFitWidth(ImageLoader.MARKER_FIT_SIZE);
                        cancellationToken.setFitHeight(ImageLoader.MARKER_FIT_SIZE);

                        ObservableValue<Boolean> isCancelled = board
                                .map(b -> b.cancelledAnimals().contains(animal));
                        cancellationToken.visibleProperty().bind(isCancelled);

                        group.getChildren().add(cancellationToken);
                    }));
                });

                // On définit le compertement à suivre lors de clics de souris sur les cases
                group.setOnMouseClicked(event -> {
                    Board currentBoard = board.getValue();
                    if (currentBoard.insertionPositions().contains(pos)
                            && nextAction.getValue() == GameState.Action.PLACE_TILE
                            && event.isStillSincePress()) {
                        // Si c'est un click droit, alors tourner la tuile dans le sens anti-horaire
                        // Ou dans le sens horaire si le bouton ALT (Option sur MacOS) est appuyée
                        if (event.getButton() == MouseButton.SECONDARY) {
                            tileRotates.accept(event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT);

                            // Si c'est un click gauche, alors poser la tuile si cela est permis
                        } else if (event.getButton() == MouseButton.PRIMARY) {
                            final PlacedTile tileToPlace1 =
                                    new PlacedTile(tileToPlace.getValue(),
                                            currentPlayer.getValue(),
                                            tileRotation.getValue(),
                                            pos);
                            if (currentBoard.canAddTile(tileToPlace1)) {
                                tileMoves.accept(pos);
                            }
                        }
                    }
                });

                group.getChildren().add(tileFace);
                boardGridPane.add(group, x + scope, y + scope);
            }
        }

        // Quelques manipulation pour mettre en commun la structure de l'interface graphique du plateau de jeu
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("board-scroll-pane");
        scrollPane.getStylesheets().add("board.css");
        scrollPane.setContent(boardGridPane);
        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);

        return scrollPane;
    }

    /**
     * Regroupement des éléments essentiels pour les transformations de chaque tuile, afin d'alléger le code lorsqu'on
     * assigne des auditeurs aux arguments de cette méthode (qui sont les éléments "essentiels" en question)
     *
     * @param image
     *         l'image de la tuile
     * @param rotation
     *         la rotation de la tuile
     * @param veilColour
     *         la couleur de la voile
     */
    private record CellData(Image image, Rotation rotation, Color veilColour) {
    }
}