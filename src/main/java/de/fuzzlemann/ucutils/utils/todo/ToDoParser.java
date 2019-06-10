package de.fuzzlemann.ucutils.utils.todo;

import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;

/**
 * @author Fuzzlemann
 */
public class ToDoParser implements ParameterParser<Integer, ToDo> {
    @Override
    public ToDo parse(Integer input) {
        return ToDoManager.getToDo(input);
    }

    @Override
    public String errorMessage() {
        return "Die ToDo wurde nicht gefunden.";
    }
}
