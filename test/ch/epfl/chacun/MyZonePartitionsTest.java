package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyZonePartitionsTest {
    List<Animal> an1 = new ArrayList<>(Collections.singletonList(new Animal(5600, Animal.Kind.AUROCHS)));
    List<Animal> an2 = new ArrayList<>(Collections.singletonList(new Animal(1720, Animal.Kind.DEER)));
    List<Animal> an3 = new ArrayList<>(Collections.singletonList(new Animal(1740, Animal.Kind.TIGER)));
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
    TileSide sN = new TileSide.Meadow(m1);
    TileSide sE = new TileSide.Forest(f1);
    TileSide sS = new TileSide.Forest(f1);
    TileSide sW = new TileSide.River(m2, r1, m1);
    TileSide sN1 = new TileSide.River(m3, r2, m4);
    TileSide sE1 = new TileSide.River(m4, r2, m3);
    TileSide sS1 = new TileSide.River(m3, r3, m5);
    TileSide sW1 = new TileSide.River(m5, r3, m3);
    Tile tile56 = new Tile(56, Tile.Kind.START, sN, sE, sS, sW);
    Tile tile17 = new Tile(17, Tile.Kind.NORMAL, sN1, sE1, sS1, sW1);
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
    ZonePartitions zonePartitions2 = new ZonePartitions(forestZonePartition2, meadowZonePartition2, riverZonePartition2, waterZonePartition2);

    @Test
    void zonePartitionsAddTileWorks(){
        ZonePartitions.Builder essai = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        essai.addTile(tile56);
        assertEquals(zonePartitions, essai.build());
    }

    @Test
    void zonePartitionsConnectSidesWorks(){
        ZonePartitions.Builder essai = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        essai.addTile(tile56);
        essai.addTile(tile17);
        essai.connectSides(sW, sE1);
        assertEquals(zonePartitions1, essai.build());
        assertThrows(IllegalArgumentException.class, () -> essai.connectSides(sN, sE1));
    }

    @Test
    void zonePartitionsAddInitialOccupantWorks(){
        ZonePartitions.Builder essai = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        essai.addTile(tile56);
        essai.addTile(tile17);
        essai.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, m5);
        essai.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, r1);
        essai.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, f1);
        //essai.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.HUT, l1);
        essai.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, r2);
        essai.connectSides(sW, sE1);
        assertEquals(zonePartitions2, essai.build());

        assertThrows(IllegalArgumentException.class, () -> essai.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, l1));
        assertThrows(IllegalArgumentException.class, () -> essai.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, m1));
        assertThrows(IllegalArgumentException.class, () -> essai.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, f1));
        assertThrows(IllegalArgumentException.class, () -> essai.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, r1));
    }

    @Test
    void zonePartitionsRemovePawnWorks(){
        ZonePartitions.Builder essai = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        essai.addTile(tile56);
        essai.addTile(tile17);
        essai.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, m5);
        essai.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, r1);
        essai.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, f1);
        essai.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, r2);
        essai.connectSides(sW, sE1);
        essai.removePawn(PlayerColor.RED, m5);
        ZonePartitions temp = new ZonePartitions(forestZonePartition2, meadowZonePartition1, riverZonePartition2, waterZonePartition2);
        assertEquals(temp, essai.build());
        assertThrows(IllegalArgumentException.class, () -> essai.removePawn(PlayerColor.RED, r3));
        assertThrows(IllegalArgumentException.class, () -> essai.removePawn(PlayerColor.RED, l1));
    }

    @Test
    void zonePartitionsClearGatherersWorks(){
        ZonePartitions.Builder essai = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        essai.addTile(tile56);
        essai.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, f1);
        essai.addTile(tile17);
        essai.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, m5);
        essai.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, r3);
        essai.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, r2);
        essai.connectSides(sW, sE1);
    }
}