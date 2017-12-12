package de.fuzzlemann.ucutils.events;

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
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class NameFormatEventHandler {

    //--------------------- Wanteds ---------------------\\
    public static final Map<String, Integer> WANTED_MAP = new HashMap<>();
    private static final Map<String, EntityPlayer> PLAYER_MAP = new HashMap<>();
    //--------------------- Hits ---------------------\\
    private static final List<String> HITLIST = new ArrayList<>();
    private static final Pattern HIT_SET_PATTERN = Pattern.compile("^\\[Contract] Es wurde ein Kopfgeld auf [a-zA-Z0-9_]+ \\(\\d+\\$\\) ausgesetzt.$");
    private static final Pattern HIT_REMOVED_PATTERN = Pattern.compile("^\\[Contract] [a-zA-Z0-9_]+ hat [a-zA-Z0-9_]+ von der Contract Liste gel\u00f6scht. \\[-\\d+]");
    private static final Pattern HIT_KILLED_PATTERN = Pattern.compile("^\\[Contract] [a-zA-Z0-9_]+ hat [a-zA-Z0-9_]+ get\u00f6tet. Kopfgeld: \\d+\\$");

    //--------------------- Blacklist ---------------------\\
    private static final List<String> BLACKLIST = new ArrayList<>();
    private static final Pattern BLACKLIST_ADDED_PATTERN = Pattern.compile("^\\[Blacklist] [a-zA-Z0-9_]+ wurde von [a-zA-Z0-9_]+ auf die Blacklist gesetzt!$");
    private static final Pattern BLACKLIST_REMOVED_PATTERN = Pattern.compile("^\\[Blacklist] [a-zA-Z0-9_]+ wurde von [a-zA-Z0-9_]+ von der Blacklist gel\u00f6scht!$");
    private static final Pattern JAILED_PATTERN = Pattern.compile("^HQ: [a-zA-Z0-9_]+ wurde von [a-zA-Z0-9_]+ eingesperrt.$");
    private static final Pattern KILLED_PATTERN = Pattern.compile("^Beamter [a-zA-Z0-9_]+ hat [a-zA-Z0-9_]+ get\u00f6tet!$");
    private static final Pattern RECORDS_DELETED_SEINE_IHRE_PATTERN = Pattern.compile("^HQ: [a-zA-Z0-9_ ]+ [a-zA-Z0-9_]+ hat [a-zA-Z0-9_]+ (seine|ihre) Akten gel\u00f6scht, over.$");
    private static final Pattern RECORDS_DELETED_S_PATTERN = Pattern.compile("^HQ: [a-zA-Z0-9_ ]+ [a-zA-Z0-9_]+ hat [a-zA-Z0-9_]+'s Akten gel\u00f6scht, over.$");
    private static final Pattern WANTEDS_GIVEN_PATTERN = Pattern.compile("^HQ: [a-zA-Z0-9_]+'s momentanes WantedLevel: \\d+$");
    private static long hitlistShown = 0L;
    private static long blacklistShown = 0L;
    private static long wantedsShown = 0L;

    @SubscribeEvent
    public static void onNameFormat(PlayerEvent.NameFormat e) {
        EntityPlayer p = e.getEntityPlayer();

        String userName = e.getUsername();
        String displayName = ScorePlayerTeam.formatPlayerName(p.getTeam(), userName);

        PLAYER_MAP.put(userName, p);

        //Prevents people who are masked from being detected
        if (displayName.contains("\u00a7k"))
            return;

        String color = getColor(userName);
        if (color == null) return;

        e.setDisplayname("\u00a7" + color + userName);
    }

    @SubscribeEvent
    public static void onHitlistShown(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        long currentTime = System.currentTimeMillis();

        if (unformattedMessage.equals("=~=~=~Contracts~=~=~=")) {
            HITLIST.clear();
            hitlistShown = currentTime;

            refreshAllDisplayNames();
            return;
        }

        if (currentTime - hitlistShown > 5000L || !unformattedMessage.startsWith(" - ")) return;

        String[] splittedMessage = StringUtils.split(unformattedMessage, " ");
        String name = splittedMessage[1];

        HITLIST.add(name);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onHitlistModified(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        if (HIT_SET_PATTERN.matcher(unformattedMessage).find()) {
            String[] splittedMessage = unformattedMessage.split(" ");
            String name = TextUtils.stripPrefix(splittedMessage[6]);

            HITLIST.add(name);
            refreshDisplayName(name);
        } else if (HIT_KILLED_PATTERN.matcher(unformattedMessage).find()) {
            String[] splittedMessage = unformattedMessage.split(" ");
            String name = TextUtils.stripPrefix(splittedMessage[3]);

            HITLIST.remove(name);
            refreshDisplayName(name);
        } else if (HIT_REMOVED_PATTERN.matcher(unformattedMessage).find()) {
            String[] splittedMessage = unformattedMessage.split(" ");
            String name = TextUtils.stripPrefix(splittedMessage[3]);

            HITLIST.remove(name);
            refreshDisplayName(name);
        }
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

        if (currentTime - blacklistShown > 5000L || !unformattedMessage.startsWith(" \u00bb ")) return;

        String[] splittedMessage = StringUtils.split(unformattedMessage, " | ");
        String name = TextUtils.stripPrefix(splittedMessage[1]);

        BLACKLIST.add(name);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onBlacklistModified(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        if (BLACKLIST_ADDED_PATTERN.matcher(unformattedMessage).find()) {
            String[] splittedMessage = unformattedMessage.split(" ");
            String name = TextUtils.stripPrefix(splittedMessage[1]);

            BLACKLIST.add(name);
            refreshDisplayName(name);
        } else if (BLACKLIST_REMOVED_PATTERN.matcher(unformattedMessage).find()) {
            String[] splittedMessage = unformattedMessage.split(" ");
            String name = TextUtils.stripPrefix(splittedMessage[1]);

            BLACKLIST.remove(name);
            refreshDisplayName(name);
        }
    }


    @SubscribeEvent
    public static void onWantedsGiven(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        if (!WANTEDS_GIVEN_PATTERN.matcher(unformattedMessage).find()) return;
        String[] splittedMessage = unformattedMessage.split(" ");

        String name = TextUtils.stripPrefix(splittedMessage[1]);
        name = name.substring(0, name.length() - 2);

        int wanteds = Integer.parseInt(splittedMessage[4]);

        WANTED_MAP.put(name, wanteds);
        refreshDisplayName(name);
    }

    @SubscribeEvent
    public static void onWantedsDeleted(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        String name = null;
        if (RECORDS_DELETED_S_PATTERN.matcher(unformattedMessage).find()) {
            String[] splittedMessage = unformattedMessage.split(" ");
            name = splittedMessage[splittedMessage.length - 4];
            name = name.substring(0, name.length() - 2);
        } else if (RECORDS_DELETED_SEINE_IHRE_PATTERN.matcher(unformattedMessage).find()) {
            String[] splittedMessage = unformattedMessage.split(" ");
            name = splittedMessage[splittedMessage.length - 5];
        } else if (JAILED_PATTERN.matcher(unformattedMessage).find()) {
            name = unformattedMessage.split(" ")[1];
        } else if (KILLED_PATTERN.matcher(unformattedMessage).find()) {
            name = unformattedMessage.split(" ")[3];
        }

        if (name != null) {
            name = TextUtils.stripPrefix(name);

            WANTED_MAP.remove(name);
            refreshDisplayName(name);
        }
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

        if (currentTime - wantedsShown > 1000L || !unformattedMessage.startsWith("  - ")) return;

        String[] splittedMessage = StringUtils.split(unformattedMessage, " | ");

        String name = TextUtils.stripPrefix(splittedMessage[1]);
        int wanteds = Integer.parseInt(splittedMessage[2]);

        WANTED_MAP.put(name, wanteds);
        refreshDisplayName(name);
    }

    private static String getColor(String userName) {
        Integer wanteds = WANTED_MAP.get(userName);
        if (wanteds != null) {
            if (wanteds == 1) {
                return "2";
            } else if (wanteds < 15) {
                return "a";
            } else if (wanteds < 25) {
                return "e";
            } else if (wanteds < 50) {
                return "6";
            } else if (wanteds < 60) {
                return "c";
            } else {
                return "4";
            }
        } else if (BLACKLIST.contains(userName) || HITLIST.contains(userName)) {
            return "4";
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
