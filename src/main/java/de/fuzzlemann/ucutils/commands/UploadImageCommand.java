package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.image.ImageUploader;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;

/**
 * @author Fuzzlemann
 */
public class UploadImageCommand {

    @Command(value = "uploadimage", async = true)
    public boolean onCommand(File file) {
        String link = ImageUploader.uploadToLink(file);

        StringSelection stringSelection = new StringSelection(link);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        Message.builder()
                .prefix()
                .of("Das Bild wurde erfolgreich hochgeladen.").color(TextFormatting.GRAY).advance()
                .newLine()
                .info()
                .of("Der Link befindet sich in deiner Zwischenablage.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }
}
