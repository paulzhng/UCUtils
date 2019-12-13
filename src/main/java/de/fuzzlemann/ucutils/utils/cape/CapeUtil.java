package de.fuzzlemann.ucutils.utils.cape;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.base.data.DataLoader;
import de.fuzzlemann.ucutils.base.data.DataModule;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@DataModule(value = "Cape", test = false)
public class CapeUtil implements DataLoader {

    private static final Map<String, ResourceLocation> CAPE_TYPES = new HashMap<>();
    private static final Map<String, String> CAPES = new HashMap<>();

    static ResourceLocation getCape(String uuid) {
        String capeType = CAPES.get(uuid);
        if (capeType == null) return null;

        return CAPE_TYPES.get(capeType);
    }

    @Override
    public void load() {
        CAPES.clear();
        CAPE_TYPES.clear();

        String result = APIUtils.get("http://tomcat.fuzzlemann.de/factiononline/capes");

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
            //noinspection ConstantConditions
            ThreadDownloadImageData capeTexture = new ThreadDownloadImageData(null, capeURL, null, imageBuffer);
            ResourceLocation resourceLocation = new ResourceLocation("cape/" + capeType);
            Main.MINECRAFT.getTextureManager().deleteTexture(resourceLocation);
            Main.MINECRAFT.getTextureManager().loadTexture(resourceLocation, capeTexture);

            CAPE_TYPES.put(capeType, resourceLocation);
        }
    }
}
