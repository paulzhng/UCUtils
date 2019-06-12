package de.fuzzlemann.ucutils.teamspeak;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class CommandResponse {

    protected final String rawResponse;
    private Map<String, String> response;
    private List<Map<String, String>> responseList;

    public CommandResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public static int parseInt(String str) {
        if (str == null) return 0;

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double parseDouble(String str) {
        if (str == null) return 0;

        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long parseLong(String str) {
        if (str == null) return 0;

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean parseBoolean(String str) {
        return str != null && str.equals("1");
    }

    public boolean succeeded() {
        String msg = getResponse().get("msg");
        if (msg == null) return false;

        return msg.equals("ok");
    }

    public Map<String, String> getResponse() {
        if (response == null) {
            response = ResponseParser.parse(rawResponse);
        }

        return response;
    }

    public List<Map<String, String>> getResponseList() {
        if (responseList == null) {
            responseList = ResponseParser.parseMap(rawResponse);
        }

        return responseList;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CommandResponse.class.getSimpleName() + "[", "]")
                .add("rawResponse='" + rawResponse + "'")
                .add("response=" + response)
                .add("responseList=" + responseList)
                .toString();
    }
}
