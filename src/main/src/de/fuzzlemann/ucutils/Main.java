package de.fuzzlemann.ucutils;

import de.fuzzlemann.ucutils.update.UpdateReminder;
import de.fuzzlemann.ucutils.utils.command.CommandHandler;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import de.fuzzlemann.ucutils.utils.faction.badfaction.DrugUtil;
import de.fuzzlemann.ucutils.utils.faction.police.WantedManager;
import de.fuzzlemann.ucutils.utils.noobchat.NoobChatManager;
import de.fuzzlemann.ucutils.utils.punishment.PunishManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * @author Fuzzlemann
 */

@SideOnly(Side.CLIENT)
@Mod(name = Main.NAME, modid = Main.MOD_ID, version = Main.VERSION, clientSideOnly = true, guiFactory = Main.GUI_FACTORY)
public class Main {

    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static final String MOD_ID = "ucutils";
    public static final String VERSION = "1.12.1-1.4";

    static final String NAME = "UC Utils";
    static final String GUI_FACTORY = "de.fuzzlemann.ucutils.utils.config.GuiFactoryUCUtils";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        ConfigUtil.config = new Configuration(e.getSuggestedConfigurationFile());
        ConfigUtil.syncConfig();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        CommandHandler.registerAllCommands();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        new Thread(Main::refreshData).start();
    }

    public static void refreshData() {
        try {
            UpdateReminder.updateUpdateNeeded();
        } catch (IOException exc) {
            UpdateReminder.updateNeeded = false;
        }

        try {
            WantedManager.fillWantedList();
        } catch (IOException exc) {
            WantedManager.readSavedWantedList();
        }

        try {
            PunishManager.fillViolationList();
            NoobChatManager.fillNoobChatAnswerList();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        DrugUtil.loadDrugs();
    }
}
