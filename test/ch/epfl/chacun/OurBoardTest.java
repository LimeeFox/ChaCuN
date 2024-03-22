package ch.epfl.chacun;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ch.epfl.chacun.Rotation.*;
import static org.junit.jupiter.api.Assertions.*;

class OurBoardTest {

    List<Animal> animal1 = new ArrayList<>(Collections.singletonList(new Animal(5600, Animal.Kind.AUROCHS)));
    List<Animal> animal2 = new ArrayList<>(Collections.singletonList(new Animal(1720, Animal.Kind.DEER)));
    List<Animal> animal3 = new ArrayList<>(Collections.singletonList(new Animal(1740, Animal.Kind.TIGER)));

    List<Animal> animal4 = new ArrayList<>(Collections.singletonList(new Animal(6100, Animal.Kind.MAMMOTH)));
    List<Animal> animal5 = new ArrayList<>(Collections.singletonList(new Animal(27_2_0, Animal.Kind.DEER)));

    Zone.Meadow meadow1 = new Zone.Meadow(560, animal1, null);
    Zone.Forest forest1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
    Zone.Lake l1 = new Zone.Lake(568, 1, null);
    Zone.River r1 = new Zone.River(563, 0, l1);
    Zone.Meadow meadow3 = new Zone.Meadow(170, new ArrayList<>(), null);
    Zone.River r2 = new Zone.River(171, 0, null);
    Zone.Meadow meadow4 = new Zone.Meadow(172, animal2, null);
    Zone.River r3 = new Zone.River(173, 0, null);
    Zone.Meadow meadow5 = new Zone.Meadow(174, animal3, null);
    Zone.Meadow meadow6 = new Zone.Meadow(610, animal4, null);
    Zone.Meadow z0 = new Zone.Meadow(27_0, List.of(), null);
    Zone.River z1 = new Zone.River(27_1, 0, null);
    Zone.Meadow z2 = new Zone.Meadow(27_2, animal5, null);
    Zone.Forest z3 = new Zone.Forest(27_3, Zone.Forest.Kind.PLAIN);
    TileSide sN = new TileSide.Meadow(meadow1);
    TileSide sE = new TileSide.Forest(forest1);
    TileSide sS = new TileSide.Forest(forest1);
    TileSide sW = new TileSide.River(meadow2, r1, meadow1);
    TileSide sN1 = new TileSide.River(meadow3, r2, meadow4);
    TileSide sE1 = new TileSide.River(meadow4, r2, meadow3);
    TileSide sS1 = new TileSide.River(meadow3, r3, meadow5);
    TileSide sW1 = new TileSide.River(meadow5, r3, meadow3);
    TileSide meadowN = new TileSide.Meadow(meadow6);
    TileSide meadowE = new TileSide.Meadow(meadow6);
    TileSide meadowS = new TileSide.Meadow(meadow6);
    TileSide meadowW = new TileSide.Meadow(meadow6);
    TileSide sN27 = new TileSide.Meadow(z0);
    TileSide sE27 = new TileSide.River(z0, z1, z2);
    TileSide sS27 = new TileSide.River(z2, z1, z0);
    TileSide sW27 = new TileSide.Forest(z3);


    Tile tile17 = new Tile(17, Tile.Kind.NORMAL, sN1, sE1, sS1, sW1);
    Tile tile56 = new Tile(56, Tile.Kind.START, sN, sE, sS, sW);
    Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sN27, sE27, sS27, sW27);

    Tile tile61 = new Tile(61, Tile.Kind.NORMAL, meadowN, meadowE, meadowS, meadowW);

    ZonePartition<Zone.Meadow> meadowZonePartition = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(meadow1)), new ArrayList<>(), 2),
                    new Area<>(new HashSet<>(Set.of(meadow2)), new ArrayList<>(), 1)
            ))
    );
    ZonePartition<Zone.Forest> forestZonePartition = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(forest1)), new ArrayList<>(), 2)
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
                    new Area<>(new HashSet<>(Set.of(meadow3, meadow2)), new ArrayList<>(), 3),
                    new Area<>(new HashSet<>(Set.of(meadow4, meadow1)), new ArrayList<>(), 2),
                    new Area<>(new HashSet<>(Set.of(meadow5)), new ArrayList<>(), 2)
            ))
    );
    ZonePartition<Zone.Forest> forestZonePartition1 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(forest1)), new ArrayList<>(), 2)
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
                    new Area<>(new HashSet<>(Set.of(meadow3, meadow2)), new ArrayList<>(), 3),
                    new Area<>(new HashSet<>(Set.of(meadow4, meadow1)), new ArrayList<>(), 2),
                    new Area<>(new HashSet<>(Set.of(meadow5)), List.of(PlayerColor.RED), 2)
            ))
    );
    ZonePartition<Zone.Forest> forestZonePartition2 = new ZonePartition<>(
            new HashSet<>(Set.of(
                    new Area<>(new HashSet<>(Set.of(forest1)), List.of(PlayerColor.YELLOW), 2)
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


    public Tile copyTile(Tile tile) { // /!\ /!\ /!\ /!\    may be redundant
        return new Tile(tile.id(), tile.kind(), tile.n(), tile.e(), tile.s(), tile.w());
    }

    Board emptyBoard = Board.EMPTY;
    Board board56 = Board.EMPTY
            .withNewTile(new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(0, 0)));

    Board board56_right = Board.EMPTY
            .withNewTile(new PlacedTile(tile56, PlayerColor.RED, HALF_TURN, new Pos(0, 0)));

    //.withOccupant(new Occupant(Occupant.Kind.HUT,56))
    Board board56_far = Board.EMPTY.withNewTile(new PlacedTile(tile56, PlayerColor.RED, NONE, new Pos(-11, 11)));

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

    Board board_many5 = Board.EMPTY
            .withNewTile(new PlacedTile(copyTile(tile56), null, NONE, new Pos(0, 0)))
            .withNewTile(new PlacedTile(copyTile(tile17), PlayerColor.RED, NONE, new Pos(-1, 0), new Occupant(Occupant.Kind.PAWN, 171)))
            .withNewTile(new PlacedTile(copyTile(tile27), PlayerColor.BLUE, NONE, new Pos(1, 0), new Occupant(Occupant.Kind.PAWN, 273)));


    Board board_closed_river = Board.EMPTY
            .withNewTile(new PlacedTile(copyTile(tile56), PlayerColor.RED, NONE, new Pos(0, 0)))
            .withNewTile(new PlacedTile(copyTile(tile17), PlayerColor.RED, NONE, new Pos(-1, 0)))
            .withNewTile(new PlacedTile(copyTile(tile56), PlayerColor.RED, LEFT, new Pos(-1, -1)));



    @Test
    void tileAt() {
        int[] placedTileIndices = new int[5];

        //assertEquals(board_many.tileAt(new Pos)());
    }

    @Test
    void tileWithId() {
    }

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
    void riverSystemArea() {
    }

    @Test
    void meadowAreas() {
    }

    @Test
    void riverSystemAreas() {
    }

    @Test
    void adjacentMeadow() {
    }

    @Test
    void occupantCount() {
    }

    @Test
    void insertionPositions() {
    }

    @Test
    void lastPlacedTile() {
    }

    @Test
    void forestsClosedByLastTile() {
    }

    @Test
    void riversClosedByLastTile() {
    }

    @Test
    void canAddTile() {
    }

    @Test
    void couldPlaceTile() {
    }

    @Test
    void withNewTile() {
    }

    @Test
    void withOccupantWorksNormalCase(){
        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN,50_2);

        // la seule diff ici c'est l'occupent
        PlacedTile placeTileMGauche = new PlacedTile(Tiles.TILES.get(50), PlayerColor.RED, Rotation.NONE, new Pos(-12,-12));
        PlacedTile placeTileMGaucheExpected = new PlacedTile(Tiles.TILES.get(50), PlayerColor.RED, Rotation.NONE, new Pos(-12,-12), occupant1);

        PlacedTile placedTileMilieu = new PlacedTile(Tiles.TILES.get(61), PlayerColor.BLUE, Rotation.NONE, new Pos(-11,-12));
        PlacedTile placedTileMDroit = new PlacedTile(Tiles.TILES.get(50), PlayerColor.RED, Rotation.NONE, new Pos(-10,-12));
        PlacedTile placeTileBasGauche = new PlacedTile(Tiles.TILES.get(26), PlayerColor.BLUE, Rotation.NONE, new Pos(-12,-11));
        PlacedTile placedTileMilieuBas = new PlacedTile(Tiles.TILES.get(40), PlayerColor.RED, Rotation.NONE, new Pos(-11,-11));
        PlacedTile placedTileBasDroit = new PlacedTile(Tiles.TILES.get(35), PlayerColor.RED, Rotation.NONE, new Pos(-10,-11));
        PlacedTile [] placedTiles = new PlacedTile[625];
        PlacedTile [] placedTilesExpected = new PlacedTile[625];

        // sur les deux morceau en dessous la seule diff c'est la placeTileMGauche avec un occupent
        placedTiles[0] = placeTileMGauche;
        placedTiles[1] = placedTileMilieu;
        placedTiles[2] = placedTileMDroit;
        placedTiles[25] = placeTileBasGauche;
        placedTiles[26] = placedTileMilieuBas;
        placedTiles[27] = placedTileBasDroit;

        placedTilesExpected[0] = placeTileMGaucheExpected;
        placedTilesExpected[1] = placedTileMilieu;
        placedTilesExpected[2] = placedTileMDroit;
        placedTilesExpected[25] = placeTileBasGauche;
        placedTilesExpected[26] = placedTileMilieuBas;
        placedTilesExpected[27] = placedTileBasDroit;


        int[] indexes = new int[6];
        indexes[0] = 0;
        indexes[1] = 1;
        indexes[2] = 2;
        indexes[3] = 25;
        indexes[4] = 26;
        indexes[5] = 27;

        var emptyPartitions = new ZonePartitions(
                new ZonePartition<>(),
                new ZonePartition<>(),
                new ZonePartition<>(),
                new ZonePartition<>());
        var b = new ZonePartitions.Builder(emptyPartitions);
        b.addTile(Tiles.TILES.get(50));
        b.addTile(Tiles.TILES.get(61));
        b.addTile(Tiles.TILES.get(50));
        b.addTile(Tiles.TILES.get(26));
        b.addTile(Tiles.TILES.get(40));
        b.addTile(Tiles.TILES.get(35));
        b.connectSides(Tiles.TILES.get(50).e(), Tiles.TILES.get(61).w());
        b.connectSides(Tiles.TILES.get(40).n(), Tiles.TILES.get(61).s());
        b.connectSides(Tiles.TILES.get(50).w(), Tiles.TILES.get(61).e());
        var partitions = b.build();


        Board boardTest = new Board(placedTiles, indexes, partitions, new HashSet<>());
        Board boardExpected = new Board(placedTilesExpected, indexes, partitions, new HashSet<>());

        boardTest.withOccupant(occupant1);

        assertTrue(boardExpected.equals(boardTest));
        //if(boardExpected.equals(boardTest)){ Assertions.assertEquals(1,1);}
        //else Assertions.assertEquals(1,2);
    }


    @Test
    void withoutOccupant() {
    }

    @Test
    void withoutGatherersOrFishersIn() {
    }

    @Test
    void withMoreCancelledAnimals() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}