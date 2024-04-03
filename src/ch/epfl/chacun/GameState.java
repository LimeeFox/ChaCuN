package ch.epfl.chacun;

import java.util.*;

/**
 *
 * @param players la liste de tous les joueurs de la partie, dans l'ordre dans lequel ils doivent jouer
 *                donc avec le joueur courant en tête de liste
 * @param tileDecks les trois tas des tuiles restantes
 * @param tileToPlace l'éventuelle tuile à placer, qui à été prise du sommet du tas des tuiles normales
 *                    ou du tas des tuiles menhir, et qui peut être null si aucune tuile n'est à placer actuellement
 * @param board le plateau de jeu
 * @param nextAction la prochaine action à effectuer
 * @param messageBoard le tableau d'affichage contenant les messages générés jusqu'à présent dans la partie
 */
public record GameState(
        List<PlayerColor> players,
        TileDecks tileDecks,
        Tile tileToPlace,
        Board board,
        Action nextAction,
        MessageBoard messageBoard) {

    /**
     * Les actions différentes qui peuvent être effectuées par des joueurs
     */
    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
        }

    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction == Action.PLACE_TILE);
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);

        players = List.copyOf(players);
    }

    /**
     * Initialisation d'une partie avec un état initial
     *
     * @param players
     * @param tileDecks
     * @param textMaker
     * @return l'état de jeu initial pour les joueurs, tas et «créateur de texte» donnés,
     *         dont la prochaine action est START_GAME (donc la tuile à placer est null),
     *         et dont le plateau et le tableau d'affichage sont vides
     */
    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.START_GAME,
                new MessageBoard(textMaker, List.of()));
    }

    /**
     * Obtenir le joueur courant (qui joue en ce moment-ci)
     *
     * @return le joueur courant, ou null s'il n'y en a pas, c.-à-d. si la prochaine action est START_GAME ou END_GAME
     */
    public PlayerColor currentPlayer() {
        if (nextAction != Action.START_GAME && nextAction != Action.END_GAME) return null;
        return players.getFirst();
    }

    /**
     * Compter le nombre d'occupants d'un @kind spécifié appartenant à un @player qui ne sont pas placés sur le plateau
     *
     * @param player joueur dont on veut compter les occupants libres
     * @param kind le type des occupents libres qu'on veut compter
     * @return le nombre d'occupants libres, c.-à-d. qui ne sont pas actuellement placés
     *         sur le plateau de jeu du type donné et appartenant au joueur donné
     */
    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        return board.occupantCount(player, kind);
    }

    /**
     * Obtenir un ensemble d'occupants potentiels de la dernière tuile posée
     *
     * @return l'ensemble des occupants potentiels de la dernière tuile posée que le joueur courant pourrait
     *         effectivement placer
     * @throws IllegalArgumentException si le plateau est vide
     */
    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(board != Board.EMPTY);
        PlacedTile tile = board.lastPlacedTile();

        return tile.potentialOccupants(); //@todo voir comment les autres ont fait parecque ducoup il manque un truc je crois...
    }

    /**
     * Gère la transition de START_GAME à PLACE_TILE en plaçant la tuile de départ au centre du plateau
     * et en tirant la première tuile du tas des tuiles normales, qui devient la tuile à jouer
     *
     * @return le nouveau état du jeu, mis à jour.
     * @throws IllegalArgumentException si la prochaine action n'est pas START_GAME
     */
    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction != Action.START_GAME);

        return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, messageBoard);
    }

    /**
     * Gère toutes les transitions à partir de PLACE_TILE en ajoutant la tuile donnée au plateau,
     * attribuant les éventuels points obtenus suite à la pose de la pirogue ou de la fosse à pieux
     * et déterminant l'action suivante, qui peut être RETAKE_PAWN si la tuile posée contient le chaman;
     *
     * @param tile la tuile qu'on aimerait ajouter au plateau
     * @return le nouveau état du jeu, mis à jour.
     * @throws IllegalArgumentException si la prochaine action n'est pas PLACE_TILE, ou si la tuile passée est déjà occupée
     */
    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction != Action.PLACE_TILE || tile.occupant() != null);

        Board newBoard = board.withNewTile(tile);
        final TileDecks newTileDecks = tileDecks.withTopTileDrawn(tile.kind());
        Tile newTile = null;

        // @todo coder l'ajout des points, section 2.2, 2.3
        Action newAction = Action.OCCUPY_TILE;//@todo c'est possible de ne pas pouvoir en placer, actually: 1) if player has no free occupants left, 2) if the area is alraedy occupied
        List<PlayerColor> newPlayerList = players;
        Zone zone = tile.specialPowerZone();

        if (zone != null) {
            final int freeOccupants = freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN);
            switch (zone) {
                // Si le pouvoir spécial est SHAMAN && le joueur possède au moins un pion sur le plateau,
                // alors il peut reprendre un pion
                case Zone.Meadow meadow -> {
                    if (freeOccupants > 0) {
                        if (meadow.SpecialPower() == Zone.SpecialPower.SHAMAN) {
                            newAction = Action.RETAKE_PAWN;
                        }
                    } else if (freeOccupants == 0) {
                        // Le joueur n'a plus de pions libres, donc l'action suivante sera forcément placer une
                        // nouvelle tuile
                        newTile = tileDecks.topTile(tile.kind());
                        newAction = Action.PLACE_TILE;
                        newPlayerList = shiftAndGetPlayerList();
                    }
                }
                // Le cas d'une forêt serait intéressent si elle contient un menhir
                // car selon les règles du jeu, le joueur peut dans ce cas poser une deuxième tuile.
                // On s'intéresse aussi à la zone forêt lorsqu'elle ferme une aire de forêt qui contient un menhir.
                case Zone.Forest forest -> {
                    if (freeOccupants == 0) {
                        newAction = Action.PLACE_TILE;
                    }
                    // Si la zone ferme une aire de forêt avec un menhir, on s'arrête à là. //@todo i think place_tile for menhirs should be called at withOccupant AH UNLESS IT HAS 0
                    Area<Zone.Forest> forestArea = board.forestArea(forest);
                    if (forestArea.isClosed() && Area.hasMenhir(forestArea)) break; //@todo check dans withNewOccupant that we dont play 3 times in a row xDDDD (pls help)

                    // Autrement, on regarde si la zone elle-même contient un menhir:
                    if (forest.kind() != Zone.Forest.Kind.WITH_MENHIR) break;
                    PlacedTile lastPlacedTile = board.lastPlacedTile();
                    if (lastPlacedTile == null) break;

                    // La vérification si-dessous permet d'assurer que le joueur ne joue pas 3 fois de suite.
                    PlayerColor lastPlacer = lastPlacedTile.placer();
                    if (currentPlayer() == lastPlacer) {
                        newTile = tileDecks.topTile(tile.kind());
                        newPlayerList = shiftAndGetPlayerList();
                        newAction = Action.PLACE_TILE;
                        break;
                    }
                }
                default -> {}
            }
        }
        // @todo IF NEXT ACTION IS OCCUPY_TILE, THEN THE PLAYER ORDER SHOULD NOT CHANGE


        // @todo get a new tileToPlace
        // @todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo
        // @todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo
        // @todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo
        // @todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo
        // @todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo
        // @todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo
        return new GameState(newPlayerList, newTileDecks, newTile, newBoard, newAction, messageBoard);
    }

    /**
     * Gère toutes les transitions à partir de RETAKE_PAWN, en supprimant l'occupant donné, sauf s'il vaut null,
     * ce qui indique que le joueur ne désire pas reprendre de pion
     *
     * @param occupant l'occupant à supprimer du Plateau du jeu
     * @return le nouveau état du jeu, mis à jour.
     * @throws IllegalArgumentException si la prochaine action n'est pas RETAKE_PAWN,
     *                                  ou si l'occupant donné n'est ni null, ni un pion
     */
    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction != Action.RETAKE_PAWN ||
                (occupant != null && occupant.kind() != Occupant.Kind.PAWN));

        if (occupant != null) {
            board.withoutOccupant(occupant);
        }
        //todo update messageboard
        return new GameState(shiftAndGetPlayerList(), tileDecks, tileToPlace, board, Action.OCCUPY_TILE, messageBoard);
    }

    /**
     * Gère toutes les transitions à partir de OCCUPY_TILE en ajoutant l'occupant donné à la dernière tuile posée,
     * sauf s'il vaut null, ce qui indique que le joueur ne désire pas placer d'occupant
     *
     * @param occupant l'occupant à rajouter à la dernière tuile posée
     * @return le nouveau état du jeu, mis à jour.
     * @throws IllegalArgumentException si la prochaine action n'est pas OCCUPY_TILE
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction != Action.OCCUPY_TILE);

        //todo check if the last placer was the same player, in which case next action might be place tile, but it can also be end_game

        if (occupant != null) {
            PlacedTile lastPlacedTile = board.lastPlacedTile();
            PlacedTile updatedLastPlacedTile;
            if (lastPlacedTile != null) {
                updatedLastPlacedTile = lastPlacedTile.withOccupant(occupant); //todo enft je ne sais pas comment faire pour update la tuile deja posee oops
            }
        }

        return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, messageBoard); //todo update all fields
    }

    /**
     * Décaler tous les joueurs d'un cran, pour que chacun puisse jouer à son tour, en sachant que
     * celui qui est en tête de la liste est celui qui joue actuellement
     *
     * @return la liste de tous les joueurs de la partie, dans l'ordre dans lequel ils doivent jouer
     *         donc avec le joueur courant en tête de liste
     */
    private List<PlayerColor> shiftAndGetPlayerList() {
        List<PlayerColor> playerList = new ArrayList<>(players);
        List<PlayerColor> newList = new ArrayList<>();

        newList.add(playerList.getFirst());

        playerList.removeFirst();
        newList.addAll(playerList);

        return newList;
    }



}
