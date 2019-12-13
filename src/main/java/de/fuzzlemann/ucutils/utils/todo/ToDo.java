package de.fuzzlemann.ucutils.utils.todo;

import de.fuzzlemann.ucutils.base.command.ParameterParser;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Fuzzlemann
 */
@ParameterParser.At(ToDoParser.class)
public class ToDo {

    private final long created;
    private final int id;
    private String message;
    private boolean done;

    public ToDo(String message) {
        this.message = message;
        this.created = System.currentTimeMillis();
        this.id = ThreadLocalRandom.current().nextInt(0, 100000);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreated() {
        return created;
    }

    public int getID() {
        return id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void delete() {
        ToDoManager.getToDoList().remove(this);
        save();
    }

    public void add() {
        ToDoManager.getToDoList().add(this);
        save();
    }

    public void save() {
        ToDoManager.save();
    }
}
