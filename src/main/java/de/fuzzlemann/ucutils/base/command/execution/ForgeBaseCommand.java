package de.fuzzlemann.ucutils.base.command.execution;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Forge representation of the command
 *
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
class ForgeBaseCommand extends CommandBase implements IClientCommand {

    private final String name;
    private final TabCompletion tabCompletion;
    private final boolean management;

    ForgeBaseCommand(String name, @Nullable TabCompletion tabCompletion, boolean management) {
        this.name = name;
        this.tabCompletion = tabCompletion;
        this.management = management;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    @Nonnull
    public String getName() {
        return name;
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/" + name;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        CommandHandler.issueCommand(name, args);
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return !management || sender.getName().equals("Fuzzlemann");
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        String input = args[args.length - 1].toLowerCase();

        List<String> tabCompletions;
        if (tabCompletion != null) {
            List<String> returnedTabCompletions = tabCompletion.getTabCompletions(AbstractionLayer.getPlayer(), args);

            if (returnedTabCompletions != null) {
                tabCompletions = new ArrayList<>(returnedTabCompletions); //prevent UnsupportedOperationException when an immutable list is returned

                if (tabCompletions.isEmpty()) tabCompletions = ForgeUtils.getOnlinePlayers();
            } else {
                tabCompletions = null;
            }
        } else {
            tabCompletions = ForgeUtils.getOnlinePlayers();
        }

        if (tabCompletions == null) return Collections.emptyList();

        List<String> replacedCompletions = new ArrayList<>();
        for (String completion : tabCompletions) {
            replacedCompletions.add(completion.replace(' ', '-'));
        }

        if (input.isEmpty()) return replacedCompletions;

        replacedCompletions.removeIf(tabComplete -> !tabComplete.toLowerCase().startsWith(input));
        Collections.sort(replacedCompletions);

        return replacedCompletions;
    }
}
