package de.fuzzlemann.ucutils.utils.command;

import net.minecraft.client.entity.EntityPlayerSP;

/**
 * @author Fuzzlemann
 */
public interface CommandExecutor {

    boolean onCommand(EntityPlayerSP p, String[] args);
}
