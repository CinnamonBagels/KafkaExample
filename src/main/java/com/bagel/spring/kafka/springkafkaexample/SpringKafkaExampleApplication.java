package com.bagel.spring.kafka.springkafkaexample;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableKafka
@RestController
public class SpringKafkaExampleApplication {

  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public SpringKafkaExampleApplication(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @PutMapping("/push")
  public Mono<ServerResponse> pushToKafka(@RequestParam(value = "k") String key,
      @RequestParam(value = "v") String val) {
    return ServerResponse.status(HttpStatus.OK)
        .body(BodyInserters
            .fromPublisher(sendToKafka(key, val).map(RecordMetadata::toString), String.class));
  }

  private Mono<RecordMetadata> sendToKafka(String k, String v) {
    return Mono.fromFuture(kafkaTemplate.send("mytopic", k, v)
        .completable())
        .map(SendResult::getRecordMetadata);
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringKafkaExampleApplication.class, args);
  }
}
