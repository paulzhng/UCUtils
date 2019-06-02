package de.fuzzlemann.ucutils.utils.cape;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.initializor.IInitializor;
import de.fuzzlemann.ucutils.utils.initializor.InitMode;
import de.fuzzlemann.ucutils.utils.initializor.Initializor;
import net.labymod.core.CoreAdapter;
import net.labymod.core.LabyModCore;
import net.labymod.core_implementation.mc112.RenderPlayerImplementation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author Fuzzlemann
 */
@Initializor(value = "Cape", initMode = InitMode.LABY_MOD)
public class LabyModCapeInitializor implements IInitializor {
    @Override
    public void init() throws Exception {
        Main.MINECRAFT.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);

        CoreAdapter coreAdapter = LabyModCore.getCoreAdapter();

        Field field = coreAdapter.getClass().getDeclaredField("renderPlayerImpl");
        field.setAccessible(true);

        field.set(coreAdapter, new LabyModRenderPlayerImpl());
    }

    public class LabyModRenderPlayerImpl extends RenderPlayerImplementation {
        @Override
        public LayerRenderer[] getLayerRenderers(RenderPlayer renderPlayer) {
            LayerRenderer[] defaultLayerRenderer = super.getLayerRenderers(renderPlayer);
            LayerRenderer[] layerRenderer = Arrays.copyOf(defaultLayerRenderer, defaultLayerRenderer.length + 1);

            layerRenderer[layerRenderer.length - 1] = new CustomLayerCape(renderPlayer);
            return layerRenderer;
        }
    }
}
