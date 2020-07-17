package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.activitytest.ActivityTestHandler;
import de.fuzzlemann.ucutils.base.command.execution.CommandHandler;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.keybind.KeyBindRegistry;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber
public class AlternateScreenshotEventHandler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
    private static long lastScreenshot;

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

        if (!ActivityTestHandler.ACTIVITY_TEST_FOLDER.exists())
            ActivityTestHandler.ACTIVITY_TEST_FOLDER.mkdir();

        String date = DATE_FORMAT.format(new Date());

        StringBuilder sb = new StringBuilder(date);
        int i = 1;
        while (new File(ActivityTestHandler.ACTIVITY_TEST_FOLDER, sb + ".jpg").exists()) {
            if (i == 1) {
                sb.append("_").append(i++);
            } else {
                sb.replace(sb.length() - 1, sb.length(), String.valueOf(i));
            }
        }

        sb.append(".jpg");
        String fullName = sb.toString();

        File file = new File(ActivityTestHandler.ACTIVITY_TEST_FOLDER, fullName);
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
