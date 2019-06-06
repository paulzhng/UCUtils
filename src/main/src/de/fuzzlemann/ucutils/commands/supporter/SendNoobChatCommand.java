package de.fuzzlemann.ucutils.commands.supporter;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.noobchat.NoobChatAnswer;
import de.fuzzlemann.ucutils.utils.noobchat.NoobChatManager;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class SendNoobChatCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(value = {"sendnoobchat", "sendneulingschat", "snc"}, usage = "/%label% [Antwort-Kürzel]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String key = args[0].replace('-', ' ');
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
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 1) return null;

        return NoobChatManager.getAnswerKeys();
    }
}