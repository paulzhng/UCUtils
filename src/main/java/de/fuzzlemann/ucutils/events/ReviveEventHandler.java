package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemSkull;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class ReviveEventHandler {

    @SubscribeEvent
    public static void onRevive(PlayerInteractEvent.RightClickBlock e) {
        if (Faction.getFactionOfPlayer() != Faction.UCMD) return;

        BlockPos pos = e.getPos();

        List<EntityItem> items = Main.MINECRAFT.world.getEntities(EntityItem.class, (ent) -> ent != null && ent.hasCustomName() && ent.getItem().getItem() instanceof ItemSkull);

        for (EntityItem entityItem : items) {
            double distance = entityItem.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());

            if (distance < 2) {
                AbstractionLayer.getPlayer().sendChatMessage("/revive");
                return;
            }
        }
    }
}
