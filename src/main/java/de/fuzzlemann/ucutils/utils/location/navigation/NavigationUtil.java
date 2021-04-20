package de.fuzzlemann.ucutils.utils.location.navigation;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.misc.navipoint.CustomNaviPoint;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */

@Mod.EventBusSubscriber
@UDFModule(value = DataRegistry.CUSTOM_NAVI_POINT, version = 1)
public class NavigationUtil implements UDFLoader<List<CustomNaviPoint>> {

    private static long executedTime = -1;
    private static final Pattern ROUTE_PATTERNS = Pattern.compile("^Du hast keine Route\\.$" +
            "|^Du hast deine Route gelöscht\\.$");

    public static final List<CustomNaviPoint> NAVI_POINTS = new ArrayList<>();

    public static CustomNaviPoint getNaviPoint(String input) {
        input = input.replace('-', ' ');

        return ForgeUtils.getMostMatchingL(NAVI_POINTS, input, CustomNaviPoint::getNames);
    }

    public static ITextComponent getNavigationText(String naviPoint) {
        return getNavigationMessage(naviPoint).toTextComponent();
    }

    public static ITextComponent getNavigationText(BlockPos blockPos) {
        return getNavigationMessage(blockPos).toTextComponent();
    }

    public static ITextComponent getNavigationText(int x, int y, int z) {
        return getNavigationMessage(x, y, z).toTextComponent();
    }

    public static ITextComponent getRawNavigationText(String naviPoint) {
        return getRawNavigationMessage(naviPoint).toTextComponent();
    }

    public static ITextComponent getRawNavigationText(BlockPos blockPos) {
        return getRawNavigationMessage(blockPos).toTextComponent();
    }

    public static ITextComponent getRawNavigationText(int x, int y, int z) {
        return getRawNavigationMessage(x, y, z).toTextComponent();
    }

    public static Message getNavigationMessage(String naviPoint) {
        return constructNavigationMessage("/navi " + naviPoint);
    }

    public static Message getNavigationMessage(BlockPos blockPos) {
        return getNavigationMessage(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static Message getNavigationMessage(int x, int y, int z) {
        return constructNavigationMessage("/navi " + x + "/" + y + "/" + z);
    }

    public static Message getRawNavigationMessage(String naviPoint) {
        return constructRawNavigationMessage("/navi " + naviPoint);
    }

    public static Message getRawNavigationMessage(BlockPos blockPos) {
        return getRawNavigationMessage(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static Message getRawNavigationMessage(int x, int y, int z) {
        return constructRawNavigationMessage("/navi " + x + "/" + y + "/" + z);
    }

    private static Message constructNavigationMessage(String naviCommand) {
        Message.Builder builder = Message.builder();

        return builder.of(" » ").color(TextFormatting.GRAY).advance()
                .messageParts(constructRawNavigationMessage(naviCommand).getMessageParts())
                .build();
    }

    private static ITextComponent constructRawNavigationText(String naviCommand) {
        return constructRawNavigationMessage(naviCommand).toTextComponent();
    }

    private static Message constructRawNavigationMessage(String naviCommand) {
        Message.Builder builder = Message.builder();

        return builder.of("Route anzeigen")
                .color(TextFormatting.RED)
                .clickEvent(ClickEvent.Action.RUN_COMMAND, naviCommand)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Route anzeigen", TextFormatting.RED))
                .advance()
                .build();
    }

    public static void stopRoute() {
        executedTime = System.currentTimeMillis();
        AbstractionLayer.getPlayer().sendChatMessage("/stoproute");
    }

    @Override
    public void supply(List<CustomNaviPoint> customNaviPoints) {
        NAVI_POINTS.addAll(customNaviPoints);
    }

    @Override
    public void cleanUp() {
        NAVI_POINTS.clear();
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent e) {
        Matcher routeMatcher = ROUTE_PATTERNS.matcher(e.getMessage().getUnformattedText());
        if (routeMatcher.find() && System.currentTimeMillis() - executedTime < 500L)
            e.setCanceled(true);
    }

}
