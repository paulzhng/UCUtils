package de.fuzzlemann.ucutils.utils.todo;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Fuzzlemann
 */
public class ToDo {

    private final String message;
    private final long created;
    private final int id;
    private boolean done;

    public ToDo(String message) {
        this.message = message;
        this.done = false;
        this.created = System.currentTimeMillis();
        this.id = ThreadLocalRandom.current().nextInt(0, 100000);
    }

    public String getMessage() {
        return message;
    }

    public long getCreated() {
        return created;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getId() {
        return id;
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
