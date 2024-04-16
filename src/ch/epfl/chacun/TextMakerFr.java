package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.StringTemplate.STR;

/**
 *
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

        return STR."\{organisePlayersAsString(scorers)} remporté \{points} en tant qu'occupant·e"
                + plurality(scorers.size()) + STR."majoritaires d'une forêt composée de \{tileCount}" + mushroomMessage;
    }

    /**
     * Affichage du nombre de points obtenus pour des joueurs donnés pour la fermeture d'un rivière
     * @param scorers
     *         les occupants majoritaires de la rivière
     * @param points
     *         les points remportés pour fermeture da la rivière
     * @param fishCount
     *         le nombre de poissons nageant dans la rivière ou les lacs adjacents
     * @param tileCount
     *         le nombre de tuiles qui constitue la rivière
     * @return une chaîne de charactèr indiquant les joueurs ayant remporté des points,
     *         le nombre de points remportés,
     *         les raisons pour lesquels les joueurs ont remporté des points
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String fishMessage = ".";

        if (fishCount > 0) {
            fishMessage = STR." et contenant \{fishCount} poisson" + plurality(fishCount);
        }
        return STR."\{organisePlayersAsString(scorers)} remporté \{points} en tant qu'occupant·e"
                + plurality(scorers.size()) + STR."composée de \{tileCount} tuiles"
                + fishMessage;
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
     * @return une chaîne de charactèrs indiquant le joueur ayant remporté des points,
     *         le nombre de points remportés par le joueur,
     *         le nombre d'animaux de châque type dans l'ordre mammouths, aurochs, cerfs
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{scorer} a remporté \{points} en plaçant la fosse à pieux dans un pré dans lequel elle est "
                + " entourée" + organiseAnimalsAsString(animals);
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
     * @return une chaîne de charactèrs indiquant le joueur ayant remporté des points,
     *         le nombre de points remportés par le joueur,
     *         le nombre de lacs ayant rapporté des points
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{scorer} a remporté \{points} en plaçant la pirogue dans un réseau hydrographique contenant "
                + STR."\{lakeCount} lac" + plurality(lakeCount);
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{organisePlayersAsString(scorers)} ";
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
    //todo much like the other private methods, check coherence with the public methods
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
            return STR."\{playerNames.getFirst()}a ";
        }

        /*
        Sinon, on construit la chaîne de characters de nom en faisant bien attention à lier les deux dernier noms
        d'un "et".
        */
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < playerNames.size() - 1; i++) {
            joiner.add(playerNames.get(i));
        }
        return STR."\{joiner} et \{playerNames.getLast()} ont";
    }

    /**
     * Méthode d'aide qui permet de créer une chaîne de charactèrs indiquant le nombre d'animaux d'une table d'animaux
     * donnée, selon l'ordre prédéfini : mammouths, aurochs, cerfs
     *
     * @param animals
     *          tableau associant des types d'animaux à leur quantité
     * @return une chaîne de charactèrs indiquant la présence d'animaux et leur quantité
     */
    //todo "what if the map contains tigers"? probably needs to be addressed
    //todo maybe some messages mention tigers, i would then add a boolean parameter withTigers which would then choose
    //todo to filter tigers
    private String organiseAnimalsAsString(Map<Animal.Kind, Integer> animals) {
        // On trie les animaux dans l'ordre et on enlève les types qui n'ont aucune présence dans la table associative
        // ainsi que les tigres (en principe, il ne sont pas compté)
        Map<Animal.Kind, Integer> filteredAnimals = new TreeMap<>();
        Arrays.stream(Animal.Kind.values()).forEach(animal -> {
            if (animals.getOrDefault(animal, 0) > 0 && animal != Animal.Kind.TIGER) {
                filteredAnimals.put(animal, animals.get(animal));
            }
        });

        // Si aucun animal n'est présent, on envoie le message suivant
        if (filteredAnimals.isEmpty()) {
            return "d'aucun animal.";
        }

        // On associe chaque type d'animal à son écriture en français
        Map<Animal.Kind, String> animalsAsString = Map.of(Animal.Kind.MAMMOTH, "mammouth",
                Animal.Kind.AUROCHS, "aurochs",
                Animal.Kind.DEER, "cerf");

        // Si notre table associative triée ne contient qu'un seul type d'animal, on envoie un message particulier
        if (animals.size() == 1) {
            Animal.Kind animalKind = (Animal.Kind) animals.keySet().toArray()[0];
            int animalCount = animals.get(animalKind);

            String plurality = "";
            if (animalKind != Animal.Kind.AUROCHS) plurality = "s";

            return STR."\{animalCount} \{animalsAsString.get(animalKind)}"
                    + plurality(animalCount);
        }

        // On construit une chaîne de charactèrs qui s'adapte aux nombres d'animaux
        StringBuilder animalMessage = new StringBuilder();

        // On construit notre chaîne de charactèrs à partir de notre table associative triée
        for (int i = 0; i < filteredAnimals.size(); i++) {
            Animal.Kind animalKind = Animal.Kind.values()[i];
            int animalCount = filteredAnimals.get(animalKind);

            // On vérifie le pluriel du type d'animal
            String plurality = "";
            // Le mot "aurochs" est invariable en français donc on ne lui ajoute pas de "s"
            if (animalKind != Animal.Kind.AUROCHS) {
                plurality = plurality(filteredAnimals.get(animalKind));
            }
            // On ajoute à notre bâtisseur les animaux qui présent dans la table associative triée
            animalMessage.append(STR."\{animalCount} \{animalsAsString.get(animalKind)}")
                    .append(plurality);
            // En principe, on sépare l'énumération des animaux par une virgule, sauf si l'animal en question est
            // l'avant-dernier de notre table associative triée, dans ce cas, on utilise un "et".
            if (i < filteredAnimals.size() - 2) {
                animalMessage.append(", ");
            } else {
                animalMessage.append("et ");
            }
        }

        return animalMessage.toString();
    }

    /** Méthode d'aide qui vérifie si une partie d'un message doit être écrite au pluriel
     *
     * @param count
     *          nombre de l'objet qu'on souhaite compter
     * @return la possibilité de mettre un mot au pluriel dans un message
     */
    private String plurality(int count) {
        if (count > 1) {
            return "·s ";
        }
        return "";
    }
}
