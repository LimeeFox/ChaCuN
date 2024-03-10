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

    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {

    }

    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {

    }

    public MessageBoard withScoredRiver(Area<Zone.River> river) {

    }

    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {

    }

    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {

    }

    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {

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
