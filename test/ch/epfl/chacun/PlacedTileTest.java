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
        /*
        @Test
        public void testPotentialOccupants() {
            // Création d'une tuile placée avec des occupants potentiels
            Zone.Forest forest1 = new Zone.Forest(560, Zone.Forest.Kind.PLAIN);
            Zone.Meadow meadow1 = new Zone.Meadow(561, new ArrayList<>(), null);
            Zone.Meadow meadow2 = new Zone.Meadow(563, new ArrayList<>(), null);
            Zone.River river1 = new Zone.River(562, 2, new Zone.Lake(568, 3, Zone.SpecialPower.LOGBOAT));
            Zone.River river2 = new Zone.River(256, 2, null);
            PlacedTile placedTile1 = new PlacedTile(new Tile(1, Tile.Kind.NORMAL,
                    new TileSide.Forest(forest1),
                    new TileSide.Meadow(meadow1),
                    new TileSide.River(meadow1, river1, meadow2),
                    new TileSide.Meadow(new Zone.Meadow(563, new ArrayList<>(), null))),
                    PlayerColor.RED, Rotation.NONE, new Pos(0, 0), null);
            PlacedTile placedTile2 = new PlacedTile(new Tile(1, Tile.Kind.NORMAL,
                    new TileSide.Forest(forest1),
                    new TileSide.Meadow(meadow1),
                    new TileSide.River(meadow1, river2, meadow2),
                    new TileSide.Meadow(meadow2)),
                    PlayerColor.RED, Rotation.NONE, new Pos(0, 0), null);


            // Vérification des occupants potentiels de la tuile placée
            Set<Occupant> potentialOccupants1 = placedTile1.potentialOccupants();
            Set<Occupant> potentialOccupants2 = placedTile2.potentialOccupants();
            // Vérifiez ici les occupants potentiels attendus en fonction de la configuration de la tuile
            assertEquals(5, potentialOccupants1.size());
            assertEquals(5, potentialOccupants2.size());
            assertTrue(potentialOccupants1.contains(new Occupant(Occupant.Kind.HUT, 568)));
            assertTrue(potentialOccupants2.contains(new Occupant(Occupant.Kind.HUT, 256)));
            assertTrue(potentialOccupants1.contains(new Occupant(Occupant.Kind.PAWN, 562)));
            assertTrue(potentialOccupants2.contains(new Occupant(Occupant.Kind.PAWN, 256)));
            assertTrue(potentialOccupants1.contains(new Occupant(Occupant.Kind.PAWN, 560)));
            assertTrue(potentialOccupants2.contains(new Occupant(Occupant.Kind.PAWN, 560)));
            assertTrue(potentialOccupants1.contains(new Occupant(Occupant.Kind.PAWN, 561)));
            assertTrue(potentialOccupants2.contains(new Occupant(Occupant.Kind.PAWN, 561)));
            assertTrue(potentialOccupants1.contains(new Occupant(Occupant.Kind.PAWN, 563)));
            assertTrue(potentialOccupants2.contains(new Occupant(Occupant.Kind.PAWN, 563)));
        }

         */

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
}