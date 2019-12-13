package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.TextUtils;

/**
 * @author Fuzzlemann
 */
public class ResetActivityTestCommand {

    @Command(value = "resetactivitytest", async = true)
    public boolean onCommand() {
        String str = APIUtils.postAuthenticated("http://tomcat.fuzzlemann.de/factiononline/activityTest/reset");

        if (!str.equals("success")) {
            TextUtils.error("Die Aktivitäten konnten nicht zurückgesetzt werden: " + str);
            return true;
        }

        TextUtils.simpleMessage("Die Aktivitäten wurden erfolgreich zurückgesetzt.");
        return true;
    }
}
