package hw1.kafka;

import edu.java.bot.BotComponent;
import edu.java.bot.botapi.kafka.KafkaController;
import edu.java.bot.botapi.kafka.KafkaDlq;
import edu.java.bot.botapi.services.BotCommunicatorService;
import edu.java.bot.botapi.services.CommunicatorService;
import edu.java.bot.configuration.kafka.KafkaConfiguration;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.mockito.ArgumentMatchers.any;

@Testcontainers
public class TestKafkaConsumer {

    @Container
    public static KafkaContainer container = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));
    private static KafkaController KafkaController;

    @SneakyThrows
    @Test
    void testSendUpdate() {
        BotComponent botComponent = Mockito.mock(BotComponent.class);
        KafkaDlq kafkaDlq = Mockito.mock(KafkaDlq.class);
        CommunicatorService communicatorService = Mockito.spy(new BotCommunicatorService(botComponent));

        var kafkaConfig = new KafkaConfiguration(kafkaDlq);
        var retryable = kafkaConfig.retryableTopicKafkaTemplate(
            container.getHost() + ":" + container.getFirstMappedPort()
        );
        KafkaController = new KafkaController(communicatorService, retryable, kafkaDlq);

        LinkUpdateOuterClass.LinkUpdate linkUpdate = LinkUpdateOuterClass.LinkUpdate.newBuilder().build();

        KafkaController.listenMessages(linkUpdate, 0, "messages.protobuf");

        Mockito.verify(communicatorService, Mockito.times(1)).sendMessage(any());
    }

}
