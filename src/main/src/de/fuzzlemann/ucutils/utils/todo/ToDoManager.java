package de.fuzzlemann.ucutils.utils.todo;

import de.fuzzlemann.ucutils.utils.io.JsonManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class ToDoManager {
    private static final File TO_DO_FILE = new File(JsonManager.DIRECTORY, "toDo.storage");
    private static final List<ToDo> TO_DO_LIST = new ArrayList<>(JsonManager.loadObjects(TO_DO_FILE, ToDo.class));

    public static ToDo getToDo(int id) {
        return TO_DO_LIST.stream()
                .filter(toDo -> toDo.getID() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<ToDo> getToDoList() {
        return TO_DO_LIST;
    }

    public static void save() {
        new Thread(() -> JsonManager.writeList(TO_DO_FILE, TO_DO_LIST)).start();
    }
}
