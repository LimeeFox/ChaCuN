package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ZonePartitionTest {

    @Test
    public void testAreaContaining() {
        // Create some zones and areas
        Zone zone1 = new Zone(/* zone details */);
        Zone zone2 = new Zone(/* zone details */);
        Area<Zone> area1 = new Area<>(/* area details */);
        Area<Zone> area2 = new Area<>(/* area details */);

        // Create a ZonePartition with the areas
        ZonePartition<Zone> partition = new ZonePartition<>(Set.of(area1, area2));

        // Test if the method correctly returns the area containing zone1
        assertEquals(area1, partition.areaContaining(zone1));

        // Test if the method throws an exception when no area contains zone2
        assertThrows(IllegalArgumentException.class, () -> partition.areaContaining(zone2));
    }

    // Add more test methods for other functionalities such as Builder class methods
}
