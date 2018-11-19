package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import de.fuzzlemann.ucutils.utils.todo.ToDoManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ToDoListCommand implements CommandExecutor {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    @Override
    @Command(labels = {"todo", "todolist"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        List<ToDo> toDoList = ToDoManager.getToDoList();

        if (toDoList.isEmpty()) {
            p.sendMessage(TextUtils.simpleMessage("Du hast derzeit keine ToDo-Einträge.", TextFormatting.GREEN));
            return true;
        }

        Message.MessageBuilder builder = Message.builder();

        builder.of("»").color(TextFormatting.GOLD).advance()
                .of(" ToDos\n").color(TextFormatting.DARK_PURPLE).advance();

        for (ToDo toDo : toDoList) {
            builder.space().space()
                    .of("*").color(TextFormatting.GRAY)
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Erstellt am " + dateFormat.format(new Date(toDo.getCreated())), TextFormatting.GRAY)).advance()
                    .space()
                    .of(toDo.getMessage()).color(toDo.isDone() ? TextFormatting.GREEN : TextFormatting.RED)
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart(toDo.isDone() ? "Erledigt" : "Nicht erledigt", toDo.isDone() ? TextFormatting.GREEN : TextFormatting.RED)).advance()
                    .space();

            if (!toDo.isDone()) {
                builder.of("[✓]").color(TextFormatting.GREEN)
                        .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Markiere den Eintrag als erledigt", TextFormatting.GREEN))
                        .clickEvent(ClickEvent.Action.RUN_COMMAND, "/donetodo " + toDo.getId()).advance()
                        .space();
            }

            builder.of("[✗]").color(TextFormatting.RED)
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Lösche den Eintrag", TextFormatting.RED))
                    .clickEvent(ClickEvent.Action.RUN_COMMAND, "/removetodo " + toDo.getId()).advance()
                    .space()
                    .of("[␈]\n").color(TextFormatting.DARK_GRAY)
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Verändere den Eintrag", TextFormatting.DARK_GRAY))
                    .clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/modifytodo " + toDo.getId() + " " + toDo.getMessage()).advance();
        }

        p.sendMessage(builder.build().toTextComponent());
        return true;
    }
}
