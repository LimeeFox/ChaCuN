package ch.epfl.chacun;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        if (nextAction != Action.START_GAME && nextAction != Action.END_GAME) return players.getFirst();
        return null;
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
        return Occupant.occupantsCount(kind) - board.occupantCount(player, kind);
    }

    /**
     * Obtenir un ensemble d'occupants potentiels de la dernière tuile posée
     *
     * @return l'ensemble des occupants potentiels de la dernière tuile posée que le joueur courant pourrait
     *         effectivement placer
     * @throws IllegalArgumentException si le plateau est vide
     */
    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(!board.equals(Board.EMPTY));
        PlacedTile tile = board.lastPlacedTile();
        Set<Occupant> filteredPotentialOccupants = new HashSet<>();

        tile.potentialOccupants().forEach(occupant -> {
            // On enlève l'occupant potentiel si le joueur n'a plus d'occupants libres
            boolean isValid;

            if (freeOccupantsCount(currentPlayer(), occupant.kind()) == 0) {
                isValid = false;
            } else {
                Zone zone = tile.zoneWithId(occupant.zoneId());
                switch (zone) {
                    case Zone.Forest forest -> isValid = !board.forestArea(forest).isOccupied();
                    case Zone.Meadow meadow -> isValid = !board.meadowArea(meadow).isOccupied();
                    case Zone.Lake lake -> isValid = !board.riverSystemArea(lake).isOccupied();
                    case Zone.River river -> {
                        if (occupant.kind() == Occupant.Kind.PAWN) {
                            isValid = !board.riverArea(river).isOccupied();
                        } else {
                            isValid = !board.riverSystemArea(river).isOccupied();
                        }
                    }
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
     * @throws IllegalArgumentException si la prochaine action n'est pas START_GAME
     */
    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction == Action.START_GAME);

        return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, messageBoard);
    }

    /**
     * Gère toutes les transitions à partir de PLACE_TILE en ajoutant la tuile donnée au plateau,
     * attribuant les éventuels points obtenus suite à la pose de la pirogue ou de la fosse à pieux
     * et déterminant l'action suivante, qui peut être RETAKE_PAWN si la tuile posée contient le chaman;
     *
     * @param tile la tuile qu'on aimerait ajouter au plateau
     * @return le nouvel état du jeu, mis à jour.
     * @throws IllegalArgumentException si la prochaine action n'est pas PLACE_TILE, ou si la tuile passée est déjà occupée
     */
    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE && tile.occupant() == null);

        Board newBoard = board.withNewTile(tile);
        final TileDecks newTileDecks = tileDecks.withTopTileDrawn(tile.kind());
        Tile newTile = null;

        // @todo coder l'ajout des points, section 2.2, 2.3
        Action newAction = Action.OCCUPY_TILE; // @todo c'est possible de ne pas pouvoir en placer, actually: 1) if player has no free occupants left, 2) if the area is alraedy occupied
        List<PlayerColor> newPlayerList = players;
        MessageBoard newMessageBoard = messageBoard;

        final int freeOccupants = freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN);
        final int placedOccupants = newBoard.occupantCount(currentPlayer(), Occupant.Kind.PAWN);

        Zone specialPowerZone = tile.specialPowerZone();
        if (specialPowerZone != null) {
            switch (specialPowerZone.specialPower()) {
                case HUNTING_TRAP -> {
                    if (!(specialPowerZone instanceof Zone.Meadow meadowZone)) break; // ce cas ne devrait pas arriver
                    Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), meadowZone);

                    // Compter les animaux
                    Set<Animal> animals = new HashSet<>();
                    for (Zone.Meadow meadow : adjacentMeadow.zones()) {
                        animals.addAll(meadow.animals());
                    }
                    Map<Animal.Kind, Long> animalCount = animals.stream()
                            .collect(Collectors.groupingBy(Animal::kind, Collectors.counting()));

                    /* TODO: les animaux mangés seront à passer en argument de messageboard smth smth lors d'une
                    TODO: etape ultérieure. Voir 3.1.1.1
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
                     */

                    newMessageBoard.withScoredHuntingTrap(currentPlayer(), adjacentMeadow);
                    newBoard.withMoreCancelledAnimals(animals);
                }
                case LOGBOAT -> {
                }
                case SHAMAN -> {
                    if (placedOccupants > 0) newAction = Action.RETAKE_PAWN;
                }
                case PIT_TRAP -> {
                }
                case RAFT -> {
                }
                case WILD_FIRE -> {
                }
                case null -> {
                }
            }
        }

        /*
        if (zone != null) {
            switch (zone) {
                // Si le pouvoir spécial est SHAMAN && le joueur possède au moins un pion sur le plateau,
                // alors il peut reprendre un pion
                case Zone.Meadow meadow -> {
                    Area<Zone.Meadow> meadowArea = newBoard.meadowArea(meadow);
                    if (placedOccupants == 0 || meadowArea.isOccupied()) {
                        // Le joueur n'a plus de pions libres OU l'aire est déjà occupée,
                        // donc l'action suivante sera forcément placer une nouvelle tuile
                        newTile = tileDecks.topTile(tile.kind());
                        newAction = Action.PLACE_TILE;
                        newPlayerList = shiftAndGetPlayerList();
                    } else if (placedOccupants > 0) {
                        switch(meadow.specialPower()) {
                            case HUNTING_TRAP -> {
                                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), (Zone.Meadow) specialPowerZone);

                                // count the animals
                                Set<Animal> animals = new HashSet<>();
                                for (Zone.Meadow meadow : adjacentMeadow.zones()) {
                                    animals.addAll(meadow.animals());
                                }
                                Map<Animal.Kind, Integer> animalMap = new HashMap<>();
                                for (Animal animal : animals) {
                                    switch (animal.kind()) {
                                        case DEER    -> animalMap.put(Animal.Kind.DEER,    1 + animalMap.getOrDefault(Animal.Kind.DEER, 0));
                                        case TIGER   -> animalMap.put(Animal.Kind.TIGER,   1 + animalMap.getOrDefault(Animal.Kind.TIGER, 0));
                                        case AUROCHS -> animalMap.put(Animal.Kind.AUROCHS, 1 + animalMap.getOrDefault(Animal.Kind.AUROCHS, 0));
                                        case MAMMOTH -> animalMap.put(Animal.Kind.MAMMOTH, 1 + animalMap.getOrDefault(Animal.Kind.MAMMOTH, 0));
                                        case null -> {}
                                    }
                                }
                                // cancel the animals
                                Set<Animal> cancelledAnimals = new HashSet<>(); // todo add to an ulterior step (3.1.1.1)
                                int nbTiger = animalMap.getOrDefault(Animal.Kind.TIGER, 0);
                                int nbDeer = animalMap.getOrDefault(Animal.Kind.DEER, 0);
                                if (nbDeer < nbTiger) {
                                    for (Animal animal : animals) {
                                        if (animal.kind() == Animal.Kind.DEER)
                                            cancelledAnimals.add(animal);
                                    }
                                } else for (int i = 0; i < nbTiger; i++) {
                                    for (Animal animal : animals) {
                                        if (animal.kind() == Animal.Kind.DEER && !cancelledAnimals.contains(animal)) {
                                            cancelledAnimals.add(animal);
                                            break;
                                        }
                                    }
                                }

                                newMessageBoard.withScoredHuntingTrap(currentPlayer(), adjacentMeadow);
                                newBoard.withMoreCancelledAnimals(animals);
                            }
                            if (meadow.SpecialPower() == Zone.SpecialPower.SHAMAN) {
                                newAction = Action.RETAKE_PAWN;
                            }
                        }

                    }
                }
                // Le cas d'une forêt serait intéressent si elle contient un menhir
                // car selon les règles du jeu, le joueur peut dans ce cas poser une deuxième tuile.
                // On s'intéresse aussi à la zone forêt lorsqu'elle ferme une aire de forêt qui contient un menhir.
                case Zone.Forest forest -> {
                    // Si la zone ferme une aire de forêt avec un menhir, on s'arrête à là. //@todo i think place_tile for menhirs should be called at withOccupant AH UNLESS IT HAS 0
                    Area<Zone.Forest> forestArea = newBoard.forestArea(forest);
                    if (freeOccupants == 0 || forestArea.isOccupied()) {
                        newTile = tileDecks.topTile(tile.kind());
                        newPlayerList = shiftAndGetPlayerList();
                        newAction = Action.PLACE_TILE;
                    }
                    PlacedTile lastPlacedTile = newBoard.lastPlacedTile();
                    if (lastPlacedTile == null) break;
                    if (forestArea.isClosed()
                            && Area.hasMenhir(forestArea)
                            && lastPlacedTile.kind() == Tile.Kind.NORMAL) break; //@todo check dans withNewOccupant that we dont play 3 times in a row xDDDD (pls help)

                    // Autrement, on regarde si la zone elle-même contient un menhir:
                    if (forest.kind() != Zone.Forest.Kind.WITH_MENHIR) break;

                    // La vérification si-dessous permet d'assurer que le joueur ne joue pas 3 fois de suite.
                    PlayerColor lastPlacer = lastPlacedTile.placer();
                    if (currentPlayer() == lastPlacer) {
                        newTile = tileDecks.topTile(tile.kind());
                        newPlayerList = shiftAndGetPlayerList();
                        newAction = Action.PLACE_TILE;
                        break;
                    }
                }
                case Zone.River river -> {
                    Area<Zone.River> riverArea = newBoard.riverArea(river);
                    if (freeOccupants == 0 || riverArea.isOccupied()) {
                        newTile = tileDecks.topTile(tile.kind());
                        newPlayerList = shiftAndGetPlayerList();
                        newAction = Action.PLACE_TILE;
                    }
                }
                default -> {}
            }
        }

        for (Zone.Meadow meadow : tile.meadowZones()) {
            Area<Zone.Meadow> meadowArea = newBoard.meadowArea(meadow);
            if (placedOccupants == 0 || meadowArea.isOccupied()) {
                // Le joueur n'a plus de pions libres OU l'aire est déjà occupée,
                // donc l'action suivante sera forcément placer une nouvelle tuile
                newTile = tileDecks.topTile(tile.kind());
                newAction = Action.PLACE_TILE;
                newPlayerList = shiftAndGetPlayerList();
            }
            // Si le joueur ferme une aire, on gère les points
            if (meadowArea.isClosed()) {
                //todo messageboard AAH MAIS ATTENTION AUX POUVOIRS SPECIAUX
            }
        }

        for (Zone.Forest forest : tile.forestZones()) {
            Area<Zone.Forest> forestArea = newBoard.forestArea(forest);
            if (placedOccupants == 0 || forestArea.isOccupied()) {
                newTile = tileDecks.topTile(tile.kind());
                newAction = Action.PLACE_TILE;
                newPlayerList = shiftAndGetPlayerList();
            }
            if (forestArea.isClosed()) {
                //todo messageboard AAH MAIS ATTENTION AUX POUVOIRS SPECIAUX
            }
        }

        for (Zone.River river : tile.riverZones()) {
            Area<Zone.River> riverArea = newBoard.riverArea(river);
            Area<Zone.Water> riverSystemArea = newBoard.riverSystemArea(river);
            if (placedOccupants == 0 || (riverArea.isOccupied() && riverSystemArea.isOccupied())) {
                newTile = tileDecks.topTile(tile.kind());
                newAction = Action.PLACE_TILE;
                newPlayerList = shiftAndGetPlayerList();
            }
            if (riverArea.isClosed()) {
                //todo messageboard AAH MAIS ATTENTION AUX POUVOIRS SPECIAUX
            }

            if (riverSystemArea.isClosed()) {
                //todo messageboard AAH MAIS ATTENTION AUX POUVOIRS SPECIAUX
            }
        }


         */



        // @todo IF NEXT ACTION IS OCCUPY_TILE, THEN THE PLAYER ORDER SHOULD NOT CHANGE


        // @todo get a new tileToPlace
        return new GameState(newPlayerList, newTileDecks, newTile, newBoard, newAction, newMessageBoard);
    }

    /**
     * Methode d'aide qui permet de gerer la fin d'un tour
     *
     * @param board le plateau du jeu
     * @return
     */
    private GameState withTurnFinished(Board board, PlacedTile tile) {
        List<PlayerColor> newPlayerList = players;
        Predicate<Tile> tileCondition = tileToPlace -> board.canAddTile(tile);
        TileDecks newTileDecks = null;// = tileDecks.withTopTileDrawnUntil(tile.kind(), tileCondition);
        Tile.Kind tileKind = Tile.Kind.NORMAL;
        Tile newTile = null;
        Action newAction = Action.PLACE_TILE;
        MessageBoard newMessageBoard = messageBoard;

        PlacedTile lastPlacedTile = board.lastPlacedTile();

        final int placedOccupants = board.occupantCount(currentPlayer(), Occupant.Kind.PAWN);
        final int freeOccupants = freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN);

        // Traitement de toutes les forêt
        if (lastPlacedTile != null) {
            for (Zone.Forest forest : lastPlacedTile.forestZones()) {
                Area<Zone.Forest> forestArea = board.forestArea(forest);

                // Regarder si on ferme une aire avec un menhir
                if (forestArea.isClosed() && Area.hasMenhir(forestArea)) {
                    // Un joueur ne peut pas jouer plus de deux fois
                    if (lastPlacedTile.placer() == currentPlayer()) {
                        newPlayerList = shiftAndGetPlayerList();
                    } else {
                        // Le joueur peut rejouer, en posant une case menhir
                        tileKind = Tile.Kind.MENHIR;
                        newTileDecks = tileDecks.withTopTileDrawnUntil(tileKind, tileCondition);
                    }
                    break;
                }

                // Autrement, on regarde si la tuile posée elle-même contient un menhir:
                if (forest.kind() != Zone.Forest.Kind.WITH_MENHIR) {
                    newPlayerList = shiftAndGetPlayerList();
                    break;
                }

                // Un joueur ne peut pas jouer plus de deux fois
                if (currentPlayer() == lastPlacedTile.placer()) {
                    newPlayerList = shiftAndGetPlayerList();
                    break;
                }
                tileKind = Tile.Kind.MENHIR;
                newTileDecks = tileDecks.withTopTileDrawnUntil(tileKind, tileCondition);
            }

            // Traitement de toutes les aires rivieres/hydrographiques
            for (Zone.River river : lastPlacedTile.riverZones()) {
                Area<Zone.River> riverArea = board.riverArea(river);
                Area<Zone.Water> riverSystemArea = board.riverSystemArea(river);
                if (riverArea.isClosed()) {
                    //todo enft il doit se passer quoi quand on ferme une riviere? on fait des trucs avec des points?
                }

                if (riverSystemArea.isClosed()) {
                    //todo enft il doit se passer quoi quand on ferme un reseau hydrographique? on fait des trucs avec des points?
                }
            }
        }

        if (newTileDecks == null) {
            newTileDecks = tileDecks.withTopTileDrawnUntil(tileKind, tileCondition);
        }
        newTile = newTileDecks.topTile(tileKind);

        return new GameState(newPlayerList, newTileDecks, newTile, board, newAction, newMessageBoard);
    }

    private GameState withFinalPointsCounted(
            List<PlayerColor> newPlayers, TileDecks newTileDecks, Board newBoard, MessageBoard newMessageBoard) {
        Board board1 = newBoard;
        MessageBoard messageBoard1 = newMessageBoard;

        // add the cancelled animals (smilodon burned, eaten dears, animals already captured)
        for (Area<Zone.Meadow> meadow : board1.meadowAreas()) {
            Set<Animal> animals = Area.animals(meadow, board1.cancelledAnimals());
            Set<Animal> cancelledAnimals = board1.cancelledAnimals();

            // get all non-canceled tigers and all non-cancelled deer
            Set<Animal> tigers = new HashSet<>();
            Set<Animal> deer = new HashSet<>();
            for (Animal animal : animals) {
                if (animal.kind() == Animal.Kind.TIGER && !cancelledAnimals.contains(animal))
                    tigers.add(animal);
                else if (animal.kind() == Animal.Kind.DEER && !cancelledAnimals.contains(animal))
                    deer.add(animal);
            }

            // if there is a fire, cancel the tigers
            Zone.Meadow fire = (Zone.Meadow) meadow.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE);
            if (fire != null) {
                board1 = board1.withMoreCancelledAnimals(tigers);
                cancelledAnimals.addAll(tigers);
            }

            // get the adjacent meadow and all the deer
            Zone.Meadow trap = (Zone.Meadow) meadow.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP);
            Area<Zone.Meadow> adjacent = new Area<>(Set.of(), List.of(), 0);
            if (trap != null)
                adjacent = board1.adjacentMeadow(board1.tileWithId(Zone.tileId(trap.id())).pos(), trap);
            Set<Animal> adjacentAnimal = Area.animals(adjacent, cancelledAnimals);

            // tell if a deer of the meadow is adjacent or not
            List<Animal> adjacentDeer = new ArrayList<>();
            for (Animal animal : adjacentAnimal) {
                if (animal.kind() == Animal.Kind.DEER)
                    adjacentDeer.add(animal);
            }
            List<Animal> nonAdjacentDeer = new ArrayList<>();
            for (Animal animal : animals) {
                if (animal.kind() == Animal.Kind.DEER && !adjacentAnimal.contains(animal))
                    nonAdjacentDeer.add(animal);
            }

            // cancel first the deer that aren't in the adjacent meadow
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
                    board1 = board1.withMoreCancelledAnimals(toCancel);
                } else {
                    board1 = board1.withMoreCancelledAnimals(deer);
                    board1 = board1.withMoreCancelledAnimals(tigers);
                }
            }

            // compute the points
            if (trap != null)
                messageBoard1 = messageBoard1.withScoredPitTrap(adjacent, board1.cancelledAnimals());
            messageBoard1 = messageBoard1.withScoredMeadow(meadow, board1.cancelledAnimals());
        }

        // hydrographic channels (Raft included)
        for (Area<Zone.Water> riverSystem : board1.riverSystemAreas()) {
            if (riverSystem.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null)
                messageBoard1 = messageBoard1.withScoredRaft(riverSystem);
            messageBoard1 = messageBoard1.withScoredRiverSystem(riverSystem);
        }

        // final message
        Map<PlayerColor, Integer> pts = messageBoard1.points();

        int maxPts = pts.values().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        Set<PlayerColor> winners = pts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxPts)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        messageBoard1 = messageBoard1.withWinners(winners, maxPts);

        return new GameState(newPlayers, newTileDecks, null, null, Action.END_GAME, messageBoard1);
    }

    private GameState withTurnFinishedIfOccupationImpossible() {

        //Pas de màj de players, puisque le même joueur qui a retiré le pion et amener à en posé un autre (ou aucun)
        //Pas de màj de tileDecks
        //Pas de màj de tileToPlace
        //Pas de màj de messageBoard

        //Le joueur ne peut passer à OCCUPY_TILE seulement s'il reste de la place sur la dernière tuile
        if (!lastTilePotentialOccupants().isEmpty()) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.OCCUPY_TILE, messageBoard);;
        }

        //Sinon son tour est terminé, car il ne peut pas placer de tuile


        return new GameState(shiftAndGetPlayerList(), tileDecks, tileToPlace, board, Action.PLACE_TILE,messageBoard);
    }

    /**
     * Gère toutes les transitions à partir de RETAKE_PAWN, en supprimant l'occupant donné, sauf s'il vaut null,
     * ce qui indique que le joueur ne désire pas reprendre de pion
     *
     * @param occupant l'occupant à supprimer du Plateau du jeu
     * @return le nouvel état du jeu, mis à jour.
     * @throws IllegalArgumentException si la prochaine action n'est pas RETAKE_PAWN,
     *                                  ou si l'occupant donné n'est ni null, ni un pion
     */
    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.RETAKE_PAWN &&
                (occupant == null || occupant.kind() == Occupant.Kind.PAWN));

        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.OCCUPY_TILE, messageBoard);
        }

        GameState updatedGameSate = new GameState(shiftAndGetPlayerList(), tileDecks, tileToPlace,
                board.withoutOccupant(occupant), Action.OCCUPY_TILE, messageBoard);

        return updatedGameSate.withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Gère toutes les transitions à partir de OCCUPY_TILE en ajoutant l'occupant donné à la dernière tuile posée,
     * sauf s'il vaut null, ce qui indique que le joueur ne désire pas placer d'occupant
     *
     * @param occupant l'occupant à rajouter à la dernière tuile posée
     * @return le nouvel état du jeu, mis à jour.
     * @throws IllegalArgumentException si la prochaine action n'est pas OCCUPY_TILE
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);

        // todo check if the last placer was the same player, in which case next action might be place tile, but it can also be end_game

        //Si le joueur ne souhaite pas placer d'occupant
        if (occupant == null) {
            return new GameState(players, tileDecks, tileToPlace, board, Action.PLACE_TILE, messageBoard);
        }

        List<PlayerColor> updatedPlayers = shiftAndGetPlayerList()
        //Pas de màj de tileDecks
        //Pas de màj de tileToPlace (traité dans withPlacedTile)
        Board updatedBoard = board.withOccupant(occupant);
        //Pas de màj de messageBoard

        return new GameState(updatedPlayers, tileDecks, tileToPlace, updatedBoard, Action.PLACE_TILE, messageBoard); //todo update all fields
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
