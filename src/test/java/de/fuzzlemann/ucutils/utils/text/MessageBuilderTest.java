package de.fuzzlemann.ucutils.utils.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Fuzzlemann
 */
class MessageBuilderTest {

    private Message.MessageBuilder builder;

    @BeforeEach
    void afterEach() {
        resetBuilder();
    }

    @Test
    void testPrefixAndInfo() {
        assertEquals(Message.MessageBuilder.PREFIX_PARTS, builder.prefix().build().getMessageParts());
        resetBuilder();
        assertEquals(Message.MessageBuilder.INFO_PARTS, builder.info().build().getMessageParts());
    }

    @Test
    void testAdd() {
        assertEquals("test", builder.add("test").toString());
        resetBuilder();
        assertEquals("\n", builder.newLine().toString());
        resetBuilder();
        assertEquals(" ", builder.space().toString());
    }

    @Test
    void testConcatenation() {
        List<MessagePart> expected = new ArrayList<>(Message.MessageBuilder.PREFIX_PARTS);
        expected.addAll(Message.MessageBuilder.INFO_PARTS);

        List<MessagePart> actual = builder.prefix().info().build().getMessageParts();

        assertEquals(expected, actual);
    }

    private void resetBuilder() {
        builder = Message.builder();
    }
}
