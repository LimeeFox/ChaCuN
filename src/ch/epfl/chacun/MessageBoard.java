package ch.epfl.chacun;

import java.util.*;

// TODO: 10/03/2024 Review descriptions of parameters 
// TODO: 10/03/2024 Verify if constructor is up to code (textMaker in particular) 

/**
 * Contenu du tableau d'affichage
 *
 * @author Cyriac Philippe (360553)
 *
 * @param textMaker
 *          permet d'obtenir le texte des différents messages
 * @param messages
 *          liste des messages affichés sur le tableau, du plus ancien au plus récent
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    //Constructeur qui garantie l'immuabilité de la classe
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    // TODO: 10/03/2024 Review description of Map<PlayerColor, Integer> points() 
    /**
     * Table associant à chaque joueur ayant obtenu des points, le nombre de points obtenus
     *
     * @return pointsMap
     *          table associant les joueurs à leur nombre de points obtenus
     */
    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> pointsMap = new HashMap<>();
        for (Message message : messages) {
            for (PlayerColor playerColor : message.scorers) {
                pointsMap.put(playerColor, message.points);
            }
        }
        return pointsMap;
    }

    /**
     * Mise à jour du tableau suite à la fermeture d'une forêt
     *
     * @param forest
     *          forêt fermée par un joueur
     * @return un tableau de message contenant un nouveau message pour les joueurs ayant remporté des points ou
     *          un tableau identique au récepteur si la forêt est inoccupée
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        if (forest.isOccupied()) {
            List<Message> forestMessages = new ArrayList<>(List.copyOf(messages));
            forestMessages.add(new Message(textMaker.playersScoredForest(forest.majorityOccupants(),
                    Points.forClosedForest(forest.tileIds().size(),
                            Area.mushroomGroupCount(forest)),
                    Area.mushroomGroupCount(forest), forest.tileIds().size()),
                    Points.forClosedForest(forest.tileIds().size(), Area.mushroomGroupCount(forest)),
                    forest.majorityOccupants(),
                    forest.tileIds()
            ));
            return new MessageBoard(this.textMaker, forestMessages);
        }
        return this;
    }

    // TODO: 11/03/2024 Check coherence of having "majorityOccupants" and "tildeIds argument in new Message

    /**
     * Mise à jour du tableau suite à la fermeture d'une forêt contenant un menhir
     *
     * @param player
     *          joueur ayant fermé la forêt contenant un menhir
     * @param forest
     *          aire de type forêt qui a été fermée
     * @return un tableau d'affichage avec un nouveau message indiquant au joueur qu'il peut jouer un second tour
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        List<Message> menhirMessages = new ArrayList<>(List.copyOf(messages));
        menhirMessages.add(new Message(textMaker.playerClosedForestWithMenhir(player),
                0, forest.majorityOccupants(), forest.tileIds() ));
        return new MessageBoard(this.textMaker, menhirMessages);

    }

    /**
     * Mise à jour du tableau suite à la fermeture d'une rivière
     *
     * @param river
     *          rivière fermée par un joueur
     * @return un tableau de message contenant un nouveau message pour les joueurs ayant remporté des points ou
     *          un tableau identique au récepteur si la rivière est inoccupée
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        if (river.isOccupied()) {
            List<Message> riverMessages = new ArrayList<>(List.copyOf(messages));
            riverMessages.add(new Message(textMaker.playersScoredRiver(river.majorityOccupants(),
                    Points.forClosedRiver(river.tileIds().size(),
                            Area.riverFishCount(river)), Area.riverFishCount(river),
                    river.tileIds().size()),
                            Points.forClosedRiver(river.tileIds().size(), Area.riverFishCount(river)),
                            river.majorityOccupants(),
                            river.tileIds()));
            return new MessageBoard(this.textMaker, riverMessages);
        }
        return this;
    }

    /**
     *
     * @param scorer
     * @param adjacentMeadow
     * @return
     */
    // TODO: 11/03/2024 check optimisation of following method (and others with regard to this one)
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
        Set<Animal> cancelledAnimals = new HashSet<>();
        for (Zone.Meadow meadowZone : adjacentMeadow.zones()) {
            cancelledAnimals.addAll(meadowZone.animals());
        }
        int mammothCount = (int) cancelledAnimals.stream()
                .filter(animal -> animal.kind().equals(Animal.Kind.MAMMOTH))
                .count();

        int aurochsCount = (int) cancelledAnimals.stream()
                .filter(animal -> animal.kind().equals(Animal.Kind.AUROCHS))
                .count();

        int deerCount = (int) cancelledAnimals.stream()
                .filter(animal -> animal.kind().equals(Animal.Kind.DEER))
                .count();

        int scorerPoints = Points.forMeadow(mammothCount, aurochsCount, deerCount);

        if (scorerPoints > 0) {
            List<Message> messageList = new ArrayList<>(List.copyOf(this.messages));
            messageList.add(new Message(textMaker.playerScoredHuntingTrap(scorer,
                    scorerPoints, Map.of(Animal.Kind.MAMMOTH, mammothCount,
                            Animal.Kind.AUROCHS, aurochsCount,
                            Animal.Kind.DEER, deerCount)),
                    scorerPoints, Set.of(scorer), adjacentMeadow.tileIds())))
            return new MessageBoard(this.textMaker, messageList);
        }
        return this;
    }

    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {

    }

    /**
     *
     * @param meadow
     * @param cancelledAnimals
     * @return
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        if (meadow.isOccupied()) {

        }
        return this;
    }

    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {

    }

    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {

    }

    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {

    }

    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {

    }

    /**
     * Message affiché sur le tableau d'affichage
     *
     * @param text
     *          texte du message à afficher
     * @param points
     *          nombre de points associés au message (possiblement 0)
     * @param scorers
     *          ensemble de joueurs qui remporte des points (possiblement vide)
     * @param tileIds
     *          identifiants des tuiles concernées par le message (possiblement vide)
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
