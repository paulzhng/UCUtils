package de.fuzzlemann.ucutils.utils.io;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.ForgeUtils;

import java.io.File;

/**
 * @author Fuzzlemann
 */
public class FileManager {

    public static final File MC_DIRECTORY;

    static {
        if (ForgeUtils.isTest()) {
            MC_DIRECTORY = new File("/testDirectory");
        } else {
            MC_DIRECTORY = Main.MINECRAFT.gameDir;
        }
    }
}
