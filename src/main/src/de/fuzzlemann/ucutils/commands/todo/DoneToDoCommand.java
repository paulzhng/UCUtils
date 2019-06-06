package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import de.fuzzlemann.ucutils.utils.todo.ToDoManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class DoneToDoCommand implements CommandExecutor {

    @Override
    @Command(value = "donetodo", usage = "/%label% [ID]", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        int id = Integer.parseInt(args[0]);
        ToDo toDo = ToDoManager.getToDo(id);

        if (toDo == null) {
            TextUtils.error("Es wurde kein ToDo Eintrag mit dieser ID gefunden.");
            return true;
        }

        toDo.setDone(true);
        toDo.save();

        TextUtils.simplePrefixMessage("Du hast die ToDo als erledigt makiert.");
        return true;
    }
}
