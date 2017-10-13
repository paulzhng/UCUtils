package de.fuzzlemann.ucutils.utils.text;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class Message {

    private final List<MessagePart> MESSAGE_PARTS;

    private Message(List<MessagePart> messageParts) {
        this.MESSAGE_PARTS = messageParts;
    }

    public List<MessagePart> getMessageParts() {
        return MESSAGE_PARTS;
    }

    public ITextComponent toTextComponent() {
        ITextComponent textComponent = null;

        for (MessagePart messagePart : MESSAGE_PARTS) {
            ITextComponent messageComponent = new TextComponentString(messagePart.getMessage());

            Style style = messageComponent.getStyle();

            TextFormatting color = messagePart.getColor();
            ClickEvent clickEvent = messagePart.getClickEvent();
            HoverEvent hoverEvent = messagePart.getHoverEvent();

            if (color != null)
                style.setColor(color);

            if (clickEvent != null)
                style.setClickEvent(clickEvent);

            if (hoverEvent != null)
                style.setHoverEvent(hoverEvent);

            if (textComponent == null) {
                textComponent = messageComponent;
            } else {
                textComponent.appendSibling(messageComponent);
            }
        }

        return textComponent;
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public static final class MessageBuilder {
        private final List<MessagePart> MESSAGE_PARTS = new ArrayList<>();

        public MessagePart.MessagePartBuilder of(String text) {
            return MessagePart.builder().currentBuilder(this).message(text);
        }

        public MessageBuilder add(String text) {
            return MessagePart.builder().currentBuilder(this).message(text).advance();
        }

        public MessageBuilder messageParts(MessagePart... messageParts) {
            Collections.addAll(this.MESSAGE_PARTS, messageParts);
            return this;
        }

        public Message build() {
            return new Message(MESSAGE_PARTS);
        }
    }
}
