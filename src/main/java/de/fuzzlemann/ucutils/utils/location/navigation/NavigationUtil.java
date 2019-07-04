package de.fuzzlemann.ucutils.utils.location.navigation;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import de.fuzzlemann.ucutils.common.CustomNaviPoint;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.data.DataModule;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@DataModule("Navigation")
public class NavigationUtil implements DataLoader {

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

    @Override
    public void load() {
        String json = APIUtils.get("http://tomcat.fuzzlemann.de/factiononline/navipoints");

        Gson gson = new Gson();
        //noinspection UnstableApiUsage
        List<CustomNaviPoint> naviPoints = gson.fromJson(json, new TypeToken<List<CustomNaviPoint>>() {
        }.getType());

        NAVI_POINTS.clear();
        NAVI_POINTS.addAll(naviPoints);
    }
}
