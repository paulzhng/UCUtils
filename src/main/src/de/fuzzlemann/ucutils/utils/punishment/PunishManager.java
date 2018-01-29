package de.fuzzlemann.ucutils.utils.punishment;

import com.google.gson.Gson;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class PunishManager {

    private static final List<Violation> VIOLATIONS = new ArrayList<>();

    public static void fillViolationList() throws IOException {
        URL url = new URL("http://fuzzlemann.de/violations.html");
        String result = IOUtils.toString(url, StandardCharsets.UTF_8);

        String[] jsonList = result.split("\n");

        Gson gson = new Gson();
        for (String json : jsonList) {
            VIOLATIONS.add(gson.fromJson(json, Violation.class));
        }
    }

    public static List<String> getViolations() {
        return VIOLATIONS.stream()
                .map(Violation::getReason)
                .collect(Collectors.toList());
    }

    public static Violation getViolation(String reason) {
        return VIOLATIONS.stream()
                .filter(violation -> violation.getReason().equalsIgnoreCase(reason))
                .findFirst()
                .orElse(null);
    }
}