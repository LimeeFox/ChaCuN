package ch.epfl.chacun;

import java.util.List;
import java.util.function.Predicate;

/**
 * Les 3 piles de tuile des 3 sortes différentes : début, normal, avec menhir
 *
 * @param startTiles
 *         tas qui contient la tuile de départ (ou rien, si la tuile a été placée)
 * @param normalTiles
 *         tas qui contient les tuiles normales
 * @param menhirTiles
 *         tas qui contient les tuiles menhir
 * @author Vladislav Yarkovoy (362242)
 * @author Cyriac Philippe (360553)
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    public TileDecks {
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    /**
     * Nombre de tuiles disponibles dans une pile d'un type demandé
     *
     * @param kind
     *         type de la pile de tuiles
     * @return taille de la pile de tuiles du type demandé
     */
    public int deckSize(Tile.Kind kind) {
        return getTileDeckForKind(kind).size();
    }

    /**
     * Première tuile d'une pile de tuile d'un type demandé
     *
     * @param kind
     *         type de tuile demandé
     * @return tuile en haut de la pile
     */
    public Tile topTile(Tile.Kind kind) {
        return getTileDeckForKind(kind).getFirst();
    }

    /**
     * Elimination de la première tuile d'une pile d'un type demandé
     *
     * @param kind
     *         type de pile demandé
     * @return nouveau triplet de pile de tuiles
     * @throws IllegalArgumentException
     *         si ce tas est vide
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        Preconditions.checkArgument(deckSize(kind) > 0);

        return switch (kind) {
            case START -> new TileDecks(startTiles.subList(1, deckSize(Tile.Kind.START)), normalTiles, menhirTiles);
            case NORMAL -> new TileDecks(startTiles, normalTiles.subList(1, deckSize(Tile.Kind.NORMAL)), menhirTiles);
            case MENHIR -> new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, deckSize(Tile.Kind.MENHIR)));
        };
    }

    /**
     * Elimination des premières tuiles d'une pile d'un type demandé jusqu'à complétion d'une condition donnée
     *
     * @param kind
     *         type de pile demandé
     * @param predicate
     *         condition à remplir pour arrêter la pioche
     * @return nouveau triplet de piles de tuiles
     */
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        TileDecks drawnDecks = this;
        while (drawnDecks.deckSize(kind) > 0 && !predicate.test(topTile(kind))) {
            drawnDecks = drawnDecks.withTopTileDrawn(kind);
        }
        return drawnDecks;
    }

    /**
     * Méthode d'aide qui permet d'obtenir un tas de tuile d'un type donnée
     *
     * @param kind
     *          type du tas de tuiles recherché
     * @return tileDeck
     *          le tas de tuile du type de tuile donné
     */
    private List<Tile> getTileDeckForKind(Tile.Kind kind) {
        List<Tile> tileDeck= List.of();

        switch (kind) {
            case START -> tileDeck = startTiles;
            case NORMAL -> tileDeck = normalTiles;
            case MENHIR -> tileDeck = menhirTiles;
        }
        return tileDeck;
    }
}
