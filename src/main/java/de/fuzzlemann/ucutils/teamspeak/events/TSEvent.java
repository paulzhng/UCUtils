package de.fuzzlemann.ucutils.teamspeak.events;

import de.fuzzlemann.ucutils.teamspeak.TSParser;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
public abstract class TSEvent extends Event {

    private final String input;
    protected Map<String, String> map;

    TSEvent(String input) {
        super();
        this.input = input;
        this.map = TSParser.parse(input);
    }

    public String getInput() {
        return input;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Name {
        String value();
    }
}
