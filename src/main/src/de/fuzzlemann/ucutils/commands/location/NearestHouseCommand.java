package de.fuzzlemann.ucutils.commands.location;

import de.fuzzlemann.ucutils.common.House;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.house.HouseUtil;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class NearestHouseCommand implements CommandExecutor {

    private static final Pattern HOUSE_PATTERN = Pattern.compile("(?:^ - Haus: (.+)$)|(?:^ - Wohnhaft: (.+)$)");

    @Override
    @Command(value = "nearesthouse", usage = "/%label% [Häuser...]", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        List<Integer> houseNumbers = new ArrayList<>();
        if (args.length == 0) {
            HouseUtil.HOUSES.forEach(house -> houseNumbers.add(house.getHouseNumber()));
        } else {
            for (String arg : args) {
                int houseNumber = Integer.parseInt(arg);
                houseNumbers.add(houseNumber);
            }
        }

        List<House> searchHouses = new ArrayList<>();
        for (House house : HouseUtil.HOUSES) {
            if (houseNumbers.contains(house.getHouseNumber()))
                searchHouses.add(house);
        }

        Map.Entry<Double, House> nearestHouseEntry = ForgeUtils.getNearestObject(searchHouses, House::getX, House::getY, House::getZ);
        int distance = (int) (double) nearestHouseEntry.getKey();
        House house = nearestHouseEntry.getValue();

        Message.builder()
                .prefix()
                .of("Das näheste Haus zu dir ist ").color(TextFormatting.GRAY).advance()
                .of("Haus " + house.getHouseNumber()).color(TextFormatting.BLUE).advance()
                .of(" (").color(TextFormatting.GRAY).advance()
                .of(distance + " Meter").color(TextFormatting.BLUE).advance()
                .of(").").color(TextFormatting.GRAY).advance()
                .newLine()
                .messageParts(NavigationUtil.getNavigationMessage(house.getX(), house.getY(), house.getZ()).getMessageParts())
                .send();
        return true;
    }

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedText = message.getUnformattedText();

        Matcher matcher = HOUSE_PATTERN.matcher(unformattedText);
        if (!matcher.find()) return;

        for (int i = 1; i < matcher.groupCount() + 1; i++) {
            String group = matcher.group(i);
            if (group == null) continue;

            String[] splitted = group.split(",");
            List<String> houseNumbers = new ArrayList<>();
            for (String s : splitted) {
                s = s.replaceAll("[^0-9]+", "");
                houseNumbers.add(s);
            }

            modifyMessage(message.getStyle(), houseNumbers);
            for (ITextComponent sibling : message.getSiblings()) {
                modifyMessage(sibling.getStyle(), houseNumbers);
            }

            break;
        }
    }

    private static void modifyMessage(Style style, List<String> houseNumbers) {
        String command = "/nearesthouse " + String.join(" ", houseNumbers);

        style.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Message.builder().of("Das näheste Haus anzeigen").color(TextFormatting.DARK_AQUA).build().toTextComponent()));
    }
}
