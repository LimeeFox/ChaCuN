package ch.epfl.chacun;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class MyGameStateTest {

    private static Tile getTile(int id, Tile.Kind kind) {
        var zoneId = id * 10;
        var l0 = new Zone.Lake(zoneId + 8, 1, null);
        var a0_0 = new Animal(zoneId * 100, Animal.Kind.AUROCHS);
        var z0 = new Zone.Meadow(zoneId * 10, List.of(a0_0), null);
        var z1 = new Zone.Forest(zoneId * 10 + 1, Zone.Forest.Kind.WITH_MENHIR);
        var z2 = new Zone.Meadow(zoneId * 10 + 2, List.of(), null);
        var z3 = new Zone.River(zoneId * 10 + 3, 0, l0);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Forest(z1);
        var sS = new TileSide.Forest(z1);
        var sW = new TileSide.River(z2, z3, z0);
        return new Tile(id, kind, sN, sE, sS, sW);
    }

    private static Tile getTile(Tile.Kind kind) {
        return getTile(56, kind);
    }

    private static MessageBoard getMessageBoard() {
        TextMaker textMaker = new TextMaker() {
            @Override
            public String playerName(PlayerColor playerColor) {
                return null;
            }

            @Override
            public String points(int points) {
                return null;
            }

            @Override
            public String playerClosedForestWithMenhir(PlayerColor player) {
                return null;
            }

            @Override
            public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
                return null;
            }

            @Override
            public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
                return null;
            }

            @Override
            public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
                return null;
            }

            @Override
            public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
                return null;
            }

            @Override
            public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
                return null;
            }

            @Override
            public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
                return null;
            }

            @Override
            public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
                return null;
            }

            @Override
            public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
                return null;
            }

            @Override
            public String playersWon(Set<PlayerColor> winners, int points) {
                return null;
            }

            @Override
            public String clickToOccupy() {
                return null;
            }

            @Override
            public String clickToUnoccupy() {
                return null;
            }
        };

        List<MessageBoard.Message> messages = new ArrayList<>();

        return new MessageBoard(textMaker, messages);
    }

    private TileDecks getTileDecks() {
        var dS = List.of(getTile(Tile.Kind.START));
        var dN = List.of(
                getTile(0, Tile.Kind.NORMAL),
                getTile(1, Tile.Kind.NORMAL),
                getTile(2, Tile.Kind.NORMAL));
        var dM = List.of(
                getTile(3, Tile.Kind.MENHIR),
                getTile(4, Tile.Kind.MENHIR),
                getTile(5, Tile.Kind.MENHIR));

        var mutS = new ArrayList<>(dS);
        var mutN = new ArrayList<>(dN);
        var mutM = new ArrayList<>(dM);

        return new TileDecks(mutS, mutN, mutM);
    }

    @Test
    void testWithStartingTilePlaced() {
        GameState gs0 = GameState.initial(List.of(PlayerColor.RED, PlayerColor.BLUE),
                getTileDecks(), getMessageBoard().textMaker());

        GameState gsE = new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE),
                getTileDecks(), getTile(0, Tile.Kind.NORMAL), Board.EMPTY, GameState.Action.PLACE_TILE,
                getMessageBoard());

        assertEquals(GameState.Action.START_GAME, gs0.nextAction());

        GameState gs1 = gs0.withStartingTilePlaced();

        assertNotNull(gs1.tileToPlace());
        assertEquals(GameState.Action.PLACE_TILE, gs1.nextAction());

        assertThrows(IllegalArgumentException.class, gsE::withStartingTilePlaced);
    }

    @Test
    void testWithPlacedTile() {

        GameState gs0 = GameState.initial(List.of(PlayerColor.RED, PlayerColor.BLUE),
                getTileDecks(), getMessageBoard().textMaker());

        PlacedTile normalTile = new PlacedTile(gs0.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1));

        PlacedTile menhirTile0 = new PlacedTile(gs0.tileDecks().menhirTiles().get(0), PlayerColor.RED,
                Rotation.RIGHT, new Pos(1, 0));
        PlacedTile menhirTile1 = new PlacedTile(gs0.tileDecks().menhirTiles().get(1), PlayerColor.BLUE,
                Rotation.HALF_TURN, new Pos(1, 1));
        PlacedTile menhirTile2 = new PlacedTile(gs0.tileDecks().menhirTiles().get(2), PlayerColor.RED,
                Rotation.LEFT, new Pos(0, 1));

        GameState gsN = gs0.withStartingTilePlaced()
                .withPlacedTile(normalTile);
        GameState gsM = gs0.withStartingTilePlaced()
                .withPlacedTile(menhirTile0)
                .withPlacedTile(menhirTile1)
                .withPlacedTile(menhirTile2);

        List<PlayerColor> shiftedPlayerList = new ArrayList<>(List.of(PlayerColor.BLUE, PlayerColor.RED));

        TileDecks updatedTileDecksN = gs0.tileDecks().withTopTileDrawn(Tile.Kind.NORMAL);
        Board updatedBoarN = gs0.board().withNewTile(normalTile);
        MessageBoard updatedMessageBoardN = gs0.messageBoard();

        TileDecks updatedTileDeckM = new TileDecks(gs0.tileDecks().startTiles(),
                List.of(),
                gs0.tileDecks().menhirTiles());
        Board updatedBoardM = gs0.board()
                .withNewTile(menhirTile0)
                .withNewTile(menhirTile1)
                .withNewTile(menhirTile2);
        MessageBoard updatedMessageBoardM = gs0.messageBoard().withClosedForestWithMenhir(PlayerColor.RED,
                gs0.withStartingTilePlaced().board().forestArea(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR)));

        //Test pour les différents cas de withPlacedTile:
        assertEquals(new GameState(shiftedPlayerList, updatedTileDecksN, null, updatedBoarN,
                        GameState.Action.OCCUPY_TILE, updatedMessageBoardN),
                gsN);
        assertEquals(new GameState(shiftedPlayerList, updatedTileDeckM, updatedTileDeckM.topTile(Tile.Kind.MENHIR),
                updatedBoardM, GameState.Action.PLACE_TILE, updatedMessageBoardM),
                gsM);
    }

    @Test
    void testWithOccupantRemoved() {
        // Test implementation here
    }

    @Test
    void testWithNewOccupant() {
        // Test implementation here
    }

    @Test
    void testCurrentPlayer() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        GameState gs2 = new GameState(players,
                new TileDecks(List.of(), List.of(), List.of()),
                null,
                Board.EMPTY,
                GameState.Action.END_GAME,
                gs0.messageBoard());

        assertEquals(PlayerColor.RED, gs1.currentPlayer());
        assertNull(gs0.currentPlayer());
        assertNull(gs2.currentPlayer());


    }

    @Test
    void testFreeOccupantsCount() {
        // Test implementation here
    }

    @Test
    void testLastTilePotentialOccupants() {
        // Test implementation here
    }

    // Additional test methods can be added as needed
}

