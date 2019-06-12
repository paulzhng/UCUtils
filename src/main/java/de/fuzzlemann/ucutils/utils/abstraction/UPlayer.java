package de.fuzzlemann.ucutils.utils.abstraction;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.inventory.Container;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * @author Fuzzlemann
 */
public interface UPlayer {

    void sendMessage(ITextComponent textComponent);

    void sendChatMessage(String message);

    void playSound(SoundEvent soundIn, float volume, float pitch);

    String getName();

    UUID getUniqueID();

    BlockPos getPosition();

    Team getTeam();

    Scoreboard getWorldScoreboard();

    World getWorld();

    Container getOpenContainer();

    Container getInventoryContainer();

    AxisAlignedBB getEntityBoundingBox();

    NetHandlerPlayClient getConnection();
}
