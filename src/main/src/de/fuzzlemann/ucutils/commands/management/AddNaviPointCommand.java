package de.fuzzlemann.ucutils.commands.management;

import de.fuzzlemann.ucutils.common.CustomNaviPoint;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class AddNaviPointCommand implements CommandExecutor {

    @Override
    @Command(labels = "addnavipoint", usage = "/%label% [x] [y] [z] [Namen...]", management = true)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        int from;

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);

            from = 3;
        } catch (NumberFormatException e) {
            BlockPos pos = p.getPosition();

            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();

            from = 0;
        }

        String[] names = Arrays.copyOfRange(args, from, args.length);
        List<String> nameList = new ArrayList<>();
        for (String name : names) {
            name = name.replace('-', ' ');
            nameList.add(name);
        }

        CustomNaviPoint naviPoint = new CustomNaviPoint(nameList, x, y, z);
        boolean response = Boolean.valueOf(APIUtils.postAuthenticated(
                "http://tomcat.fuzzlemann.de/factiononline/navipoints/add",
                "customNaviPoint", naviPoint
        ));

        TextUtils.error("navi point created; response = " + response);
        return true;
    }
}
