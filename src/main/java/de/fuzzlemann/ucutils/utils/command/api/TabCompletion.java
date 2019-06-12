package de.fuzzlemann.ucutils.utils.command.api;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import jline.internal.Nullable;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public interface TabCompletion {

    @Nullable
    List<String> getTabCompletions(UPlayer p, String[] args);
}
