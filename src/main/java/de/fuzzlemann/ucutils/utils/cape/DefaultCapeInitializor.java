package de.fuzzlemann.ucutils.utils.cape;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.initializor.IInitializor;
import de.fuzzlemann.ucutils.base.initializor.InitMode;
import de.fuzzlemann.ucutils.base.initializor.Initializor;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.util.Collection;

/**
 * @author Fuzzlemann
 */
@Initializor(value = "Cape", initMode = InitMode.DEFAULT)
public class DefaultCapeInitializor implements IInitializor {
    @Override
    public void init() {
        Main.MINECRAFT.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);

        Collection<RenderPlayer> layers = Main.MINECRAFT.getRenderManager().getSkinMap().values();
        for (RenderPlayer renderer : layers) {
            renderer.addLayer(new CustomLayerCape(renderer));
        }
    }
}
