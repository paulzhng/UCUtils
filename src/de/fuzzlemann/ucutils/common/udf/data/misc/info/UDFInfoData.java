package de.fuzzlemann.ucutils.common.udf.data.misc.info;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

/**
 * @author Fuzzlemann
 */
public class UDFInfoData extends Data<UDFInfo> {
    public UDFInfoData(UDFInfo udfInfo) {
        super(DataRegistry.INFO, 1, udfInfo);
    }
}
