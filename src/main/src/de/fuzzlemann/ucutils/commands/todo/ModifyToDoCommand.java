package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import de.fuzzlemann.ucutils.utils.todo.ToDoManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;

/**
 * @author Fuzzlemann
 */
public class ModifyToDoCommand implements CommandExecutor {

    @Override
    @Command(labels = "modifytodo", usage = "/%label% [ID] [ToDo]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        ToDo toDo = ToDoManager.getToDo(id);

        if (toDo == null) {
            TextUtils.error("Es wurde kein ToDo Eintrag mit dieser ID gefunden.");
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

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
