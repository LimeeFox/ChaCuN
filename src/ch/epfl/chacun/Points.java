package ch.epfl.chacun;

public final class Points {
    private Points(){}

    public static int forClosedForest(int tileCount, int mushroomCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(mushroomCount >= 0);
        int points = tileCount*2;
        points += mushroomCount*3;
        return points;
    }

    public static int forClosedRiver(int tileCount, int fishCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(fishCount >= 1);
        int points = tileCount;
        points += fishCount;
        return points;
    }

    public static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        Preconditions.checkArgument(mammothCount >= 0);
        Preconditions.checkArgument(aurochsCount >= 0);
        Preconditions.checkArgument(deerCount >= 0);
        int points = mammothCount*3;
        points += aurochsCount*2;
        points += deerCount;
        return points;
    }

    public static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);
        int points = fishCount;
        return points;
    }

    public static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        int points = 2*lakeCount;
        return points;
    }

    public static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        int points = lakeCount;
        return points;
    }
}
