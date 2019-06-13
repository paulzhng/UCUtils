package de.fuzzlemann.ucutils.utils.abstraction;

import de.fuzzlemann.ucutils.Main;
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
public class UPlayerImpl implements UPlayer {

    @Override
    public void sendMessage(ITextComponent textComponent) {
        Main.MINECRAFT.player.sendMessage(textComponent);
    }

    @Override
    public void sendChatMessage(String message) {
        Main.MINECRAFT.player.sendChatMessage(message);
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        Main.MINECRAFT.player.playSound(soundIn, volume, pitch);
    }

    @Override
    public String getName() {
        return Main.MINECRAFT.player.getName();
    }

    @Override
    public UUID getUniqueID() {
        return Main.MINECRAFT.player.getUniqueID();
    }

    @Override
    public BlockPos getPosition() {
        return Main.MINECRAFT.player.getPosition();
    }

    @Override
    public Team getTeam() {
        return Main.MINECRAFT.player.getTeam();
    }

    @Override
    public Scoreboard getWorldScoreboard() {
        return Main.MINECRAFT.player.getWorldScoreboard();
    }

    @Override
    public World getWorld() {
        return Main.MINECRAFT.player.getEntityWorld();
    }

    @Override
    public Container getOpenContainer() {
        return Main.MINECRAFT.player.openContainer;
    }

    @Override
    public Container getInventoryContainer() {
        return Main.MINECRAFT.player.inventoryContainer;
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return Main.MINECRAFT.player.getEntityBoundingBox();
    }

    @Override
    public NetHandlerPlayClient getConnection() {
        return Main.MINECRAFT.player.connection;
    }
}
