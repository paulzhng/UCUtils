package de.fuzzlemann.ucutils.utils.io;

import com.google.gson.Gson;
import de.fuzzlemann.ucutils.Main;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class JsonManager {

    public static final File DIRECTORY = new File(Main.MINECRAFT.mcDataDir, "storage");

    /**
     * Writes the HashMap to the file
     *
     * @param file       The file that the HashMap should be written to
     * @param objectList The HashMap that should be written to the file
     */
    @SneakyThrows
    public static void writeList(File file, List<?> objectList) {
        createFile(file);

        @Cleanup
        val writer = new BufferedWriter(new FileWriter(file));
        val gson = new Gson();

        for (val object : objectList) {
            writer.write(gson.toJson(object));
            writer.newLine();
        }
    }

    /**
     * Writes the Object to the file
     *
     * @param file   The file that the Object should be written to
     * @param object The Object that should be written to the file
     */
    @SneakyThrows
    public static void writeObject(File file, Object object) {
        createFile(file);

        @Cleanup
        val writer = new BufferedWriter(new FileWriter(file));
        val gson = new Gson();

        writer.write(gson.toJson(object));
    }

    /**
     * Loads all the objects stored in the given file.
     * One object is one line.
     *
     * @param file  The file from which the objects are loaded from
     * @param clazz The class which the objects corresponds to
     * @return A list of all objects loaded
     */
    @SneakyThrows
    public static List<Object> loadObjects(File file, Class<?> clazz) {
        createFile(file);

        List<Object> objectList = new ArrayList<>();

        @Cleanup
        val reader = new BufferedReader(new FileReader(file));
        val gson = new Gson();

        String line;
        while ((line = reader.readLine()) != null) {
            val object = gson.fromJson(line, clazz);
            objectList.add(object);
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
    @SneakyThrows
    public static Object loadObject(File file, Class<?> clazz) {
        createFile(file);

        @Cleanup
        val reader = new BufferedReader(new FileReader(file));
        val gson = new Gson();

        val line = reader.readLine();
        return gson.fromJson(line, clazz);
    }

    @SneakyThrows
    private static void createFile(File file) {
        if (DIRECTORY.mkdir())
            System.out.println("[UCUtils] " + DIRECTORY + " created");
        if (file.createNewFile())
            System.out.println("[UCUtils] " + file.getAbsoluteFile() + " created");
    }
}
