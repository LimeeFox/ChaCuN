package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.List;

public class TileDecksTest {
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

    Tile normalTile1 = new Tile(1, Tile.Kind.NORMAL, northSide1, eastSide1, southSide1, westSide1);
    Tile normalTile2 = new Tile(2, Tile.Kind.NORMAL, northSide2, eastSide2, southSide2, westSide2);
    Tile menhirTile = new Tile();

    TileDecks testDecks1 = new TileDecks(List.of(startTile),
            List.of(normalTile1, normalTile2),
            List.of());
    TileDecks testDecks2 = new TileDecks(List.of(startTile),
            List.of(normalTile1, normalTile2),
            List.of(menhirTile));

    @Test
    void deckSizeDefined() {
    }
}
