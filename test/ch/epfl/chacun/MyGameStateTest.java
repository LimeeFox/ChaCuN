package ch.epfl.chacun;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        MessageBoard updatedMessageBoardM = gs0.messageBoard().withClosedForestWithMenhir(PlayerColor.RED, gs0.withStartingTilePlaced().board().forestArea(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR)));

        //Test pour les différents cas de withPlacedTile:
        assertEquals(new GameState(shiftedPlayerList, updatedTileDecksN, null, updatedBoarN, GameState.Action.OCCUPY_TILE, updatedMessageBoardN), gsN);
        assertEquals(new GameState(shiftedPlayerList, updatedTileDeckM, updatedTileDeckM.topTile(Tile.Kind.MENHIR),
                updatedBoardM, GameState.Action.PLACE_TILE, updatedMessageBoardM),
                gsM);
    }

    @Test
    void withOccupantRemoved_NullOccupant_StateUnchanged() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);
        Occupant occupant3 = new Occupant(Occupant.Kind.HUT,8);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires withPlacedTile() to function correctly
        GameState gs3 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                        Rotation.HALF_TURN, new Pos(0, -1)))
                .withNewOccupant(occupant3);

        // Initialize an occupant assuming a constructor exists. Adapt parameters as needed.

        assertNotNull(gs3.board().lastPlacedTile());
        assertEquals(-1, gs3.board().lastPlacedTile().idOfZoneOccupiedBy(occupant3.kind()));
    }

    @Test
    void withOccupantRemoved_normal_case() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN,0);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires withPlacedTile() to function correctly
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                        Rotation.HALF_TURN, new Pos(0, -1)))
                .withNewOccupant(occupant2);

        // Initialize an occupant assuming a constructor exists. Adapt parameters as needed.


        // Requires withNewOccupant to function correctly
        //GameState finalGameState2_PRE = gs2.withNewOccupant(occupant2);

        GameState finalGameState2 = gs2.withOccupantRemoved(occupant2);
        assertNotNull(finalGameState2.board().lastPlacedTile());
        assertEquals(-1, finalGameState2.board().lastPlacedTile().idOfZoneOccupiedBy(occupant2.kind()));
    }

    @Test
    void testWithOccupantRemoved_throws_IAE_when_NOTRetake_Pawn() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);
        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        GameState gs = new GameState(players, gs1.tileDecks(), getTile(5, Tile.Kind.NORMAL), gs1.board(), GameState.Action.PLACE_TILE, getMessageBoard());

        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN,10);

        assertThrows(IllegalArgumentException.class, () -> gs.withOccupantRemoved(occupant2));
    }

    @Test
    void testWithNewOccupant() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires completion of withPlacedTile() to function
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                        Rotation.HALF_TURN, new Pos(0, -1)))
                .withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        GameState expected = new GameState(players, getTileDecks(), getTileDecks().normalTiles().get(1),
                gs1.board().withNewTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                        Rotation.HALF_TURN, new Pos(0, -1))).withOccupant(new Occupant(Occupant.Kind.PAWN, 0)),
                GameState.Action.PLACE_TILE, gs1.messageBoard());

        assertEquals(expected, gs2);
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
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires completion of withPlacedTile() to function
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)))
                .withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        GameState gs3 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                        Rotation.HALF_TURN, new Pos(0, -1)))
                .withNewOccupant(new Occupant(Occupant.Kind.HUT, 8));

        int standardPawnCount = Occupant.occupantsCount(Occupant.Kind.PAWN);
        int standardHutCount = Occupant.occupantsCount(Occupant.Kind.HUT);

        assertEquals(standardPawnCount, gs1.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(standardHutCount, gs1.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.HUT));

        assertEquals(standardPawnCount - 1, gs2.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(standardHutCount - 1, gs3.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.HUT));

        assertThrows(IllegalArgumentException.class, () -> {
            gs1.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.PAWN);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            gs1.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.HUT);
        });

    }

    @Test
    void testLastTilePotentialOccupants_pawnless_player() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        Occupant occupant_pawn1 = new Occupant(Occupant.Kind.PAWN, 0);
        Occupant occupant_pawn2 = new Occupant(Occupant.Kind.PAWN, 1);
        Occupant occupant_pawn10 = new Occupant(Occupant.Kind.PAWN, 0);
        Occupant occupant_pawn11 = new Occupant(Occupant.Kind.PAWN, 1);
        Occupant occupant_pawn100 = new Occupant(Occupant.Kind.PAWN, 0);
        Occupant occupant_pawn110 = new Occupant(Occupant.Kind.PAWN, 1); // potential
        Occupant occupant_pawn1000 = new Occupant(Occupant.Kind.PAWN, 0); // potential
        Occupant occupant_pawn1100 = new Occupant(Occupant.Kind.PAWN, 1); // potential
        Occupant occupant_hut1 = new Occupant(Occupant.Kind.HUT, 8);
        Occupant occupant_hut2 = new Occupant(Occupant.Kind.HUT, 8);
        Occupant occupant_hut3 = new Occupant(Occupant.Kind.HUT, 8);
        Occupant occupant_hut4 = new Occupant(Occupant.Kind.HUT, 8);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();

        // Requires withPlacedTile() && withNewOccupant to function correctly
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(getTile(Tile.Kind.START), PlayerColor.RED,
                Rotation.RIGHT, new Pos(1, 0))).withNewOccupant(occupant_pawn10).withNewOccupant(occupant_pawn11);
        GameState gs3 = gs2.withPlacedTile(new PlacedTile(getTile(Tile.Kind.START), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(1, 1))).withNewOccupant(occupant_pawn100);
        GameState gs4 = gs3.withPlacedTile(new PlacedTile(getTile(Tile.Kind.START), PlayerColor.RED,
                Rotation.LEFT, new Pos(0, 1))).withNewOccupant(occupant_pawn1000).withNewOccupant(occupant_pawn1100);
        GameState gs5 = gs4.withPlacedTile(new PlacedTile(getTile(Tile.Kind.START), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));

        assertEquals(gs5.lastTilePotentialOccupants(), Set.of(occupant_hut1, occupant_hut2, occupant_hut3, occupant_hut4, occupant_pawn110, occupant_pawn1, occupant_pawn2));
    }

    @Test
    void testLastTilePotentialOccupants_hutless_player() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        Occupant occupant_pawn1 = new Occupant(Occupant.Kind.PAWN, 0);
        Occupant occupant_pawn2 = new Occupant(Occupant.Kind.PAWN, 1);
        Occupant occupant_pawn10 = new Occupant(Occupant.Kind.PAWN, 0);
        Occupant occupant_pawn11 = new Occupant(Occupant.Kind.PAWN, 1);
        Occupant occupant_pawn100 = new Occupant(Occupant.Kind.PAWN, 0);
        Occupant occupant_pawn110 = new Occupant(Occupant.Kind.PAWN, 1);
        Occupant occupant_pawn1000 = new Occupant(Occupant.Kind.PAWN, 0);
        Occupant occupant_pawn1100 = new Occupant(Occupant.Kind.PAWN, 1);
        Occupant occupant_hut1 = new Occupant(Occupant.Kind.HUT, 8);
        Occupant occupant_hut2 = new Occupant(Occupant.Kind.HUT, 8);
        Occupant occupant_hut3 = new Occupant(Occupant.Kind.HUT, 8);
        Occupant occupant_hut4 = new Occupant(Occupant.Kind.HUT, 8);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();

        // Requires withPlacedTile() && withNewOccupant to function correctly
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(getTile(Tile.Kind.START), PlayerColor.RED,
                Rotation.RIGHT, new Pos(1, 0))).withNewOccupant(occupant_hut2);
        GameState gs3 = gs2.withPlacedTile(new PlacedTile(getTile(Tile.Kind.START), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(1, 1))).withNewOccupant(occupant_hut3);
        GameState gs4 = gs3.withPlacedTile(new PlacedTile(getTile(Tile.Kind.START), PlayerColor.RED,
                Rotation.LEFT, new Pos(0, 1))).withNewOccupant(occupant_hut4);
        GameState gs5 = gs4.withPlacedTile(new PlacedTile(getTile(Tile.Kind.START), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));

        assertEquals(gs5.lastTilePotentialOccupants(), Set.of(occupant_pawn1, occupant_pawn2, occupant_pawn10, occupant_pawn11, occupant_pawn100, occupant_pawn110, occupant_pawn1000, occupant_pawn1100, occupant_hut1));
    }

    @Test
    void testLastTilePotentialOccupants_pawnsoOnly_unoccupied() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires withPlacedTile() to function correctly
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));

        PlacedTile lastPlacedTile = gs2.board().lastPlacedTile();
        Set<Occupant> actualPotentialOccupants = gs2.lastTilePotentialOccupants();
        Set<Occupant> expectedPotentialOccupants = lastPlacedTile.potentialOccupants(); // ignore the null warning, its not gonna happen cuz we added an initial tile

        assertEquals(expectedPotentialOccupants, actualPotentialOccupants);
    }

    /**
     * unfinished tests
     */
    /*
    void testLastTilePotentialOccupants_pawnsOnly_occupied() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires withPlacedTile() to function correctly
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));

        PlacedTile lastPlacedTile = gs2.board().lastPlacedTile();
        Set<Occupant> actualPotentialOccupants = gs2.lastTilePotentialOccupants();

        Set<Occupant> expectedPotentialOccupants = Set.of();

        GameState finalGameState2 = finalGameState2_PRE.withOccupantRemoved(occupant2);
    }
*/

    void testLastTilePotentialOccupants_Huts() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires withPlacedTile() to function correctly
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));
        //.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        GameState gs3 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));
        //.withNewOccupant(new Occupant(Occupant.Kind.HUT, 8));

        // Initialize an occupant assuming a constructor exists. Adapt parameters as needed.
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN,10);

        // Requires withNewOccupant to function correctly
        GameState finalGameState2_PRE = gs2.withNewOccupant(occupant2);

        GameState finalGameState2 = finalGameState2_PRE.withOccupantRemoved(occupant2);
    }

    void testLastTilePotentialOccupants_MIXED() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires withPlacedTile() to function correctly
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));
        //.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        GameState gs3 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));
        //.withNewOccupant(new Occupant(Occupant.Kind.HUT, 8));

        // Initialize an occupant assuming a constructor exists. Adapt parameters as needed.
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN,10);

        // Requires withNewOccupant to function correctly
        GameState finalGameState2_PRE = gs2.withNewOccupant(occupant2);

        GameState finalGameState2 = finalGameState2_PRE.withOccupantRemoved(occupant2);
    }

    void testLastTilePotentialOccupants_None() {
        List<PlayerColor> players = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);

        GameState gs0 = GameState.initial(players, getTileDecks(), getMessageBoard().textMaker());
        GameState gs1 = gs0.withStartingTilePlaced();
        //Requires withPlacedTile() to function correctly
        GameState gs2 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));
        //.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        GameState gs3 = gs1.withPlacedTile(new PlacedTile(gs1.tileDecks().topTile(Tile.Kind.NORMAL), PlayerColor.RED,
                Rotation.HALF_TURN, new Pos(0, -1)));
        //.withNewOccupant(new Occupant(Occupant.Kind.HUT, 8));

        // Initialize an occupant assuming a constructor exists. Adapt parameters as needed.
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN,10);

        // Requires withNewOccupant to function correctly
        GameState finalGameState2_PRE = gs2.withNewOccupant(occupant2);

        GameState finalGameState2 = finalGameState2_PRE.withOccupantRemoved(occupant2);
    }

    // Additional test methods can be added as needed
}

