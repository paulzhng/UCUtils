package de.fuzzlemann.ucutils.commands.police;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.police.Wanted;
import de.fuzzlemann.ucutils.utils.police.WantedManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ASUCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(labels = "asu", usage = "/%label% [Spieler] [Grund]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        String player = args[0];
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Wanted wanted = WantedManager.getWanted(reason.replace('-', ' '));

        if (wanted == null) {
            TextComponentString text = new TextComponentString("Der Wantedgrund wurde nicht gefunden.");
            text.getStyle().setColor(TextFormatting.RED);

            p.sendMessage(text);
            return true;
        }

        p.sendChatMessage("/su " + wanted.getAmount() + " " + player + " " + wanted.getReason());
        return true;
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 2) return Collections.emptyList();

        String reason = args[args.length - 1].toLowerCase();
        List<String> wantedReasons = WantedManager.getWantedReasons()
                .stream()
                .map(wantedReason -> wantedReason.replace(" ", "-"))
                .collect(Collectors.toList());

        if (reason.isEmpty()) return wantedReasons;

        List<String> filteredReasons = new ArrayList<>();

        for (String wantedReason : wantedReasons) {
            if (wantedReason.toLowerCase().startsWith(reason.toLowerCase())) {
                filteredReasons.add(wantedReason);
            }
        }

        Collections.sort(filteredReasons);

        return filteredReasons;
    }
}
