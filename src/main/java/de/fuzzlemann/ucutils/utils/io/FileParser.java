package de.fuzzlemann.ucutils.utils.io;

import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;

import java.io.File;

/**
 * @author Fuzzlemann
 */
public class FileParser implements ParameterParser<String, File> {
    @Override
    public File parse(String input) {
        File file = new File(input);

        return file.exists() ? file : null;
    }

    @Override
    public String errorMessage() {
        return "Die Datei wurde nicht gefunden.";
    }
}
