package de.fuzzlemann.ucutils.commands.supporter;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.noobchat.NoobChatAnswer;
import de.fuzzlemann.ucutils.utils.noobchat.NoobChatManager;
import de.fuzzlemann.ucutils.utils.text.TextUtils;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class SendNoobChatCommand implements TabCompletion {

    @Command(value = {"sendnoobchat", "sendneulingschat", "snc"}, usage = "/%label% [Antwort-Kürzel]")
    public boolean onCommand(UPlayer p, String answerKey) {
        String key = answerKey.replace('-', ' ');
        NoobChatAnswer answer = NoobChatManager.getAnswer(key);

        if (answer == null) {
            TextUtils.error("Es wurde keine Antwort mit diesem Antwort-Kürzel gefunden.");
            return true;
        }

        for (String answerPart : answer.getAnswers()) {
            p.sendChatMessage("/nc " + answerPart);
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length != 1) return null;

        return NoobChatManager.getAnswerKeys();
    }
}