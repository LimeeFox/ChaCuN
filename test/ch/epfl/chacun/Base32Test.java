package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base32Test {

    @Test
    void isValid() {
        String validCode = "A3";
        String invalidCode = "0";

        assertTrue(Base32.isValid(validCode));
        assertFalse(Base32.isValid(invalidCode));
    }

    @Test
    void encodeBits5() {
        int n = 10;

        assertEquals("K", Base32.encodeBits5(n));
    }

    @Test
    void encodeBits10() {
        int n1 = 6;
        int n2 = 33;

        assertEquals("AG", Base32.encodeBits10(n1));
        assertEquals("BB", Base32.encodeBits10(n2));

    }

    @Test
    void decode() {
        String s1 = "D";
        String s2 = "AG";

        assertEquals(3, Base32.decode(s1));
        assertEquals(6, Base32.decode(s2));
        assertThrows(IllegalArgumentException.class, () ->{
            Base32.decode("0");
        });
        assertThrows(IllegalArgumentException.class, () ->{
            Base32.decode("ABC");
        });
    }
}