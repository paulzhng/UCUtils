/*
 * #%L
 * TeamSpeak 3 Java API
 * %%
 * Copyright (C) 2015 Bert De Geyter, Roger Baumgartner
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package de.fuzzlemann.ucutils.utils.teamspeak;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public final class ResultParser {

    public static Map<String, String> parse(String raw) {
        StringTokenizer st = new StringTokenizer(raw, " ", false);
        Map<String, String> options = new HashMap<>();

        while (st.hasMoreTokens()) {
            String tmp = st.nextToken();
            int pos = tmp.indexOf("=");

            if (pos == -1) {
                String valuelessKey = decode(tmp);
                options.put(valuelessKey, "");
            } else {
                String key = decode(tmp.substring(0, pos));
                String value = decode(tmp.substring(pos + 1));
                options.put(key, value);
            }
        }
        return options;
    }

    private static String decode(String str) {
        str = str.replace("\\s", " ");
        str = str.replace("\\/", "/");
        str = str.replace("\\p", "|");
        str = str.replace("\\b", "\b");
        str = str.replace("\\f", "\f");
        str = str.replace("\\n", "\n");
        str = str.replace("\\r", "\r");
        str = str.replace("\\t", "\t");
        str = str.replace("\\a", String.valueOf((char) 7)); // Bell
        str = str.replace("\\v", String.valueOf((char) 11)); // Vertical Tab

        str = str.replace("\\\\", "\\");
        return str;
    }
}