package de.fuzzlemann.ucutils.events;

import com.google.common.collect.ImmutableSet;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class MunitionEventHandler {

    private static final Set<String> WEAPONS = ImmutableSet.of("\u00a78M4", "\u00a78MP5", "\u00a78Pistole", "\u00a78Jagdflinte");

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent e) {
        if (!ConfigUtil.munitionDisplay) return;
        if (!(e instanceof PlayerInteractEvent.RightClickItem)) return;

        handleMunitionDisplay(e.getItemStack());
    }

    private static void handleMunitionDisplay(ItemStack is) {
        if (!isWeapon(is)) return;

        String text = getText(is);
        if (text == null) return;

        Main.MINECRAFT.ingameGUI.setOverlayMessage(text, true);
    }

    private static String getText(ItemStack is) {
        NBTTagCompound nbt = is.getTagCompound();
        if (nbt == null) return null;

        NBTTagCompound display = nbt.getCompoundTag("display");

        String lore = display.getTagList("Lore", Constants.NBT.TAG_STRING).getStringTagAt(0);
        String[] splittedLore = lore.split("/");

        String munitionString = splittedLore[0];
        int munition = Integer.parseInt(munitionString.substring(2, munitionString.length()));

        return (--munition < 1 ? "\u00a7c0" : "\u00a76" + munition) + "\u00a7b/\u00a76" + splittedLore[1];
    }

    private static boolean isWeapon(ItemStack is) {
        return WEAPONS.contains(is.getDisplayName());
    }
}
