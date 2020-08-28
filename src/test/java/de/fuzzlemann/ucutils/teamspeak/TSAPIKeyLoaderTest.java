package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Fuzzlemann
 */
@Ignore
class TSAPIKeyLoaderTest {

    @BeforeAll
    static void setUp() {
        UCUtilsConfig.tsAPIKey = "";
    }

    @Test
    void test() throws IOException {
        assertTrue(UCUtilsConfig.tsAPIKey.isEmpty());
        new TSAPIKeyLoader().load();
        assertEquals(UCUtilsConfig.tsAPIKey.length(), 29); // 29 is the length of the TS API Key
    }
}
