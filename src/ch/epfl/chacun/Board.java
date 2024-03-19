package ch.epfl.chacun;

import java.util.Arrays;
import java.util.Set;

/**
 * Le tableau de jeu
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */

public class Board {
    //Are they really all "final"?
    private final PlacedTile[] placedTiles = new PlacedTile[625];
    //placedTileIndices may not have to be so big, considering there's only 96 total possible tiles
    private final int[] placedTileIndices = new int[625];
    private final ZonePartitions boardPartitions = new ZonePartitions(new ZonePartition<Zone.Forest>(),
            new ZonePartition<Zone.Meadow>(),
            new ZonePartition<Zone.River>(),
            new ZonePartition<Zone.Water>());

    public static final int REACH = 12;
    public static final Board EMPTY = new Board(new PlacedTile[625], new int[625],
            ZonePartitions.EMPTY);

    private Board(PlacedTile[] placedTiles, int[] placedTileIndices, ZonePartitions boardPartitions) {
        this.placedTiles =  placedTiles;
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

    public Set<Animal> cancelledAnimals() {

    }
}
