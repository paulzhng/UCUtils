package de.fuzzlemann.ucutils.utils.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Fuzzlemann
 */
class TextUtilsTest {

    @Test
    void testStripPrefix() {
        assertEquals("Test", TextUtils.stripPrefix("[UC]Test"));
        assertEquals("[]Test", TextUtils.stripPrefix("[]Test"));
        assertEquals("Test", TextUtils.stripPrefix("[TEST]Test"));
        assertEquals("Test", TextUtils.stripPrefix("Test"));
    }

    @Test
    void testStripColor() {
        for (int i = 0; i <= 9; i++) {
            assertEquals("Test", TextUtils.stripColor("§" + i + "Test"));
        }

        for (char c = (int) 'a'; (int) c <= (int) 'f'; c++) {
            assertEquals("Test", TextUtils.stripColor("§" + c + "Test"));
        }

        for (char c = (int) 'k'; (int) c <= (int) 'o'; c++) {
            assertEquals("Test", TextUtils.stripColor("§" + c + "Test"));
        }

        assertEquals("Test", TextUtils.stripColor("§rTest"));

        assertEquals("Test", TextUtils.stripColor("Test"));
        assertEquals("§hTest", TextUtils.stripColor("§hTest"));
    }
}
