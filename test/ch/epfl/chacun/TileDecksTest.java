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



    Tile normalTile1;
    Tile normalTile2;

    TileDecks testDecks = new TileDecks(List.of(startTile),
    @Test
    void deckSizeDefined() {
                List.of(normalTile1, normalTile2), List.of());
    }
}
