package de.fuzzlemann.ucutils.commands.todo;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.todo.ToDo;
import de.fuzzlemann.ucutils.utils.todo.ToDoManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
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
            TextComponentString text = new TextComponentString("Du hast derzeit keine ToDo-Eintr\u00e4ge.");
            text.getStyle().setColor(TextFormatting.GREEN);

            p.sendMessage(text);
            return true;
        }

        TextComponentString text = new TextComponentString("\u00bb");
        text.getStyle().setColor(TextFormatting.GOLD);

        TextComponentString textMid = new TextComponentString(" ToDos\n");
        textMid.getStyle().setColor(TextFormatting.DARK_PURPLE);
        text.appendSibling(textMid);

        TextComponentString doneComponent = new TextComponentString("[\u2713] ");
        doneComponent.getStyle().setColor(TextFormatting.GREEN);

        TextComponentString doneHoverText = new TextComponentString("Makiere den Eintrag als erledigt");
        doneHoverText.getStyle().setColor(TextFormatting.GREEN);
        doneComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, doneHoverText));

        TextComponentString deleteComponent = new TextComponentString("[\u2717]\n");
        deleteComponent.getStyle().setColor(TextFormatting.RED);

        TextComponentString deleteHoverText = new TextComponentString("L\u00f6sche den Eintrag");
        deleteHoverText.getStyle().setColor(TextFormatting.RED);
        deleteComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, deleteHoverText));

        TextComponentString prefix = new TextComponentString("  * ");
        prefix.getStyle().setColor(TextFormatting.GRAY);

        for (ToDo toDo : toDoList) {
            TextComponentString messageComponent = new TextComponentString(toDo.getMessage() + " ");
            messageComponent.getStyle().setColor(toDo.isDone() ? TextFormatting.GREEN : TextFormatting.RED);

            TextComponentString hoverText = new TextComponentString(toDo.isDone() ? "Erledigt" : "Nicht erledigt");
            hoverText.getStyle().setColor(toDo.isDone() ? TextFormatting.GREEN : TextFormatting.RED);

            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText);
            messageComponent.getStyle().setHoverEvent(hoverEvent);

            TextComponentString copiedDeleteComponent = deleteComponent.createCopy();
            TextComponentString copiedDoneComponent = doneComponent.createCopy();
            TextComponentString copiedPrefix = prefix.createCopy();

            ClickEvent deleteClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/removetodo " + toDo.getId());
            copiedDeleteComponent.getStyle().setClickEvent(deleteClickEvent);

            TextComponentString prefixHoverText = new TextComponentString("Erstellt am " + dateFormat.format(new Date(toDo.getCreated())));
            prefixHoverText.getStyle().setColor(TextFormatting.GRAY);

            HoverEvent prefixHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHoverText);
            copiedPrefix.getStyle().setHoverEvent(prefixHoverEvent);

            text.appendSibling(copiedPrefix).appendSibling(messageComponent);

            if (!toDo.isDone()) {
                ClickEvent doneClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/donetodo " + toDo.getId());
                copiedDoneComponent.getStyle().setClickEvent(doneClickEvent);

                text.appendSibling(copiedDoneComponent);
            }

            text.appendSibling(copiedDeleteComponent);
        }

        p.sendMessage(text);
        return true;
    }
}
