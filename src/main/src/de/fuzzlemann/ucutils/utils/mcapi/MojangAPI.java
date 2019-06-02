package de.fuzzlemann.ucutils.utils.mcapi;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.api.APIUtils;

import javax.annotation.Nonnull;
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
                    String uuid = getUncachedUUID(name);
                    if (uuid == null) throw new NullPointerException("uuid is null");

                    return uuid;
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


    public static SetMultimap<String, String> getEarlierNames(List<String> names) {
        SetMultimap<String, String> earlierNames = HashMultimap.create();

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
        if (jsonElement.isJsonNull()) return earlierNames;

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
        if (jsonElement.isJsonNull()) return null;

        return jsonElement.getAsJsonObject().get("id").getAsString();
    }

    private static JsonElement getJsonElement(String urlString) {
        try {
            String content = APIUtils.get(urlString);
            return new JsonParser().parse(content);
        } catch (Exception e) {
            Logger.LOGGER.catching(e);
            return null;
        }
    }
}
