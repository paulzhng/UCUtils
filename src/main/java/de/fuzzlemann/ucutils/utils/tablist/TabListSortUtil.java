package de.fuzzlemann.ucutils.utils.tablist;

import com.google.common.collect.Ordering;
import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class TabListSortUtil {
    public static void replaceOrdering(Class clazz) {
        ReflectionUtil.setValue(clazz, Ordering.class, Ordering.from(new TabListComparator()));
    }
}
