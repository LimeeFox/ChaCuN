package ch.epfl.chacun;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TileSideTest0 {

    Zone.Meadow meadow1 = new Zone.Meadow(2,
            List.of(new Animal(1, Animal.Kind.TIGER)), Zone.SpecialPower.PIT_TRAP);
    Zone.Meadow meadow2 = new Zone.Meadow(0,
            List.of(new Animal(0, Animal.Kind.AUROCHS)), null);
    Zone.Forest forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
    Zone.Forest forest2 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
    Zone.River river1 = new Zone.River(1, 2, null);
    Zone.River river2 = new Zone.River(1, 2,
            new Zone.Lake(1, 0, Zone.SpecialPower.LOGBOAT));

    TileSide.Meadow tileSideMeadow1 = new TileSide.Meadow(meadow1);
    TileSide.Meadow tileSideMeadow2= new TileSide.Meadow(meadow2);
    TileSide.Forest tileSideForest1 = new TileSide.Forest(forest1);
    TileSide.Forest tileSideForest2 = new TileSide.Forest(forest2);
    TileSide.River tileSideRiver1 = new TileSide.River(meadow1, river1, meadow2);
    TileSide.River tileSideRiver2 = new TileSide.River(meadow2, river2, meadow2);

    @Test
    void isSameKindWorks() {
        assertTrue(tileSideMeadow1.isSameKindAs(tileSideMeadow2));
        assertEquals(tileSideMeadow1.isSameKindAs(tileSideMeadow2) ,tileSideMeadow2.isSameKindAs(tileSideMeadow2));
        assertFalse(tileSideMeadow1.isSameKindAs(tileSideForest1));

        assertTrue(tileSideForest1.isSameKindAs(tileSideForest2));
        assertEquals(tileSideForest1.isSameKindAs(tileSideForest2) ,tileSideForest2.isSameKindAs(tileSideForest2));
        assertFalse(tileSideForest1.isSameKindAs(tileSideRiver1));

        assertTrue(tileSideRiver1.isSameKindAs(tileSideRiver2));
        assertEquals(tileSideRiver1.isSameKindAs(tileSideRiver2) ,tileSideRiver2.isSameKindAs(tileSideRiver1));
        assertFalse(tileSideRiver1.isSameKindAs(tileSideMeadow1));
    }
}
