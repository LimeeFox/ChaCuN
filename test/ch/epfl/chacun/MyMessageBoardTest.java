package ch.epfl.chacun;

import ch.epfl.chacun.*;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
public class MyMessageBoardTest {


    private final TextMaker textMaker = new TextMaker() {
        @Override
        public String playerName(PlayerColor playerColor) {
            return playerColor.toString();
        }

        @Override
        public String points(int points) {
            return "" + points;
        }

        @Override
        public String playerClosedForestWithMenhir(PlayerColor player) {
            return player.toString();
        }

        @Override
        public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
            return scorers + ", " + points + ", " + mushroomGroupCount + ", " + tileCount;
        }

        @Override
        public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
            return scorers + ", " + points + ", " + fishCount + ", " + tileCount;
        }

        @Override
        public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
            return scorer + ", " + points + ", " + animals;
        }

        @Override
        public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
            return scorer + ", " + points + ", " + lakeCount;
        }

        @Override
        public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
            return scorers + ", " + points + ", " + animals;
        }

        @Override
        public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
            return scorers + ", " + points + ", " + fishCount;
        }

        @Override
        public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
            return scorers + ", " + points + ", " + animals;
        }

        @Override
        public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
            return scorers + ", " + points + ", " + lakeCount;
        }

        @Override
        public String playersWon(Set<PlayerColor> winners, int points) {
            return winners + ", " + points;
        }

        @Override
        public String clickToOccupy() {
            return "";
        }

        @Override
        public String clickToUnoccupy() {
            return "";
        }
    };
    private final MessageBoard messageBoard = new MessageBoard(textMaker, List.of());

    @Test
    void testPoints() {
        Map<PlayerColor, Integer> expectedPoints1 = new HashMap<>();
        expectedPoints1.put(PlayerColor.BLUE, 20);
        expectedPoints1.put(PlayerColor.RED, 15);
        expectedPoints1.put(PlayerColor.GREEN, 10);

        Map<PlayerColor, Integer> expectedPoints2 = new HashMap<>();

        MessageBoard.Message message1 = new MessageBoard.Message("Message 1", 10, Collections.singleton(PlayerColor.BLUE), Collections.emptySet());
        MessageBoard.Message message2 = new MessageBoard.Message("Message 2", 15, Collections.singleton(PlayerColor.RED), Collections.emptySet());
        MessageBoard.Message message3 = new MessageBoard.Message("Message2", 10, Set.of(PlayerColor.BLUE, PlayerColor.GREEN), Set.of(87, 56, 19));

        List<MessageBoard.Message> messages = new ArrayList<>(Arrays.asList(message1, message2, message3));
        MessageBoard messageBoard1 = new MessageBoard(textMaker, messages);
        MessageBoard messageBoard2 = new MessageBoard(textMaker, List.of());

        assertEquals(expectedPoints1, messageBoard1.points());
        assertEquals(expectedPoints2, messageBoard2.points());
    }

    @Test
    void testWithScoredForest() {
        Area<Zone.Forest> forest1 = new Area<>(Set.of(), List.of(), 0);
        MessageBoard updatedMessageBoard = messageBoard.withScoredForest(forest1);

        assertEquals(new MessageBoard(textMaker, List.of()), updatedMessageBoard);

        List<PlayerColor> scorers = List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN);
        Area<Zone.Forest> forest2 = new Area<>(Set.of(
                new Zone.Forest(986, Zone.Forest.Kind.WITH_MENHIR),
                new Zone.Forest(564, Zone.Forest.Kind.WITH_MUSHROOMS),
                new Zone.Forest(190, Zone.Forest.Kind.PLAIN)),
                scorers, 3);
        updatedMessageBoard = updatedMessageBoard.withScoredForest(forest2);
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker, List.of(
                new MessageBoard.Message(
                        textMaker.playersScoredForest(forest2.majorityOccupants(), Points.forClosedForest(3, 1), 1, 3),
                        Points.forClosedForest(3, 1), forest2.majorityOccupants(), Set.of(98, 56, 19))));

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        Area<Zone.Forest> forest3 = new Area<>(Set.of(
                new Zone.Forest(986, Zone.Forest.Kind.WITH_MENHIR),
                new Zone.Forest(564, Zone.Forest.Kind.WITH_MUSHROOMS),
                new Zone.Forest(190, Zone.Forest.Kind.PLAIN)),
                List.of(), 0);
        updatedMessageBoard = updatedMessageBoard.withScoredForest(forest3);
        assertEquals(expectedMessageBoard, updatedMessageBoard);
    }

    @Test
    void testWithClosedForestWithMenhir() {
        Area<Zone.Forest> forest = new Area<>(Set.of(new Zone.Forest(863, Zone.Forest.Kind.WITH_MENHIR)), List.of(), 0);
        MessageBoard updatedMessageBoard =  messageBoard.withClosedForestWithMenhir(PlayerColor.BLUE, forest);
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker,
                List.of(new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.BLUE), 0, Set.of(), Set.of(86))));
        assertEquals(expectedMessageBoard, updatedMessageBoard);

        Area<Zone.Forest> emptyForest = new Area<>(Set.of(), List.of(), 0);
        List<MessageBoard.Message> expMessages = new ArrayList<>(List.copyOf(expectedMessageBoard.messages()));
        expMessages.add(new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.BLUE),
                0, Set.of(), emptyForest.tileIds()));
        expectedMessageBoard = new MessageBoard(textMaker, expMessages);

        updatedMessageBoard = updatedMessageBoard.withClosedForestWithMenhir(PlayerColor.BLUE, emptyForest);

        assertEquals(expectedMessageBoard, updatedMessageBoard);

    }

    @Test
    void testWithScoredRiver() {
        Zone.Lake lake1 = new Zone.Lake(88, 3, null);
        Zone.Lake lake2 = new Zone.Lake(238, 2, null);
        Zone.River river1 = new Zone.River(83, 2, lake1);
        Zone.River river2 = new Zone.River(73, 0, null);
        Zone.River river3 = new Zone.River(234, 1, null);
        Zone.River river4 = new Zone.River(84, 0, lake1);
        Zone.River river5 = new Zone.River(274, 0, lake2);

        List<PlayerColor> scorers1 = List.of(PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.BLUE);

        Set<Zone.River> Zriver1 = Set.of(river1, river2, river3, river5);
        Set<Zone.River> Zriver2 = Set.of(river1, river2, river4);
        Set<Zone.River> Zriver3 = Set.of(river1, river2, river3);

        Area<Zone.River> Ariver1 = new Area<>(Zriver1, scorers1, 0);
        Area<Zone.River> Ariver2 = new Area<>(Zriver2, scorers1, 0);
        Area<Zone.River> Ariver3 = new Area<>(Zriver3, scorers1, 1);

        MessageBoard expectedMessageBoard1 = new MessageBoard(textMaker, List.of(new MessageBoard.Message(
                textMaker.playersScoredRiver(Set.of(PlayerColor.GREEN), Points.forClosedRiver(4, 8), 8, 4),
                Points.forClosedRiver(4, 8), Ariver1.majorityOccupants(), Set.of(8, 7, 23, 27))));
        MessageBoard expectedMessageBoard2 = new MessageBoard(textMaker, List.of(new MessageBoard.Message(
                textMaker.playersScoredRiver(Set.of(PlayerColor.GREEN), Points.forClosedRiver(2, 5), 5, 2),
                Points.forClosedRiver(2, 5), Ariver2.majorityOccupants(), Set.of(8, 7))));
        MessageBoard expectedMessageBoard3 = new MessageBoard(textMaker, List.of(new MessageBoard.Message(
                textMaker.playersScoredRiver(Set.of(PlayerColor.GREEN), Points.forClosedRiver(3, 6), 6, 3),
                Points.forClosedRiver(3, 6), Ariver3.majorityOccupants(), Set.of(8, 7, 23))));

        assertEquals(expectedMessageBoard1, messageBoard.withScoredRiver(Ariver1));
        assertEquals(expectedMessageBoard2, messageBoard.withScoredRiver(Ariver2));
        assertEquals(expectedMessageBoard3, messageBoard.withScoredRiver(Ariver3));
    }

    @Test
    void testWithScoredHuntingTrap() {
        Zone.Meadow meadow1 = new Zone.Meadow(973, List.of(new Animal(9730, Animal.Kind.MAMMOTH), new Animal(9731, Animal.Kind.DEER)), null);
        Zone.Meadow meadow2 = new Zone.Meadow(257, List.of(new Animal(2570, Animal.Kind.TIGER)), null);
        Zone.Meadow meadow3 = new Zone.Meadow(461, List.of(), null);
        Map<Animal.Kind, Integer> animals = new HashMap<>();
        animals.put(Animal.Kind.TIGER, 1);
        animals.put(Animal.Kind.DEER, 1);
        animals.put(Animal.Kind.MAMMOTH, 1);
        animals.put(Animal.Kind.AUROCHS, 0);
        Area<Zone.Meadow> meadow = new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(PlayerColor.PURPLE, PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.YELLOW), 4);
        Area<Zone.Meadow> noAnimalsMeadow = new Area<>(Set.of(meadow3), List.of(PlayerColor.BLUE, PlayerColor.RED), 3);
        Area<Zone.Meadow> emptyMeadow = new Area<>(Set.of(), List.of(), 0);

        MessageBoard updatedMessageBoard = messageBoard.withScoredHuntingTrap(PlayerColor.RED, meadow);
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker, List.of(new MessageBoard.Message(
                textMaker.playerScoredHuntingTrap(PlayerColor.RED, Points.forMeadow(1, 0 , 1), animals),
                Points.forMeadow(1, 0, 1), Set.of(PlayerColor.RED), Set.of(97, 25, 46))));
        assertEquals(expectedMessageBoard, updatedMessageBoard);

        updatedMessageBoard = updatedMessageBoard.withScoredHuntingTrap(PlayerColor.BLUE, noAnimalsMeadow);

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        updatedMessageBoard = updatedMessageBoard.withScoredHuntingTrap(PlayerColor.BLUE, emptyMeadow);

        assertEquals(expectedMessageBoard, updatedMessageBoard);
    }

    @Test
    void testWithScoredLogboat() {
        Zone.Lake lake = new Zone.Lake(1, 0, null);
        Zone.River river1 = new Zone.River(1, 2, null);
        Zone.River river2 = new Zone.River(1, 2, lake);

        Set<Zone.Water> waterSet = Set.of(lake, river1, river2);

        List<PlayerColor> occupants = List.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED);

        Area<Zone.Water> riverSystem = new Area<Zone.Water>(waterSet, occupants, 1);
        Area<Zone.Water> noLakeSystem = new Area<>(Set.of(river1, river2), occupants, 2);
        Area<Zone.Water> emptySystem = new Area<>(Set.of(), List.of(), 0);

        MessageBoard updatedMessageBoard = messageBoard.withScoredLogboat(PlayerColor.BLUE, riverSystem);
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker,
                List.of(new MessageBoard.Message(textMaker.playerScoredLogboat(PlayerColor.BLUE,
                        Points.forLogboat(1), 1),
                        Points.forLogboat(1),
                        Set.of(PlayerColor.BLUE),
                        riverSystem.tileIds())));

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        updatedMessageBoard = updatedMessageBoard.withScoredLogboat(PlayerColor.RED, noLakeSystem);

        Trivial because a riverSystem MUST have at least 1 lake

        List<MessageBoard.Message> expectedMessages = new ArrayList<>(List.copyOf(expectedMessageBoard.messages()));
        expectedMessages.add(new MessageBoard.Message(textMaker.playerScoredLogboat(PlayerColor.RED,
                 Points.forLogboat(0), 0),
                Points.forLogboat(0), Set.of(PlayerColor.RED), noLakeSystem.tileIds()));

        expectedMessageBoard = new MessageBoard(textMaker, expectedMessages);

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        updatedMessageBoard = updatedMessageBoard.withScoredLogboat(PlayerColor.GREEN, emptySystem);

        assertEquals(expectedMessageBoard, updatedMessageBoard);


    }

    @Test
    void testWithScoredMeadow() {
        Zone.Meadow meadow1 = new Zone.Meadow(973, List.of(new Animal(9730, Animal.Kind.MAMMOTH), new Animal(9731, Animal.Kind.DEER)), null);
        Zone.Meadow meadow2 = new Zone.Meadow(257, List.of(new Animal(2570, Animal.Kind.TIGER)), null);
        Zone.Meadow meadow3 = new Zone.Meadow(461, List.of(), null);
        Set<Zone.Meadow> meadows = Set.of(meadow1, meadow2, meadow3);

        Map<Animal.Kind, Integer> animalIntegerMap = new HashMap<>();
        animalIntegerMap.put(Animal.Kind.MAMMOTH, 1);
        animalIntegerMap.put(Animal.Kind.DEER, 1);
        animalIntegerMap.put(Animal.Kind.TIGER, 1);

        List<PlayerColor> occupants = List.of(PlayerColor.BLUE, PlayerColor.RED);

        Area<Zone.Meadow> meadowArea = new Area<>(meadows, occupants, 4);
        Area<Zone.Meadow> emptyMeadowArea = new Area<>(Set.of(meadow3), occupants, 4);

        MessageBoard updatedMessageBoard = messageBoard.withScoredMeadow(meadowArea, Set.of());
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker,
                List.of(new MessageBoard.Message(textMaker.playersScoredMeadow(meadowArea.majorityOccupants(),
                        Points.forMeadow(1, 0, 0),
                        animalIntegerMap),
                        Points.forMeadow(1, 0, 0),
                        Set.copyOf(occupants), meadowArea.tileIds())));
        List<MessageBoard.Message> messages = new ArrayList<>(List.copyOf(expectedMessageBoard.messages()));

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        updatedMessageBoard = updatedMessageBoard.withScoredMeadow(emptyMeadowArea,
                Set.of(new Animal(9730, Animal.Kind.MAMMOTH)));

        assertEquals(expectedMessageBoard, updatedMessageBoard);


        updatedMessageBoard = updatedMessageBoard.withScoredMeadow(meadowArea,
                Set.of(new Animal(8005, Animal.Kind.AUROCHS)));
        messages.add(new MessageBoard.Message(textMaker.playersScoredMeadow(Set.copyOf(occupants),
                Points.forMeadow(1, 0, 0),
                animalIntegerMap),
                Points.forMeadow(1, 0, 0),
                Set.copyOf(occupants), meadowArea.tileIds()));
        expectedMessageBoard = new MessageBoard(textMaker, messages);

        assertEquals(expectedMessageBoard, updatedMessageBoard);
    }

    @Test
    void testWithScoredRiverSystem() {
        Zone.Lake lake = new Zone.Lake(1, 0, null);
        Zone.River river1 = new Zone.River(1, 2, null);
        Zone.River river2 = new Zone.River(1, 2, lake);

        Set<Zone.Water> waterSet = Set.of(lake, river1, river2);

        List<PlayerColor> occupants = List.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED);

        Area<Zone.Water> riverSystem = new Area<Zone.Water>(waterSet, occupants, 1);
        Area<Zone.Water> emptyRiverSystem = new Area<>(waterSet, List.of(), 1);

        MessageBoard updatedMessageBoard = messageBoard.withScoredRiverSystem(riverSystem);
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker,
                List.of(new MessageBoard.Message(textMaker.playersScoredRiverSystem(riverSystem.majorityOccupants(),
                        Points.forRiverSystem(Area.riverSystemFishCount(riverSystem)),
                                Area.riverSystemFishCount(riverSystem)),
                                Points.forRiverSystem(Area.riverSystemFishCount(riverSystem)),
                        Set.copyOf(occupants),
                        riverSystem.tileIds())));

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        updatedMessageBoard = updatedMessageBoard.withScoredRiverSystem(emptyRiverSystem);
        assertEquals(expectedMessageBoard, updatedMessageBoard);
    }

    @Test
    void testWithScoredPitTrap() {
        Zone.Meadow meadow1 = new Zone.Meadow(973, List.of(new Animal(9730, Animal.Kind.MAMMOTH), new Animal(9731, Animal.Kind.DEER)), null);
        Zone.Meadow meadow2 = new Zone.Meadow(257, List.of(new Animal(2570, Animal.Kind.TIGER)), null);
        Zone.Meadow meadow3 = new Zone.Meadow(461, List.of(), null);

        Map<Animal.Kind, Integer> animals = new HashMap<>();
        animals.put(Animal.Kind.TIGER, 1);
        animals.put(Animal.Kind.MAMMOTH, 1);


        Area<Zone.Meadow> meadow = new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(PlayerColor.PURPLE, PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.YELLOW), 4);

        MessageBoard updatedMessageBoard = messageBoard.withScoredPitTrap(meadow, Set.of(new Animal(9731, Animal.Kind.DEER)));
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker,
                List.of(new MessageBoard.Message(textMaker.playersScoredPitTrap(meadow.majorityOccupants(),
                        Points.forMeadow(animals.getOrDefault(Animal.Kind.MAMMOTH, 0),
                                animals.getOrDefault(Animal.Kind.AUROCHS, 0),
                                animals.getOrDefault(Animal.Kind.DEER, 0)),
                        animals),
                        Points.forMeadow(animals.getOrDefault(Animal.Kind.MAMMOTH, 0),
                                animals.getOrDefault(Animal.Kind.AUROCHS, 0),
                                animals.getOrDefault(Animal.Kind.DEER, 0)),
                        meadow.majorityOccupants(),
                        meadow.tileIds())));

        assertEquals(expectedMessageBoard, updatedMessageBoard);
    }

    @Test
    void testWithScoredRaft() {
        Zone.Lake lake = new Zone.Lake(1, 0, null);
        Zone.River river1 = new Zone.River(1, 2, null);
        Zone.River river2 = new Zone.River(1, 2, lake);

        Set<Zone.Water> waterSet = Set.of(lake, river1, river2);

        List<PlayerColor> occupants = List.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED);

        Area<Zone.Water> riverSystem = new Area<Zone.Water>(waterSet, occupants, 1);
        Area<Zone.Water> emptySystem = new Area<>(waterSet, List.of(), 1);
        Area<Zone.Water> fullSystem = new Area<>(waterSet, PlayerColor.ALL, 1);

        MessageBoard updatedMessageBoard = messageBoard.withScoredRaft(riverSystem);
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker,
                List.of(new MessageBoard.Message(textMaker.playersScoredRaft(riverSystem.majorityOccupants(),
                        Points.forRaft(1), 1),
                        Points.forRaft(1),
                        riverSystem.majorityOccupants(),
                        riverSystem.tileIds())));

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        updatedMessageBoard = updatedMessageBoard.withScoredRaft(emptySystem);
        List<MessageBoard.Message> expectedMessages = new ArrayList<>(List.copyOf(expectedMessageBoard.messages()));

        expectedMessageBoard = new MessageBoard(textMaker, expectedMessages);

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        expectedMessages.add(new MessageBoard.Message(textMaker.playersScoredRaft(new HashSet<>(Set.copyOf(PlayerColor.ALL)),
                Points.forRaft(1), 1),
                Points.forRaft(1), new HashSet<>(Set.copyOf(PlayerColor.ALL)), fullSystem.tileIds()));
        updatedMessageBoard = updatedMessageBoard.withScoredRaft(fullSystem);
        expectedMessageBoard = new MessageBoard(textMaker, expectedMessages);

        assertEquals(expectedMessageBoard, updatedMessageBoard);
    }

    @Test
    void testWithWinners() {
        Set<PlayerColor> winners = new HashSet<>();
        winners.add(PlayerColor.BLUE);
        MessageBoard updatedMessageBoard = messageBoard.withWinners(winners, 1809);
        MessageBoard expectedMessageBoard = new MessageBoard(textMaker,
                List.of(new MessageBoard.Message(textMaker.playersWon(winners, 1809),
                        0, Set.of(), Set.of())));
        assertEquals(expectedMessageBoard, updatedMessageBoard);

        winners.addAll(PlayerColor.ALL);

        updatedMessageBoard = updatedMessageBoard.withWinners(winners, 0);
        List<MessageBoard.Message> expectedMessages = new ArrayList<>(List.copyOf(expectedMessageBoard.messages()));
        expectedMessages.add(new MessageBoard.Message(textMaker.playersWon(winners, 0),
                0, Set.of(), Set.of()));
        expectedMessageBoard = new MessageBoard(textMaker, expectedMessages);

        assertEquals(expectedMessageBoard, updatedMessageBoard);

        updatedMessageBoard = updatedMessageBoard.withWinners(Set.of(), 0);
        expectedMessages.add(new MessageBoard.Message(textMaker.playersWon(Set.of(),
                0), 0, Set.of(), Set.of()));
        expectedMessageBoard = new MessageBoard(textMaker, expectedMessages);

        assertEquals(expectedMessageBoard, updatedMessageBoard);
    }

}

 */