package de.fuzzlemann.ucutils.utils.io;

import com.google.gson.Gson;
import de.fuzzlemann.ucutils.Main;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class JsonManager {

    public static final File DIRECTORY = new File(Main.MINECRAFT.mcDataDir, "storage");

    /**
     * Writes a Collection to the file
     *
     * @param file       The Collection that the list should be written to
     * @param objectList The Collection that should be written to the file
     */
    public static void writeList(File file, Collection<?> objectList) {
        createFile(file);

        Gson gson = new Gson();
        List<String> jsonList = new ArrayList<>();

        for (Object object : objectList) {
            jsonList.add(gson.toJson(object));
        }

        try {
            FileUtils.writeLines(file, StandardCharsets.UTF_8.toString(), jsonList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the Object to the file
     *
     * @param file   The file that the Object should be written to
     * @param object The Object that should be written to the file
     */
    public static void writeObject(File file, Object object) {
        createFile(file);

        Gson gson = new Gson();

        try {
            FileUtils.write(file, gson.toJson(object), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all the objects stored in the given file.
     * One object is one line.
     *
     * @param file  The file from which the objects are loaded from
     * @param clazz The class which the objects corresponds to
     * @return A list of all objects loaded
     */
    public static List<Object> loadObjects(File file, Class<?> clazz) {
        createFile(file);

        List<Object> objectList = new ArrayList<>();

        Gson gson = new Gson();

        try {
            for (String line : FileUtils.readLines(file, StandardCharsets.UTF_8)) {
                Object object = gson.fromJson(line, clazz);
                objectList.add(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectList;
    }

    /**
     * Loads the object stored in the given file
     *
     * @param file  The file from which the object is loaded from
     * @param clazz The class which the object corresponds to
     * @return The object that's loaded
     */
    public static Object loadObject(File file, Class<?> clazz) {
        createFile(file);

        Gson gson = new Gson();

        String line = null;
        try {
            line = FileUtils.lineIterator(file, StandardCharsets.UTF_8.toString()).nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gson.fromJson(line, clazz);
    }

    private static void createFile(File file) {
        if (DIRECTORY.mkdir())
            System.out.println("[UCUtils] " + DIRECTORY + " created");
        try {
            if (file.createNewFile())
                System.out.println("[UCUtils] " + file.getAbsoluteFile() + " created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
