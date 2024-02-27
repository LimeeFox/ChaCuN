package ch.epfl.chacun;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Les 3 piles de tuile des 3 sortes différentes : début, normal, avec menhir
 *
 * @author Cyriac Philippe (360553)
 *
 * @param startTiles
 *          tas qui contient la tuile de départ (ou rien, si la tuile à été placée)
 * @param normalTiles
 *          tas qui contient les tuiles normales
 * @param menhirTiles
 *          tas qui contient les tuiles menhir
 */
public final record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    public TileDecks {
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    /**
     * Nombre de tuiles disponibles dans une pile d'un type demandé
     *
     * @param kind
     *          type de la pile de tuiles
     * @return deckKindSize
     *          taille de la pile de tuiles du type demandé
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
     *          type de tuile demandé
     * @return tTile
     *          tuile en haut de la pile
     */
    public Tile topTile(Tile.Kind kind) {
        Tile tTile = null;
        switch (kind) {
            case START : if (!startTiles.isEmpty()) {tTile = startTiles.getFirst();}
            case NORMAL : tTile = normalTiles.getFirst();
            case MENHIR : tTile = menhirTiles.getFirst();
        }
        return tTile;
    }

    /**
     * Elimination de la première tuile d'une pile d'un type demandé
     *
     * @param kind
     *          type de pile demandé
     * @return drawnDecks
     *          nouveau triplet de pile de tuiles
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        TileDecks drawnDecks = new TileDecks(startTiles, normalTiles, menhirTiles);
        switch (kind) {
            case START :
                Preconditions.checkArgument(!startTiles.isEmpty());
                drawnDecks = new TileDecks(startTiles.subList(1, startTiles.size() - 1), normalTiles, menhirTiles);
            case NORMAL :
                Preconditions.checkArgument(!normalTiles.isEmpty());
                drawnDecks = new TileDecks(startTiles, normalTiles.subList(1, normalTiles.size() - 1), menhirTiles);
            case MENHIR :
                Preconditions.checkArgument(!menhirTiles.isEmpty());
                drawnDecks = new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, menhirTiles.size() - 1));
        }
        return drawnDecks;
    }

    /**
     * Elimination des premières tuiles d'une pile d'un type demandé jusqu'à complétion d'une condition donnée
     *
     * @param kind
     *          type de pile demandé
     * @param predicate
     *          condition à remplir pour arrêter la pioche
     * @return drawnDecks
     *          nouveau triplet de piles de tuiles
     */
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        TileDecks drawnDecks = new TileDecks(startTiles, normalTiles, menhirTiles);
        switch (kind) {
            case START : while (!startTiles.isEmpty() && !predicate.test(startTiles.getFirst())){
                drawnDecks = withTopTileDrawn(kind);
            }
            case NORMAL : while (!normalTiles.isEmpty() && predicate.test(normalTiles.getFirst())){
                drawnDecks = withTopTileDrawn(kind);
            }
            case MENHIR : while (!menhirTiles.isEmpty() && predicate.test(menhirTiles.getFirst())){
                drawnDecks = withTopTileDrawn(kind);
            }
        }
        return drawnDecks;
    }
}
