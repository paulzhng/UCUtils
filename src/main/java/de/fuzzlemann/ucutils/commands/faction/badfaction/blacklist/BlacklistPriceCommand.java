package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.command.api.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistReason;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class BlacklistPriceCommand implements TabCompletion {

    @Command(value = {"blacklistprice", "blprice", "blp"}, usage = "/%label% [setprice/list] (Grund) (Preis)", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(String argument,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) BlacklistReason reason,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer price) {
        switch (argument.toLowerCase()) {
            case "list":
                sendBlacklistPrices();
                break;
            case "setprice":
                if (price == null) return false;

                if (reason == null) {
                    TextUtils.error("Der Blacklistgrund wurde nicht gefunden.");
                    return true;
                }

                reason.setPrice(price);
                BlacklistUtil.savePrices();

                Message.builder()
                        .prefix()
                        .of("Du hast den Preis von ").color(TextFormatting.GRAY).advance()
                        .of(reason.getReason()).color(TextFormatting.BLUE).advance()
                        .of(" zu ").color(TextFormatting.GRAY).advance()
                        .of(price + "$").color(TextFormatting.BLUE).advance()
                        .of(" geändert.").color(TextFormatting.GRAY).advance()
                        .send();
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Arrays.asList("setprice", "list");
        if (!args[0].equalsIgnoreCase("setprice")) return null;
        if (args.length == 2) {
            return BlacklistUtil.BLACKLIST_REASONS
                    .stream()
                    .map(BlacklistReason::getReason)
                    .collect(Collectors.toList());
        }

        return null;
    }

    private void sendBlacklistPrices() {
        Message.MessageBuilder builder = Message.builder();

        builder.of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Eingestellte Blacklistpreise\n").color(TextFormatting.DARK_AQUA).advance();
        for (BlacklistReason blacklistReason : BlacklistUtil.BLACKLIST_REASONS) {
            builder.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                    .of(blacklistReason.getReason()).color(TextFormatting.GRAY).advance()
                    .of(": ").color(TextFormatting.DARK_GRAY).advance()
                    .of(blacklistReason.getPrice() + "$\n").color(TextFormatting.BLUE).advance();
        }

        builder.send();
    }
}
