package ch.epfl.chacun;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ch.epfl.chacun.ZonePartition;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HassenPartitionTests {
    HassenPartitionTests(){
    }

    @Test
    void zonePartitionConstructorTrivialWorks(){

        Set<Zone> zoneSet = new HashSet<>();
        List<PlayerColor> playerColList = new ArrayList<>();
        Area<Zone> testArea = new Area<>(zoneSet, playerColList,2);


    }

    @Test
    void areaContainingTestWorksBasic(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        ZonePartition <Zone.Forest> test = new ZonePartition<>(areaSet);
        assertEquals(forestAreaTest,test.areaContaining(zoneE));
    }

    @Test
    void areaContainingTestWorksLimits(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        zoneSet1.add(zoneN);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        ZonePartition <Zone.Forest> test = new ZonePartition<>(areaSet);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> test.areaContaining(zoneE));
    }

    @Test
    void addSingletonTestNormal(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();

        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaSingle = new Area<>(zoneSingleSet,playerColorList,4);

        //On ajoute nos Area dans des sets d'areas
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTest); areaSet2.add(forestAreaSingle);

        //Rien
        Set<Zone.Forest> zoneSetModifiable = new HashSet<>(zoneSet1);
        Area<Zone.Forest> areaSetModfiable = new Area<>(zoneSetModifiable,playerColorList,4);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        //on utilise addSingelton
        testBuilder.addSingleton(zoneSingle,4);

        //c'est censé marcher damnn
        assertEquals(testSansBuilder,testBuilder.build());
    }


    @Test
    void addInitialOccupantTestNormal(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();

        playerColorListRedOnly.add(PlayerColor.RED);


        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaSingle = new Area<>(zoneSingleSet,playerColorList,4);
        Area<Zone.Forest> forestAreaTestWithRedOccupant = new Area<>(zoneSet1,playerColorListRedOnly,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTestWithRedOccupant);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        testBuilder.addInitialOccupant(zoneN,PlayerColor.RED);

        Assertions.assertEquals(testSansBuilder,testBuilder.build());

    }

    @Test
    void addInitialOccupantTestWithOccupants(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();

        playerColorListRedOnly.add(PlayerColor.RED);
        playerColorList.add(PlayerColor.BLUE);


        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaSingle = new Area<>(zoneSingleSet,playerColorList,4);
        Area<Zone.Forest> forestAreaTestWithRedOccupant = new Area<>(zoneSet1,playerColorListRedOnly,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTestWithRedOccupant);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        assertThrows(IllegalArgumentException.class, ()-> testBuilder.addInitialOccupant(zoneN,PlayerColor.RED));

    }
    @Test
    void addInitialOccupantTestWithZoneNotHere(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();

        playerColorListRedOnly.add(PlayerColor.RED);

        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaSingle = new Area<>(zoneSingleSet,playerColorList,4);
        Area<Zone.Forest> forestAreaTestWithRedOccupant = new Area<>(zoneSet1,playerColorListRedOnly,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTestWithRedOccupant);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        assertThrows(IllegalArgumentException.class, ()-> testBuilder.addInitialOccupant(zoneSingle,PlayerColor.RED));

    }

    @Test
    void removeOccupantTestNormal(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();

        playerColorListRedOnly.add(PlayerColor.RED);

        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaSingle = new Area<>(zoneSingleSet,playerColorList,4);
        Area<Zone.Forest> forestAreaTestWithRedOccupant = new Area<>(zoneSet1,playerColorListRedOnly,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTest);
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTestWithRedOccupant);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        testBuilder.removeOccupant(zoneE,PlayerColor.RED);

        assertEquals(testSansBuilder,testBuilder.build());
    }
    @Test
    void removeOccupantTestNoZone(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();

        playerColorListRedOnly.add(PlayerColor.RED);

        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaTestWithRedOccupant = new Area<>(zoneSet1,playerColorListRedOnly,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTest);
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTestWithRedOccupant);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);
        assertThrows(IllegalArgumentException.class, ()-> testBuilder.removeOccupant(zoneSS,PlayerColor.RED));
        assertThrows(IllegalArgumentException.class, ()-> testBuilder.removeOccupant(zoneSS,PlayerColor.BLUE));

    }

    @Test
    void removeOccupantTestNoOccupants(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();

        playerColorListRedOnly.add(PlayerColor.RED);

        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaSingle = new Area<>(zoneSingleSet,playerColorList,4);
        Area<Zone.Forest> forestAreaTestWithRedOccupant = new Area<>(zoneSet1,playerColorListRedOnly,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTest);
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        assertThrows(IllegalArgumentException.class, ()-> testBuilder.removeOccupant(zoneE,PlayerColor.RED));

    }

    @Test
    void removeAllOccupantsTestNormal(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListEmpty = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();

        playerColorListRedOnly.add(PlayerColor.RED);
        playerColorList.add(PlayerColor.RED);
        playerColorList.add(PlayerColor.PURPLE);

        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaTest2NoOccupants = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTestNoOccupants = new Area<>(zoneSet1,playerColorListEmpty,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet2.add(forestAreaTest2NoOccupants); areaSet2.add(forestAreaTestNoOccupants);
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        testBuilder.removeAllOccupantsOf(forestAreaTest);

        assertEquals(testSansBuilder,testBuilder.build());
    }

    @Test
    void removeAllOccupantsTestNoAreaInList(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListEmpty = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();

        playerColorListRedOnly.add(PlayerColor.RED);
        playerColorList.add(PlayerColor.RED);
        playerColorList.add(PlayerColor.PURPLE);

        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaTest2NoOccupants = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTestNoOccupants = new Area<>(zoneSet1,playerColorListEmpty,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet2.add(forestAreaTest2NoOccupants); areaSet2.add(forestAreaTestNoOccupants);
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        Area<Zone.Forest> forestAreaSingle = new Area<>(zoneSingleSet,playerColorList,4);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        assertThrows(IllegalArgumentException.class,()-> testBuilder.removeAllOccupantsOf(forestAreaSingle));
    }

    @Test
    void removeAllOccupantsTestNoOccupants(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListEmpty = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();


        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaTest2NoOccupants = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTestNoOccupants = new Area<>(zoneSet1,playerColorListEmpty,2);


        //On ajoute nos Area dans des sets d'areas
        areaSet2.add(forestAreaTest2NoOccupants); areaSet2.add(forestAreaTestNoOccupants);
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);

        //On crée les builders
        ZonePartition <Zone.Forest> testbuilder = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilder = new ZonePartition<>(areaSet2);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testbuilder);

        testBuilder.removeAllOccupantsOf(forestAreaTest);

        assertEquals(testSansBuilder,testBuilder.build());
    }

    @Test
    void unionTestNormalCase(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSetAllZones = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        Set<Area<Zone.Forest>> areaSetFusion = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();


        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneSS); zoneSet2.add(zoneEE);
        zoneSetAllZones.addAll(zoneSet1); zoneSetAllZones.addAll(zoneSet2);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaTestAllZones = new Area<>(zoneSetAllZones,playerColorList,2);
        //on aura 2 openconnections dans fusion donc 2+2 -2 =2 donc c bon



        //On ajoute nos Area dans des sets d'areas
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTest);
        areaSetFusion.add(forestAreaTestAllZones);


        //On crée les builders
        ZonePartition <Zone.Forest> testBuilderPartition = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilderPartition = new ZonePartition<>(areaSetFusion);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testBuilderPartition);

        testBuilder.union(zoneE,zoneEE);


        Assertions.assertEquals(testSansBuilderPartition,testBuilder.build());

    }


    @Test
    void unionTestOnSameArea(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        Set<Area<Zone.Forest>> areaSetFusion = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();
        List<PlayerColor> playerColorListRedOnly = new ArrayList<>();


        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);zoneSet1.add(zoneS);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaTestAllZones = new Area<>(zoneSet1,playerColorList,0);




        //On ajoute nos Area dans des sets d'areas
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTest);
        areaSetFusion.add(forestAreaTestAllZones);


        //On crée les builders
        ZonePartition <Zone.Forest> testBuilderPartition = new ZonePartition<>(areaSet);
        ZonePartition <Zone.Forest> testSansBuilderPartition = new ZonePartition<>(areaSetFusion);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testBuilderPartition);

            testBuilder.union(zoneE,zoneN);


        Assertions.assertEquals(testSansBuilderPartition,testBuilder.build());
    }


    @Test
    void unionThrowsExceptionError(){
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Set<Zone.Forest> zoneSetAllZones = new HashSet<>();
        Set<Zone.Forest> zoneSingleSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet = new HashSet<>();
        Set<Area<Zone.Forest>> areaSet2 = new HashSet<>();
        Set<Area<Zone.Forest>> areaSetFusion = new HashSet<>();
        List<PlayerColor> playerColorList = new ArrayList<>();

        Zone.Forest zoneN = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneE = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneS = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneNN = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneEE = new Zone.Forest(14, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSS = new Zone.Forest(15, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneSingle = new Zone.Forest(16, Zone.Forest.Kind.PLAIN);

        //On ajoute des Zones dans des Sets
        zoneSingleSet.add(zoneSingle);
        zoneSet1.add(zoneN);zoneSet1.add(zoneE);
        zoneSet2.add(zoneNN); zoneSet2.add(zoneEE);
        zoneSetAllZones.addAll(zoneSet1); zoneSetAllZones.addAll(zoneSet2);

        // On crée des Area
        Area<Zone.Forest> forestAreaTest2 = new Area<>(zoneSet2,playerColorList,2);
        Area<Zone.Forest> forestAreaTest = new Area<>(zoneSet1,playerColorList,2);
        Area<Zone.Forest> forestAreaTestAllZones = new Area<>(zoneSetAllZones,playerColorList,2);
        //on aura 2 openconnections dans fusion donc 2+2 -2 =2 donc c bon



        //On ajoute nos Area dans des sets d'areas
        areaSet.add(forestAreaTest2); areaSet.add(forestAreaTest);
        areaSet2.add(forestAreaTest2); areaSet2.add(forestAreaTest);
        areaSetFusion.add(forestAreaTestAllZones);


        //On crée les builders
        ZonePartition <Zone.Forest> testBuilderPartition = new ZonePartition<>(areaSet);
        ZonePartition.Builder<Zone.Forest> testBuilder = new ZonePartition.Builder<>(testBuilderPartition);

        Assertions.assertThrows(IllegalArgumentException.class, ()-> testBuilder.union(zoneSS,zoneS));
    }
}