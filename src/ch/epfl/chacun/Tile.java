package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tuile pas encore placée sur le plateau de jeu
 *
 * @author Cyriac Philipe (360553)
 * @author Vladislav Yarkovoy (362242)
 *
 * @param id
 * @param kind
 * @param n
 * @param e
 * @param s
 * @param w
 */
public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {

    /**
     * Les 3 types de tuile : début, normal, avec menhir
     */
    public enum Kind {
        START,
        NORMAL,
        MENHIR
    }

    /**
     * Liste des quatre côtés de la tuile : n, e, s, w
     *
     * @return sideList
     *          liste des côtés de la tuile
     */
    public List<TileSide> sides() {
        List<TileSide> sideList = List.of(n, e, s, w);
        return sideList;
    }

    /**
     * Zones en bordure de tuile
     *
     * @return tileSideZones
     *          set des zones en bord de tuile
     */
    public Set<Zone> sideZones() {
        Set<Zone> tileSideZones = new HashSet<>(Set.of());
        for (TileSide side : sides()) {
            for (Zone zone : side.zones()) {
                if (!(zone instanceof Zone.Lake)) {
                    tileSideZones.add(zone);
                }
            }
        }
        return tileSideZones;
    }

    /**
     * Zones de la tuile
     *
     * @return tileZones
     *          ensemble de zones que possède la tuile
     */
    public Set<Zone> zones() {
        Set<Zone> tileZones = new HashSet<>(sideZones());
        for (Zone sZone : sideZones()) {
            if (sZone instanceof Zone.River river) {
                if (river.hasLake()) {
                    tileZones.add(river.lake());
                }
            }
        }
        return tileZones;
    }
}
