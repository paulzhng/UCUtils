package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import de.fuzzlemann.ucutils.utils.faction.HouseBanHandler;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class NameFormatEventHandler {

    //--------------------- Wanteds ---------------------\\
    public static final Map<String, Integer> WANTED_MAP = new HashMap<>();
    private static final Pattern WANTEDS_GIVEN_PATTERN = Pattern.compile("^HQ: (?:\\[UC])*([a-zA-Z0-9_]+)'s momentanes WantedLevel: (\\d+)$");
    private static final Pattern WANTEDS_DELETED_PATTERN = Pattern.compile("^HQ: (?:\\[UC])*([a-zA-Z0-9_]+) wurde von (?:\\[UC])*[a-zA-Z0-9_]+ eingesperrt.$" +
            "|^.+ (?:\\[UC])*[a-zA-Z0-9_]+ hat (?:\\[UC])*([a-zA-Z0-9_]+) getötet!$" +
            "|^HQ: .+ (?:\\[UC])*[a-zA-Z0-9_]+ hat (?:\\[UC])*([a-zA-Z0-9_]+)(?:'s)*(?: seine| ihre)* Akten gelöscht, over.$");
    private static long wantedsShown;
    //--------------------- Contracts ---------------------\\
    private static final List<String> CONTRACT_LIST = new ArrayList<>();
    private static final Pattern CONTRACT_SET_PATTERN = Pattern.compile("^\\[Contract] Es wurde ein Kopfgeld auf (?:\\[UC])*([a-zA-Z0-9_]+) \\(\\d+\\$\\) ausgesetzt.$");
    private static final Pattern CONTRACT_REMOVED_PATTERN = Pattern.compile("(?:^\\[Contract] (?:\\[UC])*[a-zA-Z0-9_]+ hat (?:\\[UC])*([a-zA-Z0-9_]+) von der Contract Liste gelöscht. \\[-\\d+]$)" +
            "|(?:^\\[Contract] (?:\\[UC])*[a-zA-Z0-9_]+ hat (?:\\[UC])*([a-zA-Z0-9_]+) getötet. Kopfgeld: \\d+\\$)");
    private static long hitlistShown;
    //--------------------- Blacklist ---------------------\\
    private static final List<String> BLACKLIST = new ArrayList<>();
    private static final Pattern BLACKLIST_ADDED_PATTERN = Pattern.compile("^\\[Blacklist] (?:\\[UC])*([a-zA-Z0-9_]+) wurde von (?:\\[UC])*[a-zA-Z0-9_]+ auf die Blacklist gesetzt!$");
    private static final Pattern BLACKLIST_REMOVED_PATTERN = Pattern.compile("^\\[Blacklist] (?:\\[UC])*([a-zA-Z0-9_]+) wurde von (?:\\[UC])*[a-zA-Z0-9_]+ von der Blacklist gelöscht!$");
    private static long blacklistShown;

    private static final Map<String, EntityPlayer> PLAYER_MAP = new HashMap<>();

    @SubscribeEvent
    public static void onNameFormat(PlayerEvent.NameFormat e) {
        EntityPlayer p = e.getEntityPlayer();

        String userName = e.getUsername();
        String displayName = ScorePlayerTeam.formatPlayerName(p.getTeam(), userName);

        PLAYER_MAP.put(userName, p);

        //Prevents people who are masked from being detected
        if (displayName.contains("§k")) return;

        String color = getPrefix(userName);
        if (color == null) return;

        e.setDisplayname(color + userName);
    }

    @SubscribeEvent
    public static void onWantedsGiven(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher matcher = WANTEDS_GIVEN_PATTERN.matcher(unformattedMessage);
        if (!matcher.find()) return;

        String name = matcher.group(1);
        int wanteds = Integer.parseInt(matcher.group(2));

        WANTED_MAP.put(name, wanteds);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onWantedsDeleted(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher matcher = WANTEDS_DELETED_PATTERN.matcher(unformattedMessage);
        if (!matcher.find()) return;

        String name = null;
        for (int i = 1; i < matcher.groupCount() + 1; i++) {
            String tempName = matcher.group(i);
            if (tempName == null) continue;

            name = tempName;
            break;
        }

        WANTED_MAP.remove(name);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onWantedsShown(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        long currentTime = System.currentTimeMillis();

        if (unformattedMessage.equals("Online Spieler mit WantedPunkten:")) {
            WANTED_MAP.clear();
            wantedsShown = currentTime;

            refreshAllDisplayNames();
            return;
        }

        //TODO TRANSFORM TO REGEX
        if (currentTime - wantedsShown > 1000L || !unformattedMessage.startsWith("  - ")) return;

        String[] splittedMessage = StringUtils.split(unformattedMessage, " | ");
        if (splittedMessage.length < 3) return;

        String name;
        int wanteds;
        try {
            name = TextUtils.stripPrefix(splittedMessage[1]);
            wanteds = Integer.parseInt(splittedMessage[2]);
        } catch (Exception exc) {
            return;
        }

        WANTED_MAP.put(name, wanteds);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onHitlistShown(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        long currentTime = System.currentTimeMillis();

        if (unformattedMessage.equals("=~=~=~Contracts~=~=~=")) {
            CONTRACT_LIST.clear();
            hitlistShown = currentTime;

            refreshAllDisplayNames();
            return;
        }

        //TODO TRANSFORM TO REGEX
        if (currentTime - hitlistShown > 5000L || !unformattedMessage.startsWith(" - ")) return;
        if (!unformattedMessage.contains("$")) return;

        String[] splittedMessage = StringUtils.split(unformattedMessage, " ");
        String name = TextUtils.stripPrefix(splittedMessage[1]);

        CONTRACT_LIST.add(name);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onContractSet(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher matcher = CONTRACT_SET_PATTERN.matcher(unformattedMessage);
        if (matcher.find()) {
            String name = matcher.group(1);

            CONTRACT_LIST.add(name);
            refreshDisplayName(name);
        }
    }

    @SubscribeEvent
    public static void onContractRemoved(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher matcher = CONTRACT_REMOVED_PATTERN.matcher(unformattedMessage);
        if (!matcher.find()) return;

        String name = null;
        for (int i = 1; i < matcher.groupCount() + 1; i++) {
            String tempName = matcher.group(i);
            if (tempName == null) continue;

            name = tempName;
            break;
        }

        CONTRACT_LIST.remove(name);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onBlacklistShown(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        long currentTime = System.currentTimeMillis();

        if (unformattedMessage.equals("==== Blacklist ====")) {
            BLACKLIST.clear();
            blacklistShown = currentTime;

            refreshAllDisplayNames();
            return;
        }

        if (currentTime - blacklistShown > 5000L || !unformattedMessage.startsWith(" » ")) return;

        //TODO REGEX
        String[] splittedMessage = StringUtils.split(unformattedMessage, " | ");
        String name = TextUtils.stripPrefix(splittedMessage[1]);

        BLACKLIST.add(name);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onBlacklistAdd(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher matcher = BLACKLIST_ADDED_PATTERN.matcher(unformattedMessage);
        if (matcher.find()) {
            String name = matcher.group(1);

            BLACKLIST.add(name);
            refreshDisplayName(name);
        }
    }

    @SubscribeEvent
    public static void onBlacklistRemove(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher matcher = BLACKLIST_REMOVED_PATTERN.matcher(unformattedMessage);
        if (matcher.find()) {
            String name = matcher.group(1);

            BLACKLIST.remove(name);
            refreshDisplayName(name);
        }
    }

    private static String getPrefix(String userName) {
        Integer wanteds = WANTED_MAP.get(userName);
        if (wanteds != null) {
            if (wanteds == 1) {
                return "§2";
            } else if (wanteds < 15) {
                return "§a";
            } else if (wanteds < 25) {
                return "§e";
            } else if (wanteds < 50) {
                return "§6";
            } else if (wanteds < 60) {
                return "§c";
            } else {
                return "§4";
            }
        } else if (BLACKLIST.contains(userName) || CONTRACT_LIST.contains(userName)) {
            return "§4";
        } else if (ConfigUtil.showHausBans && HouseBanHandler.HOUSE_BANS.contains(userName)) {
            return "§8[§cHV§8] §f";
        }

        return null;
    }

    private static void refreshAllDisplayNames() {
        for (Iterator<EntityPlayer> iterator = PLAYER_MAP.values().iterator(); iterator.hasNext(); ) {
            EntityPlayer entityPlayer = iterator.next();
            if (entityPlayer == null) {
                iterator.remove();
                return;
            }

            entityPlayer.refreshDisplayName();
        }
    }

    private static void refreshDisplayName(String userName) {
        EntityPlayer entityPlayer = PLAYER_MAP.get(userName);

        if (entityPlayer == null) {
            PLAYER_MAP.remove(userName);
            return;
        }

        entityPlayer.refreshDisplayName();
    }
}
