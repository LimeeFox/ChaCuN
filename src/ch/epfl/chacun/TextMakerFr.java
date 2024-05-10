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
        return STR."\{points} point\{plurality(points, false)}";
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
     * @return une chaîne de caractères indiquant les joueurs ayant remporté des points,
     *         le nombre de points remportés,
     *         les raisons pour lesquels les joueurs ont remporté des points
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String mushroomMessage = ".";

        // On vérifie si la forêt concernée par le message contient des groupes de champignons
        if (mushroomGroupCount > 0) {
            mushroomMessage = STR." et de \{mushroomGroupCount} groupe \{plurality(mushroomGroupCount, false)} de champignons.";
        }

        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} majoritaires d'une forêt composée de "
                + STR."\{tiles(tileCount)} \{mushroomMessage}";
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
     * @return une chaîne de caractère indiquant les joueurs ayant remporté des points,
     *         le nombre de points remportés,
     *         les raisons pour lesquels les joueurs ont remporté des points
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String fishMessage = "";

        if (fishCount > 0) {
            fishMessage = STR." et contenant \{fishCount} poisson\{plurality(fishCount, false)}.";
        }
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} composée de \{tiles(tileCount)}} "
                + STR."\{fishMessage}";
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
     *         le nombre de points remportés par le joueur,
     *         le nombre d'animaux de châque type dans l'ordre mammouths, aurochs, cerfs
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {

        return STR."\{scorer} a remporté \{points(points)} en plaçant la fosse à pieux dans un "
        + STR."pré dans lequel elle est entourée \{animalMessage(animals)}.";
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
     *         le nombre de points remportés par le joueur,
     *         le nombre de lacs ayant rapporté des points
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{scorer} a remporté \{points(points)} en plaçant la pirogue dans un réseau "
        + STR."hydrographique contenant \{lakeCount} lac\{plurality(lakeCount, false)}.";
    }

    /**
     * Affichage du nombre de points obtenus par les occupants majoritaires d'un pré
     * @param scorers
     *         les occupants majoritaires du pré
     * @param points
     *         les points remportés
     * @param animals
     *         les animaux présents dans le pré (sans ceux ayant été précédemment annulés)
     * @return une châine de caractères indiquant le joueur ayant remporté des points,
     *         le nombre de points remportés,
     *         le nombre d'animaux ayant rapporté des points
     */
    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant que "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} d'un pré contenant"
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
     * @return une chaîne de caractères indiquant les joueurs ayant remportés de points,
     *         le nombre de points remportés,
     *         le nombre de poissons ayant rapporté des points
     */
    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                + STR."qu'occupant·e\{plurality(scorers.size(), true)} d'un réseau hydrographique contenant "
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
     * @return une chaîne de caractères indiquant les joueurs ayant remportés de points,
     *         le nombre de points remportés,
     *         les animaux ayant rapporté des points
     */
    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)} en tant "
                    + STR."qu'occupant·e\{plurality(scorers.size(), true)} "
                    + STR."majoritaire\{plurality(scorers.size(), false)} d'un pré contenant la grande fosse "
                    + STR."à pieux entourée \{animalMessage(animals)}.";
    }

    /**
     *
     * @param scorers
     *         les occupants majoritaires du réseau hydrographique comportant le radeau
     * @param points
     *         les points remportés
     * @param lakeCount
     *         le nombre de lacs contenus dans le réseau hydrographique
     * @return
     */
    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{organisePlayersAsString(scorers)} remporté \{points(points)}"
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
     *         les points totaux remportés par les gagnants
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
     * Méthode d'aide qui permet de créer une chaîne de caractères contenant les joueurs concernées dans l'ordre RBGYP
     * et reliant les deux derniers joueurs d'un "et".
     *
     * @param players
     *          les joueurs concernés par la création de la chaîne de caractères, sans ordre précis (ne peut pas être
     *          vide)
     * @return une chaîne de caractères contenant l'énumération des joueurs dans un ordre précis et reliant les deux
     * derniers de la liste par "et".
     */
    private String organisePlayersAsString(Set<PlayerColor> players) {
        Preconditions.checkArgument(!players.isEmpty());

        // On organise les "players" selon l'ordre prédéfini RBGYP
        Stream<PlayerColor> sortedColors = new TreeSet<>(players).stream();

        // On associe chaque couleur à son joueur correspondant
        List<String> playerNames = sortedColors.map(this::playerName)
                .toList();

        // S'il y a moins de deux joueurs, on ne retourne que le nom de l'unique joueur concerné
        if (playerNames.size() == 1) {
            return STR."\{playerNames.getFirst()} a";
        }

        /*
        Sinon, on construit la chaîne de caractères de nom en faisant bien attention à lier les deux dernier noms
        d'un "et".
        */
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < playerNames.size() - 1; i++) {
            joiner.add(playerNames.get(i));
        }
        return STR."\{joiner} et \{playerNames.getLast()} ont";
    }

    /**
     * Méthode d'aide qui permet de créer une chaîne de caractères indiquant le nombre d'animaux d'une table d'animaux
     * donnée, selon l'ordre prédéfini : mammouths, aurochs, cerfs
     *
     * @param animals
     *          tableau associant des types d'animaux à leur quantité
     * @return une chaîne de caractères indiquant la présence d'animaux et leur quantité
     */
    private String organiseAnimalsAsString(Map<Animal.Kind, Integer> animals) {
        // On trie les animaux dans l'ordre et on enlève les types qui n'ont aucune présence dans la table associative
        // ainsi que les tigres (en principe, ils ne sont pas compté)
        Map<Animal.Kind, Integer> filteredAnimals = new TreeMap<>();
        Arrays.stream(Animal.Kind.values()).forEach(animal -> {
            if (animals.getOrDefault(animal, 0) > 0 && animal != Animal.Kind.TIGER) {
                filteredAnimals.put(animal, animals.get(animal));
            }
        });

        // Si aucun animal n'est présent, on envoie le message suivant
        if (filteredAnimals.isEmpty()) {
            return "aucun animal.";
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

            return STR."\{animalCount} \{animalsAsString.get(animalKind)} \{plurality}";
        }

        // On construit une chaîne de caractères qui s'adapte aux nombres d'animaux
        StringBuilder animalMessage = new StringBuilder();

        // On construit notre chaîne de caractères à partir de notre table associative triée
        for (int i = 0; i < filteredAnimals.size(); i++) {
            Animal.Kind animalKind = Animal.Kind.values()[i];
            int animalCount = filteredAnimals.get(animalKind);

            // On vérifie le pluriel du type d'animal
            String plurality = "";
            // Le mot "aurochs" est invariable en français donc on ne lui ajoute pas de "s"
            if (animalKind != Animal.Kind.AUROCHS) {
                plurality = plurality(filteredAnimals.get(animalKind), false);
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

    /**
     * Méthode d'aide qui permet de créer une chaîne de caractères indiquant le nombre d'animaux si cette chaîne est
     * précédé du mot "entourée"
     *
     * @param animals
     *          table associative associant les types d'animaux à leur nombre
     * @return une chaîne de caractères indiquant le nombre d'animaux
     *         précédée de "de" s'il y a au moins 1 animal,
     *          où précédée de "d'" s'il y en a "aucun"
     */
    private String animalMessage(Map<Animal.Kind, Integer> animals) {
        StringBuilder animalMessage = new StringBuilder();
        boolean animalPresence = false;
        for (Animal.Kind kind : animals.keySet()) {
            if (animals.getOrDefault(kind, 0) > 0 && !animalPresence) {
                animalPresence = true;
            }
        }
        // S'il y a des animaux on affiche "de [nombres d'animaux pour chaque type]"
        if (animalPresence) {
            animalMessage.append("de ");
        } else {
            // Sinon on affiche "d'aucun animal"
            animalMessage.append("d'");
        }
        return animalMessage.append(organiseAnimalsAsString(animals)).toString();
    }

    /** Méthode d'aide qui vérifie si une partie d'un message doit être écrite au pluriel
     *
     * @param count
     *          nombre de l'objet qu'on souhaite compter
     * @param isGendered
     *          indique si le la pluralité doit prendre en compte le format d'écriture inclusive
     * @return une chaîne de caractères ajoutant la lettre s si count est pluriel
     */
    private String plurality(int count, boolean isGendered) {
        StringBuilder builder = new StringBuilder();

        if (count > 1) {
            if (isGendered) builder.append("·");
            builder.append("s");
        }

        return builder.toString();
    }

    /**
     * Méthode d'aide qui affiche le nombre de tuiles pour un nombre donnée
     * 
     * @param tileCount
     *          nombre de tuiles à afficher
     * @return une chaîne de caractères indiquant le nombre de tuiles
     */
    private String tiles(int tileCount) {
        return STR."\{tiles(tileCount)} tuiles}";
    }
}
