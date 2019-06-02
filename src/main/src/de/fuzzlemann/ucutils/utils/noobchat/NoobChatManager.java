package de.fuzzlemann.ucutils.utils.noobchat;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.data.DataModule;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@DataModule("Neulingschat")
public class NoobChatManager implements DataLoader {

    private static final List<NoobChatAnswer> NOOB_CHAT_ANSWERS = new ArrayList<>();

    public static List<String> getAnswerKeys() {
        return NOOB_CHAT_ANSWERS.stream()
                .map(NoobChatAnswer::getAnswerKey)
                .collect(Collectors.toList());
    }

    public static NoobChatAnswer getAnswer(String answerKey) {
        return ForgeUtils.getMostMatching(NOOB_CHAT_ANSWERS, answerKey, NoobChatAnswer::getAnswerKey);
    }

    @Override
    public void load() {
        NOOB_CHAT_ANSWERS.clear();

        String result = APIUtils.get("http://fuzzlemann.de/noobchatanswers.html");
        String[] answers = result.split("\n");

        for (String answerString : answers) {
            String[] splittedAnswerString = StringUtils.split(answerString, ":", 2);

            String answerKey = StringEscapeUtils.unescapeJava(splittedAnswerString[0]);
            String[] answerMessages = StringEscapeUtils.unescapeJava(splittedAnswerString[1]).split("<>");

            NOOB_CHAT_ANSWERS.add(new NoobChatAnswer(answerKey, answerMessages));
        }
    }
}
