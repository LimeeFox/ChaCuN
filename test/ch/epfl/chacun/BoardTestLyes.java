package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/*
public class BoardTestLyes {

    /*IN ORDER TO TEST INSERTIONPOSITION REMOVE PRECONDITION IN WITHNEWTILE

    @Test
    void tileAtTestWorksWithStartTile(){

        var tileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        assertEquals(placedTileForest,Board.EMPTY.withNewTile(placedTileForest).tileAt(new Pos(0,0)));
    }

    @Test
    void tileAtTestWorksWithNormalTile(){

        var tileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        assertEquals(placedTileForest2,Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileForest2).tileAt(new Pos(0,1)));

    }

    @Test
    void tileAtTestWorksWithMultipleNormalTile(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2));


        assertEquals(placedTileForest2,Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                tileAt(new Pos(0,1)));

    }

    @Test
    void tileAtTestNullWithMultipleNormalTile(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2));

        assertNull(Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                tileAt(new Pos(0, 10)));

    }

    @Test
    void tileAtTestReturnsNull(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        Board.EMPTY.withNewTile(placedTileForest);
        assertNull(Board.EMPTY.tileAt(new Pos(0, 1)));
    }

    @Test
    void tileAtTestReturnsNullWithEmpty(){
        assertNull(Board.EMPTY.tileAt(new Pos(0, 0)));
    }

    @Test
    void tileWithIdWorks(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        assertEquals(placedTileForest,Board.EMPTY.withNewTile(placedTileForest).tileWithId(1));
    }

    @Test
    void tileWithIdWorksWithMultiple(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2));


        assertEquals(placedTileForest2 ,Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                tileWithId(2));

    }

    @Test
    void tileWithIdThrowsWhenTileAbsent(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        assertThrows(IllegalArgumentException.class, () -> Board.EMPTY.withNewTile(placedTileForest).tileWithId(2));
    }

    @Test
    void tileWithIdThrowsWhenEmptyBoard(){
        assertThrows(IllegalArgumentException.class, () -> Board.EMPTY.tileWithId(1));
    }

    @Test
    void cancelledAnimalsWorkWhenEmpty(){
        assertEquals(Set.of(), Board.EMPTY.cancelledAnimals());
    }

    @Test
    void cancelledAnimalsWorkWhenMoreAnimals(){

        Set<Animal> cancelledAnim = new HashSet<>(Set.of(
                new Animal(1, Animal.Kind.DEER),
                new Animal(2, Animal.Kind.DEER),
                new Animal(3, Animal.Kind.AUROCHS)
        ));

        assertEquals(cancelledAnim, Board.EMPTY.withMoreCancelledAnimals(cancelledAnim).cancelledAnimals());

    }

    @Test
    void cancelledAnimalsWorkWithEvenMoreAnimals(){

        Set<Animal> cancelledAnim = new HashSet<>(Set.of(
                new Animal(1, Animal.Kind.DEER),
                new Animal(2, Animal.Kind.DEER),
                new Animal(3, Animal.Kind.AUROCHS)
        ));

        Set<Animal> cancelledAnim2 = new HashSet<>(Set.of(
                new Animal(4, Animal.Kind.DEER),
                new Animal(5, Animal.Kind.DEER),
                new Animal(6, Animal.Kind.AUROCHS)
        ));

        Set<Animal> cancelledAnimFinal = new HashSet<>(cancelledAnim);
        cancelledAnimFinal.addAll(cancelledAnim2);

        assertEquals(cancelledAnimFinal, Board.EMPTY.withMoreCancelledAnimals(cancelledAnim).
                withMoreCancelledAnimals(cancelledAnim2).
                cancelledAnimals());
    }

    @Test
    void occupantsWorksWithMultipleTilesAndDiffOccAndNoneNull(){

        Occupant PAWN1 = new Occupant(Occupant.Kind.PAWN,111);
        Occupant PAWN2 = new Occupant(Occupant.Kind.PAWN,121);
        Occupant HUT1 = new Occupant(Occupant.Kind.HUT,131);

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0),PAWN1);

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1),PAWN2);

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2),HUT1);

        Set<Occupant> Occupants = new HashSet<>(Set.of(
                HUT1,PAWN1,PAWN2
        ));


        assertEquals(Occupants,Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                occupants());

    }

    @Test
    void occupantsWorksWithMultipleTilesAndDiffOccAndSomeNull(){

        Occupant PAWN1 = new Occupant(Occupant.Kind.PAWN,111);
        Occupant HUT1 = new Occupant(Occupant.Kind.HUT,131);

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0),PAWN1);

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2),HUT1);

        Set<Occupant> Occupants = new HashSet<>(Set.of(
                HUT1,PAWN1
        ));


        assertEquals(Occupants,Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                occupants());

    }

    @Test
    void occupantsWorksWithMultipleTilesAndSameOcc(){

        Occupant PAWN1 = new Occupant(Occupant.Kind.PAWN,111);
        Occupant HUT1 = new Occupant(Occupant.Kind.HUT,131);

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0),PAWN1);

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1),PAWN1);

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2),HUT1);

        Set<Occupant> Occupants = new HashSet<>(Set.of(
                HUT1,PAWN1
        ));


        assertEquals(Occupants,Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                occupants());

    }

    @Test
    void occupantsWorksWithoutOcc(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2));


        assertEquals(Set.of(),Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                occupants());

    }

    @Test
    void occupantCountWorksWhenNone(){
        /* TODO
    }

    @Test
    void occupantCountWorksWhenSomeAllSame(){
        /* TODO
    }

    @Test
    void occupantCountWorksWhenSomeButDiff(){
        /* TODO
    }

    @Test
    void occupantCountWorksWhenSomeSameSomeDiff(){
        /* TODO
    }

    @Test
    void insertionPositionsWorksWhenSome(){
        Set<Pos> setPos = new HashSet<>(Set.of(
                new Pos(0,-2),new Pos(0,1),new Pos(-1,-1),new Pos(-1,0),new Pos(1,0),new Pos(1,-1)));

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,-1));

        assertEquals(setPos,Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileForest2).insertionPositions());

    }

    @Test
    void insertionPositionsWorksWhenOne(){

        Set<Pos> setPos = new HashSet<>(Set.of(
                new Pos(0,1),new Pos(-1,0),new Pos(1,0),new Pos(0,-1)));

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));


        assertEquals(setPos,Board.EMPTY.withNewTile(placedTileForest).insertionPositions());

    }

    @Test
    void insertionPositionsWorksWhenFull(){
        /* TODO
    }

    @Test
    void insertionPositionsFailsOnCorners(){

        Set<Pos> setPos = new HashSet<>(Set.of(
                new Pos(-11,-12), new Pos(-12,-11),new Pos(11,12), new Pos(12,11),new Pos(11,-12),new Pos(12,-11),
                new Pos(-11,12),new Pos(-12,11)
        ));

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(-12,-12));

        var placedTileForest2 = new PlacedTile(tileForest,PlayerColor.BLUE,Rotation.NONE,
                new Pos(12,12));

        var placedTileForest3 = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(12,-12));

        var placedTileForest4 = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(-12,12));

        assertEquals(setPos,Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileForest4).withNewTile(placedTileForest3)
                .withNewTile(placedTileForest2).insertionPositions());
    }


    @Test
    void lastPlacedTileWorkWhenStart(){

        var tileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));


        assertEquals(placedTileForest,Board.EMPTY.withNewTile(placedTileForest).
                lastPlacedTile());

    }

    @Test
    void lastPlacedTileReturnsNullWhenEmpty(){
        assertNull(Board.EMPTY.lastPlacedTile());
    }

    @Test
    void lastPlacedTileWorksWhenFull(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2));

        assertEquals(placedTileForest3,Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                lastPlacedTile());

    }


    @Test

        /* TODO: TEST FONCTIONNE MAIS PAS SUR QU'IL TESTE QUELQUE CHOSE

    void forestsClosedByLastTileWorksWhenNone(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        var placedTileForest3 = new PlacedTile(tileForest3,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2));

        assertEquals(Set.of(),Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                withNewTile(placedTileForest3).
                forestsClosedByLastTile());

    }

    @Test
    void forestsClosedByLastTileWorksWhenSome(){

        /* TODO: VERIF LE TEST

        Zone.Forest forest1 = new Zone.Forest(111, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(112, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(113, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest4 = new Zone.Forest(114, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest5 = new Zone.Forest(121, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest6 = new Zone.Forest(122, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest7 = new Zone.Forest(123, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest8 = new Zone.Forest(124, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest9 = new Zone.Forest(131, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest10 = new Zone.Forest(132, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest11 = new Zone.Forest(133, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest12 = new Zone.Forest(134, Zone.Forest.Kind.PLAIN);

        //Tile tileWithLake = new Tile(1, Tile.Kind.NORMAL,)

        Area<Zone.Forest> ForestAreas1 = new Area<>(Set.of(forest1,forest2,forest3,forest4),
                List.of(PlayerColor.RED),0);
        Area<Zone.Forest> ForestAreas2 = new Area<>(Set.of(forest5,forest6,forest7,forest8),
                List.of(PlayerColor.BLUE),10);
        Area<Zone.Forest> ForestAreas3 = new Area<>(Set.of(forest9,forest10,forest11,forest12),
                List.of(PlayerColor.GREEN),10);

        Set<Area<Zone.Forest>> setForestAreas = new HashSet<>(Set.of(ForestAreas1,ForestAreas2,ForestAreas3));

        ZonePartition<Zone.Forest> zonePartitionForests = new ZonePartition<>(setForestAreas);
        ZonePartitions ZPS = new ZonePartitions(zonePartitionForests, new ZonePartition<Zone.Meadow>(),
                new ZonePartition<Zone.River>(), new ZonePartition<Zone.Water>());

        /*
        TODO: comment faire pour créer le board avec les openCo que je veux sans me casser la tête à utiliser addTile?

        //assertEquals(Set.of(ForestAreas1),Board.EMPTY.);

    }

    @Test
    void forestsClosedByLastTileReturnsEmptyWhenEmpty(){
        assertEquals(Set.of(),Board.EMPTY.forestsClosedByLastTile());
    }

    @Test
    void couldPlaceTileWorksWithStartTile(){

        var tileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        assertTrue(Board.EMPTY.couldPlaceTile(tileForest));

    }

    @Test
    void couldPlaceTileWorksWithoutRota(){

        var tileForest = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(122, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(123, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(124, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileForest3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(133, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(134, Zone.Forest.Kind.PLAIN)));

        assertTrue(Board.EMPTY.withNewTile(placedTileForest).
                withNewTile(placedTileForest2).
                couldPlaceTile(tileForest3));

    }

    @Test
    void couldPlaceTileWorksWithRota(){

        var meadow1 = new Zone.Meadow(211,List.of(),null);
        var meadow2 = new Zone.Meadow(221,List.of(),null);
        var meadow3 = new Zone.Meadow(231,List.of(),null);
        var meadow4 = new Zone.Meadow(241,List.of(),null);
        var meadow5 = new Zone.Meadow(251,List.of(),null);
        var meadow6 = new Zone.Meadow(261,List.of(),null);
        var river1 = new Zone.River(311,0,null);
        var river2 = new Zone.River(321,0,null);
        var river3 = new Zone.River(331,0,null);

        var complexTileRiver = new Tile(2, Tile.Kind.START,
                new TileSide.River(meadow1,river1,meadow2),
                new TileSide.River(meadow3,river2,meadow4),
                new TileSide.River(meadow5,river3,meadow6),
                new TileSide.Forest(new Zone.Forest(888, Zone.Forest.Kind.PLAIN)));

        var placedRiver = new PlacedTile(complexTileRiver,PlayerColor.RED,Rotation.NONE,new Pos(0,0));

        var complexTileForestEast = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(new Zone.Meadow(211,List.of(),null)),
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(221,List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(231,List.of(),null)));

        var complexTileForestEast2 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Meadow(new Zone.Meadow(211,List.of(),null)),
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.WITH_MUSHROOMS)),
                new TileSide.Meadow(new Zone.Meadow(221,List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(231,List.of(),null)));

        assertTrue(Board.EMPTY.withNewTile(placedRiver).couldPlaceTile(complexTileForestEast));
        assertTrue(Board.EMPTY.withNewTile(placedRiver).couldPlaceTile(complexTileForestEast2));

    }

    @Test
    void couldPlaceTileFailsWithRota(){

        var meadow1 = new Zone.Meadow(211,List.of(),null);
        var meadow2 = new Zone.Meadow(221,List.of(),null);
        var meadow3 = new Zone.Meadow(231,List.of(),null);
        var meadow4 = new Zone.Meadow(241,List.of(),null);
        var meadow5 = new Zone.Meadow(251,List.of(),null);
        var meadow6 = new Zone.Meadow(261,List.of(),null);
        var meadow7 = new Zone.Meadow(271,List.of(),null);
        var meadow8 = new Zone.Meadow(281,List.of(),null);
        var river1 = new Zone.River(311,0,null);
        var river2 = new Zone.River(321,0,null);
        var river3 = new Zone.River(331,0,null);
        var river4 = new Zone.River(341,0,null);

        var complexTileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(131, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(141, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(complexTileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var complexTileRiver = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.River(meadow1,river1,meadow2),
                new TileSide.River(meadow3,river2,meadow4),
                new TileSide.River(meadow5,river3,meadow6),
                new TileSide.River(meadow7,river4,meadow8));


        assertFalse(Board.EMPTY.withNewTile(placedTileForest).couldPlaceTile(complexTileRiver));


    }

    @Test
    void adjacentMeadowWorksWhenEmpty(){

        var meadow1 = new Zone.Meadow(211,List.of(),null);
        var tilePit = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow1),new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)));

        var placedTilePit = new PlacedTile(tilePit,PlayerColor.RED,Rotation.NONE,new Pos(0,0));

        Area<Zone.Meadow> expected = new Area<>(Set.of(meadow1),List.of(PlayerColor.RED),0);

        assertEquals(expected,Board.EMPTY.withNewTile(placedTilePit).adjacentMeadow(new Pos(0,0),meadow1));

    }

    @Test
    void adjacentMeadowWorksWhenMultiple(){

        var meadow1 = new Zone.Meadow(211,List.of(),null);
        var meadow2 = new Zone.Meadow(221,List.of(),null);
        var meadow3 = new Zone.Meadow(231,List.of(),null);
        var meadow4 = new Zone.Meadow(241,List.of(),null);
        var meadow5 = new Zone.Meadow(251,List.of(),null);
        var meadow6 = new Zone.Meadow(261,List.of(),null);
        var meadow7 = new Zone.Meadow(271,List.of(),null);
        var meadow8 = new Zone.Meadow(281,List.of(),null);
        var meadow9 = new Zone.Meadow(282,List.of(),null);
        var meadow10 = new Zone.Meadow(283,List.of(),null);
        var meadow11 = new Zone.Meadow(284,List.of(),null);
        var meadow12 = new Zone.Meadow(285,List.of(),null);
        var meadow13 = new Zone.Meadow(286,List.of(),null);
        var meadow14 = new Zone.Meadow(287,List.of(),null);
        var forest1 = new Zone.Forest(400, Zone.Forest.Kind.PLAIN);
        var forest2 = new Zone.Forest(401, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(402, Zone.Forest.Kind.PLAIN);
        var forest4 = new Zone.Forest(403, Zone.Forest.Kind.PLAIN);
        var forest5 = new Zone.Forest(404, Zone.Forest.Kind.PLAIN);
        var forest6 = new Zone.Forest(405, Zone.Forest.Kind.PLAIN);
        var forest7 = new Zone.Forest(406, Zone.Forest.Kind.PLAIN);
        var river1 = new Zone.River(311,0,null);
        var river2 = new Zone.River(321,0,null);
        var river3 = new Zone.River(331,0,null);
        var river4 = new Zone.River(341,0,null);

        var tilePit = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow1),new TileSide.Meadow(meadow2),
                new TileSide.Meadow(meadow3),new TileSide.Meadow(meadow4));
        var placedTilePit = new PlacedTile(tilePit,PlayerColor.RED,Rotation.NONE,new Pos(0,0));

        var tileMeadow1 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow5),new TileSide.Meadow(meadow6),
                new TileSide.Meadow(meadow7),new TileSide.Meadow(meadow8));
        var placedTileMeadow1 = new PlacedTile(tileMeadow1,PlayerColor.RED,Rotation.NONE,new Pos(-1,-1));

        var tileMeadow2 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow9),new TileSide.Meadow(meadow10),
                new TileSide.Meadow(meadow11),new TileSide.Meadow(meadow12));
        var placedTileMeadow2 = new PlacedTile(tileMeadow2,PlayerColor.RED,Rotation.NONE,new Pos(1,0));

        var tileRiver = new Tile(4, Tile.Kind.NORMAL,
                new TileSide.River(meadow13,river1,meadow14),new TileSide.Forest(forest1),
                new TileSide.Forest(forest2),new TileSide.Forest(forest3));
        var placedTileRiver = new PlacedTile(tileRiver,PlayerColor.RED,Rotation.LEFT,new Pos(-1,0));

        var tileForest = new Tile(5, Tile.Kind.NORMAL,
                new TileSide.Forest(forest4), new TileSide.Forest(forest5),
                new TileSide.Forest(forest6), new TileSide.Forest(forest7));
        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,new Pos(0,-1));

        Area<Zone.Meadow> expected = new Area<>(Set.of(meadow1,meadow2,meadow3,meadow4,meadow5,meadow6,meadow7,meadow8,meadow9,meadow10,meadow11,meadow12,meadow13,meadow14),List.of(PlayerColor.RED),0);

        assertEquals(expected,Board.EMPTY.withNewTile(placedTilePit).
                withNewTile(placedTileMeadow2).
                withNewTile(placedTileRiver).
                withNewTile(placedTileMeadow1).
                withNewTile(placedTileForest).adjacentMeadow(new Pos(0,0),meadow1));

    }

    @Test
    void canAddTileWorksForInitial(){

        var tileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        assertTrue(Board.EMPTY.canAddTile(placedTileForest));

    }

    @Test
    void canAddTileWorksForNormalAndFailsForNormalWhenWrongKindOrWrongPos(){

        var tileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(143, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(154, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,-1));

        var placedTileForest3 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(10,-1));

        var tileMeadow = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Meadow(new Zone.Meadow(161, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(172, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(183, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(194, List.of(),null)));

        var placedTileMeadow = new PlacedTile(tileMeadow,PlayerColor.RED,Rotation.NONE,
                new Pos(0,-1));

        assertTrue(Board.EMPTY.withNewTile(placedTileForest).canAddTile(placedTileForest2));
        assertFalse(Board.EMPTY.withNewTile(placedTileForest).canAddTile(placedTileForest3));
        assertFalse(Board.EMPTY.withNewTile(placedTileForest).canAddTile(placedTileMeadow));

    }

    @Test
    void canAddTileWorksForTwoSides(){

        var tileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(143, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(154, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var tileMeadow = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Meadow(new Zone.Meadow(161, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(172, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(183, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(194, List.of(),null)));

        var placedTileMeadow = new PlacedTile(tileMeadow,PlayerColor.RED,Rotation.NONE,
                new Pos(1,-1));

        var tile = new Tile(4, Tile.Kind.NORMAL,
                new TileSide.Meadow(new Zone.Meadow(261, List.of(),null)),
                new TileSide.Forest(new Zone.Forest(172, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(183, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(294, Zone.Forest.Kind.PLAIN)));

        var placedTileShouldWork = new PlacedTile(tile,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));

        var placedTileShouldntWork = new PlacedTile(tile,PlayerColor.RED,Rotation.LEFT,
                new Pos(0,1));

        assertFalse(Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileMeadow).canAddTile(placedTileForest2));
        assertTrue(Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileMeadow).canAddTile(placedTileShouldWork));
        assertFalse(Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileMeadow).canAddTile(placedTileShouldntWork));

    }

    @Test
    void canAddTileForThreeSides(){

        var meadow1 = new Zone.Meadow(211,List.of(),null);
        var meadow2 = new Zone.Meadow(221,List.of(),null);
        var river = new Zone.River(223,0,new Zone.Lake(0,0,null));

        var tileForest = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(111, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(112, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(113, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(114, Zone.Forest.Kind.PLAIN)));

        var placedTileForest = new PlacedTile(tileForest,PlayerColor.RED,Rotation.NONE,
                new Pos(0,0));

        var tileForest2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(132, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(143, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(154, Zone.Forest.Kind.PLAIN)));

        var placedTileForest2 = new PlacedTile(tileForest2,PlayerColor.RED,Rotation.NONE,
                new Pos(0,2));

        var tileMeadow = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Meadow(new Zone.Meadow(161, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(172, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(183, List.of(),null)),
                new TileSide.Meadow(new Zone.Meadow(194, List.of(),null)));

        var placedTileMeadow = new PlacedTile(tileMeadow,PlayerColor.RED,Rotation.NONE,
                new Pos(1,-1));

        var tile = new Tile(4, Tile.Kind.NORMAL,
                new TileSide.Meadow(new Zone.Meadow(601, List.of(),null)),
                new TileSide.Forest(new Zone.Forest(602, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow1,river,meadow2),
                new TileSide.Forest(new Zone.Forest(603, Zone.Forest.Kind.PLAIN)));

        var placedTileWorking = new PlacedTile(tile,PlayerColor.RED,Rotation.NONE,
                new Pos(0,1));
        var placedTileNotWorking1 = new PlacedTile(tile,PlayerColor.RED,Rotation.RIGHT,
                new Pos(0,1));
        var placedTileNotWorking2 = new PlacedTile(tile,PlayerColor.RED,Rotation.HALF_TURN,
                new Pos(0,1));
        var placedTileNotWorking3 = new PlacedTile(tile,PlayerColor.RED,Rotation.LEFT,
                new Pos(0,1));

        assertTrue(Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileMeadow).withNewTile(placedTileForest2).
                canAddTile(placedTileWorking));
        assertFalse(Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileMeadow).withNewTile(placedTileForest2).
                canAddTile(placedTileNotWorking1));
        assertFalse(Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileMeadow).withNewTile(placedTileForest2).
                canAddTile(placedTileNotWorking2));
        assertFalse(Board.EMPTY.withNewTile(placedTileForest).withNewTile(placedTileMeadow).withNewTile(placedTileForest2).
                canAddTile(placedTileNotWorking3));

    }

     TODO: TESTER:

    - forestclosedby (test probablement faux)
    - riverSystemArea (toutes les méthodes à 1 ligne)
    - riverSystemAreas (toutes les méthodes à 1 ligne)
    - adjacentMeadow (test peut etre faux)
    - occupantCount


    TODO: REGLER
    -couldPlaceTile
    -adjacentMeadow
}

 */


