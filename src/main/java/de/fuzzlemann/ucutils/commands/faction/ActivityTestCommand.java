package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.common.activity.ActivityTestType;
import de.fuzzlemann.ucutils.keybind.KeyBindRegistry;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.command.execution.CommandHandler;
import de.fuzzlemann.ucutils.utils.image.ImageUploader;
import de.fuzzlemann.ucutils.utils.io.FileManager;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class ActivityTestCommand implements TabCompletion {

    private static final File ACTIVITY_TEST_FOLDER = new File(FileManager.MC_DIRECTORY, "activityTests");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
    private static long lastScreenshot;

    @Command(value = "activitytest", usage = "/%label% [Thema]")
    public boolean onCommand(ActivityTestType testType) {
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
            }, "UCUtils-ActivityTestCommand").start();
        } catch (Exception e) {
            TextUtils.error("Ein Fehler ist aufgetreten.");
            Logger.LOGGER.catching(e);
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length != 1) return null;

        return Arrays.stream(ActivityTestType.values())
                .map(ActivityTestType::getName)
                .collect(Collectors.toList());
    }

    @SubscribeEvent
    public static void onKeyboardClickEvent(InputEvent.KeyInputEvent e) {
        handleAlternateScreenshot();
    }

    @SubscribeEvent
    public static void onKeyboardClickEvent(GuiScreenEvent.KeyboardInputEvent.Post e) {
        handleAlternateScreenshot();
    }

    private static void handleAlternateScreenshot() {
        if (!KeyBindRegistry.alternateScreenshot.isPressed() && !KeyBindRegistry.alternateScreenshotWithUpload.isPressed())
            return;

        if (System.currentTimeMillis() - lastScreenshot < TimeUnit.SECONDS.toMillis(1)) return;

        if (!ACTIVITY_TEST_FOLDER.exists())
            ACTIVITY_TEST_FOLDER.mkdir();

        String date = DATE_FORMAT.format(new Date());

        StringBuilder sb = new StringBuilder(date);
        int i = 1;
        while (new File(ACTIVITY_TEST_FOLDER, sb + ".jpg").exists()) {
            if (i == 1) {
                sb.append("_").append(i++);
            } else {
                sb.replace(sb.length() - 1, sb.length(), String.valueOf(i));
            }
        }

        sb.append(".jpg");
        String fullName = sb.toString();

        File file = new File(ACTIVITY_TEST_FOLDER, fullName);
        ForgeUtils.makeScreenshot(file);

        Message.builder()
                .prefix()
                .of("Der Screenshot wurde als").color(TextFormatting.GRAY).advance()
                .space()
                .of(fullName).color(TextFormatting.BLUE).bold()
                .clickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath())
                .advance()
                .space()
                .of("gespeichert.").color(TextFormatting.GRAY).advance()
                .space()
                .of("⬆").color(TextFormatting.BLUE)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Hochladen", TextFormatting.DARK_AQUA))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/uploadimage " + file.getAbsolutePath())
                .advance()
                .send();

        if (KeyBindRegistry.alternateScreenshotWithUpload.isPressed()) {
            CommandHandler.issueCommand("uploadimage", file.getAbsolutePath());
        }

        lastScreenshot = System.currentTimeMillis();
    }
}
