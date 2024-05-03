package ch.epfl.chacun;

public class Base32 {
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    /**
     * Vérifie la validité d'une chaîne de charactèrs en base 32
     *
     * @param s
     *      chaîne de charactèrs à vérifier
     * @return true si la chaîne de charactèrs donnée ne contient que des charactèrs de l'ALPHABET
     */
    public boolean isValid(String s) {
        return s.chars().allMatch(c -> ALPHABET.indexOf((char) c) != -1);
    }


}
