package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest {

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
        Zone.River riverZone1 = new Zone.River(0, 2, null);
        Zone.River riverZone2 = new Zone.River(3, 1, new Zone.Lake(8, 0, null));


    }

    @Test
    void riverSystemFishCount() {
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

    }

    @Test
    void isOccupied() {
    }

    @Test
    void majorityOccupants() {
        //
        // No occupants
        //

        //assertEquals();

        //
        // 1 Occupant
        //

        //
        // Multiple occupants (1 dominant)
        //

        //
        // Multiple occupants (2 dominant)
        //


    }

    @Test
    void connectTo() {
    }

    @Test
    void withInitialOccupant() {
    }

    @Test
    void withoutOccupant() {
    }

    @Test
    void withoutOccupants() {
    }

    @Test
    void tileIds() {
    }

    @Test
    void zoneWithSpecialPower() {
    }

    @Test
    void zones() {
    }

    @Test
    void occupants() {
    }

    @Test
    void openConnections() {
    }
}