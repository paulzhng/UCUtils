package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class UpdateCommand {

    private static final File UPDATE_FILE = new File(System.getProperty("java.io.tmpdir"), "ucutils_update.jar");
    public static File modFile;
    private static boolean replace;

    @Command("updateucutils")
    public boolean onCommand() {
        if (!SystemUtils.IS_OS_WINDOWS && !SystemUtils.IS_OS_UNIX) {
            TextUtils.error("Dieser Befehl ist nur unter Windows unterstützt.");
            return true;
        }

        if (replace) {
            TextUtils.simpleMessage("Das Update wurde abgebrochen.");
            replace = false;
            return true;
        }

        TextUtils.simpleMessage("Die neue Version wird heruntergeladen...");

        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                windowsUpdate();
            } else {
                unixUpdate();
            }
        } catch (IOException e) {
            Logger.LOGGER.catching(e);
            TextUtils.error("Ein Fehler ist beim Herunterladen der neuen Version aufgetreten.");
            return true;
        }

        Message.builder()
                .prefix()
                .of("Die neue Version wurde erfolgreich heruntergeladen.").color(TextFormatting.GRAY).advance()
                .newLine()
                .info()
                .of("Zum Abschließen des Updates musst du nun dein Minecraft neustarten.").color(TextFormatting.WHITE).advance()
                .send();
        return true;
    }

    private void windowsUpdate() throws IOException {
        try {
            FileUtils.copyURLToFile(new URL("http://fuzzlemann.de/UCUtils.jar"), UPDATE_FILE, 10000, 10000);
        } catch (IOException e) {
            Logger.LOGGER.catching(e);
            TextUtils.error("Ein Fehler ist beim Herunterladen der neuen Version aufgetreten.");
            return;
        }

        replace = true;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!replace) return;
            replace = false;

            try {
                replaceJar();
            } catch (IOException e) {
                Logger.LOGGER.catching(e);
            }
        }));
    }

    private void unixUpdate() throws IOException {
        // on unix, we can just overwrite the original file directly as there's no file locking in place
        FileUtils.copyURLToFile(new URL("http://fuzzlemann.de/UCUtils.jar"), modFile, 10000, 10000);
    }

    private void replaceJar() throws IOException {
        String batContent = "@echo off\n" +
                "\n" +
                "set /a \"i=0\"\n" +
                "set /a \"x=60\"\n" +
                "set from_file=" + UPDATE_FILE.getAbsolutePath() + "\n" +
                "set to_file=" + modFile.getAbsolutePath() + "\n" +
                "\n" +
                "echo update-file: %from_file%\n" +
                "echo to-file: %to_file%\n" +
                "\n" +
                ":delete_loop\n" +
                "\ttimeout /T 1 > nul\n" +
                "\n" +
                "\t2>nul (\n" +
                "\t  move /Y \"%from_file%\" \"%to_file%\"\n" +
                "\t) && (\n" +
                "\t\techo updated UCUtils\n" +
                "\t\texit\n" +
                "\t) || (\n" +
                "\t\tset /a \"i = i + 1\"\n" +
                "\t\t\n" +
                "\t\tif %i% leq 60 (\n" +
                "\t\t\tset /a \"x = x - 1\"\n" +
                "\t\t\techo jar not yet available; waiting another second... [%x% tries left]\n" +
                "\t\t\tgoto delete_loop\n" +
                "\t\t)\n" +
                "\t\t\n" +
                "\t\techo 60 tries over; cancel update\n" +
                "\t\texit\n" +
                "\t)"; //TODO move to external resource, then replace

        File batFile = new File(System.getProperty("java.io.tmpdir"), "ucutils_update.bat");
        FileUtils.write(batFile, batContent, Charset.defaultCharset());

        Process proc = Runtime.getRuntime().exec("cmd /c start \"\" " + batFile.getAbsolutePath());
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.LOGGER.catching(e);
        }
    }
}
