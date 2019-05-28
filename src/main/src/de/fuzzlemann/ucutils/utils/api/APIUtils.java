package de.fuzzlemann.ucutils.utils.api;

import com.google.gson.Gson;
import com.mojang.authlib.exceptions.AuthenticationException;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class APIUtils {

    public static String postAuthenticated(String url, Object... paramArray) {
        Object[] newParams = new Object[paramArray.length + 2];
        newParams[0] = "apiKey";
        newParams[1] = ConfigUtil.apiKey;
        System.arraycopy(paramArray, 0, newParams, 2, paramArray.length);

        return post(url, newParams);
    }

    public static String post(String url, Object... paramArray) {
        Validate.isTrue(paramArray.length % 2 == 0, "size of array not even", Arrays.toString(paramArray));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("User-Agent", "UCUtils");

            List<NameValuePair> params = new ArrayList<>();
            for (int i = 0; i < paramArray.length; i += 2) {
                String key = (String) paramArray[i];
                Object valueObject = paramArray[i + 1];

                String value = valueObject instanceof String ? (String) valueObject : new Gson().toJson(valueObject);

                params.add(new BasicNameValuePair(key, value));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            HttpEntity entity = httpClient.execute(httpPost).getEntity();
            if (entity == null) return null;

            try {
                return EntityUtils.toString(entity);
            } finally {
                EntityUtils.consumeQuietly(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String get(String url) {
        try {
            return IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String generateAuthKey() {
        Minecraft mc = Main.MINECRAFT;
        AuthHash authHash = new AuthHash();

        try {
            mc.getSessionService().joinServer(mc.getSession().getProfile(), mc.getSession().getToken(), authHash.getHash());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        }

        String response = post("http://tomcat.fuzzlemann.de/factiononline/generateauthkey", "username", authHash.getUsername(), "hash", authHash.getHash());
        if (response == null || response.isEmpty()) return null;

        return response;
    }

    public static void loadAPIKey() {
        Minecraft mc = Main.MINECRAFT;
        AuthHash authHash = new AuthHash();

        try {
            mc.getSessionService().joinServer(mc.getSession().getProfile(), mc.getSession().getToken(), authHash.getHash());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return;
        }

        String response = post("http://tomcat.fuzzlemann.de/factiononline/getapikey", "username", authHash.getUsername(), "hash", authHash.getHash());
        if (response == null || response.isEmpty()) return;

        ConfigUtil.apiKeyProperty.set(response);
        ConfigUtil.onConfigChange(null);
    }
}
