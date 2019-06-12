package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.teamspeak.events.ClientMovedEvent;
import de.fuzzlemann.ucutils.teamspeak.events.TSEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
public class TSEventHandler {

    static final Map<String, Class<? extends TSEvent>> TEAMSPEAK_EVENTS = new HashMap<>();

    static {
        registerEvent(ClientMovedEvent.class);
    }

    static TSEvent getEvent(String input) {
        int splitIndex = input.indexOf(" ");
        String eventName = input.substring(0, splitIndex);
        Class<? extends TSEvent> clazz = TEAMSPEAK_EVENTS.get(eventName);
        if (clazz == null) return null;

        String content = input.substring(splitIndex);
        try {
            return clazz.getConstructor(String.class).newInstance(content);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void registerEvent(Class<? extends TSEvent> clazz) {
        TSEvent.Name annotation = clazz.getAnnotation(TSEvent.Name.class);

        String eventName = annotation.value();
        TEAMSPEAK_EVENTS.put(eventName, clazz);
    }
}
