package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.io.JsonManager;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class TeamSpeakAPIKeyCommand implements CommandExecutor {

    @Override
    @Command(labels = {"teamspeakapikey", "tsapikey"}, usage = "/%label% [API Key]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String apiKey = args[0];

        TSClientQuery.apiKey = apiKey;
        JsonManager.writeObject(TSClientQuery.API_KEY_FILE, apiKey);

        TSClientQuery.auth();

        p.sendMessage(TextUtils.simpleMessage("Dein TS3 API-Key wurde auf " + apiKey + " ge\u00e4ndert.", TextFormatting.GREEN));
        return true;
    }
}