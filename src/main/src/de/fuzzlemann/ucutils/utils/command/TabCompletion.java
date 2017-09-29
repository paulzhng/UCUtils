package de.fuzzlemann.ucutils.utils.command;

import net.minecraft.client.entity.EntityPlayerSP;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public interface TabCompletion {

    List<String> getTabCompletions(EntityPlayerSP p, String[] args);
}
