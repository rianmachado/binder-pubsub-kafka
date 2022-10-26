package connectors;

import connectors.dto.Sale;
import connectors.dto.SaleKey;
import lombok.SneakyThrows;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class Service {

	@SneakyThrows
	public GenericRecord jsonToAvro(BasicAcknowledgeablePubsubMessage input)  {
		String message = new String(input.getPubsubMessage().getData().toByteArray());
		ObjectMapper mapper = new ObjectMapper();
		Sale sale = mapper.readValue(message, Sale.class);
		Schema.Parser parser = new Schema.Parser();
		Schema schema = parser.parse(getClass().getResourceAsStream("/avro/sale.avsc"));
		GenericRecord avro = new GenericData.Record(schema);
		schema.getFields().forEach(field -> {
			Object value = PropertyAccessorFactory.forBeanPropertyAccess(sale).getPropertyValue(field.name());
			avro.put(field.name(), value);
		});
		return avro;
	}

	public SaleKey genaratedKeyAvro(){
		SaleKey movieKey = new SaleKey();
		movieKey.setId((int)((Math.random() * (16 - 4)) + 2));
		return movieKey;
	}

}
