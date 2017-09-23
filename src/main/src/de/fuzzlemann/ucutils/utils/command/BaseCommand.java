package de.fuzzlemann.ucutils.utils.command;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
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
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class BaseCommand extends CommandBase implements IClientCommand {

    private final String NAME;

    BaseCommand(String name) {
        this.NAME = name;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return true;
    }

    @Override
    @Nonnull
    public String getName() {
        return NAME;
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/" + NAME;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        CommandHandler.issueCommand(NAME, args);
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        List<String> players = ForgeUtils.getOnlinePlayers();

        if (players.isEmpty()) return players;

        String input = args[args.length - 1].toLowerCase();

        if (input.isEmpty()) return players;

        List<String> matchingPlayers = new ArrayList<>();

        for (String playerName : players) {
            if (playerName.toLowerCase().startsWith(input))
                matchingPlayers.add(playerName);
        }

        Collections.sort(matchingPlayers);

        return matchingPlayers;
    }
}
