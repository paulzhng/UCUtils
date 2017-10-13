package de.fuzzlemann.ucutils.utils.text;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

/**
 * @author Fuzzlemann
 */
public class MessagePart {

    private final String message;
    private final TextFormatting color;
    private final HoverEvent hoverEvent;
    private final ClickEvent clickEvent;

    private MessagePart(String message, TextFormatting color, HoverEvent hoverEvent, ClickEvent clickEvent) {
        this.message = message;
        this.color = color;
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
    }

    public String getMessage() {
        return message;
    }

    public TextFormatting getColor() {
        return color;
    }

    public HoverEvent getHoverEvent() {
        return hoverEvent;
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public static MessagePartBuilder builder() {
        return new MessagePartBuilder();
    }

    public static final class MessagePartBuilder {
        private Message.MessageBuilder currentBuilder;
        private String message;
        private TextFormatting color;
        private HoverEvent hoverEvent;
        private ClickEvent clickEvent;

        public MessagePartBuilder currentBuilder(Message.MessageBuilder currentBuilder) {
            this.currentBuilder = currentBuilder;
            return this;
        }

        public MessagePartBuilder message(String message) {
            this.message = message;
            return this;
        }

        public MessagePartBuilder color(TextFormatting color) {
            this.color = color;
            return this;
        }

        public MessagePartBuilder hoverEvent(HoverEvent.Action action, MessagePart messagePart) {
            return hoverEvent(new HoverEvent(action, Message.builder().messageParts(messagePart).build().toTextComponent()));
        }

        MessagePartBuilder hoverEvent(HoverEvent hoverEvent) {
            this.hoverEvent = hoverEvent;
            return this;
        }

        public MessagePartBuilder clickEvent(ClickEvent.Action action, String message) {
            return clickEvent(new ClickEvent(action, message));
        }

        MessagePartBuilder clickEvent(ClickEvent clickEvent) {
            this.clickEvent = clickEvent;
            return this;
        }

        MessagePart build() {
            return new MessagePart(message, color, hoverEvent, clickEvent);
        }

        public Message.MessageBuilder advance() {
            return currentBuilder.messageParts(build());
        }
    }
}
