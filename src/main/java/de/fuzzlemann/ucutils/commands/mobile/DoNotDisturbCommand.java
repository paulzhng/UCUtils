package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class DoNotDisturbCommand {

    private static boolean doNotDisturb;

    @SubscribeEvent
    public static void onSound(SoundEvent.SoundSourceEvent e) {
        if (!doNotDisturb) return;

        String name = e.getName();
        if (!name.equals("record.cat") && !name.equals("record.stal") && !name.equals("entity.sheep.ambient")) return;

        e.setCanceled(true);
    }

    @Command({"donotdisturb", "stumm", "nichtstören"})
    public boolean onCommand() {
        doNotDisturb = !doNotDisturb;

        if (doNotDisturb) {
            TextUtils.simpleMessage("Du hast den Ton deines Handys ausgeschalten.");
        } else {
            TextUtils.simpleMessage("Du hast den Ton deines Handys eingeschalten.");
        }
        return true;
    }
}
