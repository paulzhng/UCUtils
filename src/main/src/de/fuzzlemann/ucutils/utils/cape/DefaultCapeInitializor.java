package de.fuzzlemann.ucutils.utils.cape;

import de.fuzzlemann.ucutils.Main;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.util.Collection;

/**
 * @author Fuzzlemann
 */
public class DefaultCapeInitializor implements ICapeInitializor {
    @Override
    public void init() {
        Main.MINECRAFT.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);

        Collection<RenderPlayer> layers = Main.MINECRAFT.getRenderManager().getSkinMap().values();
        for (RenderPlayer renderer : layers) {
            renderer.addLayer(new CustomLayerCape(renderer));
        }
    }
}
