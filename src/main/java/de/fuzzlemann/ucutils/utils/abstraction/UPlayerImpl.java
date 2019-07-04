package de.fuzzlemann.ucutils.utils.abstraction;

import de.fuzzlemann.ucutils.Main;
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
    
    @Override
    public boolean isConnected() {
        return getPlayer() != null;
    }

    @Override
    public void sendMessage(ITextComponent textComponent) {
        getPlayer().sendMessage(textComponent);
    }

    @Override
    public void sendChatMessage(String message) {
        getPlayer().sendChatMessage(message);
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        getPlayer().playSound(soundIn, volume, pitch);
    }

    @Override
    public String getName() {
        return getPlayer().getName();
    }

    @Override
    public UUID getUniqueID() {
        return getPlayer().getUniqueID();
    }

    @Override
    public BlockPos getPosition() {
        return getPlayer().getPosition();
    }

    @Override
    public Team getTeam() {
        return getPlayer().getTeam();
    }

    @Override
    public Scoreboard getWorldScoreboard() {
        return getPlayer().getWorldScoreboard();
    }

    @Override
    public World getWorld() {
        return getPlayer().getEntityWorld();
    }

    @Override
    public Container getOpenContainer() {
        return getPlayer().openContainer;
    }

    @Override
    public Container getInventoryContainer() {
        return getPlayer().inventoryContainer;
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return getPlayer().getEntityBoundingBox();
    }

    @Override
    public NetHandlerPlayClient getConnection() {
        return getPlayer().connection;
    }
    
    private EntityPlayerSP getPlayer() {
        return Main.MINECRAFT.player;
    }
}
