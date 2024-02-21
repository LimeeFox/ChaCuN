package ch.epfl.chacun;

import java.util.List;

/**
 * Toutes les couleurs qui peuvent correspondre à un joeur
 * @author Vladislav Yarkovoy (362242)
 */
public enum PlayerColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    PURPLE;

    public static final List<PlayerColor> ALL = List.of(PlayerColor.values());

}
