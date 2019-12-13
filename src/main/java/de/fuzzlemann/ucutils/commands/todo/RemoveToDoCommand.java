package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class RemoveToDoCommand {

    @Command(value = "removetodo", usage = "/%label% [ID]")
    public boolean onCommand(ToDo toDo) {
        toDo.delete();
        TextUtils.simpleMessage("Du hast die ToDo entfernt.");
        return true;
    }
}
