package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tuile pas encore placée sur le plateau de jeu
 *
 * @param id
 *         identité de la tuile
 * @param kind
 *         le type de la tuile
 * @param n
 *         le côté NORD de la tuile
 * @param e
 *         le côté EST de la tuile
 * @param s
 *         le côté SUD de la tuile
 * @param w
 *         le côté OUEST de la tuile
 * @author Cyriac Philipe (360553)
 * @author Vladislav Yarkovoy (362242)
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
     * @return liste des côtés de la tuile
     */
    public List<TileSide> sides() {
        List<TileSide> sideList = List.of(n, e, s, w);
        return sideList;
    }

    /**
     * Zones en bordure de tuile
     *
     * @return ensemble des zones en bord de tuile
     */
    public Set<Zone> sideZones() {
        Set<Zone> tileSideZones = new HashSet<>(Set.of());
        for (TileSide side : sides()) {
            tileSideZones.addAll(side.zones());
        }
        return tileSideZones;
    }

    /**
     * Zones de la tuile
     *
     * @return ensemble de zones que possède la tuile
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
