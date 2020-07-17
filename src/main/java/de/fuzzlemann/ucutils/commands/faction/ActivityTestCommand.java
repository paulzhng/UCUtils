package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.activitytest.ActivityTestHandler;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.common.activity.ActivityTestType;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class ActivityTestCommand implements TabCompletion {

    @Command(value = "activitytest", usage = "/%label% [Thema]")
    public boolean onCommand(ActivityTestType testType, @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer id) {
        if (id != null && !ActivityTestHandler.remaining.remove(id)) {
            TextUtils.error("Diese Aktivität wurde bereits eingetragen.");
            return true;
        }

        ActivityTestHandler.enterActivityTest(testType);
        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length != 1) return null;

        return Arrays.stream(ActivityTestType.values())
                .map(ActivityTestType::getName)
                .collect(Collectors.toList());
    }
}
