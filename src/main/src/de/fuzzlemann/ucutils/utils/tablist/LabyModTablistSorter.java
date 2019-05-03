package de.fuzzlemann.ucutils.utils.tablist;

import net.labymod.core_implementation.mc112.gui.ModPlayerTabOverlay;

/**
 * @author Fuzzlemann
 */
public class LabyModTablistSorter implements ITablistSorter {
    @Override
    public void init() throws Exception {
        replaceOrdering(ModPlayerTabOverlay.class);
    }
}
