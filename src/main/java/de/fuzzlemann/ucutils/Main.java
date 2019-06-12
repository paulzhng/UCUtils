package de.fuzzlemann.ucutils;

import de.fuzzlemann.ucutils.commands.UpdateCommand;
import de.fuzzlemann.ucutils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.AnalyticsUtil;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.command.CommandRegistry;
import de.fuzzlemann.ucutils.utils.data.DataManager;
import de.fuzzlemann.ucutils.utils.initializor.InitializorHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */

@SideOnly(Side.CLIENT)
@Mod(name = Main.NAME, modid = Main.MOD_ID, version = Main.VERSION, clientSideOnly = true, certificateFingerprint = Main.CERTIFICATE_FINGERPRINT)
public class Main {

    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static final String MOD_ID = "ucutils";
    public static final String VERSION = "1.12.1-1.6";

    static final String NAME = "UC Utils";
    static final String CERTIFICATE_FINGERPRINT = "d3c444c8828b6fe0d86675d009f6c057d4bf25f1";

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent e) {
        Logger.LOGGER.warn("Invalid fingerprint detected! The mod may have been tampered with. (expected: " + e.getExpectedFingerprint() + "; keys found: " + e.getFingerprints() + ")");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        UpdateCommand.modFile = e.getSourceFile();
        ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);

        ASMDataTable asmDataTable = e.getAsmData();

        DataManager.initDataLoaders(asmDataTable);
        InitializorHandler.initInitializors(asmDataTable);
        CommandRegistry.registerAllCommands(asmDataTable);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        new Thread(() -> DataManager.loadData(false)).start();
        new Thread(AnalyticsUtil::sendStartupAnalytics).start();
        new Thread(TSClientQuery::getInstance).start();

        InitializorHandler.initAll();
    }
}
