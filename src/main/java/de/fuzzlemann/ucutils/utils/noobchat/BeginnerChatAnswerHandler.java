package de.fuzzlemann.ucutils.utils.noobchat;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.supporter.beginnerchatanswer.BeginnerChatAnswer;
import de.fuzzlemann.ucutils.utils.ForgeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.BEGINNER_CHAT_ANSWERS, version = 1)
public class BeginnerChatAnswerHandler implements UDFLoader<List<BeginnerChatAnswer>> {

    private static final List<BeginnerChatAnswer> BEGINNER_CHAT_ANSWERS = new ArrayList<>();

    @Override
    public void supply(List<BeginnerChatAnswer> beginnerChatAnswers) {
        BEGINNER_CHAT_ANSWERS.addAll(beginnerChatAnswers);
    }

    public static List<String> getAnswerKeys() {
        return BEGINNER_CHAT_ANSWERS.stream()
                .map(BeginnerChatAnswer::getAnswerKey)
                .collect(Collectors.toList());
    }

    public static BeginnerChatAnswer getAnswer(String answerKey) {
        return ForgeUtils.getMostMatching(BEGINNER_CHAT_ANSWERS, answerKey, BeginnerChatAnswer::getAnswerKey);
    }
}
