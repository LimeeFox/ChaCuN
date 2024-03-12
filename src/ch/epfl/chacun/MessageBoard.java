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
     * Mise à jour du tableau d'affichage quand un joueur pose une fosse à pieux
     *
     * @param scorer
     *          joueur qui a posé la fosse à pieux et, en principe, remporte des points
     * @param adjacentMeadow
     *          ensemble des prés adjacents à la fosse à pieux
     * @return un tableau d'affichage identique au récepteur, à moins que le joueur remporte des points en posant
     *          sa fosse
     */
    // TODO: 11/03/2024 check optimisation of following method (and others with regard to this one)
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
        Set<Animal> animals = new HashSet<>();
        for (Zone.Meadow meadowZone : adjacentMeadow.zones()) {
            animals.addAll(meadowZone.animals());
        }
        int mammothCount = (int) animals.stream()
                .filter(animal -> animal.kind().equals(Animal.Kind.MAMMOTH))
                .count();

        int aurochsCount = (int) animals.stream()
                .filter(animal -> animal.kind().equals(Animal.Kind.AUROCHS))
                .count();

        int deerCount = (int) animals.stream()
                .filter(animal -> animal.kind().equals(Animal.Kind.DEER))
                .count();

        int scoredPoints = Points.forMeadow(mammothCount, aurochsCount, deerCount);

        if (scoredPoints > 0) {
            List<Message> messageList = new ArrayList<>(List.copyOf(this.messages));
            messageList.add(new Message(textMaker.playerScoredHuntingTrap(scorer,
                    scoredPoints, Map.of(Animal.Kind.MAMMOTH, mammothCount,
                            Animal.Kind.AUROCHS, aurochsCount,
                            Animal.Kind.DEER, deerCount)),
                    scoredPoints, Set.of(scorer), adjacentMeadow.tileIds()));
            return new MessageBoard(this.textMaker, messageList);
        }
        return this;
    }

    /**
     * Mise à jour du tableau d'affichage quand un joueur pose la pirogue dans le réseau hydrographique donnée
     *
     * @param scorer
     *          joueur qui a posé la pirogue et qui remporte donc des points
     * @param riverSystem
     *          réseau hydrographique où la pirogue a été posée
     * @return un tableau d'affichage identique au récepteur, mais avec un nouveau message signalant au joueur donné
     *          qu'il a remporté les points correspondants
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        List<Message> messageList = new ArrayList<>(List.copyOf(messages));
        messageList.add(new Message(textMaker.playerScoredLogboat(scorer,
                Points.forLogboat(Area.lakeCount(riverSystem)),
                Area.lakeCount(riverSystem)),
                Points.forLogboat(Area.lakeCount(riverSystem)),
                Set.of(scorer),
                riverSystem.tileIds()));

        return new MessageBoard(this.textMaker, messageList);
    }

    /**
     * Mise à jour du tableau pour un pré fermé
     *
     * @param meadow
     *          zones pré qui ont été fermées et qui rapportent ainsi des points
     * @param cancelledAnimals
     *          animaux qui ont déjà été comptabilisés dans le calcul des points et qui ne seront donc pas pris en
     *          compte pour ce pré
     * @return un tableau d'affichage identique au récepteur avec un nouveau message indiquant les nombre de points
     *          remportés aux joueurs concernés, à moins que le pré soit inoccupé ou qu'il ne rapporte pas de points
     */
    // TODO: 12/03/2024 I don't like how I've had to duplicate code here, is there a better way?
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        if (meadow.isOccupied()) {
            Set<Animal> validAnimals = Area.animals(meadow, cancelledAnimals);

            int mammothCount = (int) validAnimals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.MAMMOTH))
                    .count();

            int aurochsCount = (int) validAnimals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.AUROCHS))
                    .count();

            int deerCount = (int) validAnimals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.DEER))
                    .count();

            int scoredPoints = Points.forMeadow(mammothCount, aurochsCount, deerCount);

            if (scoredPoints > 0) {
                Map<Animal, Integer> animalIntegerMap = new HashMap<>();

                List<Message> messageList = new ArrayList<>(List.copyOf(this.messages));
                messageList.add(new Message(textMaker.playersScoredMeadow(meadow.majorityOccupants(),
                        scoredPoints, Map.of(Animal.Kind.MAMMOTH, mammothCount,
                                Animal.Kind.AUROCHS, aurochsCount,
                                Animal.Kind.DEER, deerCount)),
                        scoredPoints, meadow.majorityOccupants(), meadow.tileIds()));

                return new MessageBoard(this.textMaker, messageList);
            }
        }
        return this;
    }

    /**
     * Mise à jour du tableau pour un système hydrographique qui apporte des points
     *
     * @param riverSystem
     *          réseau hydrographique donnée, en occurrence, qui rapporte des points
     * @return un tableau d'affichage qui contient un nouveau message indiquant les points remportés aux joueurs
     *          concernés, à moins que le système hydrographique donné soit inoccupée ou ne rapporte pas de points
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        if (riverSystem.isOccupied()) {
            int scoredPoints = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));
            if (scoredPoints > 0) {
                List<Message> messageList = new ArrayList<>(List.copyOf(this.messages));
                messageList.add(new Message(textMaker.playersScoredRiverSystem(riverSystem.majorityOccupants(),
                        scoredPoints, Area.riverSystemFishCount(riverSystem)),
                        scoredPoints, riverSystem.majorityOccupants(), riverSystem.tileIds()));

                return new MessageBoard(this.textMaker, messageList);
            }
        }
        return this;
    }

    /**
     * Mise à jour du tableau d'affichage pour une grande fosse à pieux
     *
     * @param adjacentMeadow
     *          aire de prés adjacents à la grande fosse à pieux
     * @param cancelledAnimals
     *          animaux à ne pas prendre en compte pour le calcul des points (déjà comptés)
     * @return un tableau d'affichage identique au récepteur contentant un nouveau message indiquant les joueurs
     *          ayant rapporté des points
     */
    // TODO: 12/03/2024 Compate with HuntingTrap, there's a similar vibe, so could we use one in the other?
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {
        if (adjacentMeadow.isOccupied()) {
            Set<Animal> validAnimals = Area.animals(adjacentMeadow, cancelledAnimals);

            int mammothCount = (int) validAnimals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.MAMMOTH))
                    .count();

            int aurochsCount = (int) validAnimals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.AUROCHS))
                    .count();

            int deerCount = (int) validAnimals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.DEER))
                    .count();

            int scoredPoints = Points.forMeadow(mammothCount, aurochsCount, deerCount);

            if (scoredPoints > 0) {
                List<Message> messageList = new ArrayList<>(List.copyOf(this.messages));
                messageList.add(new Message(textMaker.playersScoredPitTrap(adjacentMeadow.majorityOccupants(),
                        scoredPoints, Map.of(Animal.Kind.MAMMOTH, mammothCount,
                                Animal.Kind.AUROCHS, aurochsCount,
                                Animal.Kind.DEER, deerCount)),
                        scoredPoints, adjacentMeadow.majorityOccupants(), adjacentMeadow.tileIds()));

                return new MessageBoard(this.textMaker, messageList);
            }
        }
        return this;
    }

    /**
     * Mise à jour du tableau d'affichage pour un radeau
     * 
     * @param riverSystem
     *          réseau hydrographique do
     * @return un tableau d'affichage identique au récepteur avec un nouveau message signalant aux joueurs avec occupants
     *          majoritaires le nombre de points remportés, à moins que le réseau hydrographique soit vide
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        if (riverSystem.isOccupied()) {
            int scoredPoints = Points.forRaft(Area.lakeCount(riverSystem));
            
            List<Message> messageList = new ArrayList<>(List.copyOf(this.messages));
            messageList.add(new Message(textMaker.playersScoredRaft(riverSystem.majorityOccupants(),
                    scoredPoints, Area.lakeCount(riverSystem)),
                    scoredPoints, riverSystem.majorityOccupants(), riverSystem.tileIds()));
            
            return new MessageBoard(this.textMaker, messageList);
        }
        
        return this;
    }

    /**
     * Message de fin de partie pour signaler les gagnants
     *
     * @param winners
     *          ensemble des joueurs qui ont gagné la partie
     * @param points
     *          nombre points remportés par les gagnants
     * @return un tableau d'affichage identique au récepteur avec un nouveau message signalant les qui joueurs qui ont
     *          remportées la partie et leur nombre de points remportés
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        List<Message> messageList = new ArrayList<>(List.copyOf(this.messages));
        messageList.add(new Message(textMaker.playersWon(winners, points),
                0, Set.of(), Set.of()));

        return new MessageBoard(this.textMaker, messageList);
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
