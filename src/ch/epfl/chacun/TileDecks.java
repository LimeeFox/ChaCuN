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
        int deckKindSize = 0;
        switch (kind) {
            case START -> deckKindSize = startTiles.size();
            case NORMAL -> deckKindSize = normalTiles.size();
            case MENHIR -> deckKindSize = menhirTiles.size();
        }
        return deckKindSize;
    }

    /**
     * Première tuile d'une pile de tuile d'un type demandé
     *
     * @param kind
     *         type de tuile demandé
     * @return tuile en haut de la pile
     */
    public Tile topTile(Tile.Kind kind) {
        Tile tile = null;
        switch (kind) {
            case START:
                if (!startTiles.isEmpty()) {
                    tile = startTiles.getFirst();
                    break;
                }
            case NORMAL:
                if (!normalTiles.isEmpty()) {
                    tile = normalTiles.getFirst();
                    break;
                }
            case MENHIR:
                if (!menhirTiles.isEmpty()) {
                    tile = menhirTiles.getFirst();
                    break;
                }
        }
        return tile;
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
            case START -> new TileDecks(startTiles.subList(1, startTiles.size()), normalTiles, menhirTiles);
            case NORMAL -> new TileDecks(startTiles, normalTiles.subList(1, normalTiles.size()), menhirTiles);
            case MENHIR -> new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, menhirTiles.size()));
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
}
