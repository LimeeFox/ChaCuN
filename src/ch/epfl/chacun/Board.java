package ch.epfl.chacun;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Zone.Forest;
import static ch.epfl.chacun.Zone.River;
import static ch.epfl.chacun.Zone.Meadow;

/**
 * Le tableau de jeu
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */
public final class Board {
    private final PlacedTile[] placedTiles;
    private final int[] placedTileIndices;
    private final ZonePartitions boardPartitions;
    private final Set<Animal> cancelledAnimals;

    public static final int REACH = 12;
    public static final Board EMPTY = new Board(new PlacedTile[625], new int[0], ZonePartitions.EMPTY,
            new HashSet<>());

    private Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions boardPartitions,
                  Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.placedTileIndices = placedTileIndices;
        this.boardPartitions = boardPartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    /**
     * Tuile placée dans une position donnée
     *
     * @param pos
     *         position à laquelle la tuile est placée
     * @return la tuile placée à la position donnée, ou bien null si aucune tuile n'est placée à la position donnée ou
     * si la position donnée n'appartient pas au plateau
     */
    public PlacedTile tileAt(Pos pos) {
        if (pos.x() >= -12
                && pos.x() <= 12
                && pos.y() >= -12
                && pos.y() <= 12) {
            pos = pos.translated(REACH, REACH);
            int index = pos.x() + 25 * pos.y();
            return placedTiles[index];
        }
        return null;
    }

    /**
     * Tuile placée possédant une identité donnée
     *
     * @param tileId
     *         identité de la tuile recherchée
     * @return la tuile placée dont l'identité correspond avec celle passée en argument
     * @throws IllegalArgumentException
     *         si la tuile n'est pas présente sur le plateau, autrement dit, la tuile recherchée n'est pas encore placé
     *         ou l'identité donnée n'existe pas
     */
    public PlacedTile tileWithId(int tileId) {
        for (int index : placedTileIndices) {
            if (placedTiles[index] != null && placedTiles[index].id() == tileId) {
                return placedTiles[index];
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Obtenir l'ensemble des animaux annulés
     *
     * @return ensemble des animaux annulés
     */
    public Set<Animal> cancelledAnimals() {
        //permet de retourner l'ensemble immodifiable sans faire de copies
        return Collections.unmodifiableSet(cancelledAnimals);
    }

    /**
     * Occupants présents sur le tableau
     *
     * @return la totalité des occupants présents sur le tableau
     */
    public Set<Occupant> occupants() {
        return Arrays.stream(placedTileIndices)
                .mapToObj(index -> placedTiles[index].occupant())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Obtenir une aire avec la zone de forêt donnée
     *
     * @param forest
     *         la forêt dont on recherche l'aire
     * @return l'aire qui contient la zone de forêt donnée, si elle existe
     * @throws IllegalArgumentException
     *         si la zone en question n'appartient pas au plateau
     */
    public Area<Forest> forestArea(Forest forest) {
        return boardPartitions.forests().areaContaining(forest);
    }

    /**
     * Obtenir une aire avec la zone de pré donnée
     *
     * @param meadow
     *         le pré dont on recherche l'aire
     * @return l'aire qui contient la zone de pré donnée, si elle existe
     * @throws IllegalArgumentException
     *         si la zone en question n'appartient pas au plateau
     */
    public Area<Meadow> meadowArea(Meadow meadow) {
        return boardPartitions.meadows().areaContaining(meadow);
    }

    /**
     * Obtenir une aire avec la zone de rivière donnée
     *
     * @param riverZone
     *         la rivière dont on recherche l'aire
     * @return l'aire qui contient la zone de rivière donnée, si elle existe
     * @throws IllegalArgumentException
     *         si la zone en question n'appartient pas au plateau
     */
    public Area<River> riverArea(River riverZone) {
        return boardPartitions.rivers().areaContaining(riverZone);
    }

    /**
     * Obtenir une aire avec la zone d'eau donnée
     *
     * @param water
     *         la zone d'eau dont on recherche l'aire
     * @return l'aire qui contient la zone d'eau donnée, si elle existe
     * @throws IllegalArgumentException
     *         si la zone en question n'appartient pas au plateau
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return boardPartitions.riverSystems().areaContaining(water);
    }

    /**
     * Obtenir l'ensemble de toutes les aires du type pré
     *
     * @return l'ensemble de toutes les aires du type pré
     */
    public Set<Area<Meadow>> meadowAreas() {
        return boardPartitions.meadows().areas();
    }

    /**
     * Obtenir l'ensemble de toutes les aires du type hydrographique
     *
     * @return l'ensemble de toutes les aires du type hydrographique
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        return boardPartitions.riverSystems().areas();
    }

    /**
     * Obtenir le pré adjacent à la zone donnée, sous la forme d'une aire
     * qui ne contient que les zones de ce pré, mais tous les occupants du pré complet,
     * et qui, pour simplifier, ne possède aucune connexion ouverte
     *
     * @param pos
     *         position de la tuile centrale des prés adjacents
     * @param meadowZone
     *         zone centrale des prés adjacents
     * @return aire de prés adjacents à la zone de type pré donnée
     */
    public Area<Meadow> adjacentMeadow(Pos pos, Meadow meadowZone) {
        ZonePartition<Meadow> meadowZonePartition = boardPartitions.meadows();
        Area<Meadow> validMeadowArea = meadowZonePartition.areaContaining(meadowZone);

        Set<Meadow> allNeighbourMeadowZones = new HashSet<>();
        Set<Meadow> adjacentMeadowZones = new HashSet<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                PlacedTile tile = tileAt(pos.translated(x, y));

                if (tile != null) {
                    Set<Meadow> meadows = Objects.requireNonNull(tile.meadowZones());
                    allNeighbourMeadowZones.addAll(meadows);
                }
            }
        }

        allNeighbourMeadowZones.stream()
                .filter(zone -> validMeadowArea.zones().contains(zone))
                .forEach(adjacentMeadowZones::add);

        return new Area<>(adjacentMeadowZones, validMeadowArea.occupants(), 0);
    }

    /**
     * Nombre d'occupants d'une même sorte placés par un même joueur sur le plateau
     *
     * @param player
     *         joueur auquel appartiennent les occupants
     * @param occupantKind
     *         type d'occupant recherché
     * @return le nombre d'occupants sur le plateau étant d'une même sorte et appartenant à un même joueur
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        long occupants = Arrays.stream(placedTiles)
                .filter(tile -> tile != null
                        && tile.placer() == player
                        && tile.occupant() != null
                        && tile.occupant().kind() == occupantKind)
                .count();
        return (int) occupants;
    }

    /**
     * Positions d'insertion du plateau
     *
     * @return l'ensemble des positions du plateau sur lesquels les joueurs peuvent placer des tuiles
     */
    public Set<Pos> insertionPositions() {
        Set<Pos> validPositions = new HashSet<>();
        for (int index : placedTileIndices) {
            PlacedTile tile = placedTiles[index];

            Pos placedTilePos = tile.pos();

            Predicate<Pos> isValidPosition = pos -> {
                int x = pos.x();
                int y = pos.y();
                return x >= -12 && x <= 12 && y >= -12 && y <= 12 && tileAt(pos) == null;
            };

            Arrays.stream(Direction.values())
                    .map(placedTilePos::neighbor)
                    .filter(isValidPosition)
                    .forEach(validPositions::add);
        }

        return validPositions;
    }

    /**
     * La dernière tuile placée
     *
     * @return la dernière tuile placée sur le plateau, ou null si aucune tuile n'a encore été placée
     */
    public PlacedTile lastPlacedTile() {
        return (!this.equals(EMPTY)) ? placedTiles[placedTileIndices[placedTileIndices.length - 1]] : null;
    }

    /**
     * Aires de type forêt fermées par la dernière tuile placée
     *
     * @return l'ensemble des aires de type forêt fermées par la dernière tuile placée sur le plateau
     */
    public Set<Area<Forest>> forestsClosedByLastTile() {
        if (this.equals(EMPTY)) return null;

        PlacedTile lastPlacedTile = lastPlacedTile();
        if (lastPlacedTile == null) return null;

        Set<Forest> forestZones = new HashSet<>(lastPlacedTile.forestZones());
        return boardPartitions.forests().areas().stream()
                .filter(forestArea -> forestArea.zones().stream().anyMatch(forestZones::contains))
                .filter(Area::isClosed)
                .collect(Collectors.toSet());
    }

    /**
     * Aires de type rivière fermées par la dernière tuile placée
     *
     * @return l'ensemble des aires de type rivière fermées par la dernière tuile placée sur le plateau
     */
    public Set<Area<River>> riversClosedByLastTile() {
        if (this.equals(EMPTY)) return null;

        PlacedTile lastPlacedTile = lastPlacedTile();
        if (lastPlacedTile == null) return null;

        Set<River> riverZones = new HashSet<>(lastPlacedTile.riverZones());
        return boardPartitions.rivers().areas().stream()
                .filter(riverArea -> riverArea.zones().stream().anyMatch(riverZones::contains))
                .filter(Area::isClosed)
                .collect(Collectors.toSet());
    }

    /**
     * Indique si la tuile placée donnée pourrait être ajoutée au plateau
     *
     * @param tile
     *         tuile placée donnée
     * @return vrai si la position de la tuile placée donnée est une position d'insertion et que chaque bord de la
     * tuile qui un bord de tuile déjà posée est de la mêmê sorte que lui
     */
    public boolean canAddTile(PlacedTile tile) {

        Pos placedTilePos = tile.pos();
        Set<Pos> insertionPositions = insertionPositions();
        if (insertionPositions.contains(placedTilePos) && !this.equals(EMPTY)) {
            for (Direction direction : Direction.ALL) {
                PlacedTile neighbour = tileAt(placedTilePos.neighbor(direction));
                if (neighbour != null) {
                    return neighbour.side(direction.opposite()).isSameKindAs(tile.side(direction));
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si une tuile donnée peut être placée sur le plateau, éventuellement, après rotation
     *
     * @param tile
     *         tuile dont on cherche à vérifier la validité
     * @return vrai si la tuile donnée peut être placée sur le plateau, possiblement après rotation, et faux sinon
     */
    public boolean couldPlaceTile(Tile tile) {
        if (!this.equals(EMPTY)) {
            for (Pos position : insertionPositions()) {
                for (Rotation rotation : Rotation.ALL) {
                    if (canAddTile(new PlacedTile(tile, null, rotation, position, null))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Nouveau plateau contenant une nouvelle tuile placée donnée
     *
     * @param tile
     *         tuile placée dans le nouveau plateau
     * @return un nouveau plateau identique au récepteur, avec la tuile placée donnée
     * @throws IllegalArgumentException
     *         si le plateau n'est pas vide et la tuile donnée ne peut pas être ajoutée au plateau
     */
    public Board withNewTile(PlacedTile tile) {
        Preconditions.checkArgument(this.equals(EMPTY) || canAddTile(tile));

        int tileIndex = getIndexOfTile(tile);

        PlacedTile[] updatedPlacedTiles = placedTiles.clone();
        updatedPlacedTiles[tileIndex] = tile;

        int[] updatedPlacedTileIndices = Arrays.copyOf(placedTileIndices, placedTileIndices.length + 1);
        updatedPlacedTileIndices[updatedPlacedTileIndices.length - 1] = tileIndex;

        // Ajout de la tuile
        ZonePartitions.Builder boardPartitionsBuilder = new ZonePartitions.Builder(this.boardPartitions);
        boardPartitionsBuilder.addTile(tile.tile());
        for (Direction direction : Direction.ALL) {
            PlacedTile neighbourTile = tileAt(tile.pos().neighbor(direction));
            if (neighbourTile != null) {
                boardPartitionsBuilder.connectSides(tile.side(direction), neighbourTile.side(direction.opposite()));
            }
        }

        // Ajout d'un occupant initial si besoin
        if (tile.occupant() != null) {
            boardPartitionsBuilder.addInitialOccupant(tile.placer(), tile.occupant().kind(),
                    tile.zoneWithId(tile.occupant().zoneId()));
        }

        return new Board(updatedPlacedTiles, updatedPlacedTileIndices,
                boardPartitionsBuilder.build(), cancelledAnimals);
    }

    /**
     * Obtenir un plateau qui rajoute l'occupent à la zone qui correspond à son ID
     *
     * @param occupant
     *         occupent à rajouter
     * @return un plateau identique au récepteur, mais avec l'occupant donné en plus
     * @throws IllegalArgumentException
     *         si la tuile sur laquelle se trouverait l'occupant est déjà occupée
     */
    public Board withOccupant(Occupant occupant) {
        final int zoneId = occupant.zoneId();
        final int tileId = Zone.tileId(zoneId);

        PlacedTile tile = tileWithId(tileId);
        PlacedTile[] updatedPlacedTiles = placedTiles.clone();
        updatedPlacedTiles[getIndexOfTile(tile)] = tile.withOccupant(occupant);

        ZonePartitions.Builder updatedPartition = new ZonePartitions.Builder(boardPartitions);
        updatedPartition.addInitialOccupant(tile.placer(), occupant.kind(), tile.zoneWithId(zoneId));

        return new Board(updatedPlacedTiles, placedTileIndices.clone(), updatedPartition.build(), cancelledAnimals);
    }

    /**
     * Obtenir un plateau qui enlève l'occupent à la zone qui correspond à son ID
     *
     * @param occupant
     *         l'occupent à enlever
     * @return un plateau identique au récepteur, mais avec l'occupant donné en moins
     */
    public Board withoutOccupant(Occupant occupant) {
        final int id = occupant.zoneId();

        PlacedTile tile = tileWithId(id / 10);
        PlacedTile[] updatedPlacedTiles = placedTiles.clone();
        updatedPlacedTiles[getIndexOfTile(tile)] = tile.withNoOccupant();

        ZonePartitions.Builder updatedPartition = new ZonePartitions.Builder(boardPartitions);
        updatedPartition.removePawn(tile.placer(), tile.zoneWithId(id));

        return new Board(updatedPlacedTiles, placedTileIndices.clone(), updatedPartition.build(), cancelledAnimals);
    }

    /**
     * Obtenir un plateau identique au récépteur mais sans occupant dans les forêts et les rivières données
     *
     * @param forests
     *         l'ensemble d'aires forêts où on doit enlever les occupants
     * @param rivers
     *         l'ensemble d'aires rivières où on doit enlever les occupants
     * @return un plateau identique au récepteur mais sans aucun occupant dans les forêts et les rivières données
     */
    public Board withoutGatherersOrFishersIn(Set<Area<Forest>> forests, Set<Area<River>> rivers) {

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(boardPartitions);

        PlacedTile[] updatedPlacedTiles = placedTiles.clone();

        // Gérer les forêts
        forests.forEach(forestArea -> {
            zonePartitionsBuilder.clearGatherers(forestArea);
            forestArea.tileIds().forEach(id -> {
                PlacedTile updatedPlacedTile = tileWithId(id);
                Occupant occupant = updatedPlacedTile.occupant();
                if (occupant != null
                        && updatedPlacedTile.zoneWithId(occupant.zoneId()) instanceof Zone.Forest forestZone
                        && forestArea.zones().contains(forestZone)) {
                    updatedPlacedTiles[getIndexOfTile(updatedPlacedTile)] = updatedPlacedTile.withNoOccupant();
                }
            });
        });

        // Gérer les rivières
        rivers.forEach(riverArea -> {
            zonePartitionsBuilder.clearFishers(riverArea);
            riverArea.tileIds().forEach(id -> {
                PlacedTile updatedPlacedTile = tileWithId(id);
                Occupant occupant = updatedPlacedTile.occupant();
                if (occupant != null
                        && occupant.kind() == Occupant.Kind.PAWN
                        && updatedPlacedTile.zoneWithId(occupant.zoneId()) instanceof Zone.River riverZone
                        && riverArea.zones().contains(riverZone)) {
                    updatedPlacedTiles[getIndexOfTile(updatedPlacedTile)] = updatedPlacedTile.withNoOccupant();
                }
            });
        });

        return new Board(updatedPlacedTiles,
                placedTileIndices.clone(),
                zonePartitionsBuilder.build(),
                cancelledAnimals);
    }

    /**
     * Annulation de l'ensemble des animaux donnée
     *
     * @param newlyCancelledAnimals
     *          animaux à annuler
     * @return un plateau de jeu identique au récepteur, mais avec l'ensemble des animaux passé en argument ajoutés à
     *          l'ensemble des animaux annulés
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set<Animal> allAnimalsToCancel = new HashSet<>(cancelledAnimals);
        allAnimalsToCancel.addAll(newlyCancelledAnimals);

        return new Board(placedTiles, placedTileIndices, boardPartitions, allAnimalsToCancel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.equals(placedTiles, board.placedTiles)
                && Arrays.equals(placedTileIndices, board.placedTileIndices)
                && Objects.equals(boardPartitions, board.boardPartitions)
                && Objects.equals(cancelledAnimals, board.cancelledAnimals);
    }

    @Override
    public int hashCode() {
        final int firstDigit = Arrays.hashCode(placedTiles);
        final int secondDigit = Arrays.hashCode(placedTileIndices);
        return Objects.hash(firstDigit, secondDigit, boardPartitions, cancelledAnimals);
    }

    /**
     * Emplacement d'une tuile à une position donnée dans le tableau des tuiles du plateau
     *
     * @param tile
     *         tuile placée
     * @return l'indice de la tuile donnée dans placedTiles
     */
    private int getIndexOfTile(PlacedTile tile) {
        final Pos tilePos = tile.pos().translated(REACH, REACH);

        return tilePos.x() + 25 * tilePos.y();
    }
}
