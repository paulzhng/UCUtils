package de.fuzzlemann.ucutils.utils.faction.police;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Fuzzlemann
 */
public class MedicalLicenseHandler {

    public static boolean hasMedicalLicense(String playerName) {
        try {
            URL url = new URL("http://fuzzlemann.de/medicallicense.php?username=" + playerName);
            return Boolean.valueOf(IOUtils.toString(url, StandardCharsets.UTF_8));
        } catch (IOException e) {
            return false;
        }
    }
}
