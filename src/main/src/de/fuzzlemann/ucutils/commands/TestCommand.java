package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * @author Fuzzlemann
 */
public class TestCommand implements CommandExecutor {

    @Override
    @Command(labels = "test")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        return true;
    }
}
