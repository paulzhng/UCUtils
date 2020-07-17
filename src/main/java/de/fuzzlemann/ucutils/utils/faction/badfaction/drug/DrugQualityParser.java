package de.fuzzlemann.ucutils.utils.faction.badfaction.drug;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugQuality;

public class DrugQualityParser implements ParameterParser<String, DrugQuality> {
    @Override
    public DrugQuality parse(String input) {
        for (DrugQuality drugQuality : DrugQuality.values()) {
            if (drugQuality.getName().equalsIgnoreCase(input)) return drugQuality;
        }

        return null;
    }

    @Override
    public String errorMessage() {
        return "Die Drogenqualität wurde nicht gefunden.";
    }
}
