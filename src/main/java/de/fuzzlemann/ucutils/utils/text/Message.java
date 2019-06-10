package de.fuzzlemann.ucutils.utils.text;

import com.google.common.annotations.VisibleForTesting;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.Logger;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

/**
 * @author Fuzzlemann
 */
public class Message {

    private final List<MessagePart> messageParts;

    private Message(List<MessagePart> messageParts) {
        this.messageParts = messageParts;
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public List<MessagePart> getMessageParts() {
        return messageParts;
    }

    public ITextComponent toTextComponent() {
        ITextComponent textComponent = null;

        for (MessagePart messagePart : messageParts) {
            ITextComponent messageComponent = messagePart.toTextComponent();

            if (textComponent == null) {
                textComponent = messageComponent;
            } else {
                textComponent.appendSibling(messageComponent);
            }
        }

        return textComponent;
    }

    public static final class MessageBuilder {
        @VisibleForTesting
        static final List<MessagePart> PREFIX_PARTS;
        @VisibleForTesting
        static final List<MessagePart> INFO_PARTS;

        static {
            PREFIX_PARTS = Message.builder()
                    .of("UCUtils").color(TextFormatting.DARK_AQUA).advance()
                    .of(">").color(TextFormatting.DARK_GRAY).advance()
                    .space()
                    .build()
                    .getMessageParts();

            INFO_PARTS = Message.builder()
                    .of("  Info:").color(TextFormatting.AQUA).advance()
                    .space()
                    .build()
                    .getMessageParts();
        }

        private final List<MessagePart> messageParts = new ArrayList<>();

        private MessageBuilder() {
        }

        public MessageBuilder prefix() {
            return messageParts(PREFIX_PARTS);
        }

        public MessageBuilder info() {
            return messageParts(INFO_PARTS);
        }

        public MessagePart.MessagePartBuilder of(String text) {
            return MessagePart.builder().currentBuilder(this).message(text);
        }

        public MessageBuilder add(String text) {
            return MessagePart.builder().currentBuilder(this).message(text).advance();
        }

        public MessageBuilder space() {
            return add(" ");
        }

        public MessageBuilder newLine() {
            return add("\n");
        }

        public MessageBuilder messageParts(MessagePart... messageParts) {
            Collections.addAll(this.messageParts, messageParts);
            return this;
        }

        public MessageBuilder messageParts(Collection<MessagePart> messageParts) {
            this.messageParts.addAll(messageParts);
            return this;
        }

        public void send() {
            ITextComponent text = build().toTextComponent();

            if (ForgeUtils.isTest()) {
                Logger.LOGGER.info("MESSAGE: " + text.getUnformattedText());
                return;
            }

            Main.MINECRAFT.player.sendMessage(text);
        }

        public Message build() {
            return new Message(messageParts);
        }

        @Override
        public String toString() {
            return build().toTextComponent().getUnformattedText();
        }
    }
}
