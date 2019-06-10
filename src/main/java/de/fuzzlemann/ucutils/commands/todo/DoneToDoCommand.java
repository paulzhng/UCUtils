package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class DoneToDoCommand {

    @Command(value = "donetodo", usage = "/%label% [ID]", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(ToDo toDo) {
        toDo.setDone(true);
        toDo.save();

        TextUtils.simpleMessage("Du hast die ToDo als erledigt makiert.");
        return true;
    }
}
