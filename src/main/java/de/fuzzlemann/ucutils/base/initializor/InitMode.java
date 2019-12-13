package de.fuzzlemann.ucutils.base.initializor;

import de.fuzzlemann.ucutils.utils.ForgeUtils;

/**
 * @author Fuzzlemann
 */
public enum InitMode implements Comparable<InitMode> {
    LABY_MOD(ForgeUtils.hasLabyMod()),
    DEFAULT();

    private final boolean[] preConditions;

    InitMode(boolean... preConditions) {
        this.preConditions = preConditions;
    }

    public boolean checkPreConditions() {
        for (boolean preCondition : preConditions) {
            if (!preCondition) return false;
        }

        return true;
    }
}
