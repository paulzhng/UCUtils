package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.faction.FactionPlayersList;
import de.fuzzlemann.ucutils.utils.faction.HouseBanHandler;
import de.fuzzlemann.ucutils.utils.faction.police.Wanted;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSkull;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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

    //--------------------- Player Map ---------------------\\
    private static final Map<String, EntityPlayer> PLAYER_MAP = new HashMap<>();
    //--------------------- Wanteds ---------------------\\
    public static final Map<String, Wanted> WANTED_MAP = new HashMap<>();
    private static final Pattern WANTED_LIST_ENTRY_PATTERN = Pattern.compile("^ {2}- (?:\\[UC])*([a-zA-Z0-9_]+) \\| (\\d+) WPS \\((.+)\\)$");
    private static final Pattern WANTEDS_GIVEN_REASON_PATTERN = Pattern.compile("^HQ: Gesuchter: (?:\\[UC])*([a-zA-Z0-9_]+)\\. Grund: (.+)$");
    private static final Pattern WANTEDS_GIVEN_POINTS_PATTERN = Pattern.compile("^HQ: (?:\\[UC])*([a-zA-Z0-9_]+)'s momentanes WantedLevel: (\\d+)$");
    private static final Pattern WANTEDS_DELETED_PATTERN = Pattern.compile("^HQ: (?:\\[UC])*([a-zA-Z0-9_]+) wurde von (?:\\[UC])*[a-zA-Z0-9_]+ eingesperrt\\.$" +
            "|^HQ: (?:\\[UC])*([a-zA-Z0-9_]+) wurde von (?:\\[UC])*[a-zA-Z0-9_]+ getötet\\.$" +
            "|^HQ: .+ (?:\\[UC])*[a-zA-Z0-9_]+ hat (?:\\[UC])*([a-zA-Z0-9_]+)(?:'s)*(?: seine| ihre)* Akten gelöscht, over\\.$");
    //--------------------- Contracts ---------------------\\
    private static final List<String> CONTRACT_LIST = new ArrayList<>();
    private static final Pattern CONTRACT_SET_PATTERN = Pattern.compile("^\\[Contract] Es wurde ein Kopfgeld auf (?:\\[UC])*([a-zA-Z0-9_]+) \\(\\d+\\$\\) ausgesetzt\\.$");
    private static final Pattern CONTRACT_REMOVED_PATTERN = Pattern.compile("(?:^\\[Contract] (?:\\[UC])*[a-zA-Z0-9_]+ hat (?:\\[UC])*([a-zA-Z0-9_]+) von der Contract Liste gelöscht\\. \\[-\\d+]$)" +
            "|(?:^\\[Contract] (?:\\[UC])*[a-zA-Z0-9_]+ hat (?:\\[UC])*([a-zA-Z0-9_]+) getötet\\. Kopfgeld: \\d+\\$)");
    //--------------------- Blacklist ---------------------\\
    private static final Map<String, BlacklistModifier> BLACKLIST_MAP = new HashMap<>();
    public static final Pattern BLACKLIST_START_PATTERN = Pattern.compile("=== Blacklist .+ ===");
    public static final Pattern BLACKLIST_LIST_PATTERN = Pattern.compile("^ » (?:\\[UC])*([a-zA-Z0-9_]+) \\| (.+) \\| (.+) \\| (\\d+) Kills \\| (\\d+)\\$");
    private static final Pattern BLACKLIST_ADDED_PATTERN = Pattern.compile("^\\[Blacklist] (?:\\[UC])*([a-zA-Z0-9_]+) wurde von (?:\\[UC])*[a-zA-Z0-9_]+ auf die Blacklist gesetzt!$");
    private static final Pattern BLACKLIST_REMOVED_PATTERN = Pattern.compile("^\\[Blacklist] (?:\\[UC])*([a-zA-Z0-9_]+) wurde von (?:\\[UC])*[a-zA-Z0-9_]+ von der Blacklist gelöscht!$");
    //--------------------- Time ---------------------\\
    private static int tick;
    private static long wantedsShown;
    private static long hitlistShown;
    private static long blacklistShown;

    @SubscribeEvent
    public static void onNameFormat(PlayerEvent.NameFormat e) {
        EntityPlayer p = e.getEntityPlayer();

        String userName = e.getUsername();
        String displayName = ScorePlayerTeam.formatPlayerName(p.getTeam(), userName);

        PLAYER_MAP.put(userName, p);

        // Prevents people who are masked from being detected
        if (displayName.contains("§k")) return;

        String color = getPrefix(userName, p.getUniqueID());
        if (color == null) return;

        e.setDisplayname(color + userName);
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;

        if (Main.MINECRAFT.world == null) return;
        if (tick++ != 20) return;

        List<EntityItem> items = Main.MINECRAFT.world.getEntities(EntityItem.class, (ent) -> ent != null && ent.hasCustomName() && ent.getItem().getItem() instanceof ItemSkull);

        for (EntityItem entityItem : items) {
            String name = entityItem.getCustomNameTag();
            if (name.startsWith("§8")) continue; //Hitman Corpse

            String colorStrippedName = name.substring(2);
            String realName = colorStrippedName.substring(1);

            String color = getPrefix(TextUtils.stripPrefix(realName), null);
            if (color == null) {
                if (name.startsWith("§7")) continue;

                color = "§7";
            }

            entityItem.setCustomNameTag(color + colorStrippedName);
        }

        tick = 0;
    }

    @SubscribeEvent
    public static void onStopTracking(PlayerEvent.StopTracking e) {
        EntityPlayer p = e.getEntityPlayer();
        PLAYER_MAP.remove(p.getName());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onWantedsGiven(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher wantedsGivenReasonMatcher = WANTEDS_GIVEN_REASON_PATTERN.matcher(unformattedMessage);
        if (wantedsGivenReasonMatcher.find()) {
            String name = wantedsGivenReasonMatcher.group(1);
            String reason = wantedsGivenReasonMatcher.group(2);

            WANTED_MAP.put(name, new Wanted(reason, 0));
            return;
        }

        Matcher wantedsGivenPointsMatcher = WANTEDS_GIVEN_POINTS_PATTERN.matcher(unformattedMessage);
        if (wantedsGivenPointsMatcher.find()) {
            String name = wantedsGivenPointsMatcher.group(1);
            int wantedPoints = Integer.parseInt(wantedsGivenPointsMatcher.group(2));

            Wanted wanted = WANTED_MAP.get(name);
            if (wanted == null) return;

            wanted.setAmount(wantedPoints);
            refreshDisplayName(name);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
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

        Matcher matcher = WANTED_LIST_ENTRY_PATTERN.matcher(unformattedMessage);
        if (currentTime - wantedsShown > 1000L || !matcher.find()) return;

        String name = matcher.group(1);
        int wantedPoints = Integer.parseInt(matcher.group(2));
        String reason = matcher.group(3);

        WANTED_MAP.put(name, new Wanted(reason, wantedPoints));
        refreshDisplayName(name);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
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

        // TODO TRANSFORM TO REGEX
        if (currentTime - hitlistShown > 5000L || !unformattedMessage.startsWith(" - ")) return;
        if (!unformattedMessage.contains("$")) return;

        String[] splittedMessage = StringUtils.split(unformattedMessage, " ");
        String name = TextUtils.stripPrefix(splittedMessage[1]);

        CONTRACT_LIST.add(name);
        refreshDisplayName(name);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlacklistShown(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        long currentTime = System.currentTimeMillis();

        Matcher blacklistStartMatcher = BLACKLIST_START_PATTERN.matcher(unformattedMessage);
        if (blacklistStartMatcher.find()) {
            BLACKLIST_MAP.clear();
            blacklistShown = currentTime;

            refreshAllDisplayNames();
            return;
        }

        if (currentTime - blacklistShown > 1000L) return;

        Matcher matcher = BLACKLIST_LIST_PATTERN.matcher(unformattedMessage);
        if (matcher.find()) {
            String name = matcher.group(1);
            String reason = matcher.group(2);

            if (reason.toLowerCase().contains("vogelfrei")) {
                BLACKLIST_MAP.put(name, BlacklistModifier.OUTLAW);
            } else if (reason.toLowerCase().contains("muerte")) {
                BLACKLIST_MAP.put(name, BlacklistModifier.MUERTE);
            } else if (reason.toLowerCase().contains("yobannoe dno")) {
                BLACKLIST_MAP.put(name, BlacklistModifier.YOBANNOE_DNO);
            }

            refreshDisplayName(name);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlacklistAdd(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher matcher = BLACKLIST_ADDED_PATTERN.matcher(unformattedMessage);
        if (matcher.find()) {
            String name = matcher.group(1);

            BLACKLIST_MAP.put(name, BlacklistModifier.NONE);
            refreshDisplayName(name);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlacklistRemove(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        Matcher matcher = BLACKLIST_REMOVED_PATTERN.matcher(unformattedMessage);
        if (matcher.find()) {
            String name = matcher.group(1);

            BLACKLIST_MAP.remove(name);
            refreshDisplayName(name);
        }
    }

    private static String getPrefix(String userName, UUID uniqueID) {
        Wanted wanted = WANTED_MAP.get(userName);

        if (wanted != null) {
            int wantedPoints = wanted.getAmount();
            if (wantedPoints != 0) {
                if (wantedPoints == 1) {
                    return "§2";
                } else if (wantedPoints < 15) {
                    return "§a";
                } else if (wantedPoints < 25) {
                    return "§e";
                } else if (wantedPoints < 50) {
                    return "§6";
                } else if (wantedPoints < 60) {
                    return "§c";
                } else {
                    return "§4";
                }
            }
        }

        if (BLACKLIST_MAP.get(userName) != null) {
            if (BLACKLIST_MAP.get(userName) == BlacklistModifier.OUTLAW) {
                return "§8[§cV§8] §4";
            } else if (BLACKLIST_MAP.get(userName) == BlacklistModifier.MUERTE){
                return "§8[§cM§8] §4";
            } else if (BLACKLIST_MAP.get(userName) == BlacklistModifier.YOBANNOE_DNO){
                return "§8[§cY§8] §4";
            }
        }

        if (CONTRACT_LIST.contains(userName)) {
            return "§4";
        }

        if (UCUtilsConfig.showHouseBans && uniqueID != null && HouseBanHandler.HOUSE_BANS.contains(uniqueID.toString())) {
            return "§8[§cHV§8] §f";
        }

        if (UCUtilsConfig.showFactionPlayers) {
            Faction faction = Faction.getFactionOfPlayer();

            if (faction != null && FactionPlayersList.PLAYER_TO_FACTION_MAP.get(userName) == faction) {
                return "§9";
            }
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

    private enum BlacklistModifier {
        NONE,
        OUTLAW,
        MUERTE,
        YOBANNOE_DNO
    }
}