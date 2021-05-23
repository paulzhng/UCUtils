package de.fuzzlemann.ucutils.utils.tablist;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
class TabListComparator implements Comparator<NetworkPlayerInfo> {

    private static final List<String> ORDERED_ENTRIES = Arrays.asList(
            "§1[UC]",
            "§1",
            "§9[UC]",
            "§9",
            "§4[UC]",
            "§4",
            "§6[UC]",
            "§6",
            "§8[§r§9UC§r§8]§r§c",
            "§8[§r§6R§r§8]§r",
            "[UC]"
    );

    @Override
    public int compare(NetworkPlayerInfo playerOne, NetworkPlayerInfo playerTwo) {
        String stringOne = ForgeUtils.getTablistName(playerOne);
        String stringTwo = ForgeUtils.getTablistName(playerTwo);

        String stringOneStartsWith = ORDERED_ENTRIES.stream().filter(stringOne::startsWith).findAny().orElse(null);
        String stringTwoStartsWith = ORDERED_ENTRIES.stream().filter(stringTwo::startsWith).findAny().orElse(null);

        if (stringOneStartsWith != null && stringTwoStartsWith != null) {
            int sgn = ORDERED_ENTRIES.indexOf(stringOneStartsWith) - ORDERED_ENTRIES.indexOf(stringTwoStartsWith);
            return sgn != 0 ? sgn : stringOne.compareTo(stringTwo);
        }

        if (stringOneStartsWith != null) return -1;
        if (stringTwoStartsWith != null) return 1;

        return stringOne.compareTo(stringTwo);
    }
}
