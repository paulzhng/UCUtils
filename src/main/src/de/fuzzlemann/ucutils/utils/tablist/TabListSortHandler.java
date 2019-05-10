package de.fuzzlemann.ucutils.utils.tablist;

import com.google.common.collect.ImmutableSet;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class TabListSortHandler {

    private static final Set<Class<? extends ITablistSorter>> TABLIST_SORTERS = ImmutableSet.of(LabyModTablistSorter.class, DefaultTablistSorter.class);

    public static void init() {
        for (Class<? extends ITablistSorter> tablistSorter : TABLIST_SORTERS) {
            System.out.println("Trying to load " + tablistSorter.getName() + " as tablist sorter...");

            try {
                tablistSorter.newInstance().init();
                System.out.println("Initialized " + tablistSorter.getName() + " as tablist sorter.");
                break;
            } catch (Exception | NoClassDefFoundError e) {
                System.out.println("Couldn't load " + tablistSorter.getName() + " as tablist sorter.");
            }
        }
    }
}
