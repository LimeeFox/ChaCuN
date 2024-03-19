package ch.epfl.chacun;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import static ch.epfl.chacun.Zone.Forest;
import static ch.epfl.chacun.Zone.River;
import static ch.epfl.chacun.Zone.Meadow;
import static ch.epfl.chacun.Zone.Water;

/**
 * Le tableau de jeu
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */
public final class Board {

    private final PlacedTile[] placedTiles = new PlacedTile[625];
    //placedTileIndices may not have to be so big, considering there's only 96 total possible tiles
    private final int[] placedTileIndices = new int[625];
    private final ZonePartitions boardPartitions = new ZonePartitions(
            new ZonePartition<>(),
            new ZonePartition<>(),
            new ZonePartition<>(),
            new ZonePartition<>()
    );

    public static final int REACH = 12;
    public static final Board EMPTY = new Board(new PlacedTile[625], new int[625], ZonePartitions.EMPTY);

    private Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions boardPartitions) {
        this.placedTiles = placedTiles;
        this.placedTileIndices = placedTileIndices;
        this.boardPartitions = boardPartitions;
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
        return Collections.unmodifiableSet(); // ça permet de retourner le set inmodifiable sans faire de copies
    }

    public Set<Occupant> occupants() {

    }

    public Area<Zone.Forest> forestArea(Zone.Forest forest) {

    }

    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {

    }

    public Area<Zone.River> riverArea(Zone.River riverZone) {

    }

    public Area<Zone.Water> riverSystemArea(Zone.Water water) {

    }

    public Set<Area<Zone.Meadow>> meadowAreas() {

    }

    public Set<Area<Zone.Water>> riverSystemAreas() {

    }

    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {

    }

    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {

    }

    public Set<Pos> insertionPositions() {

    }

    public PlacedTile lastPlacedTile(){

    }

    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {

    }

    public Set<Area<Zone.River>> riversClosedByLastTile() {

    }

    public boolean canAddTile(PlacedTile tile) {

    }

    public boolean couldPlaceTile(Tile tile) {

    }

    public Board withNewTile(PlacedTile tile) {

    }

    public Board withOccupant(Occupant occupant) {

    }

    public Board withoutOccupant(Occupant occupant) {

    }

    public Board withoutGatherersOrFishersIn(Set<Area<Forest>> forests, Set<Area<River>> rivers) {

    }

    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        return new Board(placedTiles, placedTileIndices, boardPartitions);
    }

    @Override
    public Board equals() {

    }

    @Override
    public int hashCode() {

    }
}
