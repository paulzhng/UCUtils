package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistReason;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class BlacklistPriceCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(labels = {"blacklistprice", "blprice", "blp"}, usage = "/%label% [setprice/list] (Grund) (Preis)")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        switch (args[0].toLowerCase()) {
            case "list":
                sendBlacklistPrices(p);
                break;
            case "setprice":
                if (args.length < 3) return false;

                BlacklistReason blacklistReason = BlacklistUtil.getBlacklistReason(args[1]);
                if (blacklistReason == null) {
                    p.sendMessage(TextUtils.simpleMessage("Der Blacklistgrund wurde nicht gefunden.", TextFormatting.RED));
                    return true;
                }

                int price;
                try {
                    price = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    return false;
                }

                blacklistReason.setPrice(price);
                BlacklistUtil.savePrices();

                p.sendMessage(Message.builder().of("Du hast den Preis von ").color(TextFormatting.AQUA).advance()
                        .of(blacklistReason.getReason()).color(TextFormatting.RED).advance()
                        .of(" zu ").color(TextFormatting.AQUA).advance()
                        .of(String.valueOf(price) + "$ ").color(TextFormatting.RED).advance()
                        .of("ge\u00e4ndert.").color(TextFormatting.AQUA).advance().build().toTextComponent());
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 2) return Arrays.asList("setprice", "list");
        if (!args[0].equalsIgnoreCase("setprice")) return Collections.emptyList();

        String drug = args[args.length - 1].toLowerCase();
        List<String> drugNames = BlacklistUtil.BLACKLIST_REASONS
                .stream()
                .map(BlacklistReason::getReason)
                .map(blacklistReason -> blacklistReason.replace(' ', '-'))
                .collect(Collectors.toList());

        if (drug.isEmpty()) return drugNames;

        drugNames.removeIf(drugName -> !drugName.toLowerCase().startsWith(drug));

        Collections.sort(drugNames);
        return drugNames;
    }

    private void sendBlacklistPrices(EntityPlayerSP p) {
        Message.MessageBuilder builder = Message.builder();

        builder.of("\u00bb ").color(TextFormatting.GOLD).advance().of("Eingestellte Blacklistpreise\n").color(TextFormatting.DARK_PURPLE).advance();
        for (BlacklistReason blacklistReason : BlacklistUtil.BLACKLIST_REASONS) {
            builder.of("  * " + blacklistReason.getReason()).color(TextFormatting.GRAY).advance()
                    .of(": ").color(TextFormatting.DARK_GRAY).advance()
                    .of(blacklistReason.getPrice() + "$\n").color(TextFormatting.RED).advance();
        }

        p.sendMessage(builder.build().toTextComponent());
    }
}
