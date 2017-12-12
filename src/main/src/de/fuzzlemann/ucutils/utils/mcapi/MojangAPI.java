package de.fuzzlemann.ucutils.utils.mcapi;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
public class MojangAPI {

    private static final LoadingCache<String, String> UUID_CACHE = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(@Nonnull String name) {
                    return getUncachedUUID(name);
                }
            });

    private static final LoadingCache<String, List<String>> EARLIER_NAMES_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, List<String>>() {
                @Override
                public List<String> load(@Nonnull String name) {
                    return getUncachedEarlierNames(name);
                }
            });

    public static Multimap<String, String> getEarlierNames(List<String> names) {
        Multimap<String, String> earlierNames = HashMultimap.create();

        names.parallelStream().forEach(name -> earlierNames.putAll(name, getEarlierNames(name)));

        return earlierNames;
    }

    public static List<String> getEarlierNames(String name) {
        return EARLIER_NAMES_CACHE.getUnchecked(name);
    }

    private static List<String> getUncachedEarlierNames(String name) {
        List<String> earlierNames = new ArrayList<>();

        String uuid = getUUID(name);
        if (uuid == null) return earlierNames;

        JsonElement jsonElement = getJsonElement("https://api.mojang.com/user/profiles/" + uuid + "/names");
        if (jsonElement == null) return earlierNames;

        for (JsonElement element : jsonElement.getAsJsonArray()) {
            earlierNames.add(element.getAsJsonObject().get("name").getAsString());
        }

        return earlierNames;
    }

    public static String getUUID(String currentName) {
        return UUID_CACHE.getUnchecked(currentName);
    }

    private static String getUncachedUUID(String currentName) {
        JsonElement jsonElement = getJsonElement("https://api.mojang.com/users/profiles/minecraft/" + currentName);
        if (jsonElement == null) return null;

        return jsonElement.getAsJsonObject().get("id").getAsString();
    }

    private static JsonElement getJsonElement(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        String content;
        try {
            content = IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new JsonParser().parse(content);
    }

}
