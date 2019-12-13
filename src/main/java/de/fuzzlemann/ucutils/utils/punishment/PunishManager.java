package de.fuzzlemann.ucutils.utils.punishment;

import com.google.gson.Gson;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.base.data.DataLoader;
import de.fuzzlemann.ucutils.base.data.DataModule;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
@DataModule("Violations")
public class PunishManager implements DataLoader {

    private static final List<Violation> VIOLATIONS = new ArrayList<>();

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

    @Override
    public void load() {
        VIOLATIONS.clear();

        String result = APIUtils.get("http://fuzzlemann.de/violations.html");
        String[] jsonList = result.split("\n");

        Gson gson = new Gson();
        for (String json : jsonList) {
            VIOLATIONS.add(gson.fromJson(json, Violation.class));
        }
    }
}