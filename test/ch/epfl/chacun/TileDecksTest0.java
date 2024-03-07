package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TileDecksTest0 {
    Tile startTile = new Tile(56, Tile.Kind.START,
            new TileSide.Meadow(new Zone.Meadow(0, List.of(new Animal(0, Animal.Kind.AUROCHS)), null)),
            new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR)),
            new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR)),
            new TileSide.River(new Zone.Meadow(2, List.of(), null),
                    new Zone.River(3, 0, new Zone.Lake(8, 1, null)),
                    new Zone.Meadow(0, List.of(new Animal(0, Animal.Kind.AUROCHS)), null)));

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

    Tile normalTile1 = new Tile(1, Tile.Kind.NORMAL, northSide1, eastSide1, southSide1, westSide1);
    Tile normalTile2 = new Tile(2, Tile.Kind.NORMAL, northSide2, eastSide2, southSide2, westSide2);
    Tile menhirTile = new Tile(3, Tile.Kind.MENHIR, northSide3, eastSide3, southSide3, westSide3);

    TileDecks testDecks1 = new TileDecks(List.of(startTile),
            List.of(normalTile1, normalTile2),
            List.of());
    TileDecks testDecks2 = new TileDecks(List.of(),
            List.of(normalTile1, normalTile2),
            List.of(menhirTile));

    @Test
    void deckSizeDefined() {
        assertEquals(1, testDecks1.deckSize(Tile.Kind.START));
        assertEquals(2, testDecks1.deckSize(Tile.Kind.NORMAL));
        assertEquals(0, testDecks1.deckSize(Tile.Kind.MENHIR));

        assertEquals(0, testDecks2.deckSize(Tile.Kind.START));
        assertEquals(2, testDecks2.deckSize(Tile.Kind.NORMAL));
        assertEquals(1, testDecks2.deckSize(Tile.Kind.MENHIR));
    }

    @Test
    void topTileWorks() {
        assertEquals(startTile, testDecks1.topTile(Tile.Kind.START));
        assertEquals(normalTile1, testDecks1.topTile(Tile.Kind.NORMAL));
        assertNull(testDecks1.topTile(Tile.Kind.MENHIR));

        assertNull(testDecks2.topTile(Tile.Kind.START));
        assertEquals(normalTile1, testDecks2.topTile(Tile.Kind.NORMAL));
        assertEquals(menhirTile, testDecks2.topTile(Tile.Kind.MENHIR));
    }

    @Test
    void withTopTileDrawnWorks() {
        assertEquals(new TileDecks(List.of(), List.of(normalTile1, normalTile2), List.of()),
                testDecks1.withTopTileDrawn(Tile.Kind.START));
        assertEquals(new TileDecks(List.of(startTile), List.of(normalTile2), List.of()),
                testDecks1.withTopTileDrawn(Tile.Kind.NORMAL));

        assertEquals(new TileDecks(List.of(), List.of(normalTile2), List.of(menhirTile)),
                testDecks2.withTopTileDrawn(Tile.Kind.NORMAL));
        assertEquals(new TileDecks(List.of(), List.of(normalTile1, normalTile2), List.of()),
                testDecks2.withTopTileDrawn(Tile.Kind.MENHIR));

    }
    /*
    @Test void withTopTileDrawnUntilWorks() {

        Predicate<Tile> containsLake = tile -> tile.equals(normalTile2);


        assertEquals(new TileDecks(List.of(), List.of(normalTile1, normalTile2), List.of()),
                testDecks1.withTopTileDrawnUntil(Tile.Kind.START, containsLake));
        assertEquals(new TileDecks(List.of(startTile), List.of(normalTile2), List.of()),
                testDecks1.withTopTileDrawnUntil(Tile.Kind.NORMAL, containsLake));

        assertEquals(new TileDecks(List.of(), List.of(normalTile2), List.of(menhirTile)),
                testDecks2.withTopTileDrawnUntil(Tile.Kind.NORMAL, containsLake));
        assertEquals(new TileDecks(List.of(), List.of(normalTile1, normalTile2), List.of()),
                testDecks2.withTopTileDrawnUntil(Tile.Kind.MENHIR, containsLake));
    }

    */
}
