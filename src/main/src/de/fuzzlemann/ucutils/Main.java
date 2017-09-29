package de.fuzzlemann.ucutils;

import de.fuzzlemann.ucutils.update.UpdateReminder;
import de.fuzzlemann.ucutils.utils.command.CommandHandler;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import de.fuzzlemann.ucutils.utils.police.WantedManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * @author Fuzzlemann
 */

@SideOnly(Side.CLIENT)
@Mod(name = Main.NAME, modid = Main.MOD_ID, version = Main.VERSION, guiFactory = Main.GUI_FACTORY)
public class Main {
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static final String MOD_ID = "ucutils";
    public static final String VERSION = "1.12.1-1.3";

    static final String NAME = "UC Utils";
    static final String GUI_FACTORY = "de.fuzzlemann.ucutils.utils.config.GuiFactoryUCUtils";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e) {
        ConfigUtil.config = new Configuration(e.getSuggestedConfigurationFile());
        ConfigUtil.syncConfig();

        CommandHandler.registerAllCommands();

        new Thread(() -> {
            try {
                UpdateReminder.updateUpdateNeeded();
            } catch (IOException e1) {
                UpdateReminder.updateNeeded = false;
            }

            try {
                WantedManager.fillWantedList();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }).start();
    }
}
