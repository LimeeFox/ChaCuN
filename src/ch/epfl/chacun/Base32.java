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

    /**
     * Encode en base 32 un entier avec une représentation binaire en 5 bits
     *
     * @param n
     *      entier à encoder
     * @return une chaîne de charactèrs de longueur 1 contenant la valeur en base 32 de l'entier donné
     */
    public String encodeBits5(int n) {
        return String.valueOf(ALPHABET.charAt(n));
    }

    /**
     * Encode en base 32 un entier avec une représentation binaire en 10 bits
     *
     * @param n
     *      entier à encoder
     * @return  uen chaîné de charactèrs de longueur 2 contenant la valeur en base 32 de l'entier donné
     */
    public String encodeBits10(int n) {
        int n1 = n >>> 5;
        int n2 = n & 0b1111;
        return encodeBits5(n1) + encodeBits5(n2);
    }

}
