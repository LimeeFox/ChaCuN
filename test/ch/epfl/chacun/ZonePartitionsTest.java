package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import javax.swing.text.rtf.RTFEditorKit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ZonePartitionsTest {

    Tile getStartTile () {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var sn = new TileSide.Meadow(meadow0);
        var se = new TileSide.Forest(forest1);
        var ss = new TileSide.Forest(forest1);
        var sw = new TileSide.River(meadow2, river1, meadow0);

        return new Tile(56, Tile.Kind.START, sn, se, ss, sw);
    }
    ZonePartitions.Builder getStartPartition () {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var meadowArea = new Area<Zone.Meadow>(Set.of(meadow0), List.of(), 2 );
        var forestArea = new Area <Zone.Forest> (Set.of(forest1), List.of(), 2);
        var meadowArea2 = new Area <Zone.Meadow> (Set.of(meadow2), List.of(), 1);
        var riverArea = new Area <Zone.River> (Set.of(river1), List.of(), 1);
        var RSArea = new Area <Zone.Water> (Set.of(river1,lake8), List.of(), 1);

        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of(forestArea)), new ZonePartition<Zone.Meadow>(Set.of(meadowArea, meadowArea2)), new ZonePartition<Zone.River>(Set.of(riverArea)), new ZonePartition<Zone.Water>(Set.of(RSArea))
        );
        return new ZonePartitions.Builder(partition);


    }
    ZonePartitions.Builder getTwoRiversLakeBuilder () {
        var meadow0 = new Zone.Meadow(1_0, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(1_8, 2, null);
        var river1 = new Zone.River(1_1, 2, lake8);
        var meadow2 = new Zone.Meadow(1_2, new ArrayList<>(), null);
        var forest3 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        var meadow4 = new Zone.Meadow(1_4, new ArrayList<>(), Zone.SpecialPower.PIT_TRAP);
        var river2 = new Zone.River(1_5, 2, lake8);

        var meadowArea = new Area<Zone.Meadow>(Set.of(meadow0), new ArrayList<>(), 3);
        var meadowArea2 = new Area<Zone.Meadow>(Set.of(meadow2), List.of(), 1);
        var riverArea = new Area <Zone.River>(Set.of(river1), new ArrayList<>(), 1);
        var riverSystemArea = new Area <Zone.Water> (Set.of(river1, river2, lake8), new ArrayList<>(), 2);
        var forestArea = new Area<Zone.Forest>(Set.of(forest3), new ArrayList<>(), 1);
        var meadowArea3 = new Area <Zone.Meadow> (Set.of(meadow4), new ArrayList<>(), 1);
        var riverArea2 = new Area <Zone.River> (Set.of(river2), new ArrayList<>(), 1);

        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of(forestArea)), new ZonePartition<Zone.Meadow>(Set.of(meadowArea, meadowArea2, meadowArea3)), new ZonePartition<Zone.River>(Set.of(riverArea, riverArea2)), new ZonePartition<Zone.Water>(Set.of(riverSystemArea)
        ));

        return new ZonePartitions.Builder(partition);
    }
    ZonePartitions.Builder ForestsFusedBuilder() {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var meadowArea = new Area<Zone.Meadow>(Set.of(meadow0), List.of(), 2 );
        var meadowArea2 = new Area <Zone.Meadow> (Set.of(meadow2), List.of(), 1);
        var riverArea = new Area <Zone.River> (Set.of(river1), List.of(), 1);
        var RSArea = new Area <Zone.Water> (Set.of(river1,lake8), List.of(), 1);


        var m0 = new Zone.Meadow(1_0, new ArrayList<>(), null);
        var l8 = new Zone.Lake(1_8, 2, null);
        var r1 = new Zone.River(1_1, 2, l8);
        var m2 = new Zone.Meadow(1_2, new ArrayList<>(), null);
        var forest3 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        var meadow4 = new Zone.Meadow(1_4, new ArrayList<>(), Zone.SpecialPower.PIT_TRAP);
        var river2 = new Zone.River(1_5, 2, l8);

        var mArea = new Area<Zone.Meadow>(Set.of(m0), new ArrayList<>(), 3);
        var mArea2 = new Area<Zone.Meadow>(Set.of(m2), List.of(), 1);
        var rArea = new Area <Zone.River>(Set.of(r1), new ArrayList<>(), 1);
        var riverSystemArea = new Area <Zone.Water> (Set.of(r1, river2, l8), new ArrayList<>(), 2);
        var fArea = new Area<Zone.Forest>(Set.of(forest3, forest1), new ArrayList<>(), 1);
        var mArea3 = new Area <Zone.Meadow> (Set.of(meadow4), new ArrayList<>(), 1);
        var rArea2 = new Area <Zone.River> (Set.of(river2), new ArrayList<>(), 1);
        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of(fArea)), new ZonePartition<Zone.Meadow>(Set.of(meadowArea, meadowArea2, mArea, mArea2, mArea3)), new ZonePartition<Zone.River>(Set.of(riverArea, rArea, rArea2)), new ZonePartition<Zone.Water>(Set.of(RSArea, riverSystemArea)));
        return new ZonePartitions.Builder(partition);
    }
    ZonePartitions.Builder addTileFusedBuilder() {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var meadowArea = new Area<Zone.Meadow>(Set.of(meadow0), List.of(), 2 );
        var forestArea = new Area <Zone.Forest> (Set.of(forest1), List.of(), 2);
        var meadowArea2 = new Area <Zone.Meadow> (Set.of(meadow2), List.of(), 1);
        var riverArea = new Area <Zone.River> (Set.of(river1), List.of(), 1);
        var RSArea = new Area <Zone.Water> (Set.of(river1,lake8), List.of(), 1);


        var m0 = new Zone.Meadow(1_0, new ArrayList<>(), null);
        var l8 = new Zone.Lake(1_8, 2, null);
        var r1 = new Zone.River(1_1, 2, l8);
        var m2 = new Zone.Meadow(1_2, new ArrayList<>(), null);
        var forest3 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        var meadow4 = new Zone.Meadow(1_4, new ArrayList<>(), Zone.SpecialPower.PIT_TRAP);
        var river2 = new Zone.River(1_5, 2, l8);

        var mArea = new Area<Zone.Meadow>(Set.of(m0), new ArrayList<>(), 3);
        var mArea2 = new Area<Zone.Meadow>(Set.of(m2), List.of(), 1);
        var rArea = new Area <Zone.River>(Set.of(r1), new ArrayList<>(), 1);
        var riverSystemArea = new Area <Zone.Water> (Set.of(r1, river2, l8), new ArrayList<>(), 2);
        var fArea = new Area<Zone.Forest>(Set.of(forest3), new ArrayList<>(), 1);
        var mArea3 = new Area <Zone.Meadow> (Set.of(meadow4), new ArrayList<>(), 1);
        var rArea2 = new Area <Zone.River> (Set.of(river2), new ArrayList<>(), 1);
        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of(forestArea, fArea)), new ZonePartition<Zone.Meadow>(Set.of(meadowArea, meadowArea2, mArea, mArea2, mArea3)), new ZonePartition<Zone.River>(Set.of(riverArea, rArea, rArea2)), new ZonePartition<Zone.Water>(Set.of(RSArea, riverSystemArea)));
        return new ZonePartitions.Builder(partition);
    }
    ZonePartitions.Builder ConnectFusedBuilder() {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var forestArea = new Area <Zone.Forest> (Set.of(forest1), List.of(), 2);


        var m0 = new Zone.Meadow(1_0, new ArrayList<>(), null);
        var l8 = new Zone.Lake(1_8, 2, null);
        var r1 = new Zone.River(1_1, 2, l8);
        var m2 = new Zone.Meadow(1_2, new ArrayList<>(), null);
        var forest3 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        var meadow4 = new Zone.Meadow(1_4, new ArrayList<>(), Zone.SpecialPower.PIT_TRAP);
        var river2 = new Zone.River(1_5, 2, l8);

        var mArea = new Area<Zone.Meadow>(Set.of(m0, meadow0), new ArrayList<>(), 3);
        var mArea2 = new Area<Zone.Meadow>(Set.of(m2, meadow2), List.of(), 0);
        var rArea = new Area <Zone.River>(Set.of(r1, river1), new ArrayList<>(), 0);
        var riverSystemArea = new Area <Zone.Water> (Set.of(r1, river2, l8, river1, lake8), new ArrayList<>(), 1);
        var fArea = new Area<Zone.Forest>(Set.of(forest3), new ArrayList<>(), 1);
        var mArea3 = new Area <Zone.Meadow> (Set.of(meadow4), new ArrayList<>(), 1);
        var rArea2 = new Area <Zone.River> (Set.of(river2), new ArrayList<>(), 1);
        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of(forestArea, fArea)), new ZonePartition<Zone.Meadow>(Set.of(mArea, mArea2, mArea3)), new ZonePartition<Zone.River>(Set.of(rArea, rArea2)), new ZonePartition<Zone.Water>(Set.of(riverSystemArea)));
        return new ZonePartitions.Builder(partition);
    }
    Tile getTwoRiversLakeTile () {
        var l0 = new Zone.Lake(1_8, 2, null);
        var z0 = new Zone.Meadow(1_0, List.of(), null);
        var z2 = new Zone.Meadow(1_2, List.of(), null);
        var z4 = new Zone.Meadow(1_4, List.of(), Zone.SpecialPower.PIT_TRAP);

        var z1 = new Zone.River(1_1, 2, l0);
        var z5 = new Zone.River(1_5, 2, l0);

        var z3 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);

        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.River(z0, z1, z2);
        var sS = new TileSide.Forest(z3);
        var sW = new TileSide.River(z4, z5, z0);

        return new Tile(1, Tile.Kind.NORMAL, sN, sE, sS, sW);

    }
    Tile riverMeadows () {
        var z0 = new Zone.Meadow(2_0, List.of(), null);
        var z1 = new Zone.Meadow(2_2, List.of(), null);
        var r1 = new Zone.River(2_1, 2, null);

        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.River(z0, r1, z1);
        var sS = new TileSide.Meadow(z1);
        var sW = new TileSide.River(z1, r1, z0);

        return new Tile(1, Tile.Kind.NORMAL, sN, sE, sS, sW);
    }
    ZonePartitions.Builder riverMeadowsBuilder () {
        var z0 = new Zone.Meadow(2_0, List.of(), null);
        var z1 = new Zone.Meadow(2_2, List.of(), null);
        var r1 = new Zone.River(2_1, 2, null);

        var areameadow1 = new Area<Zone.Meadow>(Set.of(z0), List.of(), 3);
        var areameadow2 = new Area <Zone.Meadow> (Set.of(z1), List.of(), 3);
        var riverArea = new Area <Zone.River> (Set.of(r1), List.of(), 2);
        var riverSystemArea = new Area <Zone.Water> (Set.of(r1), List.of(), 2);
        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of()), new ZonePartition<Zone.Meadow>(Set.of(areameadow1, areameadow2)), new ZonePartition<Zone.River>(Set.of(riverArea)), new ZonePartition<Zone.Water>(Set.of(riverSystemArea)));

        return new ZonePartitions.Builder(partition);
    }
    ZonePartitions.Builder getStartPartitionwithOccupants () {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var meadowArea = new Area<Zone.Meadow>(Set.of(meadow0), List.of(PlayerColor.RED), 2);
        var forestArea = new Area<Zone.Forest>(Set.of(forest1), List.of(PlayerColor.RED), 2);
        var meadowArea2 = new Area<Zone.Meadow>(Set.of(meadow2), List.of(PlayerColor.RED), 1);
        var riverArea = new Area<Zone.River>(Set.of(river1), List.of(PlayerColor.RED), 1);
        var RSArea = new Area<Zone.Water>(Set.of(river1, lake8), List.of(PlayerColor.RED), 1);

        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of(forestArea)), new ZonePartition<Zone.Meadow>(Set.of(meadowArea, meadowArea2)), new ZonePartition<Zone.River>(Set.of(riverArea)), new ZonePartition<Zone.Water>(Set.of(RSArea))
        );
        return new ZonePartitions.Builder(partition);
    }
    ZonePartitions.Builder lakewithOccupants () {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var meadowArea = new Area<Zone.Meadow>(Set.of(meadow0), List.of(), 2);
        var forestArea = new Area<Zone.Forest>(Set.of(forest1), List.of(), 2);
        var meadowArea2 = new Area<Zone.Meadow>(Set.of(meadow2), List.of(), 1);
        var riverArea = new Area<Zone.River>(Set.of(river1), List.of(), 1);
        var RSArea = new Area<Zone.Water>(Set.of(river1, lake8), List.of(PlayerColor.RED), 1);

        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of(forestArea)), new ZonePartition<Zone.Meadow>(Set.of(meadowArea, meadowArea2)), new ZonePartition<Zone.River>(Set.of(riverArea)), new ZonePartition<Zone.Water>(Set.of(RSArea))
        );
        return new ZonePartitions.Builder(partition);
    }
    ZonePartitions.Builder ForestOccupiedPartition () {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var meadowArea = new Area<Zone.Meadow>(Set.of(meadow0), List.of(), 2 );
        var forestArea = new Area <Zone.Forest> (Set.of(forest1), List.of(PlayerColor.RED, PlayerColor.BLUE), 2);
        var meadowArea2 = new Area <Zone.Meadow> (Set.of(meadow2), List.of(), 1);
        var riverArea = new Area <Zone.River> (Set.of(river1), List.of(), 1);
        var RSArea = new Area <Zone.Water> (Set.of(river1,lake8), List.of(), 1);

        var partition = new ZonePartitions(new ZonePartition<Zone.Forest>(Set.of(forestArea)), new ZonePartition<Zone.Meadow>(Set.of(meadowArea, meadowArea2)), new ZonePartition<Zone.River>(Set.of(riverArea)), new ZonePartition<Zone.Water>(Set.of(RSArea))
        );
        return new ZonePartitions.Builder(partition);


    }

    @Test
    void addTileTest () {
        var z = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        z.addTile(getStartTile());
        var x = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        x.addTile(getTwoRiversLakeTile());
        var y = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        y.addTile(riverMeadows());
        var w = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        w.addTile(getStartTile());
        w.addTile(getTwoRiversLakeTile());
        var v = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        v.addTile(riverMeadows());



        assertEquals(riverMeadowsBuilder().build(), y.build());
        assertEquals(getStartPartition().build(), z.build());
        assertEquals(getTwoRiversLakeBuilder().build(), x.build());
        assertEquals(addTileFusedBuilder().build(), w.build());
        assertEquals(riverMeadowsBuilder().build(), v.build());

    }
    @Test
    void connectSidesExceptionTest() {
        Tile tile = getStartTile();
        ZonePartitions.Builder Z = getStartPartition();
        assertThrows(IllegalArgumentException.class, () -> Z.connectSides(tile.s(), tile.n()));
        assertThrows(IllegalArgumentException.class, () -> Z.connectSides(tile.e(), tile.w()));
    }
    @Test
    void connectTwoRiversWithLake() {
        Tile tile = getStartTile();
        Tile tile2 = getTwoRiversLakeTile();
        ZonePartitions.Builder zBefore = addTileFusedBuilder();
        zBefore.connectSides(tile2.e(), tile.w());
        //zBefore.connectSides(tile2.s(), tile.e());
        ZonePartitions.Builder zAfter = ConnectFusedBuilder();
        assertEquals(zAfter.build(), zBefore.build());
    }
    @Test
    void ForestSidesConnect() {
        Tile tile = getStartTile();
        Tile tile2 = getTwoRiversLakeTile();
        ZonePartitions.Builder zBefore = addTileFusedBuilder();
        zBefore.connectSides(tile2.s(), tile.e());
        ZonePartitions.Builder zAfter = ForestsFusedBuilder();
        assertEquals(zAfter.build(), zBefore.build());
    }
    @Test
    void zonePartitionsAreImmutable() {
        // Create initial sets of areas for each partition type
        Set<Area<Zone.Forest>> forestAreas = new HashSet<>();
        Set<Area<Zone.Meadow>> meadowAreas = new HashSet<>();
        Set<Area<Zone.River>> riverAreas = new HashSet<>();
        Set<Area<Zone.Water>> waterSystemAreas = new HashSet<>();

        // Populate the forest areas with sample data
        var f0 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        var f1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var a0 = new Area<>(Set.of(f0), List.of(), 0);
        var a1 = new Area<>(Set.of(f1), List.of(), 0);
        forestAreas.add(a0);
        forestAreas.add(a1);

        // Create a ZonePartitions instance with the initial sets
        var partition = new ZonePartitions(new ZonePartition<>(forestAreas), new ZonePartition<>(meadowAreas), new ZonePartition<>(riverAreas), new ZonePartition<>(waterSystemAreas));

        // Modify the original sets
        forestAreas.clear();
        meadowAreas.clear();
        riverAreas.clear();
        waterSystemAreas.clear();

        // Assert that the forest partition within ZonePartitions remains unchanged
        assertEquals(Set.of(a0, a1), partition.forests().areas());

        // Attempt to modify the forest areas set directly from the ZonePartitions instance
        try {
            partition.forests().areas().clear();
            //fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected, since the areas set should be immutable
        }

        // Verify that the forest areas within ZonePartitions are still unchanged
        assertEquals(Set.of(a0, a1), partition.forests().areas());
    }
    @Test
    void initialOccupantWrongZone () {
        assertThrows(IllegalArgumentException.class, () -> getStartPartition().addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, new Zone.Meadow(560, new ArrayList<>(), null)));
        assertThrows(IllegalArgumentException.class, () -> getStartPartition().addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, new Zone.Lake(568, 2, null)));
    }

    /*
    @Test
    void initialOccupantAlreadyOccupied () {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var partition = getStartPartition();
        partition.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, river1);

        assertThrows(IllegalArgumentException.class, ()->
            partition.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, lake8)
        );
    }

     */
    @Test
    void initialOccupantWorks() {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var partition = getStartPartition();
        partition.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadow0);
        partition.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, forest1);
        partition.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadow2);
        partition.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, river1);

        assertEquals(getStartPartitionwithOccupants().build(), partition.build());
    }
    @Test
    void removePawnFromLake () {
        var partition = lakewithOccupants();
        var lake8 = new Zone.Lake(568, 2, null);
        assertThrows(IllegalArgumentException.class, ()->partition.removePawn(PlayerColor.RED, lake8));
    }
    @Test
    void removePawns () {
        var meadow0 = new Zone.Meadow(560, new ArrayList<>(), null);
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var meadow2 = new Zone.Meadow(562, new ArrayList<>(), null);
        var lake8 = new Zone.Lake(568, 2, null);
        var river1 = new Zone.River(563, 2, lake8);

        var partition = getStartPartitionwithOccupants();
        partition.removePawn(PlayerColor.RED, meadow0);
        partition.removePawn(PlayerColor.RED, forest1);
        partition.removePawn(PlayerColor.RED, meadow2);
        partition.removePawn(PlayerColor.RED, river1);
        assertEquals(getStartPartition().build(), partition.build() );
    }
    @Test
    void clearGatherersWork () {
        var partition = ForestOccupiedPartition();
        var forest1 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        var forestArea = new Area <Zone.Forest> (Set.of(forest1), List.of(PlayerColor.RED, PlayerColor.BLUE), 2);

        partition.clearGatherers(forestArea);
        assertEquals(getStartPartition().build(), partition.build());


    }



}
