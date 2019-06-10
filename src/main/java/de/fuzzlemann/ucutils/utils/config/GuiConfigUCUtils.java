package de.fuzzlemann.ucutils.utils.config;

import de.fuzzlemann.ucutils.Main;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

/**
 * @author Fuzzlemann
 */
class GuiConfigUCUtils extends GuiConfig {
    GuiConfigUCUtils(GuiScreen parent) {
        super(parent,
                new ConfigElement(ConfigUtil.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                Main.MOD_ID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(ConfigUtil.config.toString()));
    }
}