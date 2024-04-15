package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.StringTemplate.STR;

/**
 *
 * @author Cyriac Philippe (360553)
 */
public final class TextMakerFr implements TextMaker{
    private final Map<PlayerColor, String> playerNamesAndColors;

    public TextMakerFr(Map<PlayerColor, String> playerNamesAndColors) {
        this.playerNamesAndColors = playerNamesAndColors;
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
        return STR."\{playerNamesAndColors.get(player)} a fermé une forêt contenant un menhir et peut donc placer une"
        + "tuile menhir.";
    }

    /**
     * Affichage du nombre de points obtenus pour la fermeture d'une forêt par les joueurs donnée
     *
     * @param scorers
     *         les occupants majoritaires de la forêt
     * @param points
     *         les points remportés pour la fermeture de la forêt
     * @param mushroomGroupCount
     *         le nombre de groupes de champignons que la forêt contient
     * @param tileCount
     *         le nombre de tuiles qui constitue la forêt
     * @return une chaîne de charactèrs indiquant les joueurs ayant remporté des points,
     *         le nombre de points remportés,
     *         les raisons pour lesquels les joueurs ont remporté des points
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String mushroomMessage = ".";

        // On vérifie si la forêt concernée par le message contient des groupes de champignons
        if (mushroomGroupCount > 0) {
            mushroomMessage = STR." et de \{mushroomMessage} groupe" + plurality(mushroomGroupCount)
                    + " de champignons.";
        }

        return STR."\{organisePlayersAsString(scorers)} ont remporté \{points} en tant qu'occupant·e"
                + plurality(scorers.size()) + STR."majoritaires d'une forêt composée de \{tileCount}" + mushroomMessage;
    }

    /**
     *
     * @param scorers
     *         les occupants majoritaires de la rivière
     * @param points
     *         les points remportés
     * @param fishCount
     *         le nombre de poissons nageant dans la rivière ou les lacs adjacents
     * @param tileCount
     *         le nombre de tuiles qui constitue la rivière
     * @return
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String fishMessage = ".";

        if (fishCount > 0) {
            fishMessage = STR." et contenant \{fishCount} poisson" + plurality(fishCount);
        }
        return STR."\{organisePlayersAsString(scorers)} "
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

    /**
     * Méthode d'aide qui permet de créer une chaîne de charactèrs contenant les joueurs concernées dans l'ordre RBGYP
     * et reliant les deux derniers joueurs d'un "et".
     *
     * @param players
     *          les joueurs concernés par la création de la chaîne de charactèrs, sans ordre précis (ne peut pas être
     *          vide)
     * @return une chaîne de charactèrs contenant l'énumération des joueurs dans un ordre précis et reliant les deux
     * derniers de la liste par "et".
     */
    private String organisePlayersAsString(Set<PlayerColor> players) {
        Preconditions.checkArgument(!players.isEmpty());

        // On organise les "players" selon l'ordre prédéfini RBGYP
        Stream<PlayerColor> sortedColors = Arrays.stream(PlayerColor.values())
                .filter(players::contains);

        // On associe chaque couleur à son joueur correspondant
        List<String> playerNames = sortedColors.map(this::playerName)
                .toList();

        // S'il y a moins de deux joueurs, on ne retourne que le nom de l'unique joueur concerné
        if (playerNames.size() == 1) {
            return playerNames.getFirst();
        }

        /*
        Sinon, on construit la chaîne de characters de nom en faisant bien attention à lier les deux dernier noms
        d'un "et".
        */
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < playerNames.size() - 1; i++) {
            joiner.add(playerNames.get(i));
        }
        return STR."\{joiner} et \{playerNames.getLast()}";
    }

    /** Méthode d'aide qui vérifie si une partie d'un message doit être écrite au pluriel
     *
     * @param count
     *          nombre de l'objet qu'on souhaite compter
     * @return la possibilité de mettre un mot au pluriel dans un message
     */
    private String plurality(int count) {
        if (count > 1) {
            return "·s";
        }
        return "";
    }
}
