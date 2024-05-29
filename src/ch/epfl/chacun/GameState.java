package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe qui définit les différents états de jeu de la partie en cours
 *
 * @param players
 *         la liste de tous les joueurs de la partie, dans l'ordre dans lequel ils doivent jouer
 *         donc avec le joueur courant en tête de liste
 * @param tileDecks
 *         les trois tas des tuiles restantes
 * @param tileToPlace
 *         l'éventuelle tuile à placer, qui a été prise du sommet du tas des tuiles normales
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

    /**
     * Constructeur de GameState qui garantie son immuabilité et certaines préconditions de la partie
     *
     * @param players
     *         la liste de tous les joueurs de la partie, dans l'ordre dans lequel ils doivent jouer
     *         donc avec le joueur courant en tête de liste
     * @param tileDecks
     *         les trois tas des tuiles restantes
     * @param tileToPlace
     *         l'éventuelle tuile à placer, qui a été prise du sommet du tas des tuiles normales
     *         ou du tas des tuiles menhir, et qui peut être null si aucune tuile n'est à placer actuellement
     * @param board
     *         le plateau de jeu
     * @param nextAction
     *         la prochaine action à effectuer
     * @param messageBoard
     *         le tableau d'affichage contenant les messages générés jusqu'à présent dans la partie
     * @throws IllegalArgumentException
     *         si la liste des joueurs de la partie est inférieur à 2
     *         si la tuile à placer est null alors que l'action à effectuer est de placer une tuile
     *         si la tuile à placer n'est pas null tandis que l'action à effectuer n'est pas de placer une tuile
     * @throws NullPointerException
     *         si les tas de tuiles (tileDecks) sont définies comme étant null
     *         si le plateau de jeu (board) est null
     *         si la prochaine action à effectuer (nextAction) est null
     *         si le tableau de message de la partie (messageBoard) est null
     */
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
     * Initialisation d'une partie avec un état initial, avant le placement de la tuile de départ
     *
     * @param players
     *         la liste de joueurs qui vont jouer dans la partie, dans l'ordre
     * @param tileDecks
     *         les piles de tuiles qui seront potentiellement placées
     * @param textMaker
     *         l'outil qui servira à afficher les messages sur le tableau de messages
     * @return l'état de jeu initial pour les joueurs, tas et « créateur de texte » donnés,
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
     * @return le joueur courant, ou null s'il n'y en a pas, c.-à-d., si la prochaine action est START_GAME ou END_GAME
     */
    public PlayerColor currentPlayer() {
        return (nextAction != Action.START_GAME && nextAction != Action.END_GAME) ? players.getFirst() : null;
    }

    /**
     * Compter le nombre d'occupants d'un @kind spécifié appartenant à un @player qui ne sont pas placés sur le plateau,
     * soit les occupants qui lui restent à placer
     *
     * @param player
     *         joueur dont on veut compter le nombre d'occupants libres
     * @param kind
     *         le type des occupants libres qu'on veut compter
     * @return le nombre d'occupants libres, c.-à-d., qui ne sont pas actuellement placés
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
     *         si le plateau est vide, autrement dit, si aucune tuile n'a été posée
     */
    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(!board.equals(Board.EMPTY));
        PlacedTile tile = board.lastPlacedTile();
        Preconditions.checkArgument(tile != null);

        // On crée un ensemble des occupants potentiels
        Set<Occupant> potentialOccupants = new HashSet<>(tile.potentialOccupants());

        // On enlève les occupants potentiels qui ne se sont pas conforme aux conditions
        potentialOccupants.removeIf(occupant -> {
            boolean hasFreeOccupants = freeOccupantsCount(currentPlayer(), occupant.kind()) <= 0;
            if (hasFreeOccupants) {
                return true; // Aucun occupant ne peut être placé si le joueur en possède aucun
            }

            // On vérifie l'occupation de châque zone
            Zone zone = tile.zoneWithId(occupant.zoneId());
            switch (zone) {
                case Zone.Forest forest:
                    return board.forestArea(forest).isOccupied();
                case Zone.Meadow meadow:
                    return board.meadowArea(meadow).isOccupied();
                case Zone.Lake lake:
                    return board.riverSystemArea(lake).isOccupied();
                case Zone.River river:
                    if (occupant.kind() == Occupant.Kind.PAWN) {
                        return board.riverArea(river).isOccupied();
                    } else {
                        return board.riverSystemArea(river).isOccupied();
                    }
                default:
                    return false;
            }
        });

        return potentialOccupants;
    }

    /**
     * Gère la transition de START_GAME à PLACE_TILE en plaçant la tuile de départ au centre du plateau
     * et en tirant la première tuile du tas des tuiles normales, qui devient la tuile à jouer
     *
     * @return le nouvel état du jeu, mis à jour, avec comme action de placer une tuile pour le joueur qui commence la
     * partie
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
     * Gère toutes les transitions à partir de PLACE_TILE en ajoutant la tuile donnée au plateau, attribuant les
     * éventuels points obtenus suite à la pose de la pirogue ou de la fosse à pieux avant de vérifier la fin du tour du
     * joueur courant
     *
     * @param tile
     *         la tuile à placer sur le plateau
     * @return le nouvel état du jeu, mis à jour, passé dans une méthode pour gérer la fin du tour du joueur courant
     * @throws IllegalArgumentException
     *         si la prochaine action n'est pas PLACE_TILE, ou si la tuile passée est déjà occupée
     */
    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tile.occupant() == null);

        /*
        Màj par défaut des paramètres de GameState
        Pas de màj de players (par défaut, on considère que le joueur ayant posé la tuile n'a pas encore terminé son
        tour, la fin du tour étant géré dans un withTurnFinished)
        */
        List<PlayerColor> updatedPlayerList = players;
        final TileDecks updatedTileDecks = tileDecks;
        Board updatedBoard = board.withNewTile(tile);
        Action updatedNextAction = Action.OCCUPY_TILE;
        // La màj de messageBoard dépend des points obtenus et des pouvoirs utilisés
        MessageBoard updatedMessageBoard = messageBoard;

        PlayerColor scorer = currentPlayer();

        final int placedPawns = updatedBoard.occupantCount(currentPlayer(), Occupant.Kind.PAWN);

        // Traitement des pouvoirs spéciaux qui ont un effet immédiat après la pose de la tuile
        Zone specialPowerZone = tile.specialPowerZone();
        if (specialPowerZone != null) {
            List<Zone.SpecialPower> immediateEffectPowers = List.of(Zone.SpecialPower.SHAMAN, Zone.SpecialPower.LOGBOAT,
                    Zone.SpecialPower.HUNTING_TRAP);
            Zone.SpecialPower tilePower = specialPowerZone.specialPower();
            if (immediateEffectPowers.contains(tilePower)) {
                switch (tilePower) {
                    case SHAMAN -> {
                        if (placedPawns > 0) {
                            return new GameState(updatedPlayerList, updatedTileDecks, null, updatedBoard,
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
                        Set<Animal> animals = adjacentMeadow.zones().stream()
                                .flatMap(meadow -> meadow.animals().stream())
                                .collect(Collectors.toSet());

                        Map<Animal.Kind, Long> animalCount = animals.stream()
                                .collect(Collectors.groupingBy(Animal::kind, Collectors.counting()));

                        // Filtrer les animaux
                        Set<Animal> cancelledAnimals = new HashSet<>();
                        long tigerCount = animalCount.getOrDefault(Animal.Kind.TIGER, 0L);
                        long deerCount = animalCount.getOrDefault(Animal.Kind.DEER, 0L);
                        // Compter le nombre de cerfs à annuler en fonction du nombre de tigres (qui les mangent)
                        int deerToCancel = (int) Math.min(deerCount, tigerCount);
                        // Ajout dès cerfs annulés aux animaux annulés
                        animals.stream()
                                .filter(animal -> animal.kind() == Animal.Kind.DEER)
                                .limit(deerToCancel)
                                .forEach(cancelledAnimals::add);

                        updatedMessageBoard = updatedMessageBoard.withScoredHuntingTrap(scorer, adjacentMeadow,
                                cancelledAnimals);
                        // Annule tous les animaux du plateau de jeu, y compris les cerfs "mangés"
                        cancelledAnimals.addAll(animals);
                        updatedBoard = updatedBoard.withMoreCancelledAnimals(cancelledAnimals);
                    }
                }
            }
        }
        return new GameState(updatedPlayerList, updatedTileDecks, null, updatedBoard, updatedNextAction,
                updatedMessageBoard).withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Methode d'aide qui permet de gérer la fin d'un tour et détermine la potentielle prochaine action du joueur
     * courant, si son tour n'est pas encore terminé, ou termine le tour du joueur courant et permet au prochain joueur
     * de continuer la partie
     *
     * @return l'état de jeu pour que le joueur courant puisse placer sa prochaine tuile, s'il a fermé une aire forêt
     * contenant un menhir avec une tuile normal,
     * ou l'état de jeu pour que le prochain joueur puisse jouer (placer sa tuile)
     * ou l'état du jeu qui correspond à la fin de la partie
     */
    private GameState withTurnFinished() {
        Preconditions.checkArgument(!board().equals(Board.EMPTY));

        List<PlayerColor> updatedPlayers = players;
        TileDecks updatedTileDecks = tileDecks;
        Tile updatedTileToPlace = tileToPlace;
        Board updatedBoard = board;
        MessageBoard updatedMessageBoard = messageBoard;

        /*
        Par défaut, on considère que le joueur courant ne rejouera pas, donc qu'il faudra piocher dans le tas de tuiles
        normal
        */
        Tile.Kind tileKind = Tile.Kind.NORMAL;

        PlacedTile lastPlacedTile = board.lastPlacedTile();

        /*
        Par défaut, on considère que le joueur courant ne rejouera pas, donc qu'il n'obtiendra pas de tuile menhir
        */
        boolean playerGetsMenhir = false;

        // Gérer les aires rivières fermées lors de ce tour
        Set<Area<Zone.River>> lastClosedRivers = board.riversClosedByLastTile();
        if (lastClosedRivers != null) {
            for (Area<Zone.River> closedRiver : Objects.requireNonNull(lastClosedRivers)) {
                //Màj du tableau de messages pour les points marqués par la fermeture des rivières
                updatedMessageBoard = updatedMessageBoard.withScoredRiver(closedRiver);
            }
        } else {
            lastClosedRivers = Set.of();
        }

        // Gérer les aires forêt fermées lors de ce tour
        Set<Area<Zone.Forest>> lastClosedForests = board.forestsClosedByLastTile();
        if (lastClosedForests != null) {
            for (Area<Zone.Forest> closedForest : Objects.requireNonNull(lastClosedForests)) {
                //Màj du tableau de messages pour les points marqués par la fermeture des forêts
                updatedMessageBoard = updatedMessageBoard.withScoredForest(closedForest);

                /*
                Le joueur courant aura la possibilité de rejouer si :
                    l'aire forêt fermée par le joueur courant contient un menhir
                    la dernière tuile placée (par le joueur courant) n'est pas de type menhir (ce qui assure qu'il ne
                    rejoue pas plus d'une fois)
                On vérifie aussi que la dernière tuile placée n'est pas null, pour éviter des erreurs, et qu'on n'a pas
                déjà confirmé que le joueur puisse rejouer (par soucis d'optimisation)
                */
                if (lastPlacedTile != null
                        && Area.hasMenhir(closedForest)
                        && !lastPlacedTile.tile().kind().equals(Tile.Kind.MENHIR)
                        && !playerGetsMenhir
                        && !tileDecks.menhirTiles().isEmpty()) {

                    playerGetsMenhir = true;
                    //Màj du tableau de message pour indiquer que le joueur courant puisse piocher une tuile menhir
                    updatedMessageBoard = updatedMessageBoard.withClosedForestWithMenhir(currentPlayer(), closedForest);
                }
            }
        } else {
            lastClosedForests = Set.of();
        }

        // Les cueilleurs et pêcheurs des aires fermées sont retirés du plateau
        updatedBoard = updatedBoard.withoutGatherersOrFishersIn(lastClosedForests, lastClosedRivers);

        // Si les conditions précédentes sont confirmés, on accorde au joueur courant la pioche d'une tuile menhir
        if (playerGetsMenhir) {
            tileKind = Tile.Kind.MENHIR;
        } else {
            // Sinon le joueur termine son tour et on passe au prochain
            updatedPlayers = shiftAndGetPlayerList();
        }

        /*
        S'il n'y a plus de tuiles normales à poser et que le joueur courant n'a pas la chance de rejouer, alors c'est
        la fin du jeu
        */
        if (updatedTileDecks.normalTiles().isEmpty() && !playerGetsMenhir) {
            return new GameState(updatedPlayers, updatedTileDecks, updatedTileToPlace, updatedBoard, Action.END_GAME,
                    updatedMessageBoard).withFinalPointsCounted();
        }

        // On retire les tuiles du tas donné (dépendant de playerGetMenhir) qui ne peuvent être placées sur le plateau
        updatedTileDecks = updatedTileDecks.withTopTileDrawnUntil(tileKind, board::couldPlaceTile);
        updatedTileToPlace = updatedTileDecks.topTile(tileKind);
        updatedTileDecks = updatedTileDecks.withTopTileDrawn(tileKind);

        return new GameState(updatedPlayers, updatedTileDecks, updatedTileToPlace, updatedBoard, Action.PLACE_TILE,
                updatedMessageBoard);
    }

    /**
     * Méthode d'aide qui gére la fin de la partie, avec le comptage des points pour la fin de partie
     *
     * @return un nouvel état de jeu indiquant la fin de la partie
     */
    private GameState withFinalPointsCounted() {
        Preconditions.checkArgument(nextAction.equals(Action.END_GAME));

        // Paramètres potentiellement modifiés par rapport à ceux de l'instance
        Board updatedBoard = board;
        MessageBoard updatedMessageBoard = messageBoard;

        // Gestion des animaux annulés
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

            // Si la zone possède le pouvoir spécial du feu, alors les tigres dans l'aire doivent être annulés
            Zone.Meadow wildFireZone = (Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE);
            if (wildFireZone != null) {
                Set<Animal> cancelledTigers = animals.stream()
                        .filter(animal -> animal.kind() == Animal.Kind.TIGER && !cancelledAnimals.contains(animal))
                        .collect(Collectors.toSet());
                cancelledAnimals.addAll(cancelledTigers);
                tigers.removeAll(cancelledTigers);
            }

            Zone.Meadow pitTrapZone = (Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP);

            // Les tigres mangent les cerfs dans le même pré qu'eux,
            // en priorité hors de l'aire adjacente de la grande fosse à pieux
            if (!tigers.isEmpty()) {
                int tigerCount = tigers.size();
                int deerCount = deer.size();

                int deerToCancel = deerCount;

                if (pitTrapZone != null) {
                    Area<Zone.Meadow> pitTrapArea = updatedBoard
                            .adjacentMeadow(updatedBoard.tileWithId(pitTrapZone.tileId()).pos(), pitTrapZone);
                    Set<Animal> adjacentDeer = pitTrapArea.zones().stream()
                            .flatMap(zone -> zone.animals().stream()
                                    .filter(animal -> animal.kind() == Animal.Kind.DEER))
                            .collect(Collectors.toSet());

                    deerToCancel = Math.min(tigerCount,deerCount - adjacentDeer.size());

                    Set<Animal> cancelledDeer = animals.stream()
                            .filter(animal -> animal.kind() == Animal.Kind.DEER && !adjacentDeer.contains(animal))
                            .limit(deerToCancel).collect(Collectors.toSet());
                    cancelledAnimals.addAll(cancelledDeer);

                    tigerCount -= deerToCancel;
                    deerToCancel = adjacentDeer.size();

                    updatedMessageBoard = updatedMessageBoard.withScoredPitTrap(pitTrapArea,
                            cancelledAnimals);
                }
                deerToCancel = Math.min(tigerCount, deerToCancel);

                animals.stream()
                        .filter(animal -> animal.kind() == Animal.Kind.DEER)
                        .limit(deerToCancel)
                        .forEach(cancelledAnimals::add);
            }

            // Màj du plateau de jeu pour l'obtention de points
            updatedMessageBoard = updatedMessageBoard.withScoredMeadow(meadowArea, cancelledAnimals);
            updatedBoard = updatedBoard.withMoreCancelledAnimals(cancelledAnimals);
        }

        // Points pour les systèmes hydrographiques
        for (Area<Zone.Water> riverSystem : updatedBoard.riverSystemAreas()) {
            if (riverSystem.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null)
                updatedMessageBoard = updatedMessageBoard.withScoredRaft(riverSystem);
            updatedMessageBoard = updatedMessageBoard.withScoredRiverSystem(riverSystem);
        }

        // Màj du tableau de message avec le message final de la partie
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

        return new GameState(players, tileDecks, null, updatedBoard, Action.END_GAME, updatedMessageBoard);
    }

    /**
     * Méthode d'aide qui permet de gérer la possible fin de tour du joueur courant si la dernière tuile posée, par ce
     * dernier, ne peut être occupée
     *
     * @return l'état du jeu pour que le joueur courant puisse placer un occupant (si cela lui est possible),
     * ou l'état de jeu correspondant à la possible fin de partie (withTurnFinished)
     */
    private GameState withTurnFinishedIfOccupationImpossible() {
        // Le joueur ne peut passer à OCCUPY_TILE seulement s'il reste de la place sur la dernière tuile
        if (!lastTilePotentialOccupants().isEmpty()) {
            return this;
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
     * @return updatedGameState.withTurnFinishedIfOccupationImpossible()
     * avec l'occupant donné retiré du plateau de jeu, si celui-ci n'est pas null
     * @throws IllegalArgumentException
     *         si la prochaine action n'est pas RETAKE_PAWN,
     *         si l'occupant donné n'est ni null, ni un pion
     */
    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.RETAKE_PAWN
                && (occupant == null || occupant.kind() == Occupant.Kind.PAWN));

        Board updatedBoard = board;

        // Si l'occupant à retirer n'est pas null, on le retire du plateau
        if (occupant != null) {
            updatedBoard = updatedBoard.withoutOccupant(occupant);
        }

        // Màj de l'état du jeu, avec le nouveau plateau de jeu qui tien compte des possibles modifications
        GameState updatedGameState = new GameState(players, tileDecks, tileToPlace,
                updatedBoard, Action.OCCUPY_TILE, messageBoard);

        return updatedGameState.withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Gère toutes les transitions à partir de OCCUPY_TILE en ajoutant l'occupant donné à la dernière tuile posée,
     * sauf s'il vaut null, ce qui indique que le joueur ne désire pas placer d'occupant
     *
     * @param occupant
     *         l'occupant à rajouter à la dernière tuile posée
     * @return updatedGameState.withTurnFinished()
     * un nouvel état de jeu avec l'occupant donné ajouté s'il n'est pas null
     * @throws IllegalArgumentException
     *         si la prochaine action n'est pas OCCUPY_TILE
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);

        Board updatedBoard = board;

        // Si le joueur souhaite placer un occupant, on l'ajoute au plateau de jeu
        if (occupant != null) {
            updatedBoard = board.withOccupant(occupant);
        }

        // Màj de l'état du jeu avec une possible modification du plateau de jeu
        GameState updatedGameState = new GameState(players, tileDecks, tileToPlace, updatedBoard, nextAction,
                messageBoard);

        return updatedGameState.withTurnFinished();
    }

    /**
     * Décaler tous les joueurs d'un cran, pour que chacun puisse jouer à son tour, en sachant que
     * celui qui est en tête de la liste est celui qui joue actuellement
     *
     * @return newList
     * la liste de tous les joueurs de la partie, dans l'ordre dans lequel ils doivent jouer
     * donc avec le joueur courant en tête de liste
     */
    private List<PlayerColor> shiftAndGetPlayerList() {

        // Liste des joueurs dans le même ordre que l'instance
        List<PlayerColor> playerList = new ArrayList<>(players);

        // On retire le premier joueur de la liste, pour le mettre à la fin de la nouvelle liste
        PlayerColor lastPlayer = playerList.getFirst();
        playerList.removeFirst();
        List<PlayerColor> newList = new ArrayList<>(playerList);
        newList.add(lastPlayer);

        return newList;
    }
}