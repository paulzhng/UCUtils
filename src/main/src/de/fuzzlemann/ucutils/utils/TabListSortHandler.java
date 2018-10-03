package de.fuzzlemann.ucutils.utils;

import com.google.common.collect.Ordering;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class TabListSortHandler {

    public static void initTablistSort() throws NoSuchFieldException, IllegalAccessException {
        Field field = null;

        for (Field f : GuiPlayerTabOverlay.class.getDeclaredFields()) {
            if (f.getType().equals(Ordering.class)) {
                field = f;
                break;
            }
        }

        if (field == null) return;

        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, Ordering.from(new ModifiedComparator()));
    }

    @SideOnly(Side.CLIENT)
    static class ModifiedComparator implements Comparator<NetworkPlayerInfo> {

        private static final List<String> ORDERED_ENTRIES = Arrays.asList(
                "\u00a71[UC]",
                "\u00a71",
                "\u00a79[UC]",
                "\u00a79",
                "\u00a74[UC]",
                "\u00a74",
                "\u00a76[UC]",
                "\u00a76",
                "\u00a78[\u00a7r\u00a79UC\u00a7r\u00a78]\u00a7r\u00a7c",
                "\u00a78[\u00a7r\u00a76R\u00a7r\u00a78]\u00a7r",
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
}