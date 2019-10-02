package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandParam;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class AddToDoCommand {

    @Command(value = "addtodo", usage = "/%label% [ToDo]")
    public boolean onCommand(@CommandParam(joinStart = true) String message) {
        ToDo toDo = new ToDo(message);
        toDo.add();

        Message.builder()
                .prefix()
                .of("Du hast ein Element deiner ToDo-Liste hinzugefügt: ").color(TextFormatting.GRAY).advance()
                .of("\"" + message + "\"").color(TextFormatting.BLUE).advance()
                .of(".").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }
}
