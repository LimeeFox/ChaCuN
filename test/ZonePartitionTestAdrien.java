import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import ch.epfl.chacun.*;

/**
 * @author Adrien Renggli (363078)
 * @author Nicolas Schilliger (363739)
 */

public class ZonePartitionTestAdrien {
    @Test
    void testConstructorAndGetters() {
        Set<Area<Zone>> areas = new HashSet<>();
        Area<Zone> area1 = new Area<>(new HashSet<>(), List.of(), 0);
        areas.add(area1);
        Area<Zone> area2 = new Area<>(new HashSet<>(), List.of(), 0);
        areas.add(area2);

        ZonePartition<Zone> partition = new ZonePartition<>(areas);

        assertEquals(areas, partition.areas());
    }

    @Test
    void testAreaContaining() {
        Zone.Forest forest1 = new Zone.Forest(468, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(865, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest3 = new Zone.Forest(963, Zone.Forest.Kind.PLAIN);
        Set<Area<Zone.Forest>> areas = new HashSet<>();
        Area<Zone.Forest> area1 = new Area<>(Set.of(forest1), List.of(), 0);
        areas.add(area1);
        Area<Zone.Forest> area2 = new Area<>(Set.of(forest2, forest3), List.of(), 0);
        areas.add(area2);

        ZonePartition<Zone.Forest> partition = new ZonePartition<>(areas);

        assertEquals(area1, partition.areaContaining(forest1));
        assertEquals(area2, partition.areaContaining(forest2));
        assertEquals(area2, partition.areaContaining(forest3));
        assertThrows(IllegalArgumentException.class, () ->
                partition.areaContaining(new Zone.Forest(973, Zone.Forest.Kind.PLAIN)));
    }

    @Test
    void testAddSingleton() {
        Zone.Forest forest1 = new Zone.Forest(876, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(852, Zone.Forest.Kind.WITH_MUSHROOMS);
        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>());
        builder.addSingleton(forest1 , 2);
        builder.addSingleton(forest2, 1);

        ZonePartition<Zone.Forest> zones = builder.build();

        assertTrue(zones.areas().contains(new Area<>(Set.of(forest1), List.of(), 2)));
        assertTrue(zones.areas().contains(new Area<>(Set.of(forest2), List.of(), 1)));
        assertEquals(2, zones.areaContaining(forest1).openConnections());
        assertEquals(1, zones.areaContaining(forest2).openConnections());
        assertEquals(new ArrayList<PlayerColor>(), zones.areaContaining(forest1).occupants());
        assertEquals(new ArrayList<PlayerColor>(), zones.areaContaining(forest2).occupants());
    }

    @Test
    void testAddInitialOccupant() { // max 1 nouvel occupant par zone
        Zone.Forest forest1 = new Zone.Forest(876, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(852, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest3 = new Zone.Forest(268, Zone.Forest.Kind.WITH_MENHIR);
        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>());
        builder.addSingleton(forest1, 2);
        builder.addSingleton(forest2, 1);
        builder.addSingleton(forest3, 1);
        builder.addInitialOccupant(forest1, PlayerColor.RED);
        builder.addInitialOccupant(forest2, PlayerColor.BLUE);
        ZonePartition<Zone.Forest> partition = builder.build();

        assertTrue(partition.areaContaining(forest1).isOccupied());
        assertTrue(partition.areaContaining(forest2).isOccupied());
        assertFalse(partition.areaContaining(forest3).isOccupied());
        assertTrue(partition.areaContaining(forest1).occupants().contains(PlayerColor.RED));
        assertTrue(partition.areaContaining(forest2).occupants().contains(PlayerColor.BLUE));
        assertThrows(IllegalArgumentException.class, () -> builder.addInitialOccupant(forest2, PlayerColor.GREEN));
        assertThrows(IllegalArgumentException.class, () -> builder.addInitialOccupant(forest2, PlayerColor.BLUE));
        assertThrows(IllegalArgumentException.class, () -> builder.addInitialOccupant(
                new Zone.Forest(752, Zone.Forest.Kind.WITH_MENHIR), PlayerColor.PURPLE));
    }

    @Test
    void testRemoveOccupant() {
        Zone.Forest forest1 = new Zone.Forest(876, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(852, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest3 = new Zone.Forest(268, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> zoneForest1 = new Area<>(Set.of(forest1), List.of(PlayerColor.RED, PlayerColor.RED), 2);
        Area<Zone.Forest> zoneForest2 = new Area<>(Set.of(forest2, forest3), List.of(PlayerColor.GREEN, PlayerColor.BLUE), 1);
        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(zoneForest1, zoneForest2)));

        builder.removeOccupant(forest1, PlayerColor.RED);
        builder.removeOccupant(forest2, PlayerColor.BLUE);

        ZonePartition<Zone.Forest> partition = builder.build();

        assertEquals(List.of(PlayerColor.RED), partition.areaContaining(forest1).occupants());
        assertEquals(List.of(PlayerColor.GREEN), partition.areaContaining(forest2).occupants());
        assertEquals(1, partition.areaContaining(forest2).occupants().size());
    }

    @Test
    void testRemoveAllOccupantsOf() {
        Zone.Forest forest1 = new Zone.Forest(876, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(852, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest3 = new Zone.Forest(268, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> zoneForest1 = new Area<>(Set.of(forest1), List.of(PlayerColor.RED, PlayerColor.RED), 2);
        Area<Zone.Forest> zoneForest2 = new Area<>(Set.of(forest2, forest3), List.of(PlayerColor.GREEN, PlayerColor.BLUE), 1);
        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(zoneForest1, zoneForest2)));

        builder.removeAllOccupantsOf(zoneForest1);
        ZonePartition<Zone.Forest> partition = builder.build();

        assertFalse(partition.areaContaining(forest1).isOccupied());

        builder.removeAllOccupantsOf(zoneForest2);
        partition = builder.build();
        assertFalse(partition.areaContaining(forest3).isOccupied());

        assertThrows(IllegalArgumentException.class, () ->
                builder.removeAllOccupantsOf(new Area<>(Set.of(forest2), List.of(PlayerColor.GREEN, PlayerColor.PURPLE), 2)));
    }

    @Test
    void testUnion() {
        Zone.Forest forest1 = new Zone.Forest(876, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(852, Zone.Forest.Kind.WITH_MUSHROOMS);

        Set<Zone.Forest> forestZones1 = new HashSet<>();
        forestZones1.add(forest1);
        Area<Zone.Forest> forestArea1 = new Area<>(forestZones1, List.of(), 1);

        Set<Zone.Forest> forestZones2 = new HashSet<>();
        forestZones2.add(forest2);
        Area<Zone.Forest> forestArea2 = new Area<>(forestZones2, List.of(), 1);

        ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(new ZonePartition<>(Set.of(forestArea1, forestArea2)));

        builder.union(forest1, forest2);
        ZonePartition<Zone.Forest> partition = builder.build();

        assertEquals(Set.of(new Area<>(Set.of(forest1, forest2), List.of(), 0)), partition.areas());
    }

    @Test
    void testBuild() {
        ZonePartition<Zone.Forest> partition = new ZonePartition.Builder<Zone.Forest>(new ZonePartition<>()).build();
        assertEquals(new ZonePartition<>(), partition);
        // On pourrait faire plus de test (p. ex. avec des partitions non vides)
    }
}