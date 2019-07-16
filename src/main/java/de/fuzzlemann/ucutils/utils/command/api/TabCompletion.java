package de.fuzzlemann.ucutils.utils.command.api;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public interface TabCompletion {

    List<String> getTabCompletions(UPlayer p, String[] args);
}
