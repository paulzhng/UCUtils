package de.fuzzlemann.ucutils.utils;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.io.JsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Fuzzlemann
 */
public class AnalyticsUtil {

    private static final File ANALYTICS_STORAGE_FILE = new File(JsonManager.DIRECTORY, "analyticsid.storage");

    public static void sendStartupAnalytics() {
        if (!Minecraft.getMinecraft().isSnooperEnabled()) return;

        Session session = Main.MINECRAFT.getSession();
        String uuid = session.getProfile().getId().toString();
        String name = session.getUsername();

        String version = Main.VERSION;
        String minecraftVersion = Main.MINECRAFT.getVersion();
        String javaVersion = System.getProperty("java.version");

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://analytics.fuzzlemann.de/submit.php");

            httpPost.setHeader("User-Agent", "UCUtils");

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("analyticsID", getAnalyticsID()));
            params.add(new BasicNameValuePair("uuid", uuid));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("version", version));
            params.add(new BasicNameValuePair("minecraftVersion", minecraftVersion));
            params.add(new BasicNameValuePair("javaVersion", javaVersion));

            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getAnalyticsID() {
        if (ANALYTICS_STORAGE_FILE.exists()) {
            try {
                return UUID.fromString((String) JsonManager.loadObject(ANALYTICS_STORAGE_FILE, String.class)).toString();
            } catch (Exception e) {
                if (ANALYTICS_STORAGE_FILE.delete()) {
                    System.out.println("[UCUtils] " + ANALYTICS_STORAGE_FILE.getAbsoluteFile() + " deleted");
                    return getAnalyticsID();
                }
            }
        }

        String analyticsID = UUID.randomUUID().toString();
        JsonManager.writeObject(ANALYTICS_STORAGE_FILE, analyticsID);

        return analyticsID;
    }
}
