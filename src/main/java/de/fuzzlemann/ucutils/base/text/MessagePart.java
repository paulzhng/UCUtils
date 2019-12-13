package de.fuzzlemann.ucutils.base.text;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class MessagePart {

    private final String message;
    private final TextFormatting color;
    private final boolean bold;
    private final boolean italic;
    private final boolean strikethrough;
    private final boolean underlined;
    private final boolean obfuscated;
    private final HoverEvent hoverEvent;
    private final ClickEvent clickEvent;

    private MessagePart(String message, TextFormatting color, boolean bold, boolean italic, boolean strikethrough, boolean underlined, boolean obfuscated, HoverEvent hoverEvent, ClickEvent clickEvent) {
        this.message = message;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.strikethrough = strikethrough;
        this.underlined = underlined;
        this.obfuscated = obfuscated;
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static MessagePart simple(String message, TextFormatting color) {
        return MessagePart.builder().message(message).color(color).build();
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

    public ITextComponent toTextComponent() {
        ITextComponent messageComponent = new TextComponentString(message);
        Style style = messageComponent.getStyle();

        if (color != null)
            style.setColor(color);

        if (clickEvent != null)
            style.setClickEvent(clickEvent);

        if (hoverEvent != null)
            style.setHoverEvent(hoverEvent);

        if (bold)
            style.setBold(true);

        if (italic)
            style.setItalic(true);

        if (strikethrough)
            style.setStrikethrough(true);

        if (underlined)
            style.setUnderlined(true);

        if (obfuscated)
            style.setObfuscated(true);

        return messageComponent;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MessagePart.class.getSimpleName() + "[", "]")
                .add("message='" + message + "'")
                .add("color=" + color)
                .add("bold=" + bold)
                .add("italic=" + italic)
                .add("strikethrough=" + strikethrough)
                .add("underlined=" + underlined)
                .add("obfuscated=" + obfuscated)
                .add("hoverEvent=" + hoverEvent)
                .add("clickEvent=" + clickEvent)
                .toString();
    }

    public static final class Builder {
        private Message.Builder currentBuilder;
        private String message;
        private TextFormatting color;
        private boolean bold;
        private boolean italic;
        private boolean strikethrough;
        private boolean underlined;
        private boolean obfuscated;
        private HoverEvent hoverEvent;
        private ClickEvent clickEvent;

        public Builder currentBuilder(Message.Builder currentBuilder) {
            this.currentBuilder = currentBuilder;
            return this;
        }

        public Builder bold() {
            return bold(true);
        }

        public Builder bold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public Builder italic() {
            return italic(true);
        }

        public Builder italic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public Builder strikethrough() {
            return strikethrough(true);
        }

        public Builder strikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public Builder underlined() {
            return underlined(true);
        }

        public Builder underlined(boolean underlined) {
            this.underlined = underlined;
            return this;
        }

        public Builder obfuscated() {
            return obfuscated(true);
        }

        public Builder obfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder color(TextFormatting color) {
            this.color = color;
            return this;
        }

        public Builder hoverEvent(HoverEvent.Action action, MessagePart messagePart) {
            return hoverEvent(action, Message.builder().messageParts(messagePart).build());
        }

        public Builder hoverEvent(HoverEvent.Action action, Message message) {
            return hoverEvent(new HoverEvent(action, message.toTextComponent()));
        }

        Builder hoverEvent(HoverEvent hoverEvent) {
            this.hoverEvent = hoverEvent;
            return this;
        }

        public Builder clickEvent(ClickEvent.Action action, String message) {
            return clickEvent(new ClickEvent(action, message));
        }

        Builder clickEvent(ClickEvent clickEvent) {
            this.clickEvent = clickEvent;
            return this;
        }

        public MessagePart build() {
            return new MessagePart(message, color, bold, italic, strikethrough, underlined, obfuscated, hoverEvent, clickEvent);
        }

        public Message.Builder advance() {
            return currentBuilder.messageParts(build());
        }
    }
}
