package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import de.fuzzlemann.ucutils.utils.todo.ToDoManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class DoneToDoCommand implements CommandExecutor {

    @Override
    @Command(labels = "donetodo", usage = "/%label% [ID]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        ToDo toDo = ToDoManager.getToDo(id);

        if (toDo == null) {
            TextUtils.error("Es wurde kein ToDo Eintrag mit dieser ID gefunden.", p);
            return true;
        }

        toDo.setDone(true);
        toDo.save();

        TextComponentString text = new TextComponentString("Du hast die ToDo als erledigt makiert.");
        text.getStyle().setColor(TextFormatting.AQUA);

        p.sendMessage(text);
        return true;
    }
}
