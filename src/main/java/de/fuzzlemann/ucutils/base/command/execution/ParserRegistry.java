package de.fuzzlemann.ucutils.base.command.execution;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import de.fuzzlemann.ucutils.utils.faction.ActivityTestParser;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistParser;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.DrugQualityParser;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.DrugTypeParser;
import de.fuzzlemann.ucutils.utils.house.HouseParser;
import de.fuzzlemann.ucutils.utils.io.FileParser;
import de.fuzzlemann.ucutils.utils.punishment.ViolationParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
class ParserRegistry {

    static Map<Class<?>, Class<? extends ParameterParser<?, ?>>> PARSER_REGISTRY = new HashMap<>();

    static {
        registerParser(HouseParser.class);
        registerParser(ActivityTestParser.class);
        registerParser(FileParser.class);
        registerParser(ViolationParser.class);
        registerParser(DrugTypeParser.class);
        registerParser(DrugQualityParser.class);
        registerParser(BlacklistParser.class);
    }

    static void registerParser(Class<? extends ParameterParser<?, ?>> parserClass) {
        Class<?> objectClass = ReflectionUtil.getGenericParameter(parserClass, 0, 1);

        PARSER_REGISTRY.put(objectClass, parserClass);
    }
}
