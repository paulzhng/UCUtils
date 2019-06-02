package de.fuzzlemann.ucutils.utils.tablist;

import de.fuzzlemann.ucutils.utils.initializor.IInitializor;
import de.fuzzlemann.ucutils.utils.initializor.InitMode;
import de.fuzzlemann.ucutils.utils.initializor.Initializor;
import net.minecraft.client.gui.GuiPlayerTabOverlay;

/**
 * @author Fuzzlemann
 */
@Initializor(value = "TablistSorter", initMode = InitMode.DEFAULT)
public class DefaultTablistSorter implements IInitializor {
    @Override
    public void init() {
        TabListSortHandler.replaceOrdering(GuiPlayerTabOverlay.class);
    }
}
