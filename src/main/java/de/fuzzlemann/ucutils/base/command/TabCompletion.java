package de.fuzzlemann.ucutils.base.command;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public interface TabCompletion {

    List<String> getTabCompletions(UPlayer p, String[] args);
}
