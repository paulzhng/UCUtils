package de.fuzzlemann.ucutils.utils.tablist;

import net.minecraft.client.gui.GuiPlayerTabOverlay;

/**
 * @author Fuzzlemann
 */
public class DefaultTablistSorter implements ITablistSorter {
    @Override
    public void init() throws Exception {
        replaceOrdering(GuiPlayerTabOverlay.class);
    }
}
