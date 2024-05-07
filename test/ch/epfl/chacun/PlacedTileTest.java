package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
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
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.LEFT, Pos.ORIGIN);

        assertEquals(placedTile.side(Direction.E), meadowZone);
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
        Zone.River riverZone2 = new Zone.River(3, 1, new Zone.Lake(8, 0, null));

        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadowZone1),
                new TileSide.River(meadowZone1, riverZone1, meadowZone2),
                new TileSide.River(meadowZone2, riverZone2, meadowZone3),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        assertEquals(placedTile.riverZones(), Set.of(riverZone1, riverZone2));
    }

    @Test
    void potentialOccupants() {
        // Random test
        Tile tile1 = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(2, List.of(), null)),
                new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        Set<Occupant> actual1 = new HashSet<>();
        actual1.add(new Occupant(Occupant.Kind.PAWN, 0));
        actual1.add(new Occupant(Occupant.Kind.PAWN, 1));
        actual1.add(new Occupant(Occupant.Kind.PAWN, 2));
        actual1.add(new Occupant(Occupant.Kind.PAWN, 3));

        assertEquals(placedTile1.potentialOccupants(), actual1);

        // River test
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, List.of(), null);
        Zone.Meadow meadowZone3 = new Zone.Meadow(4, List.of(), null);

        Zone.River riverZone1 = new Zone.River(0, 2, null);
        Zone.River riverZone2 = new Zone.River(3, 1, new Zone.Lake(8, 0, null));

        Tile tile2 = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadowZone1),
                new TileSide.River(meadowZone1, riverZone1, meadowZone2),
                new TileSide.River(meadowZone2, riverZone2, meadowZone3),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        Set<Occupant> actual2 = new HashSet<>();
        actual2.add(new Occupant(Occupant.Kind.PAWN, 1));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 2));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 3));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 0));
        actual2.add(new Occupant(Occupant.Kind.HUT, 0));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 4));
        actual2.add(new Occupant(Occupant.Kind.HUT, 8));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 5));

        assertEquals(placedTile2.potentialOccupants(), actual2);
    }

    @Test
    void withOccupant() {
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, List.of(), null);
        Zone.Meadow meadowZone3 = new Zone.Meadow(4, List.of(), null);

        Zone.Lake lake = new Zone.Lake(8, 0, null);
        Zone.River riverZone2 = new Zone.River(3, 1, lake);

        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadowZone1),
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadowZone2, riverZone2, meadowZone3),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN);

        Set<Occupant> actual2 = new HashSet<>();
        actual2.add(new Occupant(Occupant.Kind.PAWN, 1));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 2));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 3));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 0));
        actual2.add(new Occupant(Occupant.Kind.PAWN, 4));
        final Occupant occupant1 = new Occupant(Occupant.Kind.HUT, 8);
        final Occupant occupant2 = new Occupant(Occupant.Kind.PAWN, 3);

        actual2.add(occupant1);
        actual2.add(new Occupant(Occupant.Kind.PAWN, 5));

        assertEquals(placedTile.withOccupant(occupant1), new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN, occupant1));
        assertEquals(placedTile.withOccupant(occupant2), new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN, occupant2));
    }

    @Test
    void withNoOccupant() {
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, List.of(), null);
        Zone.Meadow meadowZone3 = new Zone.Meadow(4, List.of(), null);

        Zone.Lake lake = new Zone.Lake(8, 0, null);
        Zone.River riverZone2 = new Zone.River(3, 1, lake);

        final Occupant occupant1 = new Occupant(Occupant.Kind.HUT, 8);

        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadowZone1),
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadowZone2, riverZone2, meadowZone3),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN, occupant1);

        assertEquals(placedTile.withNoOccupant(), new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN));
    }

    @Test
    void idOfZoneOccupiedBy() {
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, List.of(), null);
        Zone.Meadow meadowZone3 = new Zone.Meadow(4, List.of(), null);

        Zone.Lake lake = new Zone.Lake(8, 0, null);
        Zone.River riverZone2 = new Zone.River(3, 1, lake);

        Tile tile = new Tile(56, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadowZone1),
                new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadowZone2, riverZone2, meadowZone3),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MENHIR)));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.GREEN, Rotation.NONE, Pos.ORIGIN, new Occupant(Occupant.Kind.HUT, 8));

        assertEquals(placedTile.idOfZoneOccupiedBy(Occupant.Kind.HUT), 8);
        assertEquals(placedTile.idOfZoneOccupiedBy(Occupant.Kind.PAWN), -1);
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
}