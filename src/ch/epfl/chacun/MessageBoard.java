package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contenu du tableau d'affichage
 *
 * @param textMaker
 *         permet d'obtenir le texte des différents messages
 * @param messages
 *         liste des messages affichés sur le tableau, du plus ancien au plus récent
 * @author Cyriac Philippe (360553)
 * @author Vladislav Yarkovoy (362242)
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    /**
     * Constructeur garantissant l'immuabilité de la classe
     *
     * @param textMaker
     *          créateur de text qui affiche les messages selon un format défini
     * @param messages
     *          liste des messages contenus dans le tableau d'affichage
     */
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    /**
     * Table associant à chaque joueur ayant obtenu des points, le nombre de points obtenus
     *
     * @return pointsMap
     * table associant les joueurs à leur nombre de points obtenus
     */
    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> playerPointsMap = new HashMap<>();
        for (Message message : messages) {
            if (message.points > 0) {
                for (PlayerColor scorerColor : message.scorers) {
                    playerPointsMap.put(scorerColor,
                            playerPointsMap.getOrDefault(scorerColor, 0) + message.points);
                }
            }
        }
        return playerPointsMap;
    }

    /**
     * Mise à jour du tableau suite à la fermeture d'une forêt
     *
     * @param forest
     *         forêt fermée par un joueur
     * @return un tableau de message contenant un nouveau message pour les joueurs ayant remporté des points ou
     * un tableau identique au récepteur si la forêt est inoccupée
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        if (forest.isOccupied()) {
            final Set<Integer> forestIds = forest.tileIds();
            final Set<PlayerColor> majorityOccupants = forest.majorityOccupants();
            final int mushroomCount = Area.mushroomGroupCount(forest);
            final int tileCount = forestIds.size();
            final int points = Points.forClosedForest(tileCount, mushroomCount);

            return withNewMessage(new Message(textMaker.playersScoredForest(majorityOccupants, points, mushroomCount, tileCount),
                    points,
                    majorityOccupants,
                    forestIds));
        }
        return this;
    }

    /**
     * Mise à jour du tableau suite à la fermeture d'une forêt contenant un menhir
     *
     * @param player
     *         joueur ayant fermé la forêt contenant un menhir
     * @param forest
     *         aire de type forêt qui a été fermée
     * @return un tableau d'affichage avec un nouveau message indiquant au joueur qu'il peut jouer un second tour
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        return withNewMessage(new Message(textMaker.playerClosedForestWithMenhir(player),
                0, forest.majorityOccupants(), forest.tileIds()));
    }

    /**
     * Mise à jour du tableau suite à la fermeture d'une rivière
     *
     * @param river
     *         rivière fermée par un joueur
     * @return un tableau de message contenant un nouveau message pour les joueurs ayant remporté des points ou
     * un tableau identique au récepteur si la rivière est inoccupée
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        if (river.isOccupied()) {
            final int fishCount = Area.riverFishCount(river);
            final int tileCount = river.tileIds().size();
            final Set<PlayerColor> majorityOccupants = river.majorityOccupants();
            final int pointsForClosedRiver = Points.forClosedRiver(tileCount, fishCount);

            return withNewMessage(new Message(textMaker.playersScoredRiver(majorityOccupants,
                    pointsForClosedRiver, fishCount, tileCount),
                    pointsForClosedRiver, majorityOccupants, river.tileIds()));
        }
        return this;
    }

    /**
     * Mise à jour du tableau d'affichage quand un joueur pose une fosse à pieux
     *
     * @param scorer
     *         joueur qui a posé la fosse à pieux et, en principe, remporte des points
     * @param adjacentMeadow
     *         ensemble des prés adjacents à la fosse à pieux
     * @return un tableau d'affichage identique au récepteur, à moins que le joueur remporte des points en posant
     * sa fosse
     */
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
        Map<Animal.Kind, Integer> animalMap = new HashMap<>();
                adjacentMeadow.zones().forEach(meadow -> {
            meadow.animals().forEach(animal -> animalMap.put(animal.kind(),
                    animalMap.getOrDefault(animal.kind(), 0) + 1));
        });

        final int scoredPoints = Points.forMeadow(animalMap.getOrDefault(Animal.Kind.MAMMOTH, 0),
                animalMap.getOrDefault(Animal.Kind.AUROCHS, 0),
                animalMap.getOrDefault(Animal.Kind.DEER, 0));

        if (scoredPoints > 0) {
            return withNewMessage(new Message(textMaker.playerScoredHuntingTrap(scorer,
                    scoredPoints, animalMap),
                    scoredPoints, Set.of(scorer), adjacentMeadow.tileIds()));
        }
        return this;
    }

    /**
     * Mise à jour du tableau d'affichage quand un joueur pose la pirogue dans le réseau hydrographique donnée
     *
     * @param scorer
     *         joueur qui a posé la pirogue et qui remporte donc des points
     * @param riverSystem
     *         réseau hydrographique où la pirogue a été posée
     * @return un tableau d'affichage identique au récepteur, mais avec un nouveau message signalant au joueur donné
     * qu'il a remporté les points correspondants
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        final int lakeCount = Area.lakeCount(riverSystem);
        final int pointsForLogboat = Points.forLogboat(lakeCount);

        return withNewMessage(new Message(textMaker.playerScoredLogboat(scorer,
                pointsForLogboat, lakeCount),
                pointsForLogboat, Set.of(scorer), riverSystem.tileIds()));
    }

    /**
     * Mise à jour du tableau pour un pré fermé
     *
     * @param meadow
     *         zones de type pré qui ont été fermées et qui rapportent ainsi des points
     * @param cancelledAnimals
     *         animaux qui ont déjà été comptabilisés dans le calcul des points et qui ne seront donc pas pris en
     *         compte pour ce pré
     * @return un tableau d'affichage identique au récepteur avec un nouveau message indiquant les nombre de points
     * remportés aux joueurs concernés, à moins que le pré soit inoccupé ou qu'il ne rapporte pas de points
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        if (meadow.isOccupied()) {
            Map<Animal.Kind, Integer> animalIntegerMap = animalMap(meadow, cancelledAnimals);

            int scoredPoints = Points.forMeadow(
                    animalIntegerMap.getOrDefault(Animal.Kind.MAMMOTH, 0),
                    animalIntegerMap.getOrDefault(Animal.Kind.AUROCHS, 0),
                    animalIntegerMap.getOrDefault(Animal.Kind.DEER, 0));

            if (scoredPoints > 0) {
                final Set<PlayerColor> majorityOccupants = meadow.majorityOccupants();

                return withNewMessage(new Message(textMaker.playersScoredMeadow(majorityOccupants,
                        scoredPoints, animalIntegerMap),
                        scoredPoints, majorityOccupants, meadow.tileIds()));
            }
        }
        return this;
    }

    /**
     * Méthode d'aide qui sert à compter les animaux par leur type
     *
     * @param meadow
     *         le pré dans lequel on compte les animaux
     * @param cancelledAnimals
     *         les animaux annulés, si il y en a
     * @return une Map où chaque type d'animal a un nombre d'occurrences qui lui est attribué
     */
    private Map<Animal.Kind, Integer> animalMap(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> validAnimals = Area.animals(meadow, cancelledAnimals);

        Map<Animal.Kind, Integer> animalIntegerMap = new HashMap<>();
        Map<Animal.Kind, Long> kindCount = validAnimals.stream()
                .collect(Collectors.groupingBy(Animal::kind, Collectors.counting()));

        kindCount.forEach((kind, count) -> animalIntegerMap.merge(kind, count.intValue(), Integer::sum));
        return animalIntegerMap;
    }

    /**
     * Mise à jour du tableau pour un système hydrographique qui apporte des points
     *
     * @param riverSystem
     *         réseau hydrographique donnée, en occurrence, qui rapporte des points
     * @return un tableau d'affichage qui contient un nouveau message indiquant les points remportés aux joueurs
     * concernés, à moins que le système hydrographique donné soit inoccupée ou ne rapporte pas de points
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        if (riverSystem.isOccupied()) {
            final int fishCount = Area.riverSystemFishCount(riverSystem);
            int scoredPoints = Points.forRiverSystem(fishCount);

            if (scoredPoints > 0) {
                final Set<PlayerColor> majorityOccupants = riverSystem.majorityOccupants();

                return withNewMessage(new Message(textMaker.playersScoredRiverSystem(majorityOccupants,
                        scoredPoints, fishCount),
                        scoredPoints, majorityOccupants, riverSystem.tileIds()));
            }
        }
        return this;
    }

    /**
     * Mise à jour du tableau d'affichage pour une grande fosse à pieux
     *
     * @param adjacentMeadow
     *         aire de prés adjacents à la grande fosse à pieux
     * @param cancelledAnimals
     *         animaux à ne pas prendre en compte pour le calcul des points (déjà comptés)
     * @return un tableau d'affichage identique au récepteur contentant un nouveau message indiquant les joueurs
     * ayant rapporté des points
     */
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {
        if (adjacentMeadow.isOccupied()) {
            Map<Animal.Kind, Integer> animalIntegerMap = animalMap(adjacentMeadow, cancelledAnimals);

            final int scoredPoints = Points.forMeadow(
                    animalIntegerMap.getOrDefault(Animal.Kind.MAMMOTH, 0),
                    animalIntegerMap.getOrDefault(Animal.Kind.AUROCHS, 0),
                    animalIntegerMap.getOrDefault(Animal.Kind.DEER, 0));

            if (scoredPoints > 0) {
                final Set<PlayerColor> majorityOccupants = adjacentMeadow.majorityOccupants();

                return withNewMessage(new Message(textMaker.playersScoredPitTrap(majorityOccupants,
                        scoredPoints, animalIntegerMap),
                        scoredPoints, majorityOccupants, adjacentMeadow.tileIds()));
            }
        }
        return this;
    }

    /**
     * Mise à jour du tableau d'affichage pour un radeau
     *
     * @param riverSystem
     *         réseau hydrographique do
     * @return un tableau d'affichage identique au récepteur avec un nouveau message signalant aux joueurs avec occupants
     * majoritaires le nombre de points remportés, à moins que le réseau hydrographique soit vide
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        if (riverSystem.isOccupied()) {
            final int lakeCount = Area.lakeCount(riverSystem);
            final Set<PlayerColor> majorityOccupants = new HashSet<>(riverSystem.majorityOccupants());

            final int scoredPoints = Points.forRaft(Area.lakeCount(riverSystem));

            return withNewMessage(new Message(textMaker.playersScoredRaft(majorityOccupants,
                    scoredPoints, lakeCount),
                    scoredPoints, majorityOccupants, riverSystem.tileIds()));
        }
        return this;
    }

    /**
     * Message de fin de partie pour signaler les gagnants
     *
     * @param winners
     *         ensemble des joueurs qui ont gagné la partie
     * @param points
     *         nombre de points remportés par les gagnants
     * @return un tableau d'affichage identique au récepteur avec un nouveau message signalant leur victoire aux
     * joueurs qui ont remporté la partie et leur nombre de points remportés
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        return withNewMessage(new Message(textMaker.playersWon(winners, points),
                0, Set.of(), Set.of()));
    }

    /**
     * Méthode d'aide qui permet de créer un nouveau tableau d'affichage avec un nouveau message
     *
     * @param message
     *          message à ajouter à l'instance
     * @return un tableau d'affichage similaire à l'instance mais avec l'ajout du nouveau message
     */
    private MessageBoard withNewMessage(Message message) {
        List<Message> messageList = new ArrayList<>(messages);
        messageList.add(message);
        return new MessageBoard(textMaker, messageList);
    }

    /**
     * Message affiché sur le tableau d'affichage
     *
     * @param text
     *         texte du message à afficher
     * @param points
     *         nombre de points associés au message (possiblement 0)
     * @param scorers
     *         ensemble de joueurs qui remporte des points (possiblement vide)
     * @param tileIds
     *         identifiants des tuiles concernées par le message (possiblement vide)
     */
    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {

        //Constructeur qui garantie l'immuabilité et vérifie la validité des paramètres
        public Message {
            Objects.requireNonNull(text);
            Preconditions.checkArgument(points >= 0);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}
