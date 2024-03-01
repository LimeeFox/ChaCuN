package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileTest {

    @Test
    void tileSideZonesWorks() {

        Zone.Meadow meadow1 = new Zone.Meadow(2,
                List.of(new Animal(1, Animal.Kind.TIGER)), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(0,
                List.of(new Animal(0, Animal.Kind.AUROCHS)), null);
        Zone.Forest forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.River river1 = new Zone.River(1, 2, null);
        Zone.Lake lake = new Zone.Lake(1, 0, Zone.SpecialPower.LOGBOAT);
        Zone.River river2 = new Zone.River(1, 2, lake);

        TileSide northSide1 = new TileSide.Meadow(meadow1);
        TileSide eastSide1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide southSide1 = new TileSide.Meadow(meadow2);
        TileSide westSide1 = new TileSide.River(meadow2, river1, meadow1);

        TileSide northSide2 = new TileSide.Meadow(meadow1);
        TileSide eastSide2 = new TileSide.Meadow(meadow2);
        TileSide southSide2 = new TileSide.River(meadow1, river2, meadow2);
        TileSide westSide2 = new TileSide.River(meadow2, river2, meadow1);

        TileSide northSide3 = new TileSide.Meadow(meadow1);
        TileSide eastSide3 = new TileSide.Forest(forest1);
        TileSide southSide3 = new TileSide.Forest(forest1);
        TileSide westSide3 = new TileSide.Meadow(meadow1);

        Tile testTile1 = new Tile(1, Tile.Kind.NORMAL, northSide1, eastSide1, southSide1, westSide1);
        Tile testTile2 = new Tile(2, Tile.Kind.NORMAL, northSide2, eastSide2, southSide2, westSide2);
        Tile testTile3 = new Tile(3, Tile.Kind.MENHIR, northSide3,eastSide3,southSide3, westSide3);

        Set<Zone> expectedSideZones1 = new HashSet<>(Set.of(meadow1, river1, meadow2));
        Set<Zone> expectedSideZones2 = new HashSet<>(Set.of(meadow1, meadow2, river2));
        Set<Zone> expectedSideZones3 = new HashSet<>(Set.of(meadow1, forest1));

        assertEquals(testTile1.sideZones(), expectedSideZones1);
        assertEquals(testTile2.sideZones(), expectedSideZones2);
        assertEquals(testTile3.sideZones(), expectedSideZones3);
    }

    @Test
    void sidesIsDefined() {

        Zone.Meadow meadow1 = new Zone.Meadow(2,
                List.of(new Animal(1, Animal.Kind.TIGER)), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(0,
                List.of(new Animal(0, Animal.Kind.AUROCHS)), null);
        Zone.River river1 = new Zone.River(1, 2, null);

        TileSide northSide1 = new TileSide.Meadow(meadow1);
        TileSide eastSide1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide southSide1 = new TileSide.Meadow(meadow2);
        TileSide westSide1 = new TileSide.River(meadow2, river1, meadow1);


        Tile testTile = new Tile(1, Tile.Kind.NORMAL, northSide1, eastSide1, southSide1, westSide1);

        List<TileSide> expectedSides = List.of(northSide1, eastSide1, southSide1, westSide1);

        assertEquals(testTile.sides(), expectedSides);
    }

    @Test
    void tileZonesWorks() {
        Zone.Meadow meadow1 = new Zone.Meadow(2,
                List.of(new Animal(1, Animal.Kind.TIGER)), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(0,
                List.of(new Animal(0, Animal.Kind.AUROCHS)), null);
        Zone.Forest forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.River river1 = new Zone.River(1, 2, null);
        Zone.Lake lake = new Zone.Lake(1, 0, Zone.SpecialPower.LOGBOAT);
        Zone.River river2 = new Zone.River(1, 2, lake);

        TileSide northSide1 = new TileSide.Meadow(meadow1);
        TileSide eastSide1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide southSide1 = new TileSide.Meadow(meadow2);
        TileSide westSide1 = new TileSide.River(meadow2, river1, meadow1);

        TileSide northSide2 = new TileSide.Meadow(meadow1);
        TileSide eastSide2 = new TileSide.Meadow(meadow2);
        TileSide southSide2 = new TileSide.River(meadow1, river2, meadow2);
        TileSide westSide2 = new TileSide.River(meadow2, river2, meadow1);

        TileSide northSide3 = new TileSide.Meadow(meadow1);
        TileSide eastSide3 = new TileSide.Forest(forest1);
        TileSide southSide3 = new TileSide.Forest(forest1);
        TileSide westSide3 = new TileSide.Meadow(meadow1);

        Tile testTile1 = new Tile(1, Tile.Kind.NORMAL, northSide1, eastSide1, southSide1, westSide1);
        Tile testTile2 = new Tile(2, Tile.Kind.NORMAL, northSide2, eastSide2, southSide2, westSide2);
        Tile testTile3 = new Tile(3, Tile.Kind.MENHIR, northSide3,eastSide3,southSide3, westSide3);

        Set<Zone> expectedZones1 = new HashSet<>(Set.of(meadow1, river1, meadow2));
        Set<Zone> expectedZones2 = new HashSet<>(Set.of(meadow1, meadow2, lake, river2));
        Set<Zone> expectedZones3 = new HashSet<>(Set.of(meadow1, forest1));

        assertEquals(testTile1.zones(), expectedZones1);
        assertEquals(testTile2.zones(), expectedZones2);
        assertEquals(testTile3.zones(), expectedZones3);
    }
}
