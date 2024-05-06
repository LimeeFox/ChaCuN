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
     * Ajoute une tuile à notre état de jeu et encode cette action
     *
     * @param initialGameState
     *          état de jeu initial, avant l'ajout de la tuile
     * @param tileToPlace
     *          tuile que l'on souhaite ajouter à l'état de jeu
     * @return  une paire composée d'un nouvel état de jeu contenant le tuile ajouté,
     *          et d'une chaîne de charactèrs représentant le code en base32 de l'ajout de le tuile
     */
    public static Pair<GameState, String> withPlacedTile(GameState initialGameState, PlacedTile tileToPlace) {
        GameState currentGameState = initialGameState.withPlacedTile(tileToPlace);

        // Encodage de la pose d'une tuile
        // Index de position sur la frange de la tuile à placer
        int p = getIndexedFringe(initialGameState).get(tileToPlace.pos());
        // Entier correspondant à la rotation de la tuile à placer
        int r = tileToPlace.rotation().ordinal();
        // Concatenation des deux morceaux d'information sous la forme "ppppp ppprr"
        int n = (p << 2) + r;

        String code = Base32.encodeBits10(n);

        return new Pair<>(currentGameState, code);
    }

    /**
     * Ajoute un occupant à notre état de jeu et encode cette action
     *
     * @param initialCameState
     *          état de jeu initial, avant l'ajout de l'occupant
     * @param occupant
     *          occupant que l'on souhaite ajouter à l'état de jeu
     * @return une paire composée d'un nouvel état de jeu avec l'occupant ajouté,
     *         et d'une chaîne de charactèrs représentant le code en base32 de l'ajout de l'occupant
     */
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

    /**
     * Retire un pion de notre état de jeu et encode de cette action
     *
     * @param initialGameState
     *          état de jeu initial, avant la reprise de la tuile
     * @param removedOccupant
     *          occupant que l'on souhaite réprendre à l'état de jeu
     * @return une paire composée du nouvel état de jeu avec l'occupant retiré,
     *         et d'une chaîne de charactèrs représentant le code en base32 de la reprise du pion
     */
    //todo what to do if removedOccupant is a hut?
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

    //todo use cannotDecode in case the code cannot be run normally (throw exception)
    public static Pair<GameState, String> decodeAndApply(GameState initialGameSate, String initialCode) {
        if (Base32.isValid(initialCode)) {
            GameState.Action nextAction = initialGameSate.nextAction();

            GameState updatedGameState = initialGameSate;
            String updatedCode = initialCode;

            switch (nextAction) {
                //todo check if tile position is indeed on fringe
                case PLACE_TILE -> {
                    int decoded = Base32.decode(initialCode);
                    int p = decoded >>> 2;
                    int r = decoded & 0b11;

                    //todo make sure the list is in fact ordered the right way so as to get the proper indexed position
                    Pos tilePos = getIndexedFringe(initialGameSate).keySet().stream().toList().get(p);

                    updatedGameState = initialGameSate
                            .withPlacedTile(new PlacedTile(initialGameSate.tileToPlace(),
                                    initialGameSate.currentPlayer(),
                                    Arrays.stream(Rotation.values()).toList().get(r),
                                    tilePos));

                }
                //todo check if zone can be occupied (in general find possible error case and check for it
                case OCCUPY_TILE ->  {

                }
                //todo check if pawn can be retaken
                case RETAKE_PAWN -> {

                }
            }
            return new Pair<>(updatedGameState, updatedCode);
        }
        return null;
    }

    //todo ask about an example for the "fringe order"
    private static Map<Pos, Integer> getIndexedFringe(GameState gameState) {
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

    //todo this method will take care of throwing exceptions (probably)
    private static void cannotDecode() {

    }
}
