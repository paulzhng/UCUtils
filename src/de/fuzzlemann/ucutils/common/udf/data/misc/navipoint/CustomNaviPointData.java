package de.fuzzlemann.ucutils.common.udf.data.misc.navipoint;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class CustomNaviPointData extends Data<List<CustomNaviPoint>> {
    public CustomNaviPointData(List<CustomNaviPoint> customNaviPoints) {
        super(DataRegistry.CUSTOM_NAVI_POINT, 1, customNaviPoints);
    }
}
