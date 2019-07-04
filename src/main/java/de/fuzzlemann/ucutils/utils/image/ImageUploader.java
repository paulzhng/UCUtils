package de.fuzzlemann.ucutils.utils.image;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Derived from https://github.com/DV8FromTheWorld/Imgur-Uploader-Java/blob/master/src/net/dv8tion/Uploader.java
 *
 * @author DV8FromTheWorld (Austin Keener)
 */
public class ImageUploader {

    public static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";

    /**
     * Takes a file and uploads it to Imgur.
     * Does not check to see if the file is an image, this should be done
     * before the file is passed to this method.
     *
     * @param file The image to be uploaded to Imgur.
     * @return The JSON response from Imgur.
     */
    public static String upload(File file) {
        HttpURLConnection conn = getHttpConnection(UPLOAD_API_URL);
        writeToConnection(conn, "image=" + toBase64(file));
        return getResponse(conn);
    }

    /**
     * Converts a file to a Base64 String.
     *
     * @param file The file to be converted.
     * @return The file as a Base64 String.
     */
    private static String toBase64(File file) {
        try {
            byte[] b = new byte[(int) file.length()];
            try (FileInputStream fs = new FileInputStream(file)) {
                fs.read(b);
            }
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates and sets up an HttpURLConnection for use with the Imgur API.
     *
     * @param url The URL to connect to. (check Imgur API for correct URL).
     * @return The newly created HttpURLConnection.
     */
    private static HttpURLConnection getHttpConnection(String url) {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID 578628302728265");
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeToConnection(HttpURLConnection conn, String message) {
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getResponse(HttpURLConnection conn) {
        StringBuilder str = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return str.toString();
    }

}
