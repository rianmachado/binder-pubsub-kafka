/**
 * @author Rian Vasconcelos
 */
package connectors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.subscriber.PubSubSubscriberTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import tech.demo.movie.v1.Movie;
import tech.demo.movie.v1.MovieKey;

@SpringBootApplication
@Slf4j
public class Application {

	private final Service service;

	@Value("${spring.cloud.stream.bindings.process-in-0.destination}.${spring.cloud.stream.bindings.process-in-0.group}")
	private String subscription;


	@Autowired
	public Application(Service service){
		this.service = service;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	Function<Message<String>, Message<Movie>> process(){
		return payload -> {
			BasicAcknowledgeablePubsubMessage pubsub = (BasicAcknowledgeablePubsubMessage) payload.getHeaders()
					.get(GcpPubSubHeaders.ORIGINAL_MESSAGE);
			Movie avro =  service.jsonToAvro(pubsub);
			MovieKey key = service.genaratedKeyAvro();
			Message<Movie> message = MessageBuilder
					.withPayload(avro)
					.setHeader(KafkaHeaders.MESSAGE_KEY,key)
					.build();
			pubsub.ack();
			return message;
		};
	}
	@Bean
	public MessageChannel pubsubInputChannel() {
		return new PublishSubscribeChannel();
	}
	@Bean
	public PubSubInboundChannelAdapter messageChannelAdapter(
			@Qualifier("pubsubInputChannel") MessageChannel inputChannel,
			PubSubSubscriberTemplate subscriber) {
		PubSubInboundChannelAdapter adapter =
				new PubSubInboundChannelAdapter(subscriber,subscription);
		adapter.setOutputChannel(inputChannel);
		adapter.setAckMode(AckMode.MANUAL);
		adapter.setErrorChannelName("errorChannel");
		return adapter;
	}
	static class Producer {
		private AtomicBoolean semaphore = new AtomicBoolean(true);
		@Bean
		Supplier<String> sendData() {
			return () -> this.semaphore.getAndSet(!this.semaphore.get())
					? "{\"title\": \"Rei Leao\",\"category\": \"Drama\",\"year\": 1982}"
					: "{\"title\": \"Tropa de Elite\",\"category\": \"title\",\"year\": 1992}";
		}
	}
}
