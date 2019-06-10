package de.fuzzlemann.ucutils.utils.command.api;

import jline.internal.Nullable;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public interface TabCompletion {

    @Nullable
    List<String> getTabCompletions(EntityPlayerSP p, String[] args);
}
