package de.fuzzlemann.ucutils.utils.teamspeak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
public class TSClientQuery {

    public static String apiKey = "";

    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static PrintWriter printWriter;

    public static Map<String, String> exec(String command) {
        return exec(command, false);
    }

    private static Map<String, String> exec(String command, boolean auth) {
        String result = rawExec(command, auth);
        if (result == null) return null;

        return ResultParser.parse(result);
    }

    public static String rawExec(String command, boolean auth) {
        return rawExec(command, auth, true);
    }

    private static String rawExec(String command, boolean auth, boolean tryAgain) {
        if (auth && !connect()) return null;

        if (bufferedReader == null) connect();

        String result;
        try {
            while (bufferedReader.ready()) {
                bufferedReader.readLine();
            }

            System.out.println("command: " + command);
            printWriter.println(command);

            result = bufferedReader.readLine();
            System.out.println("result: " + result);

            if (tryAgain && result != null && result.equals("error id=1796 msg=currently\\snot\\spossible")) {
                auth();
                return rawExec(command, auth, false);
            }
        } catch (SocketException e) {
            connect(false);
            return rawExec(command, auth, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static boolean connect() {
        return connect(true);
    }

    private static boolean connect(boolean check) {
        if (check && socket != null && !socket.isClosed()) return true;

        try {
            socket = new Socket("127.0.0.1", 25639);

            socket.setTcpNoDelay(true);
            socket.setSoTimeout(4000);

            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            while (bufferedReader.ready()) bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        KeepAliveUtil.start();
        return auth();
    }

    private static boolean auth() {
        if (apiKey == null || apiKey.isEmpty()) return false;

        Map<String, String> result = exec("auth apikey=" + apiKey, true);
        if (result == null) return false;

        String msg = result.get("msg");
        return msg != null && msg.equals("ok");
    }
}
