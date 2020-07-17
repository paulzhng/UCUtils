package de.fuzzlemann.ucutils.activitytest;

import de.fuzzlemann.ucutils.base.command.execution.CommandHandler;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.common.activity.ActivityTestType;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.image.ImageUploader;
import de.fuzzlemann.ucutils.utils.io.FileManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityTestHandler {

    public static final File ACTIVITY_TEST_FOLDER = new File(FileManager.MC_DIRECTORY, "activityTests");
    public static int counter;
    public static List<Integer> remaining = new ArrayList<>();

    public static void enterActivityTest(ActivityTestType testType) {
        try {
            CommandHandler.issueCommand("clock");

            if (!ACTIVITY_TEST_FOLDER.exists())
                ACTIVITY_TEST_FOLDER.mkdir();

            File file = new File(ACTIVITY_TEST_FOLDER, testType.getName().toLowerCase() + "_" + System.currentTimeMillis() + ".jpg");
            ForgeUtils.makeScreenshot(file);

            new Thread(() -> {
                try {
                    String link = ImageUploader.uploadToLink(file);

                    String str = APIUtils.postAuthenticated("http://tomcat.fuzzlemann.de/factiononline/activityTest/add",
                            "typeString", testType.getName(),
                            "link", link);

                    if (!str.equals("success")) {
                        TextUtils.error("Die Aktivität konnte nicht eingetragen werden: " + str);
                        return;
                    }

                    TextUtils.simpleMessage("Die Aktivität wurde erfolgreich eingetragen.");
                } catch (Exception e) {
                    TextUtils.error("Ein Fehler ist aufgetreten.");
                    Logger.LOGGER.catching(e);
                }
            }, "UCUtils-ActivityTest").start();
        } catch (Exception e) {
            TextUtils.error("Ein Fehler ist aufgetreten.");
            Logger.LOGGER.catching(e);
        }
    }

    public static void modifyTextComponent(ActivityTestType activityTestType, ITextComponent original) {
        counter++;

        remaining.add(counter);

        ITextComponent button = Message.builder()
                .space()
                .of("[⬆]").color(TextFormatting.BLUE)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Aktivität eintragen", TextFormatting.DARK_AQUA))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/activitytest " + activityTestType.getName() + " " + counter)
                .advance()
                .build()
                .toTextComponent();

        original.appendSibling(button);
    }
}
