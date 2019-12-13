package de.fuzzlemann.ucutils.base.text;

import com.google.common.annotations.VisibleForTesting;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
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

    public static Builder builder() {
        return new Builder();
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

    public void send() {
        AbstractionLayer.getPlayer().sendMessage(toTextComponent());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
                .add("messageParts=" + messageParts)
                .toString();
    }

    public static final class Builder {
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

        private Builder() {
        }

        public Builder prefix() {
            return messageParts(PREFIX_PARTS);
        }

        public Builder info() {
            return messageParts(INFO_PARTS);
        }

        public <T> MessageJoiner.Builder<T> joiner(T[] over) {
            return MessageJoiner.builder(over).currentBuilder(this);
        }

        public <T> MessageJoiner.Builder<T> joiner(Iterable<T> over) {
            return MessageJoiner.builder(over).currentBuilder(this);
        }

        public <T> MessageJoiner.Builder<T> joiner(Collection<T> over) {
            return MessageJoiner.builder(over).currentBuilder(this);
        }

        public <T> MessageJoiner.Builder<T> joiner(List<T> over) {
            return MessageJoiner.builder(over).currentBuilder(this);
        }

        public MessagePart.Builder of(String text) {
            return MessagePart.builder().currentBuilder(this).message(text);
        }

        public Builder add(String text) {
            return MessagePart.builder().currentBuilder(this).message(text).advance();
        }

        public Builder space() {
            return add(" ");
        }

        public Builder newLine() {
            return add("\n");
        }

        public Builder messageParts(MessagePart... messageParts) {
            Collections.addAll(this.messageParts, messageParts);
            return this;
        }

        public Builder messageParts(Collection<MessagePart> messageParts) {
            this.messageParts.addAll(messageParts);
            return this;
        }

        public void send() {
            build().send();
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
