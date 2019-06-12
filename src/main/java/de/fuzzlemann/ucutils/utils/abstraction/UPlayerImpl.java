package de.fuzzlemann.ucutils.utils.abstraction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
    private final EntityPlayerSP player;

    public UPlayerImpl() {
        this.player = Minecraft.getMinecraft().player;
    }

    @Override
    public void sendMessage(ITextComponent textComponent) {
        player.sendMessage(textComponent);
    }

    @Override
    public void sendChatMessage(String message) {
        player.sendChatMessage(message);
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        player.playSound(soundIn, volume, pitch);
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getUniqueID() {
        return player.getUniqueID();
    }

    @Override
    public BlockPos getPosition() {
        return player.getPosition();
    }

    @Override
    public Team getTeam() {
        return player.getTeam();
    }

    @Override
    public Scoreboard getWorldScoreboard() {
        return player.getWorldScoreboard();
    }

    @Override
    public World getWorld() {
        return player.getEntityWorld();
    }

    @Override
    public Container getOpenContainer() {
        return player.openContainer;
    }

    @Override
    public Container getInventoryContainer() {
        return player.inventoryContainer;
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return player.getEntityBoundingBox();
    }

    @Override
    public NetHandlerPlayClient getConnection() {
        return player.connection;
    }

    public EntityPlayerSP getPlayer() {
        return player;
    }
}
