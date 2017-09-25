package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class AddToDoCommand implements CommandExecutor {

    @Override
    @Command(labels = "addtodo", usage = "/%label% [ToDo]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String message = String.join(" ", args);

        ToDo toDo = new ToDo(message);
        toDo.add();

        TextComponentString text = new TextComponentString("Du hast eine ToDo erstellt. ToDo: ");
        text.getStyle().setColor(TextFormatting.AQUA);

        TextComponentString toDoText = new TextComponentString(message);
        toDoText.getStyle().setColor(TextFormatting.RED);

        p.sendMessage(text.appendSibling(toDoText));
        return true;
    }
}
