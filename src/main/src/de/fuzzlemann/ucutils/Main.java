package de.fuzzlemann.ucutils;

import de.fuzzlemann.ucutils.utils.command.CommandHandler;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */

@SideOnly(Side.CLIENT)
@Mod(name = Main.NAME, modid = Main.MOD_ID, version = Main.VERSION, guiFactory = Main.GUI_FACTORY)
public class Main {
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static final String MOD_ID = "ucutils";

    static final String NAME = "UC Utils";
    static final String VERSION = "1.12.1-1.1";
    static final String GUI_FACTORY = "de.fuzzlemann.ucutils.utils.config.GuiFactoryUCUtils";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e) {
        ConfigUtil.config = new Configuration(e.getSuggestedConfigurationFile());
        ConfigUtil.syncConfig();

        CommandHandler.registerAllCommands();
    }
}
