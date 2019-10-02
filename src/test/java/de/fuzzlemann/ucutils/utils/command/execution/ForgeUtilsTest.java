package de.fuzzlemann.ucutils.utils.command.execution;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.location.ATM;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fuzzlemann
 */
class ForgeUtilsTest {

    private static final List<String> NAME_LIST = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        NAME_LIST.add("Justin");
        NAME_LIST.add("Felix");
        NAME_LIST.add("Alex");
        NAME_LIST.add("Thorben");
        NAME_LIST.add("Eldar");
    }

    @Test
    void testIsTest() {
        assertTrue(ForgeUtils.isTest());
    }

    @Test
    void testHasLabyMod() {
        assertTrue(ForgeUtils.hasLabyMod());
    }

    @Test
    void testGetNearestObject_1() {
        ATM expectedATM = ATM.values()[0];
        BlockPos blockPos = new BlockPos(expectedATM.getX(), expectedATM.getY(), expectedATM.getZ());

        Map.Entry<Double, ATM> atmEntry = ForgeUtils.getNearestObject(blockPos, ATM.values(), ATM::getX, ATM::getY, ATM::getZ);
        double distance = atmEntry.getKey();
        ATM atm = atmEntry.getValue();

        assertEquals(expectedATM, atm);
        assertEquals(0, distance);
    }

    @Test
    void testGetNearestObject_2() {
        ATM atm1 = ATM.values()[0];

        ATM posATM = ATM.values()[1];
        BlockPos blockPos = new BlockPos(posATM.getX(), posATM.getY(), posATM.getZ());

        Map.Entry<Double, ATM> atmEntry = ForgeUtils.getNearestObject(blockPos, ATM.values(), ATM::getX, ATM::getY, ATM::getZ);
        double distance = atmEntry.getKey();
        ATM nearestATM = atmEntry.getValue();

        assertNotEquals(atm1, nearestATM);
        assertEquals(distance, 0);
    }

    @Test
    void testMostMatching_1() {
        String expected = "Thorben";
        String actual = ForgeUtils.getMostMatching(NAME_LIST, expected);

        assertEquals(actual, expected);
    }

    @Test
    void testMostMatching_2() {
        String expected = "Felix";
        String actual = ForgeUtils.getMostMatching(NAME_LIST, "Fel");

        assertEquals(actual, expected);
    }

    @Test
    void testMostMatching_3() {
        assertNull(ForgeUtils.getMostMatching(Collections.emptyList(), "Felix"));
    }
}
