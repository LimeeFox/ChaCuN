package ch.epfl.chacun;

import javafx.util.Pair;

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
        List<Pos> sortedPositions = initialGameState.board().insertionPositions().stream()
                .sorted(Comparator.comparing(Pos::x).thenComparing(Pos::y)).toList();

        Map<Pos, Integer> indexedFrange = IntStream.range(0, sortedPositions.size())
                .boxed()
                .collect(Collectors.toMap(sortedPositions::get, i -> i));

        // Index de position sur la frange de la tuile à placer
        int p = indexedFrange.get(tileToPlace.pos());
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
        int z = 0;
        if (occupant != null) {
            z = occupant.zoneId();
        } else {
            z = 0b11111;
        }
        // Concatenation sous forme "kzzzz"
        int n = (k << 4) + z;

        String code = Base32.encodeBits5(n);

        return new Pair<>(currentGameState, code);
    }

}
