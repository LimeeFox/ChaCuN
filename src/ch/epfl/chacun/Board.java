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
    public static final Board EMPTY = new Board(new PlacedTile[625], new int[96], ZonePartitions.EMPTY, new HashSet<>());

    public Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions boardPartitions,
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
     *          position à laquelle la tuile est placée
     * @return la tuile placée à la position donnée, ou bien null si aucune tuile n'est placée à la position donnée ou
     *          si la position donnée n'appartient pas au plateau
     */
    public PlacedTile tileAt(Pos pos) {
        if (pos.x() >= -12 && pos.x() <= 12
                && pos.y() >= -12 && pos.y() <= 12) {
            int index = pos.x() + 12 + (pos.y() + 12) * 12;
            return placedTiles[index];
        }
        return null;
    }

    /**
     * Tuile placée possédant une identité donnée
     *
     * @param tileId
     *          identité de la tuile recherchée
     * @return la tuile placée dont l'identité correspond avec celle passée en argument
     *
     * @throws IllegalArgumentException
     *          si la tuile n'est pas présente sur le plateau, autrement dit, la tuile recherchée n'est pas encore placé
     *          ou l'identité donnée n'existe pas
     */
    public PlacedTile tileWithId(int tileId) {
        for (int index : placedTileIndices) {
            if (placedTiles[index].id() == tileId) {
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
        return Collections.unmodifiableSet(cancelledAnimals); //permet de retourner le set immodifiable sans faire de copies
    }

    /**
     * Occupants présents sur le tableau
     *
     * @return boardOccupants
     *          la totalité des occupants présents sur le tableau
     */
    public Set<Occupant> occupants() {
        Set<Occupant> boardOccupants = new HashSet<>();
        for(int index : placedTileIndices) {
            boardOccupants.add(placedTiles[index].occupant());
        }
        return boardOccupants;
    }

    /**
     * Obtenir une aire avec la zone de forêt donnée
     *
     * @param forest
     * @return l'aire avec la zone de forêt donnée, si elle existe
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        return boardPartitions.forests().areaContaining(forest);
    }

    /**
     * Obtenir une aire avec la zone de pré donnée
     *
     * @param meadow
     * @return l'aire avec la zone de pré donnée, si elle existe
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return boardPartitions.meadows().areaContaining(meadow);
    }

    /**
     * Obtenir une aire avec la zone de rivière donnée
     *
     * @param riverZone
     * @return l'aire avec la zone de rivière donnée, si elle existe
     */
    public Area<Zone.River> riverArea(Zone.River riverZone) {
        return boardPartitions.rivers().areaContaining(riverZone);
    }

    /**
     * Obtenir une aire avec la zone d'eau donnée
     *
     * @param water
     * @return l'aire avec la zone d'eau donnée, si elle existe
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return boardPartitions.riverSystems().areaContaining(water);
    }

    /**
     * Obtenir l'ensemble de toutes les aires du type pré
     *
     * @return l'ensemble de toutes les aires du type pré
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
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
     * @param meadowZone
     * @return aire de pré
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        //@todo
    }

    /**
     *
     * @param player
     * @param occupantKind
     * @return
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        /*
        final int[] playerCount = new int[1];
        List<PlacedTile> tiles = Arrays.stream(placedTiles).toList();

        Predicate<Area<Meadow>> meadowAreaFilter = meadowArea -> meadowArea.occupants() != null;
        Predicate<PlayerColor> playerFilter = playerColor -> !(playerColor == player);

        //Predicate<Meadow> meadowZoneFilter = meadowZone -> meadowZone.

        boardPartitions.meadows().areas().stream().filter(meadowAreaFilter).forEach(area -> {
            area.occupants().stream().filter(playerFilter).forEach(playerColor -> {
                playerCount[0]++;
            });
        });

        Predicate<Area<Forest>> forestAreaFilter = forestArea -> forestArea.occupants() != null;

        boardPartitions.forests().areas().stream().filter(forestAreaFilter).forEach(area -> {
            area.occupants().stream().filter(playerFilter).forEach(playerColor -> {
                playerCount[0]++;
            });
        });

        boardPartitions.

        return playerCount[0];

         */

        long occupants = Arrays.stream(placedTileIndices)
                .mapToObj(index -> placedTiles[index])
                .filter(tile -> tile.placer() == player && tile.occupant().kind() == occupantKind)
                .count();
        return (int) occupants;
    }

    /**
     * Positions d'insertion du plateau
     *
     * @return l'ensemble des positions du plateau sur lesquels les joueurs peuvent placer des tuiles
     */
    //@todo review this goofy ahh code
    public Set<Pos> insertionPositions() {
        Set<Pos> positions = new HashSet<>();
        for (int index : placedTileIndices) {
            Pos placedTilePos = new Pos(index % 25, index / 25);

            Predicate<Pos> isValidPosition = pos -> {
                int x = pos.x();
                int y = pos.y();
                return x >= -12 && x <= 12 && y >= -12 && y <= 12;
            };

            Arrays.stream(Direction.values())
                    .map(placedTilePos::neighbor)
                    .filter(isValidPosition)
                    .forEach(positions::add);
        }
        return positions;
    }

    /**
     * La dernière tuile placée
     *
     * @return la dernière tuile placée sur le plateau, ou null si aucune tuile n'a encore été placée
     */
    public PlacedTile lastPlacedTile(){
        if (!this.equals(EMPTY)) {
            return placedTiles[placedTileIndices[placedTileIndices.length - 1]];
        }
        return null;
    }

    /**
     * Aires de type forêt fermées par la dernière tuile placée
     *
     * @return l'ensemble des aires de type forêt fermées par la dernière tuile placée sur le plateau
     */
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        if (!this.equals(EMPTY)) {
            PlacedTile lastPlacedTile = lastPlacedTile();
            if (lastPlacedTile != null) {
                Set<Forest> forestZones = new HashSet<>(lastPlacedTile.forestZones());

                return boardPartitions.forests().areas().stream()
                         .filter(forestArea -> forestArea.zones().containsAll(forestZones))
                         .filter(Area::isClosed)
                         .collect(Collectors.toSet());
            }
        }
        return null;
    }

    /**
     * Aires de type rivière fermées par la dernière tuile placée
     *
     * @return l'ensemble des aires de type rivière fermées par la dernière tuile placée sur le plateau
     */
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        if (!this.equals(EMPTY)) {
            PlacedTile lasPlacedTile = lastPlacedTile();
            if(lasPlacedTile != null) {
                Set<Zone.River> riverZones = new HashSet<>(lasPlacedTile.riverZones());

                return boardPartitions.rivers().areas().stream()
                        .filter(riverArea -> riverArea.zones().containsAll(riverZones))
                        .filter(Area::isClosed)
                        .collect(Collectors.toSet());
            }
        }
        return null;
    }

    /**
     * Indique si la tuile placée donnée pourrait être ajoutée au plateau
     *
     * @param tile
     *          tuile placée donnée
     * @return vrai si la position de la tuile placée donnée est une position d'insertion et que chaque bord de la
     *          tuile qui un bord de tuile déjà posée est de la mêmê sorte que lui
     */
    public boolean canAddTile(PlacedTile tile) {
        Pos placedTilePos = tile.pos();
        if (insertionPositions().contains(tile.pos())) {
            for (Direction direction : Direction.values()) {
                if (tileAt(placedTilePos.neighbor(direction)) != null) {
                    if (!Objects.requireNonNull(tileAt(placedTilePos.neighbor(direction))).side(direction.opposite())
                            .isSameKindAs(tile.side(direction))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean couldPlaceTile(Tile tile) {

    }

    public Board withNewTile(PlacedTile tile) {

    }

    /**
     *
     *
     * @param occupant
     * @return
     */
    public Board withOccupant(Occupant occupant) {
        final int id = occupant.zoneId();

        PlacedTile tile = tileWithId(id / 10);
        PlacedTile[] updatedPlacedTiles = placedTiles.clone();
        updatedPlacedTiles[getIndexOfTile(tile)] = tile;

        ZonePartitions.Builder updatedPartition = new ZonePartitions.Builder(boardPartitions);
        updatedPartition.addInitialOccupant(tile.placer(), occupant.kind(), tile.zoneWithId(id));

        return new Board(updatedPlacedTiles, placedTileIndices, updatedPartition.build(), cancelledAnimals);
    }

    public Board withoutOccupant(Occupant occupant) {

    }

    public Board withoutGatherersOrFishersIn(Set<Area<Forest>> forests, Set<Area<River>> rivers) {

    }

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
        return Arrays.equals(placedTiles, board.placedTiles) && Arrays.equals(placedTileIndices, board.placedTileIndices) && Objects.equals(boardPartitions, board.boardPartitions);
    }

    @Override
    public int hashCode() {
        final int firstDigit = Arrays.hashCode(placedTiles);
        final int secondDigit = Arrays.hashCode(placedTileIndices);
        return Objects.hash(firstDigit, secondDigit);
    }

    private int getIndexOfTile(PlacedTile tile) {
        final Pos tilePos = tile.pos().translated(REACH, REACH);

        return tilePos.x() + 25 * tilePos.y();
    }
}
