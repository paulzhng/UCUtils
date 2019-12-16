package de.fuzzlemann.ucutils.common.udf.data.supporter.beginnerchatanswer;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class BeginnerChatAnswerData extends Data<List<BeginnerChatAnswer>> {
    public BeginnerChatAnswerData(List<BeginnerChatAnswer> beginnerChatAnswer) {
        super(DataRegistry.BEGINNER_CHAT_ANSWERS, 1, beginnerChatAnswer);
    }
}
