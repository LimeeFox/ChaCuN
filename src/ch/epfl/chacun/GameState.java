package ch.epfl.chacun;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Classe qui définit les différents état de jeu.
 *
 * @param players
 *         la liste de tous les joueurs de la partie, dans l'ordre dans lequel ils doivent jouer
 *         donc avec le joueur courant en tête de liste
 * @param tileDecks
 *         les trois tas des tuiles restantes
 * @param tileToPlace
 *         l'éventuelle tuile à placer, qui à été prise du sommet du tas des tuiles normales
 *         ou du tas des tuiles menhir, et qui peut être null si aucune tuile n'est à placer actuellement
 * @param board
 *         le plateau de jeu
 * @param nextAction
 *         la prochaine action à effectuer
 * @param messageBoard
 *         le tableau d'affichage contenant les messages générés jusqu'à présent dans la partie
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
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
     *         la liste de joueurs qui vont jouer dans la partie
     * @param tileDecks
     *         les piles de tuiles (1x START,
     * @param textMaker
     *         l'état initial d'un
     * @return l'état de jeu initial pour les joueurs, tas et «créateur de texte» donnés,
     * dont la prochaine action est START_GAME (donc la tuile à placer est null),
     * et dont le plateau et le tableau d'affichage sont vides
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
        return (nextAction != Action.START_GAME && nextAction != Action.END_GAME) ? players.getFirst() : null;
    }

    /**
     * Compter le nombre d'occupants d'un @kind spécifié appartenant à un @player qui ne sont pas placés sur le plateau
     *
     * @param player
     *         joueur dont on veut compter les occupants libres
     * @param kind
     *         le type des occupants libres qu'on veut compter
     * @return le nombre d'occupants libres, c.-à-d. qui ne sont pas actuellement placés
     * sur le plateau de jeu du type donné et appartenant au joueur donné
     */
    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        return Occupant.occupantsCount(kind) - board.occupantCount(player, kind);
    }

    /**
     * Obtenir un ensemble d'occupants potentiels de la dernière tuile posée
     *
     * @return l'ensemble des occupants potentiels de la dernière tuile posée que le joueur courant pourrait
     * effectivement placer
     * @throws IllegalArgumentException
     *         si le plateau est vide
     */
    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(!board.equals(Board.EMPTY));
        PlacedTile tile = board.lastPlacedTile();
        Set<Occupant> filteredPotentialOccupants = new HashSet<>();

        tile.potentialOccupants().forEach(occupant -> {
            // On enlève l'occupant potentiel si le joueur n'a plus d'occupants libres
            int freeOccupants = freeOccupantsCount(currentPlayer(), occupant.kind());
            boolean hasFreeOccupants = freeOccupantsCount(currentPlayer(), occupant.kind()) > 0;

            boolean isValid = hasFreeOccupants;

            if (hasFreeOccupants) {
                Zone zone = tile.zoneWithId(occupant.zoneId());
                switch (zone) {
                    case Zone.Forest forest -> isValid = !board.forestArea(forest).isOccupied();
                    case Zone.Meadow meadow -> isValid = !board.meadowArea(meadow).isOccupied();
                    case Zone.Lake lake -> isValid = !board.riverSystemArea(lake).isOccupied();
                    case Zone.River river -> isValid = (occupant.kind() == Occupant.Kind.PAWN) ?
                            !board.riverArea(river).isOccupied() :
                            !board.riverSystemArea(river).isOccupied();
                }
            }
            if (isValid) filteredPotentialOccupants.add(occupant);
        });
        return filteredPotentialOccupants;
    }

    /**
     * Gère la transition de START_GAME à PLACE_TILE en plaçant la tuile de départ au centre du plateau
     * et en tirant la première tuile du tas des tuiles normales, qui devient la tuile à jouer
     *
     * @return le nouvel état du jeu, mis à jour.
     * @throws IllegalArgumentException
     *         si la prochaine action n'est pas START_GAME
     */
    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction == Action.START_GAME);

        TileDecks updatedTileDecks = tileDecks.withTopTileDrawn(Tile.Kind.START);
        Tile updatedTileToPlace = tileDecks.topTile(Tile.Kind.NORMAL);
        Board upatedBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START),
                null, Rotation.NONE, Pos.ORIGIN));
        updatedTileDecks = updatedTileDecks.withTopTileDrawn(Tile.Kind.NORMAL);
        return new GameState(
                players, updatedTileDecks, updatedTileToPlace, upatedBoard, Action.PLACE_TILE, messageBoard);
    }

    /**
     * Gère toutes les transitions à partir de PLACE_TILE en ajoutant la tuile donnée au plateau,
     * attribuant les éventuels points obtenus suite à la pose de la pirogue ou de la fosse à pieux
     * et déterminant l'action suivante, qui peut être RETAKE_PAWN si la tuile posée contient le chaman;
     *
     * @param tile
     *         la tuile qu'on aimerait ajouter au plateau
     * @return le nouvel état du jeu, mis à jour.
     * @throws IllegalArgumentException
     *         si la prochaine action n'est pas PLACE_TILE, ou si la tuile passée est déjà occupée
     */
    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tile.occupant() == null);

        // Màj par défaut des paramètres de GameState
        // Pas de màj de players (par défaut, le même joueur qui a posé la tuile devra poser un occupant)
        List<PlayerColor> updatedPlayerList = players;
        final TileDecks updatedTileDecks = tileDecks;
        Tile updatedTileToPlace = null;
        Board updatedBoard = board.withNewTile(tile);
        Action updatedNextAction = Action.OCCUPY_TILE;
        // La màj de messageBoard dépend des points obtenus et des pouvoirs utilisés
        MessageBoard updatedMessageBoard = messageBoard;

        PlayerColor scorer = currentPlayer();

        final int placedPawns = updatedBoard.occupantCount(currentPlayer(), Occupant.Kind.PAWN);

        // Traitement des pouvoirs spéciaux
        Zone specialPowerZone = tile.specialPowerZone();
        if (specialPowerZone != null) {
            List<Zone.SpecialPower> immediateEffectPowers = List.of(Zone.SpecialPower.SHAMAN, Zone.SpecialPower.LOGBOAT,
                    Zone.SpecialPower.HUNTING_TRAP);
            Zone.SpecialPower tilePower = specialPowerZone.specialPower();
            if (immediateEffectPowers.contains(tilePower)) {
                switch (tilePower) {
                    case SHAMAN -> {
                        if (placedPawns > 0) {
                            return new GameState(updatedPlayerList, updatedTileDecks, updatedTileToPlace, updatedBoard,
                                    Action.RETAKE_PAWN, updatedMessageBoard);
                        }
                    }
                    case LOGBOAT -> {
                        Zone.Water logBoatZone = (Zone.Water) specialPowerZone;
                        updatedMessageBoard = updatedMessageBoard.withScoredLogboat(scorer,
                                updatedBoard.riverSystemArea(logBoatZone));
                    }
                    case HUNTING_TRAP -> {
                        if (!(specialPowerZone instanceof Zone.Meadow meadowZone)) break;
                        Area<Zone.Meadow> adjacentMeadow = updatedBoard.adjacentMeadow(tile.pos(), meadowZone);

                        // Compter les animaux
                        Set<Animal> animals = new HashSet<>();
                        for (Zone.Meadow meadow : adjacentMeadow.zones()) {
                            animals.addAll(meadow.animals());
                        }
                        Map<Animal.Kind, Long> animalCount = animals.stream()
                                .collect(Collectors.groupingBy(Animal::kind, Collectors.counting()));

                        // Filtrer les animaux
                        Set<Animal> cancelledAnimals = new HashSet<>();
                        long tigerCount = animalCount.getOrDefault(Animal.Kind.TIGER, 0L);
                        long deerCount = animalCount.getOrDefault(Animal.Kind.DEER, 0L);
                        // Compter le nombre de cerfs à anuller en fonction du nombre de tigres (qui les mangent)
                        int deerToCancel = (int) Math.min(deerCount, tigerCount);
                        animals.stream()
                                .filter(animal -> animal.kind() == Animal.Kind.DEER)
                                .limit(deerToCancel)
                                .forEach(cancelledAnimals::add);

                        updatedMessageBoard = updatedMessageBoard.withScoredHuntingTrap(scorer, adjacentMeadow);
                        updatedBoard.withMoreCancelledAnimals(cancelledAnimals);
                    }
                }
            }
        }
        return new GameState(updatedPlayerList, updatedTileDecks, updatedTileToPlace, updatedBoard, updatedNextAction,
                updatedMessageBoard).withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Methode d'aide qui permet de gerer la fin d'un tour. Le joueur actuel devient le prochain joueur
     * si il a posé une tuile forêt avec un menhir ou si il a fermé une aire forêt qui contenait un menhir, tant qu'il
     * n'a joué qu'une seule fois à présent. Autrement, l'ordre des joueurs change.
     * Cette méthode accorde aussi certains points si des aires ont été fermées
     *
     * @return l'état du jeu avec le tour terminé
     */
    private GameState withTurnFinished() {
        Preconditions.checkArgument(!board().equals(Board.EMPTY));

        List<PlayerColor> updatedPlayers = players;
        TileDecks updatedTileDecks = tileDecks;
        Tile updatedTileToPlace = tileToPlace;
        Board updatedBoard = board;
        MessageBoard updatedMessageBoard = messageBoard;

        Tile.Kind tileKind = Tile.Kind.NORMAL;

        PlacedTile lastPlacedTile = board.lastPlacedTile();

        boolean playerGetsMenhir = false;

        // Gérer les aires rivères fermées lors de ce tour
        Set<Area<Zone.River>> lastClosedRivers = new HashSet<>();
        if (board.riversClosedByLastTile() != null) {
            lastClosedRivers = board.riversClosedByLastTile();
            for (Area<Zone.River> closedRiver : Objects.requireNonNull(lastClosedRivers)) {
                updatedMessageBoard = updatedMessageBoard.withScoredRiver(closedRiver);
            }
        }

        // Gérer les aires forêt fermées lors de ce tour
        Set<Area<Zone.Forest>> lastClosedForests = new HashSet<>();
        if (board.forestsClosedByLastTile() != null) {
            lastClosedForests = board.forestsClosedByLastTile();
            for (Area<Zone.Forest> closedForest : Objects.requireNonNull(lastClosedForests)) {
                updatedMessageBoard = updatedMessageBoard.withScoredForest(closedForest);

                if (lastPlacedTile != null
                        && Area.hasMenhir(closedForest)
                        && !lastPlacedTile.tile().kind().equals(Tile.Kind.MENHIR)
                        && !playerGetsMenhir) {
                    playerGetsMenhir = true;
                    updatedMessageBoard = updatedMessageBoard.withClosedForestWithMenhir(currentPlayer(), closedForest);
                }
            }
        }

        updatedBoard = updatedBoard.withoutGatherersOrFishersIn(lastClosedForests, lastClosedRivers);

        // Si le joueur courant a posé une forêt contenant un menhir, on lui accorde un deuxième tour
        // todo enfaite il n'y a pas de TEST provided pour voir si on traite le cas ou le joueur joue plus de 2 fois, de ce que je vois.
        // todo est-ce que on traite bien le cas ou joueur pose une tuile forest avec menhir -> obtien une tuile du type MENHIR qu'il pose et ferme ainsi une autre foret avec un menhir -> etc...?
        if (playerGetsMenhir) {
            tileKind = Tile.Kind.MENHIR;
        } else {
            updatedPlayers = shiftAndGetPlayerList();
        }

        // Si il n'y a plus de tuiles à poser, alors c'est la fin du jeu
        if (updatedTileDecks.normalTiles().isEmpty() && !playerGetsMenhir) {
            return new GameState(updatedPlayers, updatedTileDecks, updatedTileToPlace, updatedBoard, Action.END_GAME,
                    updatedMessageBoard).withFinalPointsCounted();
        }

        // Mise à jour du tas des tuiles à piocher
        updatedTileDecks = updatedTileDecks.withTopTileDrawnUntil(tileKind, board::couldPlaceTile);
        updatedTileToPlace = updatedTileDecks.topTile(tileKind);
        updatedTileDecks = updatedTileDecks.withTopTileDrawn(tileKind);

        return new GameState(updatedPlayers, updatedTileDecks, updatedTileToPlace, updatedBoard, Action.PLACE_TILE,
                updatedMessageBoard);
    }

    //todo optimise cuz this is a very adrien-optimal code (manière "rudimentaire")
    //todo also it's still litterally a copy paste of his work so this is dangerous. Have to do things our way
    //todo CYRIAC CETTE METHODE J'EN PEUX PLUS A L'AIDE STP <33333333
    private GameState withFinalPointsCounted() {
        Preconditions.checkArgument(nextAction.equals(Action.END_GAME));

        Board updatedBoard = board;
        MessageBoard updatedMessageBoard = messageBoard;

        // Gérer les animaux annulés
        for (Area<Zone.Meadow> meadowArea : updatedBoard.meadowAreas()) {
            Set<Animal> animals = Area.animals(meadowArea, updatedBoard.cancelledAnimals());
            Set<Animal> cancelledAnimals = new HashSet<>(updatedBoard.cancelledAnimals());

            // Obtention de tous les tigres et cerfs non-annulés
            Set<Animal> tigers = new HashSet<>();
            Set<Animal> deer = new HashSet<>();
            for (Animal animal : animals) {
                if (!cancelledAnimals.contains(animal)) {
                    if (animal.kind() == Animal.Kind.TIGER) tigers.add(animal);
                    else if (animal.kind() == Animal.Kind.DEER) deer.add(animal);
                }
            }

            // Si la zonne contient du feu, alors les tigres dans l'aire doivent être annulés
            Zone.Meadow fire = (Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE);
            if (fire != null) {
                updatedBoard = updatedBoard.withMoreCancelledAnimals(tigers);
                cancelledAnimals.addAll(tigers);
            }

            // Récuperer les cerfs dans les zones adjacentes à
            Zone.Meadow trapZone = (Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP);
            Area<Zone.Meadow> adjacentArea = new Area<>(Set.of(), List.of(), 0);
            if (trapZone != null) {
                adjacentArea = updatedBoard
                        .adjacentMeadow(updatedBoard.tileWithId(Zone.tileId(trapZone.id())).pos(), trapZone);
            }
            Set<Animal> adjacentAnimals = Area.animals(adjacentArea, cancelledAnimals);

            List<Animal> adjacentDeer = new ArrayList<>();
            adjacentAnimals.forEach(animal -> {
                if (animal.kind() == Animal.Kind.DEER) adjacentDeer.add(animal);
            });

            List<Animal> nonAdjacentDeer = new ArrayList<>();
            animals.forEach(animal -> {
                if (animal.kind() == Animal.Kind.DEER && !adjacentAnimals.contains(animal)) nonAdjacentDeer.add(animal);
            });

            //@Deprecated// OLD COMMENT: cancel first the deer that aren't in the adjacent meadow
            // Annuler les animaux qui ne sont pas dans les zones pré adjacentes
            if (fire == null) {
                if (tigers.size() < deer.size()) {
                    Set<Animal> toCancel = new HashSet<>();
                    for (Animal tiger : tigers) {
                        if (!nonAdjacentDeer.isEmpty())
                            toCancel.add(nonAdjacentDeer.removeFirst());
                        else
                            toCancel.add(adjacentDeer.removeFirst());
                        toCancel.add(tiger);
                    }
                    updatedBoard = updatedBoard.withMoreCancelledAnimals(toCancel);
                } else {
                    updatedBoard = updatedBoard.withMoreCancelledAnimals(deer);
                    updatedBoard = updatedBoard.withMoreCancelledAnimals(tigers);
                }
            }

            // Calcul des points
            if (trapZone != null)
                updatedMessageBoard = updatedMessageBoard.withScoredPitTrap(adjacentArea, updatedBoard.cancelledAnimals());
            updatedMessageBoard = updatedMessageBoard.withScoredMeadow(meadowArea, updatedBoard.cancelledAnimals());
        }

        // Les systèmes hydrographiques
        for (Area<Zone.Water> riverSystem : updatedBoard.riverSystemAreas()) {
            if (riverSystem.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null)
                updatedMessageBoard = updatedMessageBoard.withScoredRaft(riverSystem);
            updatedMessageBoard = updatedMessageBoard.withScoredRiverSystem(riverSystem);
        }

        // Message final
        Map<PlayerColor, Integer> pts = updatedMessageBoard.points();

        int maxPts = pts.values().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        Set<PlayerColor> winners = pts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxPts)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        updatedMessageBoard = updatedMessageBoard.withWinners(winners, maxPts);

        return new GameState(players, tileDecks, null, board, Action.END_GAME, updatedMessageBoard);
    }

    private GameState withTurnFinishedIfOccupationImpossible() {
        // Le joueur ne peut passer à OCCUPY_TILE seulement s'il reste de la place sur la dernière tuile
        if (!lastTilePotentialOccupants().isEmpty()) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.OCCUPY_TILE, messageBoard);
        }

        // Sinon, on vérifie la fin du tour du joueur
        return withTurnFinished();
    }

    /**
     * Gère toutes les transitions à partir de RETAKE_PAWN, en supprimant l'occupant donné, sauf s'il vaut null,
     * ce qui indique que le joueur ne désire pas reprendre de pion
     *
     * @param occupant
     *         l'occupant à supprimer du Plateau du jeu
     * @return le nouvel état du jeu, mis à jour.
     * @throws IllegalArgumentException
     *         si la prochaine action n'est pas RETAKE_PAWN,
     *         ou si l'occupant donné n'est ni null, ni un pion
     */
    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.RETAKE_PAWN &&
                (occupant == null || occupant.kind() == Occupant.Kind.PAWN));

        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.OCCUPY_TILE, messageBoard);

        }

        GameState updatedGameSate = new GameState(players, tileDecks, tileToPlace,
                board.withoutOccupant(occupant), Action.OCCUPY_TILE, messageBoard);

        return updatedGameSate.withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Gère toutes les transitions à partir de OCCUPY_TILE en ajoutant l'occupant donné à la dernière tuile posée,
     * sauf s'il vaut null, ce qui indique que le joueur ne désire pas placer d'occupant
     *
     * @param occupant
     *         l'occupant à rajouter à la dernière tuile posée
     * @return le nouvel état du jeu, mis à jour.
     * @throws IllegalArgumentException
     *         si la prochaine action n'est pas OCCUPY_TILE
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);

        // Si le joueur ne souhaite pas placer d'occupant
        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, nextAction, messageBoard).withTurnFinished();
        }

        Board updatedBoard = board.withOccupant(occupant);

        return new GameState(players, tileDecks, tileToPlace, updatedBoard, nextAction, messageBoard)
                .withTurnFinished();
    }

    /**
     * Décaler tous les joueurs d'un cran, pour que chacun puisse jouer à son tour, en sachant que
     * celui qui est en tête de la liste est celui qui joue actuellement
     *
     * @return la liste de tous les joueurs de la partie, dans l'ordre dans lequel ils doivent jouer
     * donc avec le joueur courant en tête de liste
     */
    private List<PlayerColor> shiftAndGetPlayerList() {
        List<PlayerColor> playerList = new ArrayList<>(players);

        PlayerColor lastPlayer = playerList.getFirst();
        playerList.removeFirst();
        List<PlayerColor> newList = new ArrayList<>(playerList);
        newList.add(lastPlayer);

        return newList;
    }
}