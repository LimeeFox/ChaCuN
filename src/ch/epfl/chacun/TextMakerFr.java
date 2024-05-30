package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Nom de joueur associé à une couleur donnée
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

    /**
     * Retourne une chaîne de caractères indiquant le nombre de points.
     *
     * @param points
     *         le nombre de points
     * @return une chaîne de caractères indiquant le nombre de points
     */
    @Override
    public String points(int points) {
        return STR."\{points} point\{plurality(points, false)}";
    }

    /**
     * Retourne une chaîne de caractères indiquant qu'un joueur a fermé une forêt contenant un menhir.
     *
     * @param player
     *         le joueur qui a fermé la forêt
     * @return une chaîne de caractères indiquant la fermeture d'une forêt avec un menhir par le joueur
     */
    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerName(player)} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
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
     * @return une chaîne de caractères indiquant les joueurs ayant remporté des points,
     * le nombre de points remportés,
     * les raisons pour lesquels les joueurs ont remporté des points
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String mushroomMessage = mushroomGroupCount > 0
                ? " et de "
                + STR."\{mushroomGroupCount} groupe\{plurality(mushroomGroupCount, false)} de champignons."
                : ".";
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} "
                + STR."majoritaire\{plurality(scorers.size(), false)} d'une forêt composée de "
                + STR."\{tiles(tileCount)}\{mushroomMessage}";
    }

    /**
     * Affichage du nombre de points obtenus pour des joueurs donnés pour la fermeture d'une rivière
     *
     * @param scorers
     *         les occupants majoritaires de la rivière
     * @param points
     *         les points remportés pour fermeture da la rivière
     * @param fishCount
     *         le nombre de poissons nageant dans la rivière ou les lacs adjacents
     * @param tileCount
     *         le nombre de tuiles qui constitue la rivière
     * @return une chaîne de caractères indiquant les joueurs ayant remporté des points,
     * le nombre de points remportés,
     * les raisons pour lesquels les joueurs ont remporté des points
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String fishMessage = fishCount > 0
                ? STR." et contenant \{fishCount} poisson\{plurality(fishCount, false)}." : "";
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} "
                + STR."majoritaire\{plurality(scorers.size(), false)} d'une rivière composée de "
                + STR."\{tiles(tileCount)}\{fishMessage}";
    }

    /**
     * Affichage du nombre de points obtenus lors de la pose de la fosse à pieux par un joueur donné
     *
     * @param scorer
     *         le joueur ayant posé la fosse à pieux
     * @param points
     *         les points remportés pour la pose de la fosse à pieux
     * @param animals
     *         les animaux présents dans le même pré que la fosse à pieux et sur les 8 tuiles voisines
     * @return une chaîne de caractères indiquant le joueur ayant remporté des points,
     * le nombre de points remportés par le joueur,
     * le nombre d'animaux de châque type dans l'ordre mammouths, aurochs, cerfs
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerName(scorer)} a remporté \{points(points)} "
                + STR."en plaçant la fosse à pieux dans un pré entourée \{animalMessage(animals)}.";
    }

    /**
     * Affichage du nombre de points obtenus lors de la pose de la pirogue par un joueur donné
     *
     * @param scorer
     *         le joueur ayant déposé la pirogue
     * @param points
     *         les points remportés pour la pose de la pirogue
     * @param lakeCount
     *         le nombre de lacs accessibles à la pirogue
     * @return une chaîne de caractères indiquant le joueur ayant remporté des points,
     * le nombre de points remportés par le joueur,
     * le nombre de lacs ayant rapporté des points
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{playerName(scorer)} a remporté \{points(points)} en plaçant la pirogue dans un réseau "
                + STR."hydrographique contenant \{lakeCount} lac\{plurality(lakeCount, false)}.";
    }

    /**
     * Affichage du nombre de points obtenus par les occupants majoritaires d'un pré
     *
     * @param scorers
     *         les occupants majoritaires du pré
     * @param points
     *         les points remportés
     * @param animals
     *         les animaux présents dans le pré (sans ceux ayant été précédemment annulés)
     * @return une chaîne de caractères indiquant le joueur ayant remporté des points,
     * le nombre de points remportés,
     * le nombre d'animaux ayant rapporté des points
     */
    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} "
                + STR."majoritaire\{plurality(scorers.size(), false)} d'un pré contenant "
                + STR."\{organiseAnimalsAsString(animals)}.";
    }

    /**
     * Affichage du nombre de points obtenus par les occupants majoritaires d'un réseau hydrographique
     *
     * @param scorers
     *         les occupants majoritaires du réseau hydrographique
     * @param points
     *         les points remportés
     * @param fishCount
     *         le nombre de poissons nageant dans le réseau hydrographique
     * @return une chaîne de caractères indiquant les joueurs ayant remporté de points,
     * le nombre de points remportés,
     * le nombre de poissons ayant rapporté des points
     */
    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} "
                + STR."majoritaire\{plurality(scorers.size(), false)} d'un réseau hydrographique contenant "
                + STR."\{fishCount} poisson\{plurality(fishCount, false)}.";
    }

    /**
     * Affichage du nombre de points obtenus par les occupants majoritaires d'un pré contenant la grande fosse à pieux
     *
     * @param scorers
     *         les occupants majoritaires du pré contenant la fosse à pieux
     * @param points
     *         les points remportés
     * @param animals
     *         les animaux présents sur les tuiles voisines de la fosse (sans ceux ayant été précédemment annulés)
     * @return une chaîne de caractères indiquant les joueurs ayant remporté de points,
     * le nombre de points remportés,
     * les animaux ayant rapporté des points
     */
    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} "
                + STR."majoritaire\{plurality(scorers.size(), false)} d'un pré contenant la grande "
                + STR."fosse à pieux entourée \{animalMessage(animals)}.";
    }

    /**
     * Affichage du nombre de points obtenus pour le placement du radeau
     *
     * @param scorers
     *         les occupants majoritaires du réseau hydrographique comportant le radeau
     * @param points
     *         les points remportés
     * @param lakeCount
     *         le nombre de lacs contenus dans le réseau hydrographique du radeau
     * @return une chaîne de caractères indiquant les joueurs ayant marqué des points,
     * le nombre de points remportés
     */
    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} "
                + STR."majoritaire\{plurality(scorers.size(), false)} contenant le radeau et "
                + STR."\{lakeCount} lac\{plurality(lakeCount, false)}";
    }

    /**
     * Affichage des gagnants de la partie
     *
     * @param winners
     *         l'ensemble des joueurs ayant remporté la partie
     * @param points
     *         les points des vainqueurs
     * @return une chaîne de caractères indiquant les joueurs gagnants de la partie,
     * les points totaux remportés par les gagnants
     */
    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{organisePlayersAsString(winners)} remporté la partie avec \{points(points)}!";
    }

    /**
     * Affichage du message pour indiquer au joueur courant qu'il peut placer un occupant
     *
     * @return une chaîne de caractères indiquant la possibilité de placer un pion ou une hutte
     */
    @Override
    public String clickToOccupy() {
        return "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    /**
     * Affichage d'un message pour indiquer au joueur courant qu'il peut retirer un occupant
     *
     * @return une chaîne de caractères indiquant la possibilité de retirer un pion
     */
    @Override
    public String clickToUnoccupy() {
        return "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }

    /**
     * Méthode d'aide qui permet de créer une chaîne de caractères contenant les joueurs concernés dans l'ordre RBGYP
     * et reliant les deux derniers joueurs d'un "et".
     *
     * @param players
     *         les joueurs concernés par la création de la chaîne de caractères, sans ordre précis (ne peut pas être
     *         vide)
     * @return une chaîne de caractères contenant l'énumération des joueurs dans un ordre précis et reliant les deux
     * derniers de la liste par "et".
     */
    private String organisePlayersAsString(Set<PlayerColor> players) {
        if(players.isEmpty()) {
            return "Personne n'a";
        }

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

    /**
     * Méthode d'aide qui permet de créer une chaîne de caractères indiquant le nombre d'animaux si cette chaîne est
     * précédée du mot "entourée"
     *
     * @param animals
     *         table associative associant les types d'animaux à leur nombre
     * @return une chaîne de caractères indiquant le nombre d'animaux
     * précédée de "de" s'il y a au moins 1 animal,
     * ou précédée de "d'" s'il n'y en a aucun
     */
    private String animalMessage(Map<Animal.Kind, Integer> animals) {
        boolean animalPresence = animals.values().stream().anyMatch(count -> count > 0);
        String prefix = animalPresence ? "de " : "d'";
        return prefix + organiseAnimalsAsString(animals);
    }

    /**
     * Méthode d'aide qui vérifie si une partie d'un message doit être écrite au pluriel
     *
     * @param count
     *         nombre de l'objet qu'on souhaite compter
     * @param isGendered
     *         indique si la pluralité doit prendre en compte le format d'écriture inclusive
     * @return une chaîne de caractères ajoutant la lettre "s" si count est pluriel
     */
    private String plurality(int count, boolean isGendered) {
        return count > 1 ? (isGendered ? "·s" : "s") : "";
    }

    /**
     * Méthode d'aide qui affiche le nombre de tuiles pour un nombre donné
     *
     * @param tileCount
     *         nombre de tuiles à afficher
     * @return une chaîne de caractères indiquant le nombre de tuiles
     */
    private String tiles(int tileCount) {
        return STR."\{tileCount} tuile\{plurality(tileCount, false)}";
    }
}
