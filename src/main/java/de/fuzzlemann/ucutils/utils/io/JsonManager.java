package de.fuzzlemann.ucutils.utils.io;

import com.google.gson.Gson;
import de.fuzzlemann.ucutils.utils.Logger;
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

    public static final File DIRECTORY = new File(FileManager.MC_DIRECTORY, "storage");

    /**
     * Writes a Collection to the file
     *
     * @param file       The Collection that the list should be written to
     * @param objectList The Collection that should be written to the file
     */
    public static <T> void writeList(File file, Collection<T> objectList) {
        createFile(file);

        Gson gson = new Gson();
        List<String> jsonList = new ArrayList<>();

        for (T object : objectList) {
            jsonList.add(gson.toJson(object));
        }

        try {
            FileUtils.writeLines(file, StandardCharsets.UTF_8.toString(), jsonList);
        } catch (IOException e) {
            Logger.LOGGER.catching(e);
        }
    }

    /**
     * Writes the Object to the file
     *
     * @param file   The file that the Object should be written to
     * @param object The Object that should be written to the file
     */
    public static <T> void writeObject(File file, T object) {
        createFile(file);

        Gson gson = new Gson();

        try {
            FileUtils.write(file, gson.toJson(object), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.LOGGER.catching(e);
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
    public static <T> List<T> loadObjects(File file, Class<T> clazz) {
        createFile(file);

        List<T> objectList = new ArrayList<>();

        Gson gson = new Gson();

        try {
            for (String line : FileUtils.readLines(file, StandardCharsets.UTF_8)) {
                T object = gson.fromJson(line, clazz);
                objectList.add(object);
            }
        } catch (IOException e) {
            Logger.LOGGER.catching(e);
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
    public static <T> T loadObject(File file, Class<T> clazz) {
        createFile(file);

        Gson gson = new Gson();

        String line = null;
        try {
            line = FileUtils.lineIterator(file, StandardCharsets.UTF_8.toString()).nextLine();
        } catch (IOException e) {
            Logger.LOGGER.catching(e);
        }

        return gson.fromJson(line, clazz);
    }

    private static void createFile(File file) {
        if (DIRECTORY.mkdir())
            Logger.LOGGER.info("[UCUtils] " + DIRECTORY + " created");
        try {
            if (file.createNewFile())
                Logger.LOGGER.info("[UCUtils] " + file.getAbsoluteFile() + " created");
        } catch (IOException e) {
            Logger.LOGGER.catching(e);
        }
    }
}
