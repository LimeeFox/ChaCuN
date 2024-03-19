package ch.epfl.chacun;

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

    public PlacedTile tileAt(Pos pos) {
        if (pos.x() >= -12 && pos.x() <= 12
                && pos.y() >= -12 && pos.y() <= 12) {

            int index = pos.x() + pos.y() * 12;
        }
    }
}
