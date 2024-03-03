package ch.epfl.chacun;

import java.util.*;
import java.util.function.Predicate;

/**
 * Aire formée par un ensemble de zones du même type connéctés
 *
 * @author Vladislav Yarkovoy (362242)
 *
 * @param zones
 * @param occupants
 * @param openConnections
 * @param <Z>
 */
public record  Area<Z extends Zone>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {

    public Area {
        Preconditions.checkArgument(openConnections > 0);

        Collections.sort(occupants);
        //this.zones = Set.copyOf(zones);
        //this.occupants = List.copyOf(occupants);
        // todo
    }

    /**
     * Détermine si une aire de forêts possède au moins un menhir
     *
     * @param forest
     *          aire de forêts dans laquelle nous cherchons un menhir
     * @return true si la forêt possède un menhir et false sinon.
     * @param <Z>
     */
    public static <Z extends Zone> boolean hasMenhir(Area<Zone.Forest> forest) {
        for (Zone.Forest zone : forest.zones()) {
            if (zone.kind().equals(Zone.Forest.Kind.WITH_MENHIR)) return true;
        }

        return false;
    }

    /**
     * Détermine la quantité de groupes de champignons dans une aire de forêt
     *
     * @param forest
     *          aire de forêts dans laquelle nous cherchons les groupes de champignons
     * @return le nombre de groupes de champignons trouvés dans la forêt
     */
    public static int mushroomGroupCount(Area<Zone.Forest> forest) {
        int count = 0;
        for (Zone.Forest zone : forest.zones()) {
            if (zone.kind().equals(Zone.Forest.Kind.WITH_MUSHROOMS)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Recherche de tous les animaux d'une aire de pré, avec possibilité de les filtrer
     *
     * @param meadow
     *          aire de prés dans laquelle on recherche tous les animaux
     * @param cancelledAnimals
     *          les animaux qu'on aimerait ignorer et ne pas ajouter dans l'ensemble
     * @return l'ensemble des animaux se trouvant dans le pré donné mais qui ne font pas partie de l'ensemble des animaux annulés donné,
     *         les animaux annulés pouvant p. ex. être des cerfs dévorés par des smilodons
     */
    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> animals = new HashSet<>();

        // Condition qu'on doit respecter (la liste qu'on veut retourner ne doit pas contenir le paramètre "cancelledAnimals")
        Predicate<Animal> animalFilter = animal -> !cancelledAnimals.contains(animal);

        // On rajoute tous les animaux qui respectent les critères donnés dans une nouvelle liste
        for (Zone.Meadow zone : meadow.zones) {
            zone.animals().stream().filter(animalFilter).forEach(animals::add);
        }

        return animals;
    }

    /**
     * Déterminer la quantité de poissons dans une aire de rivière (avec lacs)
     *
     * @param river
     * @return le nombre de poissons nageant dans la rivière donnée ou dans l'un des éventuels lacs se trouvant à ses extrémités
     *         les poissons d'un lac donné ne devant être comptés qu'une seule fois même dans le cas où un unique lac termine la rivière aux deux bouts
     */
    public static int riverFishCount(Area<Zone.River> river) {
        // todo
    }

    /**
     * Déterminer la quantité de poissons dans un réseau de rivières
     *
     * @param riverSystem
     *          aire de rivières
     * @return le nombre de poissons nageant dans un système hydrographique donné
     */
    public static int riverSystemFishCount(Area<Zone.Water> riverSystem) {
        // todo
    }

    /**
     * Déterminer la quantité de lacs dans un système hydrographique
     *
     * @param riverSystem
     *          aire de rivières qui possèdent un ou plusieurs lacs
     * @return le nombre de lacs dans l'air donnée
     */
    public static int lakeCount(Area<Zone.Water> riverSystem) {
        // todo
    }

    /**
     * Vérification si l'aire est fermée ou pas
     *
     * @return true si l'aire est fermée, false sinon
     */
    public boolean isClosed() {
        return openConnections == 0;
    }

    /**
     * Vérification si l'aire possède des zones occupées par au moins un occupent
     *
     * @return vrai ssi l'aire est occupée par au moins un occupant
     */
    public boolean isOccupied() {
        return !occupants.isEmpty();
    }

    /**
     * Recherche des joueur dominant/majoritaires de l'aire
     *
     * @return l'ensemble des occupants majoritaires de l'aire
     */
    public Set<PlayerColor> majorityOccupants() {
        int[] occurrence = new int[5];

        // D'abord compter les occurrences de chaque couleur
        for (PlayerColor occupant : occupants) {
            occurrence[PlayerColor.ALL.indexOf(occupant)]++;
        }

        // Trouver les couleurs de joueurs qui apparaissent le plus souvent
        int max = 0;

        for (int i = 0; i < occurrence.length; i++) {
            if (occurrence[i] > max) {
                max = occurrence[i];
            }
        }

        // Filtrer les joueurs qui n'ont pas autant d'occupants que "max"
        Set<PlayerColor> dominators = new HashSet<>();

        for (int i = 0; i < occurrence.length; i++) {
            if (occurrence[i] == max) {
                dominators.add(PlayerColor.ALL.get(i));
            }
        }

        return dominators;
    }

    /**
     * Méthode qui permet de connecter une aire à une autre
     *
     * @param that
     *          la tuile à laquelle on aimerait connecter le récepteur (this)
     * @return l'aire résultant de la connexion du récepteur (this) à l'aire donnée (that)
     */
    public Area<Z> connectTo(Area<Z> that) {
        // Je crois qu'il faut regarder les cotes libres et les supprimer dans les deux tuiles, mais attention car la tuile elle meme peut se passer en parametre
        // todo
    }

    /**
     * Méthode qui permet d'obtenir une aire identique à this, mais avec un occupent initial donné
     *
     * @param occupant
     *          la couleur du joueur qu'on aimerait ajouter en tant qu'occupent à l'aire
     * @return une aire identique au récepteur, si ce n'est qu'elle est occupée par l'occupant donné
     */
    public Area<Z> withInitialOccupant(PlayerColor occupant) {
        Preconditions.checkArgument(!occupants.isEmpty());

        return new Area<Z>(zones, List.of(occupant), openConnections);
    }

    /**
     * Méthode qui permet d'obtenir une aire identique à this, mais sans l'occupant donné
     *
     * @param occupant
     *          la couleur du joueur dont on aimerait se débarasser dans l'aire donnée
     * @return une aire identique au récepteur, mais qui comporte un occupant de la couleur donnée en moins
     */
    Area<Z> withoutOccupant(PlayerColor occupant) {
        Preconditions.checkArgument(occupants.contains(occupant));

        List<PlayerColor> updatedOccupants = new ArrayList<>(occupants);
        updatedOccupants.remove(occupant);

        return new Area<Z>(zones, updatedOccupants, openConnections);
    }

    /**
     * Méthode qui permet d'obtenir une aire identique à this, mais sans aucun occupant
     *
     * @return une aire identique au récepteur, mais totalement dénuée d'occupants
     */
    public Area<Z> withoutOccupants() {
        return new Area<Z>(zones, List.of(), openConnections);
    }

    /**
     * Détermine toutes les identifications de chaque tuile qui contient une zone dans cette aire
     *
     * @return l'ensemble de l'identité des tuiles contenant l'aire
     */
    public Set<Integer> tileIds() {
        Set<Integer> Ids = new HashSet<>();
        for (Zone zone : zones) {
            Ids.add(zone.tileId());
        }

        return Ids;
    }

    /**
     * Recherche de la zone qui contient un pouvoir spécial donné
     *
     * @param specialPower
     *          le pouvoir spécial dont on recherche la zone
     * @return la zone de l'aire qui possède le pouvoir spécial donné, ou null s'il n'en existe aucune.
     */
    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
        for (Zone zone : zones) {
            if (zone.specialPower() != null) {
                return zone;
            }
        }

        return null;
    }
}

