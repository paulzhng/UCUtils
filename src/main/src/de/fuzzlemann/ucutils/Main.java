package de.fuzzlemann.ucutils;

import de.fuzzlemann.ucutils.commands.UpdateCommand;
import de.fuzzlemann.ucutils.update.UpdateReminder;
import de.fuzzlemann.ucutils.utils.AnalyticsUtil;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.cape.CapeUtil;
import de.fuzzlemann.ucutils.utils.command.CommandHandler;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import de.fuzzlemann.ucutils.utils.faction.HouseBanHandler;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.DrugUtil;
import de.fuzzlemann.ucutils.utils.faction.police.WantedManager;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.noobchat.NoobChatManager;
import de.fuzzlemann.ucutils.utils.punishment.PunishManager;
import de.fuzzlemann.ucutils.utils.tablist.TabListSortHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
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
@Mod(name = Main.NAME, modid = Main.MOD_ID, version = Main.VERSION, clientSideOnly = true, guiFactory = Main.GUI_FACTORY, certificateFingerprint = Main.CERTIFICATE_FINGERPRINT)
public class Main {

    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static final String MOD_ID = "ucutils";
    public static final String VERSION = "1.12.1-1.6";

    static final String NAME = "UC Utils";
    static final String GUI_FACTORY = "de.fuzzlemann.ucutils.utils.config.GuiFactoryUCUtils";
    static final String CERTIFICATE_FINGERPRINT = "d3c444c8828b6fe0d86675d009f6c057d4bf25f1";

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent e) {
        System.out.println("Invalid fingerprint detected! The mod may have been tampered with. (expected: " + e.getExpectedFingerprint() + "; keys found: " + e.getFingerprints() + ")");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        UpdateCommand.modFile = e.getSourceFile();

        ConfigUtil.config = new Configuration(e.getSuggestedConfigurationFile());
        ConfigUtil.syncConfig();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        CommandHandler.registerAllCommands();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        new Thread(Main::loadData).start();
        new Thread(AnalyticsUtil::sendStartupAnalytics).start();

        TabListSortHandler.init();
        CapeUtil.init();
    }

    public static void loadData() {
        try {
            UpdateReminder.updateUpdateNeeded();
        } catch (IOException e) {
            e.printStackTrace();
            UpdateReminder.updateNeeded = false;
        }

        APIUtils.loadAPIKey();

        try {
            WantedManager.fillWantedList();
            HouseBanHandler.fillHouseBanList();
            PunishManager.fillViolationList();
            NoobChatManager.fillNoobChatAnswerList();
            NavigationUtil.fillNaviPoints();
            CapeUtil.loadCapes();
        } catch (IOException e) {
            e.printStackTrace();
            WantedManager.readSavedWantedList();
        }

        DrugUtil.loadDrugs();
        BlacklistUtil.loadBlacklistReasons();
    }
}
