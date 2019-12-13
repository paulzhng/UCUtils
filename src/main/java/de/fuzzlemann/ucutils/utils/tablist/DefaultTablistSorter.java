package de.fuzzlemann.ucutils.utils.tablist;

import de.fuzzlemann.ucutils.base.initializor.IInitializor;
import de.fuzzlemann.ucutils.base.initializor.InitMode;
import de.fuzzlemann.ucutils.base.initializor.Initializor;
import net.minecraft.client.gui.GuiPlayerTabOverlay;

/**
 * @author Fuzzlemann
 */
@Initializor(value = "TablistSorter", initMode = InitMode.DEFAULT)
public class DefaultTablistSorter implements IInitializor {
    @Override
    public void init() {
        TabListSortUtil.replaceOrdering(GuiPlayerTabOverlay.class);
    }
}
