package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.StringTemplate.STR;

/**
 * Classe qui permet de générer tout le texte français nécessaire à l'interface graphique de ChaCuN
 *
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */
public final class TextMakerFr implements TextMaker {
    private final Map<PlayerColor, String> playerNamesAndColors;

    public TextMakerFr(Map<PlayerColor, String> playerNamesAndColors) {
        this.playerNamesAndColors = playerNamesAndColors;
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        Preconditions.checkArgument(playerNamesAndColors.containsKey(playerColor));
        return playerNamesAndColors.get(playerColor);
    }

    @Override
    public String points(int points) {
        return STR."\{points} point\{plurality(points, false)}";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerName(player)} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String mushroomMessage = mushroomGroupCount > 0
                ? " et de " + STR."\{mushroomGroupCount} groupe\{plurality(mushroomGroupCount, false)}"
                + STR."de champignons."
                : ".";
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant qu'occupant·e"
                + STR."\{plurality(scorers.size(), true)} majoritaires d'une forêt composée de "
                + STR."\{tiles(tileCount)}\{mushroomMessage}";
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String fishMessage = fishCount > 0
                ? STR." et contenant \{fishCount} poisson\{plurality(fishCount, false)}." : "";
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant qu'occupant·e"
                + STR."\{plurality(scorers.size(), true)} majoritaire d'une rivière composée de "
                + STR."\{tiles(tileCount)}\{fishMessage}";
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerName(scorer)} a remporté \{points(points)} "
                + STR."en plaçant la fosse à pieux dans un pré entourée \{animalMessage(animals)}.";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{playerName(scorer)} a remporté \{points(points)} en plaçant la pirogue dans un réseau "
                + STR."hydrographique contenant \{lakeCount} lac\{plurality(lakeCount, false)}.";
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant que qu'occupant·e"
                + STR."\{plurality(scorers.size(), true)} d'un pré contenant "
                + STR."\{organiseAnimalsAsString(animals)}.";
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant qu'occupant·e"
                + STR."\{plurality(scorers.size(), true)} d'un réseau hydrographique contenant "
                + STR."\{fishCount} poisson\{plurality(fishCount, false)}.";
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant qu'occupant·e"
                + STR."\{plurality(scorers.size(), true)} majoritaire"
                + STR."\{plurality(scorers.size(), false)} d'un pré contenant la grande fosse à pieux "
                + STR."entourée \{animalMessage(animals)}.";
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant qu'occupant·e"
                + STR."\{plurality(scorers.size(), true)} majoritaire"
                + STR."\{plurality(scorers.size(), false)} contenant le radeau et "
                + STR."\{lakeCount} lac\{plurality(lakeCount, false)}";
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{organisePlayersAsString(winners)} remporté la partie avec \{points(points)}!";
    }

    @Override
    public String clickToOccupy() {
        return "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    @Override
    public String clickToUnoccupy() {
        return "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }

    private String organisePlayersAsString(Set<PlayerColor> players) {
        Preconditions.checkArgument(!players.isEmpty());

        List<String> playerNames = players.stream()
                .sorted()
                .map(this::playerName)
                .toList();

        if (playerNames.size() == 1) {
            return STR."\{playerNames.getFirst()} a";
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < playerNames.size() - 1; i++) {
            joiner.add(playerNames.get(i));
        }
        return STR."\{joiner.toString()} et \{playerNames.getLast()} ont";
    }

    /**
     * Méthode qui génère une chaîne de caractères décrivant le nombre et le type d'animaux présents,
     * dans un ordre prédéfini : mammouths, aurochs, cerfs.
     *
     * @param animals
     *         table associative associant chaque type d'animal à son nombre.
     * @return une chaîne de caractères décrivant les animaux présents et leur quantité,
     * ou "aucun animal." si aucun animal n'est présent.
     */
    private String organiseAnimalsAsString(Map<Animal.Kind, Integer> animals) {
        // Association des types d'animaux à leur nom en français
        Map<Animal.Kind, String> animalsAsString = Map.of(
                Animal.Kind.MAMMOTH, "mammouth",
                Animal.Kind.AUROCHS, "aurochs",
                Animal.Kind.DEER, "cerf"
        );

        // Filtrage des animaux avec une quantité supérieure à 0 et exclusion des tigres. Ce sont les animaux qui
        // sont importants lors du comptage des points
        Map<Animal.Kind, Integer> filteredAnimals = animals.entrySet().stream()
                .filter(entry -> entry.getValue() > 0 && entry.getKey() != Animal.Kind.TIGER)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Aucun animal n'est présent
        if (filteredAnimals.isEmpty()) {
            return "aucun animal.";
        }

        // Si un seul type d'animal est présent, on aura un message spécial
        if (filteredAnimals.size() == 1) {
            Map.Entry<Animal.Kind, Integer> entry = filteredAnimals.entrySet().iterator().next();
            // "Aurochs" ne s'accorde pas au pluriel
            String plurality = entry.getKey() != Animal.Kind.AUROCHS
                    ? plurality(entry.getValue(), false) : "";
            return STR."\{entry.getValue()} \{animalsAsString.get(entry.getKey())}\{plurality}";
        }

        // Construction de la chaîne pour plusieurs types d'animaux
        StringJoiner joiner = new StringJoiner(", ");
        List<Map.Entry<Animal.Kind, Integer>> entries = new ArrayList<>(filteredAnimals.entrySet());
        for (int i = 0; i < entries.size() - 1; i++) {
            Map.Entry<Animal.Kind, Integer> entry = entries.get(i);
            String plurality = entry.getKey() != Animal.Kind.AUROCHS
                    ? plurality(entry.getValue(), false) : "";
            joiner.add(STR."\{entry.getValue()} \{animalsAsString.get(entry.getKey())}\{plurality}");
        }
        Map.Entry<Animal.Kind, Integer> lastEntry = entries.getLast();
        String lastPlurality = lastEntry.getKey() != Animal.Kind.AUROCHS
                ? plurality(lastEntry.getValue(), false) : "";
        return joiner + " et " + STR."\{lastEntry.getValue()} \{animalsAsString.get(lastEntry.getKey())}\{lastPlurality}";
    }


    private String animalMessage(Map<Animal.Kind, Integer> animals) {
        boolean animalPresence = animals.values().stream().anyMatch(count -> count > 0);
        String prefix = animalPresence ? "de " : "d'";
        return prefix + organiseAnimalsAsString(animals);
    }

    private String plurality(int count, boolean isGendered) {
        return count > 1 ? (isGendered ? "·s" : "s") : "";
    }

    private String tiles(int tileCount) {
        return STR."\{tileCount} tuile\{plurality(tileCount, false)}";
    }
}
