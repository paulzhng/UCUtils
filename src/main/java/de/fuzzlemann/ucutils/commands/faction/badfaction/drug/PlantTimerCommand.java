package de.fuzzlemann.ucutils.commands.faction.badfaction.drug;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.execution.CommandHandler;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.utils.FormatUtils;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.sound.SoundUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber
public class PlantTimerCommand {

    private static final Set<Plant> PLANTS = new TreeSet<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private final Timer timer = new Timer();

    @Command(value = "planttimer")
    public boolean onCommand(UPlayer p,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) String argument,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer x,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer y,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer z) {
        if (argument == null) {
            showList();
        } else if (argument.equalsIgnoreCase("dünger")) {
            setTimer(p, TimerType.FERTILIZE);
        } else if (argument.equalsIgnoreCase("wasser")) {
            setTimer(p, TimerType.WATER);
        } else if (argument.equalsIgnoreCase("delete")) {
            if (x == null || y == null || z == null) return false;

            Plant foundPlant = null;
            for (Plant plant : PLANTS) {
                if (plant.getX() == x && plant.getY() == y && plant.getZ() == z) {
                    foundPlant = plant;
                    break;
                }
            }

            if (foundPlant == null) {
                TextUtils.error("Es wurde keine Plant an der Position gefunden.");
                return true;
            }

            PLANTS.remove(foundPlant);
            TextUtils.error("Die Plant wurde entfernt.");
        }
        return true;
    }

    private void showList() {
        if (PLANTS.isEmpty()) {
            TextUtils.error("Es ist derzeit keine Plant aktiv.");
            return;
        }

        // automatically remove plant when 5 hours have passed after it was created
        PLANTS.removeIf(plant -> System.currentTimeMillis() - plant.getCreation() > TimeUnit.HOURS.toMillis(5));

        Message.builder().of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Plant Timer").color(TextFormatting.DARK_AQUA).advance()
                .newLine()
                .joiner(PLANTS)
                .consumer((builder, plant) -> {
                    String position = plant.getX() + "/" + plant.getY() + "/" + plant.getZ();
                    builder.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                            .of("Plant bei " + position).color(TextFormatting.GRAY).advance()
                            .space()
                            .of("[✈]").color(TextFormatting.BLUE).clickEvent(ClickEvent.Action.RUN_COMMAND, "/navi " + plant.getX() + "/" + plant.getY() + "/" + plant.getZ()).hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Route anzeigen", TextFormatting.BLUE)).advance()
                            .space()
                            .of("[✗]").color(TextFormatting.RED).clickEvent(ClickEvent.Action.RUN_COMMAND, "/planttimer delete " + plant.getX() + " " + plant.getY() + " " + plant.getZ())
                            .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Den Timer löschen", TextFormatting.RED)).advance();

                    builder.newLine()
                            .of("     * ").color(TextFormatting.DARK_GRAY).advance()
                            .of("Düngen: ").color(TextFormatting.GRAY).advance();
                    addTime(builder, plant.getLastFertilize(), TimerType.FERTILIZE);

                    builder.newLine()
                            .of("     * ").color(TextFormatting.DARK_GRAY).advance()
                            .of("Wässern: ").color(TextFormatting.GRAY).advance();
                    addTime(builder, plant.getLastWater(), TimerType.WATER);
                }).newLineJoiner().advance().send();
    }

    private void addTime(Message.Builder builder, long lastTime, TimerType type) {
        if (lastTime != -1) {
            long timePassed = System.currentTimeMillis() - lastTime;
            if (timePassed >= type.getDuration()) {
                builder.of("bereit").color(TextFormatting.RED).advance();
            } else {
                Date readyDate = new Date(lastTime + type.getDuration());
                String date = dateFormat.format(readyDate);
                long timeNeeded = type.getDuration() - timePassed;

                builder.messageParts(FormatUtils.formatMillisecondsToMessage(timeNeeded))
                        .of(" verbleibend (").color(TextFormatting.GRAY).advance()
                        .of(date + " Uhr").color(TextFormatting.BLUE).advance()
                        .of(")").color(TextFormatting.GRAY);
            }
        } else {
            builder.of("n/A").color(TextFormatting.RED).advance();
        }
    }

    private void setTimer(UPlayer p, TimerType type) {
        Plant plant = getNearestPlant(p, true);
        if (plant == null) {
            TextUtils.error("Es wurde keine Plant in deiner Nähe gefunden.");
            return;
        }

        switch (type) {
            case FERTILIZE:
                plant.updateLastFertilize();
                break;
            case WATER:
                plant.updateLastWater();
        }

        for (int minutes = 0; minutes < 3; minutes++) {
            int finalMinutes = minutes;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!PLANTS.contains(plant)) return;

                    String typeString;
                    switch (type) {
                        case FERTILIZE:
                            typeString = "düngern";
                            break;
                        case WATER:
                            typeString = "wässern";
                            break;
                        default:
                            throw new IllegalStateException();
                    }

                    Message.Builder builder = Message.builder().prefix()
                            .of("Die Plant bei ").color(TextFormatting.GRAY).advance()
                            .of(plant.getX() + "/" + plant.getY() + "/" + plant.getZ()).color(TextFormatting.BLUE).advance();

                    if (finalMinutes == 0) {
                        readySound(p);

                        builder.of(" ist bereit zum ").color(TextFormatting.GRAY).advance()
                                .of(typeString).color(TextFormatting.BLUE).advance()
                                .of("!").color(TextFormatting.GRAY).advance();
                    } else {
                        builder.of(" ist in ").color(TextFormatting.GRAY).advance()
                                .of(finalMinutes + " Minuten").color(TextFormatting.BLUE).advance()
                                .of(" bereit zum ").color(TextFormatting.GRAY).advance()
                                .of(typeString).color(TextFormatting.BLUE).advance()
                                .of("!").color(TextFormatting.GRAY).advance();
                    }

                    builder.send();
                    NavigationUtil.getNavigationMessage(plant.getX(), plant.getY(), plant.getZ()).send();
                }
            }, type.getDuration() - TimeUnit.MINUTES.toMillis(minutes));
        }

        TextUtils.simpleMessage("Es wurde ein Timer für die Plant gesetzt.");
    }

    private void readySound(UPlayer p) {
        for (int i = 0; i < 3; i++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Main.MINECRAFT.addScheduledTask(() -> p.playSound(Objects.requireNonNull(SoundUtil.getSoundEvent("block.note.pling")), 1, 1));
                }
            }, i * 300);
        }
    }

    private static Plant getNearestPlant(UPlayer p, boolean createNew) {
        World world = p.getWorld();
        BlockPos playerPosition = p.getPosition();

        int curX = playerPosition.getX();
        int curY = playerPosition.getY();
        int curZ = playerPosition.getZ();

        // check already inserted plants
        for (Plant plant : PLANTS) {
            double distance = playerPosition.getDistance(plant.getX(), plant.getY(), plant.getZ());
            if (distance < 1.5) {
                return plant;
            }
        }

        if (!createNew) return null;

        // check nearby blocks for farn
        for (int x = curX - 1; x < curX + 1; x++) {
            for (int y = curY - 1; y < curY + 1; y++) {
                for (int z = curZ - 1; z < curZ + 1; z++) {
                    BlockPos plantPosition = new BlockPos(x, y, z);
                    Block block = world.getBlockState(plantPosition).getBlock();
                    if (block instanceof BlockTallGrass) {
                        // create new plant
                        Plant plant = new Plant(x, y, z);
                        PLANTS.add(plant);

                        return plant;
                    }
                }
            }
        }

        return null;
    }

    private static final Pattern TRIGGER_PATTERN = Pattern.compile("^\\[Plantage] Eine .+-Plantage wurde von (?:\\[UC])*([a-zA-Z0-9_]+) (gewässert|gedüngt)\\.$");
    private static final Pattern DELETE_PATTERN = Pattern.compile("^\\[Plantage] Eine .+-Plantage wurde von (?:\\[UC])*([a-zA-Z0-9_]+) geerntet\\. \\[\\d+g]$");

    @SubscribeEvent
    public static void onMessage(ClientChatReceivedEvent e) {
        UPlayer p = AbstractionLayer.getPlayer();
        if (UCUtilsConfig.plantTimer) {
            Matcher matcher = TRIGGER_PATTERN.matcher(e.getMessage().getUnformattedText());
            if (matcher.find()) {
                String name = matcher.group(1);
                if (!name.equals(p.getName())) return;

                String type = matcher.group(2).equals("gewässert") ? "wasser" : "dünger";
                CommandHandler.issueCommand("planttimer", type);
                return;
            }
        }

        Matcher matcher = DELETE_PATTERN.matcher(e.getMessage().getUnformattedText());
        if (matcher.find()) {
            String name = matcher.group(1);
            if (!name.equals(p.getName())) return;

            Plant plant = getNearestPlant(p, false);
            if (plant == null) return;

            CommandHandler.issueCommand("planttimer", "delete", String.valueOf(plant.getX()), String.valueOf(plant.getY()), String.valueOf(plant.getZ()));
        }
    }

    static class Plant implements Comparable<Plant> {
        private final int x;
        private final int y;
        private final int z;
        private final long creation;
        private long lastFertilize;
        private long lastWater;

        Plant(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.creation = System.currentTimeMillis();
            this.lastFertilize = -1;
            this.lastWater = -1;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public long getCreation() {
            return creation;
        }

        public void updateLastFertilize() {
            this.lastFertilize = System.currentTimeMillis();
        }

        public void updateLastWater() {
            this.lastWater = System.currentTimeMillis();
        }

        public long getLastFertilize() {
            return lastFertilize;
        }

        public long getLastWater() {
            return lastWater;
        }

        private long getLowestTimeLeft() {
            long now = System.currentTimeMillis();

            long timeToFertilize = now + TimerType.FERTILIZE.getDuration() - lastFertilize;
            long timeToWater = now + TimerType.WATER.getDuration() - lastWater;

            if (lastFertilize == -1 && lastWater == -1) {
                return Long.MAX_VALUE;
            } else if (lastFertilize == -1) {
                return lastWater;
            } else if (lastWater == -1) {
                return lastFertilize;
            }

            return Math.min(timeToFertilize, timeToWater);
        }

        @Override
        public int compareTo(Plant o) {
            return Long.compare(this.getLowestTimeLeft(), o.getLowestTimeLeft());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Plant plant = (Plant) o;
            return x == plant.x && y == plant.y && z == plant.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    enum TimerType {
        FERTILIZE(TimeUnit.MINUTES.toMillis(70)),
        WATER(TimeUnit.MINUTES.toMillis(50));

        private final long duration;

        TimerType(long duration) {
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }
    }
}
