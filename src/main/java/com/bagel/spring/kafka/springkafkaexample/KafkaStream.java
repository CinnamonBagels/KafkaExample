package com.bagel.spring.kafka.springkafkaexample;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

@EnableKafkaStreams
@EnableKafka
@Configuration
public class KafkaStream {

  @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
  public StreamsConfig streamsConfig(KafkaProperties kafkaProperties) {
    Map<String, Object> properties = new HashMap<String, Object>() {{
      put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProperties.getClientId());
      put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
      put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
      put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
      put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
          WallclockTimestampExtractor.class.getName());
    }};
    return new StreamsConfig(properties);
  }

  @Bean
  public KStream<String, String> myStreamTopic(StreamsBuilder sb) {
    KStream<String, String> stream = sb.stream("streamtopic");

    stream
        .filter((k, v) -> v.contains("keepme"))
        .mapValues(v -> "new value")
        .to("filteredstreamtopic");

    return stream;
  }

  @Bean
  public KStream<String, JsonNode> jacksonExample(StreamsBuilder sb) {
    KStream<String, JsonNode> stream = sb.stream("jsontopic", Consumed
        .with(Serdes.String(), Serdes.serdeFrom(new JsonSerializer(), new JsonDeserializer())));

    stream.mapValues(JsonNode::asText)
        .to("filteredjsontopic");

    return stream;
  }
}
