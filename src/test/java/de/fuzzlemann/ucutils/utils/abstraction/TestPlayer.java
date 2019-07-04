package de.fuzzlemann.ucutils.utils.abstraction;

import de.fuzzlemann.ucutils.utils.Logger;
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
public class TestPlayer implements UPlayer {

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void sendMessage(ITextComponent textComponent) {
        Logger.LOGGER.info("MESSAGE: " + textComponent.getUnformattedText());
    }

    @Override
    public void sendChatMessage(String message) {
        Logger.LOGGER.info("SELF: " + message);
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        Logger.LOGGER.info("soundIn = " + soundIn + ", volume = " + volume + ", pitch = " + pitch);
    }

    @Override
    public String getName() {
        return "TestPlayer";
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(0, 0, 0);
    }

    @Override
    public Team getTeam() {
        return null;
    }

    @Override
    public Scoreboard getWorldScoreboard() {
        return null;
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public Container getOpenContainer() {
        return null;
    }

    @Override
    public Container getInventoryContainer() {
        return null;
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return null;
    }

    @Override
    public NetHandlerPlayClient getConnection() {
        return null;
    }

    @Override
    public UUID getUniqueID() {
        return UUID.fromString("4ed47123-7831-4ca2-a18b-131b77711dbe");
    }
}
