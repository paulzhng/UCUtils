package de.fuzzlemann.ucutils.utils.tablist;

import de.fuzzlemann.ucutils.base.initializor.IInitializor;
import de.fuzzlemann.ucutils.base.initializor.InitMode;
import de.fuzzlemann.ucutils.base.initializor.Initializor;
import net.labymod.core_implementation.mc112.gui.ModPlayerTabOverlay;

/**
 * @author Fuzzlemann
 */
@Initializor(value = "TablistSorter", initMode = InitMode.LABY_MOD)
public class LabyModTablistSorter implements IInitializor {
    @Override
    public void init() {
        TabListSortUtil.replaceOrdering(ModPlayerTabOverlay.class);
    }
}
