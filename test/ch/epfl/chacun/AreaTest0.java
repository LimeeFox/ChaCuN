package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest0 {

    @Test
    void hasMenhir() {
        //
        // Verifier avec aucun menhir
        //
        Zone.Forest forest1 =  new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);

        Area<Zone.Forest> area1 = new Area<>(Set.of(forest1, forest2), List.of(PlayerColor.RED), 2);

        assertFalse(Area.hasMenhir(area1));

        //
        // Verifier avec un menhir
        //
        Zone.Forest forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area2 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.RED), 2);

        assertTrue(Area.hasMenhir(area2));

        //
        // Verifier avec plusieurs menhirs
        //
        Zone.Forest forest4 = new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area3 = new Area<>(Set.of(forest1, forest2, forest3, forest4), List.of(PlayerColor.RED), 2);

        assertTrue(Area.hasMenhir(area3));
    }

    @Test
    void mushroomGroupCount() {
        //
        // Avec 0 champignons
        //
        Zone.Forest forest1 =  new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area1 = new Area<>(Set.of(forest1, forest2), List.of(PlayerColor.RED), 2);

        assertEquals(0, Area.mushroomGroupCount(area1));
        //
        // Avec 1 champignon
        //
        Zone.Forest forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);

        Area<Zone.Forest> area2 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.RED), 2);

        assertEquals(1, Area.mushroomGroupCount(area2));

        //
        // Avec plusieurs champignons
        //
        Zone.Forest forest4 = new Zone.Forest(3, Zone.Forest.Kind.WITH_MUSHROOMS);

        Area<Zone.Forest> area3 = new Area<>(Set.of(forest1, forest2, forest3, forest4), List.of(PlayerColor.RED), 2);

        assertEquals(2, Area.mushroomGroupCount(area3));
    }

    @Test
    void animals() {
        //
        // Verifier si il n'y a aucun animal
        //
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, List.of(), null);
        Zone.Meadow meadowZone3 = new Zone.Meadow(3, List.of(), null);

        Area<Zone.Meadow> area1 = new Area<>(Set.of(meadowZone1, meadowZone2, meadowZone3), List.of(PlayerColor.RED, PlayerColor.YELLOW), 3);

        assertEquals(Set.of(), Area.animals(area1, Set.of()));

        //
        // Verifier avec des animaux, sans filtre
        //
        Animal animal1 = new Animal(40, Animal.Kind.TIGER);
        Animal animal2 = new Animal(41, Animal.Kind.DEER);
        Animal animal3 = new Animal(42, Animal.Kind.DEER);

        Animal animal4 = new Animal(50, Animal.Kind.MAMMOTH);

        Zone.Meadow meadowZone4 = new Zone.Meadow(4, List.of(animal1, animal2, animal3), null);
        Zone.Meadow meadowZone5 = new Zone.Meadow(5, List.of(animal4), null);

        Area<Zone.Meadow> area2 = new Area<>(Set.of(meadowZone4, meadowZone5), List.of(PlayerColor.BLUE), 2);

        assertEquals(Set.of(animal1, animal2, animal3, animal4), Area.animals(area2, Set.of()));

        //
        // Verifier avec des animaux, avec des filtres
        //
        assertEquals(Set.of(animal1, animal4), Area.animals(area2, Set.of(animal2, animal3)));
    }

    @Test
    void riverFishCount() {
        //
        // with no fish
        //
        Zone.River riverZone0 = new Zone.River(1, 0, null);

        Area<Zone.River> area0 = new Area<>(Set.of(riverZone0), List.of(), 1);

        assertEquals(0, Area.riverFishCount(area0));

        //
        // with fish only in lake
        //
        Zone.River riverZone1 = new Zone.River(3, 0, new Zone.Lake(8, 2, null));

        Area<Zone.River> area1 = new Area<>(Set.of(riverZone0, riverZone1), List.of(), 1);

        assertEquals(2, Area.riverFishCount(area1));

        //
        // with fish only in rivers
        //
        Zone.River riverZone2 = new Zone.River(0, 2, null);
        Zone.River riverZone3 = new Zone.River(3, 1, new Zone.Lake(8, 0, null));

        Area<Zone.River> area2 = new Area<>(Set.of(riverZone0, riverZone2, riverZone3), List.of(), 1);

        assertEquals(3, Area.riverFishCount(area2));

        //
        // with fish everywhere
        //
        Area<Zone.River> area3 = new Area<>(Set.of(riverZone0, riverZone1, riverZone2, riverZone3), List.of(), 1);

        assertEquals(5, Area.riverFishCount(area3));
    }

    @Test
    void riverSystemFishCount() {
        //
        // with no fish
        //
        Zone.River riverZone0 = new Zone.River(1, 0, null);

        Area<Zone.Water> area0 = new Area<>(Set.of(riverZone0), List.of(), 1);

        assertEquals(0, Area.riverSystemFishCount(area0));

        //
        // at this point i just copy pasted the above test i have no clue what im doing
        //
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.River riverZone1 = new Zone.River(2, 0, lake1);

        Area<Zone.Water> area1 = new Area<>(Set.of(riverZone0, riverZone1, lake1), List.of(), 1);

        assertEquals(2, Area.riverSystemFishCount(area1));

        //
        // read above
        //
        Zone.Lake lake2 =  new Zone.Lake(9, 0, null);
        Zone.River riverZone2 = new Zone.River(0, 2, null);
        Zone.River riverZone3 = new Zone.River(3, 1,null);

        Area<Zone.Water> area2 = new Area<>(Set.of(riverZone0, riverZone2, riverZone3, lake1, lake2), List.of(), 1);

        assertEquals(5, Area.riverSystemFishCount(area2));
    }

    @Test
    void lakeCount() {
        //
        // Avec une liste vide
        //
        assertEquals(Area.lakeCount(new Area<Zone.Water>(new HashSet<>(), new ArrayList<>(), 1)), 0);

        //
        // Avec 0 lac
        //
        Zone.River riverZone1 = new Zone.River(0, 2, null);

        Area<Zone.Water> area1 = new Area<>(Set.of(riverZone1), List.of(PlayerColor.BLUE), 2);

        assertEquals(0, Area.lakeCount(area1));

        //
        // Avec 1 lac attribué à une rivière
        //
        Zone.River riverZone2 = new Zone.River(3, 1, new Zone.Lake(8, 0, null));

        Area<Zone.Water> area2 = new Area<>(Set.of(riverZone1, riverZone2), List.of(PlayerColor.BLUE), 2);

        assertEquals(1, Area.lakeCount(area2));

        //
        // Avec plusieurs lacs
        //
        Zone.Lake lakeZone1 = new Zone.Lake(9, 0, null);

        Area<Zone.Water> area3 = new Area<>(Set.of(riverZone1, riverZone2, lakeZone1), List.of(PlayerColor.BLUE), 2);

        assertEquals(2, Area.lakeCount(area3));
    }

    @Test
    void isClosed() {
        //
        // Closed one
        //
        Area<Zone.Forest> area0 = new Area<>(new HashSet<>(), new ArrayList<>(), 2);
        Area<Zone.Forest> newArea = area0.connectTo(area0);

        assertTrue(newArea.isClosed());
        //
        // Open one
        //
        Area<Zone.Forest> area1 = new Area<>(new HashSet<>(), new ArrayList<>(), 1);

        assertFalse(area1.isClosed());
    }

    @Test
    void isOccupied() {
        //
        // Has no occupant
        //
        Area<Zone.Forest> area0 = new Area<>(new HashSet<>(), new ArrayList<>(), 1);

        assertFalse(area0.isOccupied());

        //
        // Has an occupant
        //
        List<PlayerColor> occupants1 = new ArrayList<>();
        occupants1.add(PlayerColor.BLUE);
        Area<Zone.Forest> area1 = new Area<>(new HashSet<>(), occupants1, 1);
        assertTrue(area1.isOccupied());
    }

    @Test
    void majorityOccupants() {
        //
        // No occupants
        //
        Area<Zone.Forest> area0 = new Area<>(new HashSet<>(), new ArrayList<>(), 1);

        assertEquals(Set.of(), area0.majorityOccupants());

        //
        // 1 Occupant
        //
        List<PlayerColor> occupants1 = new ArrayList<>();
        occupants1.add(PlayerColor.BLUE);

        Area<Zone.Forest> area1 = new Area<>(new HashSet<>(), occupants1, 1);

        assertEquals(Set.of(PlayerColor.BLUE), area1.majorityOccupants());

        //
        // Multiple occupants (1 dominant)
        //
        List<PlayerColor> occupants2 = new ArrayList<>();
        occupants2.add(PlayerColor.BLUE);
        occupants2.add(PlayerColor.BLUE);
        occupants2.add(PlayerColor.RED);
        occupants2.add(PlayerColor.GREEN);

        Area<Zone.Forest> area2 = new Area<>(new HashSet<>(), occupants2, 1);

        assertEquals(Set.of(PlayerColor.BLUE), area2.majorityOccupants());


        //
        // Multiple occupants (2 dominant)
        //
        List<PlayerColor> occupants3 = new ArrayList<>();
        occupants3.add(PlayerColor.BLUE);
        occupants3.add(PlayerColor.BLUE);
        occupants3.add(PlayerColor.RED);
        occupants3.add(PlayerColor.GREEN);
        occupants3.add(PlayerColor.GREEN);
        occupants3.add(PlayerColor.PURPLE);

        Area<Zone.Forest> area3 = new Area<>(new HashSet<>(), occupants3, 1);

        assertEquals(Set.of(PlayerColor.BLUE, PlayerColor.GREEN), area3.majorityOccupants());
    }

    @Test
    void connectTo() {

    }

    @Test
    void tileIds() {
        //
        // With no zones
        //
        Area<Zone.Forest> area0 = new Area<>(new HashSet<>(), new ArrayList<>(), 1);

        assertEquals(Set.of(), area0.tileIds());

        //
        // With a couple of zones
        //
        Zone.Forest forest1 =  new Zone.Forest(560, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(561, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(562, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest4 = new Zone.Forest(573, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area1 = new Area<>(Set.of(forest1, forest2, forest3, forest4), List.of(PlayerColor.RED), 2);

        assertEquals(Set.of(56, 57), area1.tileIds());
    }

    @Test
    void zoneWithSpecialPower() {
        //
        // With no zones
        //
        Area<Zone.Forest> area0 = new Area<>(new HashSet<>(), new ArrayList<>(), 1);

        assertNull(area0.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN));
        //
        // With no special power
        //
        Zone.Forest forest1 =  new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);

        Area<Zone.Forest> area1 = new Area<>(Set.of(forest1, forest2), new ArrayList<>(), 1);

        assertNull(area1.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN));
        //
        // With 1 special power (not the one we want)
        //
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.LOGBOAT);

        Area<Zone.Meadow> area2 = new Area<>(Set.of(meadowZone1), new ArrayList<>(), 1);

        assertNull(area2.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN));
        //
        // With 1 special power (the one we want)
        //
        Area<Zone.Meadow> area3 = new Area<>(Set.of(meadowZone1), new ArrayList<>(), 1);

        assertEquals(meadowZone1, area3.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));

        //
        // With multiple special powers, the same that we want
        //
        Zone.Meadow meadowZone2 = new Zone.Meadow(1, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone3 = new Zone.Meadow(2, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadowZone4 = new Zone.Meadow(3, List.of(), Zone.SpecialPower.PIT_TRAP);

        Area<Zone.Meadow> area4 = new Area<>(Set.of(meadowZone2, meadowZone3, meadowZone4), new ArrayList<>(), 1);

        Zone actual = area4.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN);
        assertTrue(meadowZone2 == actual || meadowZone3 == actual);
    }
}