package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// TODO: 07/03/2024 Test builder methods

public class ZonePartitionTest0 {

    private final Zone.Meadow meadow1 = new Zone.Meadow(2,
            List.of(new Animal(1, Animal.Kind.TIGER)), Zone.SpecialPower.PIT_TRAP);
    private final Zone.Meadow meadow2 = new Zone.Meadow(0,
            List.of(new Animal(0, Animal.Kind.AUROCHS)), null);
    private final Zone.Forest forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
    private final Zone.River river1 = new Zone.River(1, 2, null);
    private final Zone.Lake lake = new Zone.Lake(1, 0, Zone.SpecialPower.LOGBOAT);
    private final Zone.River river2 = new Zone.River(1, 2, lake);

    private final Area<Zone.Meadow> meadowArea1 = new Area<>(Set.of(meadow1),
            List.of(PlayerColor.BLUE), 2);
    private final Area<Zone.Meadow> meadowArea2 = new Area<>(Set.of(meadow2),
            List.of(PlayerColor.RED), 1);
    private final Area<Zone.Water> waterArea1 = new Area<>(Set.of(river1, lake), List.of(), 1);
    private final Area<Zone.Water> waterArea2 = new Area<>(Set.of(river2), List.of(PlayerColor.BLUE), 2);


    ZonePartition<Zone.Meadow> emptyPartition = new ZonePartition<>();
    ZonePartition<Zone.Meadow> partition1 = new ZonePartition<>(Set.of(meadowArea1, meadowArea2));
    ZonePartition<Zone.Water> partition2 = new ZonePartition<>(Set.of(waterArea1));

    @Test
    public void testAreaContaining() {
        assertEquals(meadowArea2, partition1.areaContaining(meadow2));

        assertThrows(IllegalArgumentException.class, () -> emptyPartition.areaContaining(meadow1));
        assertThrows(IllegalArgumentException.class, () -> partition2.areaContaining(river2));
    }

    @Test
    public void testBuilder() {
        ZonePartition.Builder<Zone.Meadow> zonePartitionBuilder = new ZonePartition.Builder<>();

        zonePartitionBuilder.addSingleton(meadow1, 2);
        zonePartitionBuilder.addInitialOccupant(meadow1, PlayerColor.BLUE);
        zonePartitionBuilder.addSingleton(meadow2, 1);

        zonePartitionBuilder.union(meadow1, meadow2);
        zonePartitionBuilder.addInitialOccupant(meadow2, PlayerColor.BLUE);
        zonePartitionBuilder.removeOccupant(meadow2,PlayerColor.BLUE);
        zonePartitionBuilder.addInitialOccupant(meadow2, PlayerColor.RED);

        ZonePartition<Zone.Meadow> partition2 = zonePartitionBuilder.build();

        assertEquals(partition1, partition2);

    }
}