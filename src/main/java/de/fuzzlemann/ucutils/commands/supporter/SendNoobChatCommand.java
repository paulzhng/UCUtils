package de.fuzzlemann.ucutils.commands.supporter;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.common.udf.data.supporter.beginnerchatanswer.BeginnerChatAnswer;
import de.fuzzlemann.ucutils.utils.noobchat.BeginnerChatAnswerHandler;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class SendNoobChatCommand implements TabCompletion {

    @Command(value = {"sendnoobchat", "sendneulingschat", "snc"}, usage = "/%label% [Antwort-Kürzel]")
    public boolean onCommand(UPlayer p, String answerKey) {
        String key = answerKey.replace('-', ' ');
        BeginnerChatAnswer answer = BeginnerChatAnswerHandler.getAnswer(key);

        if (answer == null) {
            TextUtils.error("Es wurde keine Antwort mit diesem Antwort-Kürzel gefunden.");
            return true;
        }

        for (String answerPart : answer.getLines()) {
            p.sendChatMessage("/nc " + answerPart);
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length != 1) return null;

        return BeginnerChatAnswerHandler.getAnswerKeys();
    }
}