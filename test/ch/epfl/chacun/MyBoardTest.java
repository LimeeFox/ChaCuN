package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ch.epfl.chacun.Rotation.*;
import static org.junit.jupiter.api.Assertions.*;

public class MyBoardTest {

    //<editor-fold desc="Board's attributs initialisation">
    List<Animal> an1 = new ArrayList<>(Collections.singletonList(new Animal(5600, Animal.Kind.AUROCHS)));
    List<Animal> an2 = new ArrayList<>(Collections.singletonList(new Animal(1720, Animal.Kind.DEER)));
    List<Animal> an3 = new ArrayList<>(Collections.singletonList(new Animal(1740, Animal.Kind.TIGER)));

    List<Animal> an4 = new ArrayList<>(Collections.singletonList(new Animal(6100, Animal.Kind.MAMMOTH)));

    Zone.Meadow m1 = new Zone.Meadow(560, an1, null);
    Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow m2 = new Zone.Meadow(562, new ArrayList<>(), null);
    Zone.Lake l1 = new Zone.Lake(568, 1, null);
    Zone.River r1 = new Zone.River(563, 0, l1);
    Zone.Meadow m3 = new Zone.Meadow(170, new ArrayList<>(), null);
    Zone.River r2 = new Zone.River(171, 0, null);
    Zone.Meadow m4 = new Zone.Meadow(172, an2, null);
    Zone.River r3 = new Zone.River(173, 0, null);
    Zone.Meadow m5 = new Zone.Meadow(174, an3, null);
    Zone.Meadow m6 = new Zone.Meadow(610, an4, null);
    TileSide sN = new TileSide.Meadow(m1);
    TileSide sE = new TileSide.Forest(f1);
    TileSide sS = new TileSide.Forest(f1);
    TileSide sW = new TileSide.River(m2, r1, m1);
    TileSide sN1 = new TileSide.River(m3, r2, m4);
    TileSide sE1 = new TileSide.River(m4, r2, m3);
    TileSide sS1 = new TileSide.River(m3, r3, m5);
    TileSide sW1 = new TileSide.River(m5, r3, m3);
    TileSide Mn = new TileSide.Meadow(m6);
    TileSide Me = new TileSide.Meadow(m6);
    TileSide Ms = new TileSide.Meadow(m6);
    TileSide Mw = new TileSide.Meadow(m6);


    Tile tile56 = new Tile(56, Tile.Kind.START, sN, sE, sS, sW);
    Tile tile17 = new Tile(17, Tile.Kind.NORMAL, sN1, sE1, sS1, sW1);

    Tile tile61 = new Tile(61, Tile.Kind.NORMAL, Mn, Me, Ms, Mw);

    ZonePartition<Zone.Meadow> meadowZonePartition = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(m1)), new ArrayList<>(), 2),
                    new Area<>(new HashSet<>(Set.of(m2)), new ArrayList<>(), 1)
            ))
    );
    ZonePartition<Zone.Forest> forestZonePartition = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(f1)), new ArrayList<>(), 2)
            ))
    );
    ZonePartition<Zone.River> riverZonePartition = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(r1)), new ArrayList<>(), 1)
            ))
    );
    ZonePartition<Zone.Water> waterZonePartition = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(r1, l1)), new ArrayList<>(), 1)
            ))
    );
    ZonePartition<Zone.Meadow> meadowZonePartition1 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(m3, m2)), new ArrayList<>(), 3),
                    new Area<>(new HashSet<>(Set.of(m4, m1)), new ArrayList<>(), 2),
                    new Area<>(new HashSet<>(Set.of(m5)), new ArrayList<>(), 2)
            ))
    );
    ZonePartition<Zone.Forest> forestZonePartition1 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(f1)), new ArrayList<>(), 2)
            ))
    );
    ZonePartition<Zone.River> riverZonePartition1 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(r1, r2)), new ArrayList<>(), 1),
                    new Area<>(new HashSet<>(Set.of(r3)), new ArrayList<>(), 2)
            ))
    );
    ZonePartition<Zone.Water> waterZonePartition1 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(r3)), new ArrayList<>(), 2),
                    new Area<>(new HashSet<>(Set.of(r2, r1, l1)), new ArrayList<>(), 1)
            ))
    );
    ZonePartitions zonePartitions = new ZonePartitions(forestZonePartition, meadowZonePartition, riverZonePartition, waterZonePartition);
    ZonePartitions zonePartitions1 = new ZonePartitions(forestZonePartition1, meadowZonePartition1, riverZonePartition1, waterZonePartition1);
    ZonePartition<Zone.Meadow> meadowZonePartition2 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(m3, m2)), new ArrayList<>(), 3),
                    new Area<>(new HashSet<>(Set.of(m4, m1)), new ArrayList<>(), 2),
                    new Area<>(new HashSet<>(Set.of(m5)), List.of(PlayerColor.RED), 2)
            ))
    );
    ZonePartition<Zone.Forest> forestZonePartition2 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(f1)), List.of(PlayerColor.YELLOW), 2)
            ))
    );
    ZonePartition<Zone.River> riverZonePartition2 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(r1, r2)), List.of(PlayerColor.BLUE), 1),
                    new Area<>(new HashSet<>(Set.of(r3)), new ArrayList<>(), 2)
            ))
    );
    ZonePartition<Zone.Water> waterZonePartition2 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(r3)), new ArrayList<>(), 2),
                    new Area<>(new HashSet<>(Set.of(r2, r1, l1)), List.of(PlayerColor.GREEN), 1)
            ))
    );
    //</editor-fold>

    public Tile copyTile(Tile tile) {

        return new Tile(tile.id(), tile.kind(), tile.n(), tile.e(), tile.s(), tile.w());
    }

    Board emptyBoard = Board.EMPTY;
    Board board56 = Board.EMPTY
            .withNewTile(new PlacedTile(tile56, PlayerColor.RED, Rotation.NONE, new Pos(0, 0)));

    Board board56_right = Board.EMPTY
            .withNewTile(new PlacedTile(tile56, PlayerColor.RED, HALF_TURN, new Pos(0, 0)));

    //.withOccupant(new Occupant(Occupant.Kind.HUT,56))
    Board board56_far = Board.EMPTY.withNewTile(new PlacedTile(tile56, PlayerColor.RED, Rotation.NONE, new Pos(-11, 11)));

    Board board_many = Board.EMPTY
            .withNewTile(new PlacedTile(copyTile(tile56), PlayerColor.RED, NONE, new Pos(0, 0)))
            .withNewTile(new PlacedTile(copyTile(tile17), PlayerColor.RED, NONE, new Pos(-1, 0)))
            .withOccupant(new Occupant(Occupant.Kind.HUT, 171));


    Board board_many2 = Board.EMPTY
            .withNewTile(new PlacedTile(copyTile(tile56), PlayerColor.RED, NONE, new Pos(0, 0)))
            .withNewTile(new PlacedTile(copyTile(tile17), PlayerColor.RED, NONE, new Pos(-1, 0)));

    Board board_many3 = Board.EMPTY
            .withNewTile(new PlacedTile(copyTile(tile56), PlayerColor.RED, NONE, new Pos(0, 0)))
            .withNewTile(new PlacedTile(copyTile(tile61), PlayerColor.PURPLE, NONE, new Pos(0, -1))); //bizarre

    Board board_many4 = Board.EMPTY
            .withNewTile(new PlacedTile(copyTile(tile56), PlayerColor.RED, NONE, new Pos(0, 0)))
            .withNewTile(new PlacedTile(copyTile(tile17), PlayerColor.RED, NONE, new Pos(-1, 0)))
            .withOccupant(new Occupant(Occupant.Kind.PAWN, 171));


    Board board_closed_river = Board.EMPTY
            .withNewTile(new PlacedTile(copyTile(tile56), PlayerColor.RED, NONE, new Pos(0, 0)))
            .withNewTile(new PlacedTile(copyTile(tile17), PlayerColor.RED, NONE, new Pos(-1, 0)));

    //.withNewTile(new PlacedTile(copyTile(tile56), PlayerColor.RED, LEFT, new Pos(-1, -1)))

    @Test
    void tileAt() {
        PlacedTile expected1 = new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(0, 0));
        PlacedTile expected2 = new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(-11, 11));
        PlacedTile expected3 = new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0), (new Occupant(Occupant.Kind.HUT, 171)));


        assertEquals(board56.tileAt(new Pos(0, 0)), expected1);
        assertEquals(board56_far.tileAt(new Pos(-11, 11)), expected2);
        assertEquals(board_many.tileAt(new Pos(-1, 0)), expected3);
    }

    @Test
    void tileWithId() {
        PlacedTile expected1 = new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(0, 0));
        PlacedTile expected2 = new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(-11, 11));
        PlacedTile expected3 = new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0), (new Occupant(Occupant.Kind.HUT, 171)));

        assertEquals(board56.tileWithId(56), expected1);
        assertEquals(board56_far.tileWithId(56), expected2);
        assertEquals(board_many.tileWithId(17), expected3);
    }

/**
    @Test
    void cancelledAnimals() {
    }

    @Test
    void occupants() {
    }

    @Test
    void forestArea() {
    }

    @Test
    void meadowArea() {
    }

    @Test
    void riverArea() {
    }

    @Test
    void riverSystemAreas() {
    }







    @Test
    void insertionPositions() {


    }

 */

    @Test
    void adjacentMeadow() {
        Board board = Board.EMPTY;
    }

    @Test
    void occupantCount() {
        //assertEquals(board56.occupantCount(PlayerColor.RED, Occupant.Kind.HUT), 0);
        //assertEquals(board_many.occupantCount(PlayerColor.RED, Occupant.Kind.HUT), 1);
    }

    @Test
    void lastPlacedTile() {
        PlacedTile expected1 = new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(0, 0));
        PlacedTile expected3 = new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0), (new Occupant(Occupant.Kind.HUT, 171)));

        assertEquals(board56.lastPlacedTile(), expected1);
        assertNull(emptyBoard.lastPlacedTile());
        assertEquals(board_many.lastPlacedTile(), expected3);
    }


    @Test
    void riverClosedByLastTile() {
        /*
        assertEquals(Set.of(new Area<>(Set.of(r1, r2), List.of(), 0)),
                board_closed_river.riversClosedByLastTile());

         */
    }

    @Test
    void canAddTile() {
        assertTrue(board56.canAddTile(new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0))));
        assertFalse(board56.canAddTile(new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(1, 0))));

    }

    @Test
    void couldPlaceTile() {
        assertTrue(board56.couldPlaceTile(tile17));
        assertTrue(emptyBoard.couldPlaceTile(tile56));
        assertTrue(board56.couldPlaceTile(tile61));

    }

    @Test
    void withNewTile() {
        assertEquals(board56.withNewTile(new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0))),
                board_many2);

    }

    @Test
    void withOccupant() {
        //assertEquals();
    }

    @Test
    void withoutOccupant() {
        Board board_many_noOccup = Board.EMPTY
                .withNewTile(new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(0, 0)))
                .withNewTile(new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0)));

        assertThrows(IllegalArgumentException.class, () ->
                board_many.withoutOccupant(new Occupant(Occupant.Kind.HUT, 171))); // must fail because ED#571
        assertThrows(IllegalArgumentException.class, () ->
                board_many.withoutOccupant(new Occupant(Occupant.Kind.PAWN, 561)));
        assertTrue(board_many4.withoutOccupant(new Occupant(Occupant.Kind.PAWN, 171)).equals(board_many_noOccup));
    }

    @Test
    void withoutGatherersOrFishersIn() {
    }

    @Test
    void withMoreCancelledAnimals() {
        List<Animal> cancelled = new ArrayList<>();
        cancelled.add(new Animal(6100, Animal.Kind.MAMMOTH));

        assertEquals(new HashSet<>(cancelled),
                board56.withMoreCancelledAnimals(new HashSet<>(cancelled)).cancelledAnimals());
    }

    @Test
    void equalsBoard() {

        Board board_many_noOccup = Board.EMPTY
                .withNewTile(new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(0, 0)))
                .withNewTile(new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0)));


        assertNotEquals(board_many, board56.withNewTile(new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0))));
        assertEquals(board_many_noOccup, board56.withNewTile(new PlacedTile(tile17, PlayerColor.RED, NONE, new Pos(-1, 0))));
    }

//    @Test
//    void hashcode() {
//    }

//    @Test
//    void getIndexOfTile() {
//        assertEquals(getIndexOfTile(new PlacedTile(tile56,PlayerColor.RED, NONE, new Pos(0,0))), 312);
//    }

}
