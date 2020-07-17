/*
 * Copyright 2014 DV8FromTheWorld (Austin Keener)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fuzzlemann.ucutils.utils.image;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Derived from https://github.com/DV8FromTheWorld/Imgur-Uploader-Java/blob/master/src/net/dv8tion/Uploader.java
 *
 * @author DV8FromTheWorld (Austin Keener)
 */
public class ImageUploader {

    private static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";

    public static String uploadToLink(File file) {
        String json = upload(file);

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        return jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("link").getAsString();
    }

    /**
     * Takes a file and uploads it to Imgur.
     * Does not check to see if the file is an image, this should be done
     * before the file is passed to this method.
     *
     * @param file The image to be uploaded to Imgur.
     * @return The JSON response from Imgur.
     */
    public static String upload(File file) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(UPLOAD_API_URL);
            writeToConnection(conn, "image=" + toBase64(file));

            return getResponse(conn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    /**
     * Converts a file to a Base64 String.
     *
     * @param file The file to be converted.
     * @return The file as a Base64 String.
     */
    private static String toBase64(File file) throws IOException {
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return URLEncoder.encode(new String(encoded, StandardCharsets.US_ASCII), StandardCharsets.UTF_8.toString());
    }

    /**
     * Creates and sets up an HttpURLConnection for use with the Imgur API.
     *
     * @param url The URL to connect to. (check Imgur API for correct URL).
     * @return The newly created HttpURLConnection.
     */
    private static HttpURLConnection getHttpConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID 578628302728265");
        conn.setReadTimeout(100000);
        conn.connect();

        return conn;
    }

    private static void writeToConnection(HttpURLConnection conn, String message) throws IOException {
        try (OutputStream outputStream = conn.getOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            writer.write(message);
            writer.flush();
        }
    }

    private static String getResponse(HttpURLConnection conn) throws IOException {
        InputStream inputStream = conn.getInputStream();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            inputStream.close();
        }

        return sb.toString();
    }
}
