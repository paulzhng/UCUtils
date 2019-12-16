package de.fuzzlemann.ucutils.commands.management;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.common.udf.data.misc.navipoint.CustomNaviPoint;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class AddNaviPointCommand {

    @Command(value = "addnavipoint", usage = "/%label% (x) (y) (z) [Namen...]", management = true)
    public boolean onCommand(UPlayer p,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer x,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer y,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer z,
                             @CommandParam(arrayStart = true) String[] names) {
        if (x == null || y == null || z == null) {
            BlockPos pos = p.getPosition();

            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
        }

        List<String> nameList = new ArrayList<>();
        for (String name : names) {
            name = name.replace('-', ' ');
            nameList.add(name);
        }

        CustomNaviPoint naviPoint = new CustomNaviPoint(nameList, x, y, z);
        boolean response = Boolean.parseBoolean(APIUtils.postAuthenticated(
                "http://tomcat.fuzzlemann.de/factiononline/navipoints/add",
                "customNaviPoint", naviPoint
        ));

        TextUtils.error("navi point created; response = " + response);
        return true;
    }
}
