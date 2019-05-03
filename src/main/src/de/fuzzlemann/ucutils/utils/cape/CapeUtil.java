package de.fuzzlemann.ucutils.utils.cape;

import com.google.common.collect.ImmutableSet;
import de.fuzzlemann.ucutils.Main;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class CapeUtil {

    private static final Set<Class<? extends ICapeInitializor>> CAPE_INITIALIZORS = ImmutableSet.of(LabyModCapeInitializor.class, DefaultCapeInitializor.class);
    private static final Map<String, ResourceLocation> CAPE_TYPES = new HashMap<>();
    private static final Map<String, String> CAPES = new HashMap<>();

    public static void init() {
        for (Class<? extends ICapeInitializor> capeInitializor : CAPE_INITIALIZORS) {
            System.out.println("Trying to use " + capeInitializor.getName() + " as cape initializor...");

            try {
                capeInitializor.newInstance().init();
                System.out.println("Initialized " + capeInitializor.getName() + " as cape initializor.");
                break;
            } catch (Exception | NoClassDefFoundError e) {
                System.out.println("Couldn't use " + capeInitializor.getName() + " as cape initializor.");
            }
        }
    }

    public static void loadCapes() throws IOException {
        CAPES.clear();
        CAPE_TYPES.clear();

        URL url = new URL("http://tomcat.fuzzlemann.de/factiononline/capes");
        String result = IOUtils.toString(url, StandardCharsets.UTF_8);

        for (String entry : result.split("\\|")) {
            String[] splittedEntry = entry.split(":");

            String uuid = splittedEntry[0];
            String capeType = splittedEntry[1];

            CAPES.put(uuid, capeType);
        }

        for (String capeType : CAPES.values()) {
            if (CAPE_TYPES.containsKey(capeType)) continue;

            IImageBuffer imageBuffer = new IImageBuffer() {
                @Nonnull
                @Override
                public BufferedImage parseUserSkin(@Nonnull BufferedImage image) {
                    return image;
                }

                @Override
                public void skinAvailable() {
                }
            };

            String capeURL = "http://fuzzlemann.de/capes/" + capeType + ".png";
            ThreadDownloadImageData capeTexture = new ThreadDownloadImageData(null, capeURL, null, imageBuffer);
            ResourceLocation resourceLocation = new ResourceLocation("cape/" + capeType);
            Main.MINECRAFT.getTextureManager().deleteTexture(resourceLocation);
            Main.MINECRAFT.getTextureManager().loadTexture(resourceLocation, capeTexture);

            CAPE_TYPES.put(capeType, resourceLocation);
        }

    }

    public static ResourceLocation getCape(String uuid) {
        String capeType = CAPES.get(uuid);
        if (capeType == null) return null;

        return CAPE_TYPES.get(capeType);
    }
}
