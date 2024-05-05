package ch.epfl.chacun;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contient des méthodes permettant d'encoder et de décoder des actions et les appliquer à un état de jeu
 *
 * @author Cyriac Philippe (360553)
 */
public class ActionEncoder {
    /**
     *
     *
     * @param initialGameState
     * @param tileToPlace
     * @return
     */
    public static Pair<GameState, String> withPlacedTile(GameState initialGameState, PlacedTile tileToPlace) {
        GameState currentGameState = initialGameState.withPlacedTile(tileToPlace);

        // Encodage de la pose d'une tuile
        // Index de position sur la frange de la tuile à placer
        int p = getIndexedFrange(initialGameState).get(tileToPlace.pos());
        // Entier correspondant à la rotation de la tuile à placer
        int r = tileToPlace.rotation().ordinal();
        // Concatenation des deux morceaux d'information sous la forme "ppppp ppprr"
        int n = (p << 2) + r;

        String code = Base32.encodeBits10(n);

        return new Pair<>(currentGameState, code);
    }

    public static Pair<GameState, String> withNewOccupant(GameState initialCameState, Occupant occupant) {
        GameState currentGameState = initialCameState.withNewOccupant(occupant);

        // Encodage de l'ajout d'un occupant
        // Type d'occupant
        int k = occupant.kind().ordinal();
        // Identifiant de la zone occupé
        int z = 0b11111;
        if (occupant != null) {
            z = occupant.zoneId();
        }
        // Concatenation sous forme "kzzzz"
        int n = (k << 4) + z;

        String code = Base32.encodeBits5(n);

        return new Pair<>(currentGameState, code);
    }

    public static Pair<GameState, String> withOccupantRemoved(GameState initialGameState, Occupant removedOccupant) {
        GameState currentGameState = initialGameState.withOccupantRemoved(removedOccupant);

        // Encodage de la reprise d'un pion
        int o = 0b11111;
        if (removedOccupant != null) {

            o = getIndexedOccupants(initialGameState).get(removedOccupant);
        }
        String code = Base32.encodeBits5(o);

        return new Pair<>(currentGameState, code);
    }

    //todo i don't understand what the fuck the "Conseils de programmation" is saying for this
    public static Pair<GameState, String> decodeAndApply(GameState initialGameSate, String initialCode) {
        if (Base32.isValid(initialCode)) {
            GameState.Action nextAction = initialGameSate.nextAction();

            GameState updatedGameState = initialGameSate;
            String updatedCode = initialCode;

            switch (nextAction) {
                case PLACE_TILE -> {
                    int decoded = Base32.decode(initialCode);
                    int p = decoded >>> 2;
                    int r = decoded & 0b11;

                    //todo make sure the list is in fact ordered the right way so as to get the proper indexed position
                    Pos tilePos = getIndexedFrange(initialGameSate).keySet().stream().toList().get(p);

                    updatedGameState = initialGameSate
                            .withPlacedTile(new PlacedTile(initialGameSate.tileToPlace(),
                                    initialGameSate.currentPlayer(),
                                    Arrays.stream(Rotation.values()).toList().get(r),
                                    tilePos));

                }
            }
            return new Pair<>(updatedGameState, updatedCode);
        }
        return null;
    }

    private static Map<Pos, Integer> getIndexedFrange(GameState gameState) {
        List<Pos> sortedPositions = gameState.board().insertionPositions().stream()
                .sorted(Comparator.comparing(Pos::x).thenComparing(Pos::y)).toList();

        return IntStream.range(0, sortedPositions.size())
                .boxed()
                .collect(Collectors.toMap(sortedPositions::get, i -> i));
    }

    private static Map<Occupant, Integer> getIndexedOccupants(GameState gameState) {
        List<Occupant> sortedPawns = gameState.board().occupants().stream()
                .sorted(Comparator.comparing(Occupant::zoneId)).toList();

        return IntStream.range(0, sortedPawns.size())
                .boxed()
                .collect(Collectors.toMap(sortedPawns::get, i -> i));
    }
}
