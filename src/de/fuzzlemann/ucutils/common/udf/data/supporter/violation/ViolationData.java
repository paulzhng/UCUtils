package de.fuzzlemann.ucutils.common.udf.data.supporter.violation;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class ViolationData extends Data<List<Violation>> {
    public ViolationData(List<Violation> violations) {
        super(DataRegistry.VIOLATIONS, 1, violations);
    }
}
