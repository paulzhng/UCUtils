package de.fuzzlemann.ucutils.common.udf.data.misc.cape;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class CapeData extends Data<List<UDFCape>> {

    public CapeData(List<UDFCape> udfCapes) {
        super(DataRegistry.CAPES, 1, udfCapes);
    }

}
