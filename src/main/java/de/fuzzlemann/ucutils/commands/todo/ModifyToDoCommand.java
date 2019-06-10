package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Fuzzlemann
 */
public class ModifyToDoCommand {

    @Command(value = "modifytodo", usage = "/%label% [ID] [ToDo]")
    public boolean onCommand(ToDo toDo, @CommandParam(joinStart = true) String message) {
        toDo.setMessage(message);
        toDo.save();

        Message.builder()
                .prefix()
                .of("Du hast die Nachricht der ToDo auf \"").color(TextFormatting.GRAY).advance()
                .of(message).color(TextFormatting.BLUE).advance()
                .of("\" geändert.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }
}
