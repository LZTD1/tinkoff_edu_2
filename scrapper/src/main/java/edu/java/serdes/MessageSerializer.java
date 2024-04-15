package edu.java.serdes;

import edu.java.kafka.messages.LinkUpdateOuterClass;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSerializer implements Serializer<LinkUpdateOuterClass.LinkUpdate> {

    @Override
    public byte[] serialize(String topic, LinkUpdateOuterClass.LinkUpdate data) {
        return data.toByteArray();
    }
}

