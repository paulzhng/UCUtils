package de.fuzzlemann.ucutils.utils.cape;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.misc.cape.UDFCape;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@UDFModule(value = DataRegistry.CAPES, version = 1)
public class CapeUtil implements UDFLoader<List<UDFCape>> {

    private static final Map<String, ResourceLocation> CAPE_TYPES = new HashMap<>();
    private static final Map<UUID, String> CAPES = new HashMap<>();

    static ResourceLocation getCape(UUID uuid) {
        String capeType = CAPES.get(uuid);
        if (capeType == null) return null;

        return CAPE_TYPES.get(capeType);
    }

    @Override
    public void supply(List<UDFCape> capes) {
        for (UDFCape cape : capes) {
            CAPES.put(cape.getUUID(), cape.getType());
        }

        loadCapes();
    }

    @Override
    public void cleanUp() {
        CAPES.clear();
        CAPE_TYPES.clear();
    }

    public void loadCapes() {
        if (ForgeUtils.isTest()) return;

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
