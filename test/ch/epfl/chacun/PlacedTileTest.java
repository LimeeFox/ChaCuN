<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.*;
class PlacedTileTest {
  
=======
package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlacedTileTest {

    @Test
    void id() {
        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(2, List.of(), null)),
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.id(), 56);
    }

    @Test
    void kind() {
        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(2, List.of(), null)),
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.kind(), Tile.Kind.NORMAL);
    }

    @Test
    void side() {
        TileSide meadowZone = new TileSide.Meadow(new Zone.Meadow(2, List.of(), null));
        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                meadowZone,
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.side(Direction.S), meadowZone);
    }

    @Test
    void zoneWithId() {
        Zone.Meadow meadowZone = new Zone.Meadow(2, List.of(), null);
        TileSide meadowSide = new TileSide.Meadow(meadowZone);
        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                meadowSide,
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.zoneWithId(2), meadowZone);
    }

    @Test
    void specialPowerZone() {
        Zone.Meadow meadowZone = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        TileSide meadowSide = new TileSide.Meadow(meadowZone);
        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                meadowSide,
                new TileSide.Meadow(new Zone.Meadow(2, List.of(), null)),
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.specialPowerZone(), meadowZone);
    }

    @Test
    void forestZones() {
        Zone.Forest forest1 =  new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR);
        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(forest1),
                new TileSide.Forest(forest2),
                new TileSide.Meadow(new Zone.Meadow(2, List.of(), null)),
                new TileSide.Forest(forest3));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.forestZones(), Set.of(forest1, forest2, forest3));
    }

    @Test
    void meadowZones() {
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, List.of(), null);
        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(meadowZone1),
                new TileSide.Meadow(meadowZone2),
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.meadowZones(), Set.of(meadowZone1, meadowZone2));
    }

    @Test
    void riverZones() {
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, List.of(), null);
        Zone.Meadow meadowZone3 = new Zone.Meadow(4, List.of(), null);

        Zone.River riverZone1 = new Zone.River(0, 2, null);
        Zone.River riverZone2 = new Zone.River(3, 1, null);

        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadowZone1),
                new TileSide.River(meadowZone1, riverZone1, meadowZone2),
                new TileSide.River(meadowZone2, riverZone2, meadowZone3),
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.riverZones(), Set.of(riverZone1, riverZone2));
    }

    @Test
    void potentialOccupants() {
        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(2, List.of(), null)),
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.kind(), Tile.Kind.NORMAL);
    }

    /*
    @Test
    void withOccupant() {

    }

    @Test
    void withNoOccupant() {
    }

    @Test
    void idOfZoneOccupiedBy() {
    }

    @Test
    void tile() {
    }

    @Test
    void placer() {
    }

    @Test
    void rotation() {
    }

    @Test
    void pos() {
    }

    @Test
    void occupant() {
    }

     */
>>>>>>> 8836fdc (working on placedTileTest)
}