/**
 * @author Rian Vasconcelos
 */
package connectors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

import connectors.dto.SaleKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
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
	Function<Message<String>, Message<GenericRecord>> process(){
		return payload -> {
			BasicAcknowledgeablePubsubMessage pubsub = (BasicAcknowledgeablePubsubMessage) payload.getHeaders()
					.get(GcpPubSubHeaders.ORIGINAL_MESSAGE);
			GenericRecord avro =  service.jsonToAvro(pubsub);
			SaleKey key = service.genaratedKeyAvro();
			Message<GenericRecord> message = MessageBuilder
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
		private final AtomicBoolean semaphore = new AtomicBoolean(true);
		@Bean
		Supplier<String> sendData() {
			return () -> this.semaphore.getAndSet(!this.semaphore.get())
					? "{\n\t\"sistemaOrigem\": \"BEMA\",\n\t\"vendasFranquia\": {\n\t\t\"vendaFranquia\": [{\n\t\t\t\"numero\": 2872,\n\t\t\t\"codigo\": 2872,\n\t\t\t\"data\": \"2022-10-07T11:11:49\",\n\t\t\t\"status\": \"ENTREGUE\",\n\t\t\t\"itens\": {\n\t\t\t\t\"itemVenda\": [{\n\t\t\t\t\t\"quantidade\": 1,\n\t\t\t\t\t\"valorTotal\": 104.9,\n\t\t\t\t\t\"produto\": {\n\t\t\t\t\t\t\"sku\": 25411\n\t\t\t\t\t},\n\t\t\t\t\t\"valorDesconto\": 0,\n\t\t\t\t\t\"tipoItem\": \"PADRAO\",\n\t\t\t\t\t\"numeroSequencia\": 1\n\t\t\t\t}]\n\t\t\t},\n\t\t\t\"identificadorPDV\": 5,\n\t\t\t\"funcionarioFranquia\": {\n\t\t\t\t\"documentos\": {\n\t\t\t\t\t\"documento\": [{\n\t\t\t\t\t\t\"valor\": \"98028311580\",\n\t\t\t\t\t\t\"tipoDocumento\": \"CPF\"\n\t\t\t\t\t}]\n\t\t\t\t}\n\t\t\t},\n\t\t\t\"franquia\": {\n\t\t\t\t\"codigo\": 14661\n\t\t\t},\n\t\t\t\"origemPedidoExterno\": \"1\"\n\t\t}]\n\t}\n}" :
					"{\n\t\"sistemaOrigem\": \"UX\",\n\t\"vendasFranquia\": {\n\t\t\"vendaFranquia\": [{\n\t\t\t\"numero\": 2872,\n\t\t\t\"codigo\": 2872,\n\t\t\t\"data\": \"2022-10-07T11:11:49\",\n\t\t\t\"status\": \"ENTREGUE\",\n\t\t\t\"itens\": {\n\t\t\t\t\"itemVenda\": [{\n\t\t\t\t\t\"quantidade\": 1,\n\t\t\t\t\t\"valorTotal\": 104.9,\n\t\t\t\t\t\"produto\": {\n\t\t\t\t\t\t\"sku\": 25411\n\t\t\t\t\t},\n\t\t\t\t\t\"valorDesconto\": 0,\n\t\t\t\t\t\"tipoItem\": \"PADRAO\",\n\t\t\t\t\t\"numeroSequencia\": 1\n\t\t\t\t}]\n\t\t\t},\n\t\t\t\"identificadorPDV\": 5,\n\t\t\t\"funcionarioFranquia\": {\n\t\t\t\t\"documentos\": {\n\t\t\t\t\t\"documento\": [{\n\t\t\t\t\t\t\"valor\": \"98028311580\",\n\t\t\t\t\t\t\"tipoDocumento\": \"CPF\"\n\t\t\t\t\t}]\n\t\t\t\t}\n\t\t\t},\n\t\t\t\"franquia\": {\n\t\t\t\t\"codigo\": 14661\n\t\t\t},\n\t\t\t\"origemPedidoExterno\": \"1\"\n\t\t}]\n\t}\n}";
		}
	}
}
