package de.fuzzlemann.ucutils.utils.tabcomplete;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;

/**
 * @author Fuzzlemann
 */
public class TabCompleterEx extends GuiChat.ChatTabCompleter {

    public TabCompleterEx(GuiTextField guiTextField) {
        super(guiTextField);
    }

    @Override
    public void complete() {
        if (this.didComplete) {
            this.textField.deleteFromCursor(0);
            this.textField.deleteFromCursor(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false) - this.textField.getCursorPosition());

            if (this.completionIdx >= this.completions.size()) {
                this.completionIdx = 0;
            }
        } else {
            int i = this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false);
            this.completions.clear();
            this.completionIdx = 0;
            String s = this.textField.getText().substring(0, this.textField.getCursorPosition());
            this.requestCompletions(s);

            if (this.completions.isEmpty()) {
                return;
            }

            this.didComplete = true;
            this.textField.deleteFromCursor(i - this.textField.getCursorPosition());
        }

        this.textField.writeText(net.minecraft.util.text.TextFormatting.getTextWithoutFormattingCodes(this.completions.get(this.completionIdx++)));

        if (this.completions.size() > 1) {
            StringBuilder stringbuilder = new StringBuilder();

            for (String s : this.completions) {
                if (stringbuilder.length() > 0) {
                    stringbuilder.append(", ");
                }

                stringbuilder.append(s);
            }

            Main.MINECRAFT.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(stringbuilder.toString()), 1);
        }
    }

    private void requestCompletions(String prefix) {
        TextUtils.error(prefix);

        if (prefix.length() >= 1) {
            ClientCommandHandler.instance.autoComplete(prefix);

            String[] splitted = prefix.toLowerCase().split(" ");

            if (splitted.length != 0) {
                String command = splitted[0].toLowerCase();
                if (TabCompleterHandler.EXCLUDED_SERVER.contains(command)) {
                    this.requestedCompletions = true;
                    setCompletions();
                    return;
                }
            }

            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketTabComplete(prefix, this.getTargetBlockPos(), this.hasTargetBlock));
            this.requestedCompletions = true;
        }
    }
}
