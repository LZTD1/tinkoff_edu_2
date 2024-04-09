package edu.java.bot.serdes;

import com.google.protobuf.InvalidProtocolBufferException;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class LinkUpdateDeserializer implements Deserializer<LinkUpdateOuterClass.LinkUpdate> {

    @Override
    public LinkUpdateOuterClass.LinkUpdate deserialize(String s, byte[] bytes) {
        try {
            return LinkUpdateOuterClass.LinkUpdate.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new SerializationException("Error when deserializing byte[] to protobuf", e);
        }
    }
}
