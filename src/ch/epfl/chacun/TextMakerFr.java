package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Cyriac Philippe (360553)
 */
public final class TextMakerFr implements TextMaker{
    private SortedMap<PlayerColor, String> playerNamesAndColors;

    public TextMakerFr(Map<PlayerColor, String> playerNamesAndColors) {
        this.playerNamesAndColors = Arrays.stream(PlayerColor.values())
                .collect(Collectors.toMap(
                        color -> color,
                        playerNamesAndColors::get,
                        (oldValue, newValue) -> oldValue,
                        TreeMap::new
                ));
    }

    /**
     *
     * @param playerColor
     *         la couleur du joueur dont on cherche le nom
     * @return le nom du joueur associé à la couleur donnée
     */
    @Override
    public String playerName(PlayerColor playerColor) {
        Preconditions.checkArgument(playerNamesAndColors.containsKey(playerColor));
        return playerNamesAndColors.get(playerColor);
    }

    @Override
    public String points(int points) {
        return Integer.toString(points);
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return playerName(player)
                + " a fermé une forêt un menhir et peut donc placer une tuile menhir.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        Set<PlayerColor> orderedScorers = new TreeSet<>();

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
}
