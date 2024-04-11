package ch.epfl.chacun;

/**
 * Calcul des points
 *
 * @author Cyriac Philippe (360553)
 */
public final class Points {
    // Constructeur privé pour prévenir l'instanciation de la classe
    private Points() {
    }

    /**
     * Calcul des points pour une forêt fermée
     *
     * @param tileCount
     *         taille de la forêt en tuiles
     * @param mushroomCount
     *         nombre de champignons dans la forêt
     * @return nombre de points obtenus selon la formule
     */
    public static int forClosedForest(int tileCount, int mushroomCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(mushroomCount >= 0);
        int points = tileCount * 2;
        points += mushroomCount * 3;
        return points;
    }

    /**
     * Calcul des points pour une rivière fermée
     *
     * @param tileCount
     *         taille ou longueur de la rivière en tuiles
     * @param fishCount
     *         nombre de poissons dans la rivière
     * @return nombre de points obtenus selon la formule
     */
    public static int forClosedRiver(int tileCount, int fishCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(fishCount >= 0);
        int points = tileCount;
        points += fishCount;
        return points;
    }

    /**
     * Calcul des points pour un pré, en fin de partie
     *
     * @param mammothCount
     *         nombre de mammouths dans le pré
     * @param aurochsCount
     *         nombre de aurochs dans le pré
     * @param deerCount
     *         nombre de cerfs dans le pré
     * @return nombre de points obtenus selon la formule
     */
    public static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        Preconditions.checkArgument(mammothCount >= 0);
        Preconditions.checkArgument(aurochsCount >= 0);
        Preconditions.checkArgument(deerCount >= 0);
        int points = mammothCount * 3;
        points += aurochsCount * 2;
        points += deerCount;
        return points;
    }

    /**
     * Calcul des points dans un réseau hydroponique
     *
     * @param fishCount
     *         nombre de poisson dans le réseau hydroponique
     * @return nombre de points obtenus selon la formule
     */
    public static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);
        return fishCount;
    }

    /**
     * Calcul des points pour un joueur qui place la pirogue
     *
     * @param lakeCount
     *         nombre de lacs dans le même réseau hydroponique
     *         que la pirogue
     * @return nombre de points obtenus selon la formule
     */
    public static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return 2 * lakeCount;
    }

    /**
     * Calcul des points obtenus dans le réseau hydroponique
     * contenant le radeau
     *
     * @param lakeCount
     *         nombre de lacs dans le réseau hydroponique
     * @return nombre de points obtenus selon la formule
     */
    public static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount;
    }
}
