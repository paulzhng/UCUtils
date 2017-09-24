package de.fuzzlemann.ucutils.utils.todo;

import de.fuzzlemann.ucutils.utils.io.JsonManager;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class ToDoManager {
    private static final File TO_DO_FILE = new File(JsonManager.directory, "toDo.storage");
    private static final List<ToDo> TO_DO_LIST = JsonManager.loadObjects(TO_DO_FILE, ToDo.class)
            .stream()
            .map(object -> (ToDo) object)
            .collect(Collectors.toList());

    public static ToDo getToDo(int id) {
        return TO_DO_LIST.stream()
                .filter(toDo -> toDo.getId() == id)
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
