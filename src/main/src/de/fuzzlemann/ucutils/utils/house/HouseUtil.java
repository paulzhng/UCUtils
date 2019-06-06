package de.fuzzlemann.ucutils.utils.house;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import de.fuzzlemann.ucutils.common.House;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.data.DataModule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@DataModule("House")
public class HouseUtil implements DataLoader {

    public static final List<House> HOUSES = new ArrayList<>();

    @Override
    public void load() {
        String json = APIUtils.get("http://tomcat.fuzzlemann.de/factiononline/houses");

        Gson gson = new Gson();
        //noinspection UnstableApiUsage
        List<House> houses = gson.fromJson(json, new TypeToken<List<House>>() {
        }.getType());

        HOUSES.clear();
        HOUSES.addAll(houses);
    }
}
