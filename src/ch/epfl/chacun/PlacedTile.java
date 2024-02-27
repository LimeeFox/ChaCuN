package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Tuile placée
 *
 * @author Vladislav Yarkovoy (362242)
 *
 * @param tile
 * @param placer
 * @param rotation
 * @param pos
 * @param occupant
 */
public record PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant) {

    // On ne veut pas que Tile, Rotation ou Pos soient null
    public PlacedTile {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }

    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }

    /**
     * Récupérer l'identification de la tuile placée
     *
     * @return L'identité de la tuile placée
     */
    public int id() {
        return tile.id();
    }

    /**
     * Récupérer le type de la tuile placée
     *
     * @return type de la tuile placée
     */
    public Tile.Kind kind() {
        return tile.kind();
    }

    /**
     * Récupérer le bord de tuile selon une direction spécifiée, en tenant compte de la rotation actuelle de la tuile
     *
     * @param direction
     * @return le bord de tuile selon la direction spécifiée, en tenant compte de la rotation actuelle de la tuile
     */
    public TileSide side(Direction direction) {
        Direction newDirection = direction.rotated(rotation);
        return switch (newDirection) {
            case N -> tile.n();
            case E -> tile.e();
            case S -> tile.s();
            case W -> tile.w();
        };
    }

    /**
     * Trouve la zone d'une tuile à partir d'un identifient de zone
     *
     * @param id
     *          l'identifiant de zone qu'on recherche
     * @return la zone de la tuile qui a le même ID spécifié, ou null si aucune zone avec cette ID existe dans la tuile.
     * @throws IllegalArgumentException
     */
    public Zone zoneWithId(int id) throws IllegalArgumentException {
        for (Zone zone : tile.zones()) {
            if (zone.id() == id) {
                return zone;
            }
        }

        throw new IllegalArgumentException();
    }

    /**
     * Recherche d'une zone contenant un pouvoir spécial, et null si il n'y a aucune zone avec un pouvoir spécial
     *
     * @return le pouvoir spécial de la zone, et null si la zone n'en a aucun
     */
    public Zone specialPowerZone() {
        for (Zone zone : tile.zones()) {
            if (zone.specialPower() != null) {
                return zone;
            }
        }

        return null;
    }

    /**
     * Recherche de toutes les zones du type forêt dans une tuile
     *
     * @return Ensemble de zones de type forêt de la tuile
     */
    public Set<Zone.Forest> forestZones() {
        final Set<Zone.Forest> forestZones = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Forest forest) {
                forestZones.add(forest);
            }
        }

        return forestZones;
    }

    /**
     * Recherche de toutes les zones du type pré dans une tuile
     *
     * @return Ensemble de zones de type pré de la tuile
     */
    public Set<Zone.Meadow> meadowZones() {
        final Set<Zone.Meadow> MeadowZones = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.Meadow meadow) {
                MeadowZones.add(meadow);
            }
        }

        return MeadowZones;
    }

    /**
     * Recherche de toutes les zones du type rivière dans une tuile
     *
     * @return Ensemble de zones de type rivière de la tuile
     */
    public Set<Zone.River> riverZones() {
        final Set<Zone.River> RiverZones = new HashSet<>();
        for (Zone zone : tile.zones()) {
            if (zone instanceof Zone.River river) {
                RiverZones.add(river);
            }
        }

        return RiverZones;
    }

    /**
     * Recherche de tous les occupents potentiels de la tuile placée
     *
     * @return un ensemble d'occupents potentiels de la tuile placée
     */
    public Set<Occupant> potentialOccupants() {
        final Set<Occupant> occupants = new HashSet<>();

        // On récupère les occupents qu'on peut placer dans la forêt
        for (Zone.Forest zone : forestZones()) {
            occupants.add(new Occupant(Occupant.Kind.PAWN, zone.zoneId()));
        }

        // On récupère les occupents qu'on peut placer dans un pré
        for (Zone.Meadow zone : meadowZones()) {
            occupants.add(new Occupant(Occupant.Kind.PAWN, zone.zoneId()));
        }

        // On récupère les occupents qu'on peut placer dans la rivière
        for (Zone.River zone : riverZones()) {
            final int zoneId = zone.zoneId();
            // Les rivières connectées à des lacs ne peuvent pas avoir de huttes
            if (zone.hasLake()) {
                occupants.add(new Occupant(Occupant.Kind.PAWN, zoneId));
                occupants.add(new Occupant(Occupant.Kind.HUT, zone.lake().zoneId()));
                continue;
            }
            // Sinon, elle peut contenir une hutte ou un pion
            occupants.add(new Occupant(Occupant.Kind.HUT, zoneId));
            occupants.add(new Occupant(Occupant.Kind.PAWN, zoneId));
        }

        return occupants;
    }

    /**
     * Obtention d'une copie de la tuile placée sans occupant au préalable, avec un nouveau occupant
     *
     * @param occupant
     * @return une copie de la tuile placée, mais avec le nouveau occupant passé en paramètre. Erreur si la tuile a déjà un occupant
     * @throws IllegalArgumentException
     */
    public PlacedTile withOccupant(Occupant occupant) throws IllegalArgumentException {
        Preconditions.checkArgument(this.occupant == null);
        return new PlacedTile(tile, placer, rotation, pos, occupant);
    }

    /**
     * Obtention d'une copie de la tuile placée, mais avec aucun occupant
     *
     * @return une tuile placée identique au récepteur, mais sans occupant
     */
    public PlacedTile withNoOccupant() {
        return new PlacedTile(tile, placer, rotation, pos, null);
    }

    /**
     * Recherche de l'identification de la zone où un occupent se situe
     *
     * @param occupantKind
     * @return l'identification de la zone où un occupent se situe
     */
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        return occupant.kind().equals(occupantKind) ? occupant.zoneId() : -1;
    }

}
