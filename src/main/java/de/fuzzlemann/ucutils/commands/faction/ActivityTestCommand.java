package de.fuzzlemann.ucutils.commands.faction;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.commands.time.ClockCommand;
import de.fuzzlemann.ucutils.common.activity.ActivityTestType;
import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.TabCompletion;
import de.fuzzlemann.ucutils.utils.image.ImageUploader;
import de.fuzzlemann.ucutils.utils.io.FileManager;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class ActivityTestCommand implements TabCompletion {

    private final File directory = new File(FileManager.MC_DIRECTORY, "activityTests");

    @Command(value = "activitytest", usage = "/%label% [Thema]")
    public boolean onCommand(ActivityTestType testType) {
        try {
            new ClockCommand().onCommand();

            if (!directory.exists()) {
                directory.mkdir();
            }

            Framebuffer framebuffer = ReflectionUtil.getValue(Main.MINECRAFT, Framebuffer.class);
            assert framebuffer != null;

            File file = new File(directory, testType.getName().toLowerCase() + "_" + System.currentTimeMillis() + ".jpg");
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

            BufferedImage image = ScreenShotHelper.createScreenshot(Main.MINECRAFT.displayWidth, Main.MINECRAFT.displayHeight, framebuffer);

            new Thread(() -> {
                try {
                    try {
                        ImageIO.write(image, "jpg", file);
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }

                    String json = ImageUploader.upload(file);

                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(json);
                    String link = jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("link").getAsString();

                    String str = APIUtils.postAuthenticated("http://tomcat.fuzzlemann.de/factiononline/activityTest/add",
                            "typeString", testType.getName(),
                            "link", link);

                    if (!str.equals("success")) {
                        TextUtils.error("Die Aktivität konnte nicht eingetragen werden: " + str);
                        return;
                    }

                    TextUtils.simpleMessage("Die Aktivität wurde erfolgreich eingetragen.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "ActivityTestCommand").start();
        } catch (Exception e) {
            e.printStackTrace();
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
}
