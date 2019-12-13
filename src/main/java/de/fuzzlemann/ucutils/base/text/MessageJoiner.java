package de.fuzzlemann.ucutils.base.text;

import com.google.common.collect.Lists;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Fuzzlemann
 */
public class MessageJoiner<T> {

    private Message.Builder currentBuilder;
    private final List<T> over;
    private final BiConsumer<Message.Builder, T> consumer;
    private final MessagePart joiner;
    private final MessagePart niceJoiner;
    private final boolean nice;

    public MessageJoiner(Message.Builder currentBuilder, List<T> over, BiConsumer<Message.Builder, T> consumer, MessagePart joiner, MessagePart niceJoiner, boolean nice) {
        this.currentBuilder = currentBuilder;
        this.over = over;
        this.consumer = consumer;
        this.joiner = joiner;
        this.niceJoiner = niceJoiner;
        this.nice = nice;
    }

    public static <T> Builder<T> builder(T[] over) {
        return new Builder<T>().over(over);
    }

    public static <T> Builder<T> builder(Iterable<T> over) {
        return new Builder<T>().over(over);
    }

    public static <T> Builder<T> builder(Collection<T> over) {
        return new Builder<T>().over(over);
    }

    public static <T> Builder<T> builder(List<T> over) {
        return new Builder<T>().over(over);
    }

    public void apply() {
        int size = over.size();
        for (int i = 0; i < size; i++) {
            T t = over.get(i);
            consumer.accept(currentBuilder, t);

            if (i == size - 1) continue;
            if (nice && i == size - 2) {
                currentBuilder.messageParts(niceJoiner);
            } else {
                currentBuilder.messageParts(joiner);
            }
        }
    }

    public Message.Builder getCurrentBuilder() {
        return currentBuilder;
    }

    public Iterable<T> getOver() {
        return over;
    }

    public BiConsumer<Message.Builder, T> getConsumer() {
        return consumer;
    }

    public MessagePart getJoiner() {
        return joiner;
    }

    public MessagePart getNiceJoiner() {
        return niceJoiner;
    }

    public boolean isNice() {
        return nice;
    }

    public static final class Builder<T> {
        private Message.Builder currentBuilder;
        private List<T> over;
        private BiConsumer<Message.Builder, T> consumer;
        private MessagePart joiner;
        private MessagePart niceJoiner;
        private boolean nice;

        public Builder<T> currentBuilder(Message.Builder currentBuilder) {
            this.currentBuilder = currentBuilder;
            return this;
        }

        public Builder<T> over(T[] over) {
            return over(Arrays.asList(over));
        }

        public Builder<T> over(Iterable<T> over) {
            return over(Lists.newArrayList(over));
        }

        public Builder<T> over(Collection<T> over) {
            return over(new ArrayList<>(over));
        }

        public Builder<T> over(List<T> over) {
            this.over = over;
            return this;
        }

        public Builder<T> consumer(BiConsumer<Message.Builder, T> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder<T> commaJoiner() {
            return commaJoiner(TextFormatting.GRAY);
        }

        public Builder<T> commaJoiner(TextFormatting color) {
            return joiner(MessagePart.simple(", ", color));
        }

        public Builder<T> newLineJoiner() {
            return joiner(MessagePart.simple("\n", null));
        }

        public Builder<T> joiner(MessagePart joiner) {
            this.joiner = joiner;
            return this;
        }

        public Builder<T> andNiceJoiner() {
            return andNiceJoiner(TextFormatting.GRAY);
        }

        public Builder<T> andNiceJoiner(TextFormatting color) {
            return niceJoiner(MessagePart.simple(" und ", color));
        }

        public Builder<T> niceJoiner(MessagePart niceJoiner) {
            this.niceJoiner = niceJoiner;
            return nice();
        }

        public Builder<T> nice() {
            return nice(true);
        }

        public Builder<T> nice(boolean nice) {
            this.nice = nice;
            return this;
        }

        public MessageJoiner<T> build() {
            return new MessageJoiner<>(currentBuilder, over, consumer, joiner, niceJoiner, nice);
        }

        public Message.Builder advance() {
            build().apply();
            return currentBuilder;
        }
    }
}
