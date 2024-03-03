package ch.epfl.chacun;

/**
 * Calcul des points
 *
 * @author Cyriac Philippe (360553)
 */
public final class Points {
    private Points() {}

    /**
     * Calcul des points pour une forêt fermée
     *
     * @param tileCount
     *          taille de la forêt en tuiles
     * @param mushroomCount
     *          nombre de champignons dans la forêt
     * @return points
     *          nombre de points obtenus selon la formule
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
     *          taille ou longueur de la rivière en tuiles
     * @param fishCount
     *          nombre de poissons dans la rivière
     * @return points
     *          nombre de points obtenus selon la formule
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
     *          nombre de mammouths dans le pré
     * @param aurochsCount
     *          nombre de aurochs dans le pré
     * @param deerCount
     *          nombre de cerfs dans le pré
     * @return points
     *          nombre de points obtenus selon la formule
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
     *          nombre de poisson dans le réseau hydroponique
     * @return points
     *      nombre de points obtenus selon la formule
     */
    public static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);
        int points = fishCount;
        return points;
    }

    /**
     * Calcul des points pour un joueur qui place la pirogue
     *
     * @param lakeCount
     *          nombre de lacs dans le même réseau hydroponique
     *          que la pirogue
     * @return points
     *          nombre de points obtenus selon la formule
     */
    public static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        int points = 2*lakeCount;
        return points;
    }

    /**
     * Calcul des points obtenus dans le réseau hydroponique
     * contenant le radeau
     *
     * @param lakeCount
     *          nombre de lacs dans le réseau hydroponique
     * @return points
     *          nombre de points obtenus selon la formule
     */
    public static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        int points = lakeCount;
        return points;
    }
}
