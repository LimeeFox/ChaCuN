package ch.epfl.chacun;

/**
 * Classe contenant des méthodes permettant d'encoder et de décoder des valeurs binaires en base 32
 *
 * @author Cyriac Philippe (360553)
 */
public class Base32 {
    // Alphabet complet de la base 32
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    /**
     * Vérifie la validité d'une chaîne de charactèrs en base 32
     *
     * @param s
     *      chaîne de charactèrs à vérifier
     * @return true si la chaîne de charactèrs donnée ne contient que des charactèrs de l'ALPHABET
     */
    public static boolean isValid(String s) {
        return s.toUpperCase().chars().allMatch(c -> ALPHABET.indexOf((char) c) != -1);
    }

    /**
     * Encode en base 32 un entier avec une représentation binaire en 5 bits
     *
     * @param n
     *      entier à encoder
     * @return une chaîne de charactèrs de longueur 1 contenant la valeur en base 32 de l'entier donné
     */
    public static String encodeBits5(int n) {
        return String.valueOf(ALPHABET.charAt(n));
    }

    /**
     * Encode en base 32 un entier avec une représentation binaire en 10 bits
     *
     * @param n
     *      entier à encoder
     * @return  uen chaîné de charactèrs de longueur 2 contenant la valeur en base 32 de l'entier donné
     */
    public static String encodeBits10(int n) {
        int n1 = n >>> 5;
        int n2 = n & 0b11111;
        return encodeBits5(n1) + encodeBits5(n2);
    }

    /**
     * Decode un chiffre en base 32 passé en chaîne de charactèrs
     *
     * @param encoded
     *          chaîne de charactèrs du chiffre en base 32 à décoder
     * @return  l'entier correspondant à la chaîne de charactèrs en base 32 donnée
     */
    public static int decode(String encoded) {
        Preconditions.checkArgument(isValid(encoded));

        int codeLength = encoded.length();
        Preconditions.checkArgument(codeLength == 1 || codeLength == 2);

        if (codeLength == 1) {
            return ALPHABET.indexOf(encoded);
        } else {
            return (ALPHABET.indexOf(encoded.charAt(0)) << 5) + ALPHABET.indexOf(encoded.charAt(1));
        }
    }
}
