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
     *          et d'une chaîne de charactèrs représentant le code en base32 de l'ajout de la tuile
     */
    public static StateAction withPlacedTile(GameState initialGameState, PlacedTile tileToPlace) {
        GameState currentGameState = initialGameState.withPlacedTile(tileToPlace);

        // Encodage de la pose d'une tuile
        // Index de position sur la frange de la tuile à placer
        int p = getIndexedFringe(initialGameState).get(tileToPlace.pos());
        // Entier correspondant à la rotation de la tuile à placer
        int r = tileToPlace.rotation().ordinal();
        // Concatenation des deux morceaux d'information sous la forme "ppppp ppprr"
        int n = (p << 2) | r;

        String code = Base32.encodeBits10(n);

        return new StateAction(currentGameState, code);
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
    public static StateAction withNewOccupant(GameState initialCameState, Occupant occupant) {
        GameState currentGameState = initialCameState.withNewOccupant(occupant);

        // Encodage de l'ajout d'un occupant
        // Type d'occupant
        int k = 0b1;
        // Identifiant de la zone occupé
        int z = 0b01111;
        if (occupant != null) {
            k = occupant.kind().ordinal();
            z = occupant.zoneId() % 10;
        }
        // Concatenation sous forme "kzzzz"
        int n = (k << 4) | z;

        String code = Base32.encodeBits5(n);

        return new StateAction(currentGameState, code);
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
    public static StateAction withOccupantRemoved(GameState initialGameState, Occupant removedOccupant) {
        // On vérifie que l'occupant a retiré est un PION, ou est null
        // On vérifie que le code ne tente pas de retirer un occupant qui n'appartient pas au joueur courant
        Preconditions.checkArgument((removedOccupant.kind().equals(Occupant.Kind.PAWN)
                && initialGameState.board().tileWithId(removedOccupant.zoneId() % 10)
                .placer() == initialGameState.currentPlayer())
                || removedOccupant == null);

        GameState currentGameState = initialGameState.withOccupantRemoved(removedOccupant);

        // Encodage de la reprise d'un pion
        int o = 0b11111;
        if (removedOccupant != null) {
            o = getIndexedPawns(initialGameState).get(removedOccupant);
        }
        String code = Base32.encodeBits5(o);

        return new StateAction(currentGameState, code);
    }

    /**
     * Decode et applique une action passée en base32 à notre état de jeu
     *
     * @param initialGameSate
     *          état de jeu initial
     * @param code
     *          chaîne de charactèrs représentant une action en base32
     * @return une paire composée d'un nouvel état de jeu et le code base32 qu'on lui a appliqué,
     *         ou null si au cas où ces paramètres lancent une erreur à l'appel de decodeAndApplyThrows
     */
    public static StateAction decodeAndApply(GameState initialGameSate, String code) {
        try {
            return decodeAndApplyThrows(initialGameSate, code);
        }
        catch(IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Méthode d'aide qui gère les exceptions lors de l'appel de decodeAndApplyThrows
     *
     * @param initialGameState
     *          état de jeu initial
     * @param code
     *          chaîne de charactèrs correspondant au code en base32 de l'action à appliquer
     * @return une paire composée d'un nouvel état de jeu et le code base32 qu'on lui a appliqué
     *
     * @throws IllegalArgumentException
     *          si la chaîne de charactèrs en base32 passé en argument n'est pas valid
     *          si l'index de la tuile à placer ne fait pas partie de la frange
     *          si l'occupant qu'on souhaite placer ne fait pas partie des occupants potentiels
     *          si l'occupant qu'on souhaite retirer ne peux pas être retirée
     * @throws NullPointerException
     *          si la dernière tuile placée dans l'état de jeu est null
     */
    private static StateAction decodeAndApplyThrows(GameState initialGameState, String code) {
        Preconditions.checkArgument(Base32.isValid(code));

        GameState.Action nextAction = initialGameState.nextAction();

        GameState updatedGameState = initialGameState;

        int decoded = Base32.decode(code);

        switch (nextAction) {
            case PLACE_TILE -> {
                Preconditions.checkArgument(code.length() == 2);
                int p = decoded >>> 2;
                int r = decoded & 0b11;

                List<Pos> fringe = getIndexedFringe(initialGameState).keySet().stream().toList();

                // On vérifie que la position de à la tuile est bien compris dans la frange
                Preconditions.checkArgument(p <= fringe.size() - 1);

                Pos tilePos = fringe.get(p);

                updatedGameState = initialGameState
                        .withPlacedTile(new PlacedTile(initialGameState.tileToPlace(),
                                initialGameState.currentPlayer(),
                                Arrays.stream(Rotation.values()).toList().get(r),
                                tilePos));
            }
            case OCCUPY_TILE ->  {
                Preconditions.checkArgument(code.length() == 1);
                Occupant occupantToPlace = null;
                if (decoded != 0b11111) {
                    int k = decoded >>> 4;
                    int z = decoded & 0b1111;

                    occupantToPlace = new Occupant(Occupant.Kind.values()[k],
                            initialGameState.board().lastPlacedTile().id() * 10 + z);

                    // On vérifie que notre occupant peut bien être placé
                    Preconditions.checkArgument(initialGameState.board().lastPlacedTile().occupant() == null);
                }
                updatedGameState = initialGameState.withNewOccupant(occupantToPlace);
            }
            case RETAKE_PAWN -> {
                Preconditions.checkArgument(code.length() == 1);
                Occupant occupantToRemove = null;
                if (decoded != 0b11111) {
                    occupantToRemove = getIndexedPawns(initialGameState).get(decoded);
                }
                // Lance une "IllegalArgumentException" si l'occupant ne peut pas être retiré
                updatedGameState = initialGameState.withOccupantRemoved(occupantToRemove);
            }
        }
        return new StateAction(updatedGameState, code);
    }

    /**
     * Méthode d'aide qui permet d'obtenir la frange d'un état de jeu, triée et indexée
     *
     * @param gameState
     *          état de jeu dont on souhaite obtenir la frange
     * @return une table associant les positions comprises sur la frange à leur index selon l'ordre x, y
     */
    private static Map<Pos, Integer> getIndexedFringe(GameState gameState) {
        List<Pos> sortedPositions = gameState.board().insertionPositions().stream()
                .sorted(Comparator.comparing(Pos::y).thenComparing(Pos::x)).toList();

        return IntStream.range(0, sortedPositions.size())
                .boxed()
                .collect(Collectors.toMap(sortedPositions::get, i -> i));
    }

    /**
     * Méthode d'aide qui permet d'obtenir les pions d'un état de jeu, triée selon leur identifiant
     *
     * @param gameState
     *          état de jeu dont on souhaite obtenir les pions
     * @return une table associant les pions à leur index selon l'ordre des identifiants
     */
    private static List<Occupant> getIndexedPawns(GameState gameState) {
        return gameState.board().occupants().stream()
                .filter(occupant ->  occupant.kind().equals(Occupant.Kind.PAWN))
                .sorted(Comparator.comparing(Occupant::zoneId)).toList();
    }

    /**
     * Enregistrement représentant une paire composée de l'état du jeu résultant du code en base 32 auquel il est
     * associé
     *
     * @param gameState
     *          état de jeu résultant du code en base 32 auquel il est associé
     * @param base32Code
     *          chaîne de charactèrs représentant le code en base 32 qui a produit l'état de jeu auquel la chaîne est
     *          associée
     */
    public record StateAction(GameState gameState, String base32Code) {}
}
