package de.fuzzlemann.ucutils.utils.noobchat;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class NoobChatManager {

    private static final List<NoobChatAnswer> NOOB_CHAT_ANSWERS = new ArrayList<>();

    public static void fillNoobChatAnswerList() throws IOException {
        URL url = new URL("http://fuzzlemann.de/noobchatanswers.html");
        String result = IOUtils.toString(url, StandardCharsets.UTF_8);

        String[] answers = result.split("\n");

        for (String answerString : answers) {
            String[] splittedAnswerString = StringUtils.split(answerString, ":", 2);

            String answerKey = StringEscapeUtils.unescapeJava(splittedAnswerString[0]);
            String[] answerMessages = StringEscapeUtils.unescapeJava(splittedAnswerString[1]).split("<>");

            NOOB_CHAT_ANSWERS.add(new NoobChatAnswer(answerKey, answerMessages));
        }
    }

    public static List<String> getAnswerKeys() {
        return NOOB_CHAT_ANSWERS.stream()
                .map(NoobChatAnswer::getAnswerKey)
                .collect(Collectors.toList());
    }

    public static NoobChatAnswer getAnswer(String answerKey) {
        for (NoobChatAnswer wanted : NOOB_CHAT_ANSWERS) {
            if (wanted.getAnswerKey().equalsIgnoreCase(answerKey)) return wanted;
        }

        NoobChatAnswer foundNoobChatAnswer = null;
        String lowerAnswerKey = answerKey.toLowerCase();

        int delta = Integer.MAX_VALUE;

        for (NoobChatAnswer noobChatAnswer : NOOB_CHAT_ANSWERS) {
            String currentAnswerKey = noobChatAnswer.getAnswerKey().toLowerCase();
            if (!currentAnswerKey.startsWith(lowerAnswerKey)) continue;

            int curDelta = Math.abs(currentAnswerKey.length() - lowerAnswerKey.length());
            if (curDelta < delta) {
                foundNoobChatAnswer = noobChatAnswer;
                delta = curDelta;
            }

            if (curDelta == 0) break;
        }

        return foundNoobChatAnswer;
    }

}
