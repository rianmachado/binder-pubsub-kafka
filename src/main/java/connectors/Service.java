package connectors;

import java.util.Optional;

import lombok.SneakyThrows;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.demo.movie.v1.Movie;
import tech.demo.movie.v1.MovieKey;


@Component
public class Service {

	@SneakyThrows
	public Movie jsonToAvro(BasicAcknowledgeablePubsubMessage input)  {
		Optional.of(input).orElseThrow();
		String message = new String(input.getPubsubMessage().getData().toByteArray());
		Movie movie = new ObjectMapper().readValue(message, Movie.class);
		Optional.of(movie).orElseThrow();
		return movie;
	}

	public MovieKey	genaratedKeyAvro(){
		MovieKey movieKey = new MovieKey();
		movieKey.setId((int)((Math.random() * (16 - 4)) + 2));
		return movieKey;
	}

}
