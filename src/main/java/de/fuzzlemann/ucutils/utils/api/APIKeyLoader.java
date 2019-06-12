package de.fuzzlemann.ucutils.utils.api;

import com.mojang.authlib.exceptions.AuthenticationException;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.data.DataModule;
import net.minecraft.client.Minecraft;

/**
 * @author Fuzzlemann
 */
@DataModule("API-Connection")
public class APIKeyLoader implements DataLoader {

    @Override
    public void load() {
        Minecraft mc = Main.MINECRAFT;
        AuthHash authHash = new AuthHash();

        try {
            mc.getSessionService().joinServer(mc.getSession().getProfile(), mc.getSession().getToken(), authHash.getHash());
        } catch (AuthenticationException e) {
            Logger.LOGGER.catching(e);
            return;
        }

        String response = APIUtils.post("http://tomcat.fuzzlemann.de/factiononline/getapikey", "username", authHash.getUsername(), "hash", authHash.getHash());
        if (response == null || response.isEmpty()) return;
        if (response.contains(" ")) return; //check for invalid API Keys

        UCUtilsConfig.apiKey = response;
        UCUtilsConfig.onConfigChange(null);
    }
}
