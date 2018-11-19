package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
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

        ITextComponent text = Message.builder()
                .of("Du hast eine ToDo erstellt. ToDo: ").color(TextFormatting.AQUA).advance()
                .of(message).color(TextFormatting.RED).advance()
                .build().toTextComponent();

        p.sendMessage(text);
        return true;
    }
}
