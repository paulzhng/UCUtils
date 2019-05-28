package de.fuzzlemann.ucutils.utils.text;

import de.fuzzlemann.ucutils.Main;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        private MessageBuilder() {
        }

        private final List<MessagePart> messageParts = new ArrayList<>();

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
            Main.MINECRAFT.player.sendMessage(build().toTextComponent());
        }

        public Message build() {
            return new Message(messageParts);
        }
    }
}
