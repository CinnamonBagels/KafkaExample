package com.bagel.spring.kafka.springkafkaexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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


  @PutMapping("/push/topic/{topicName}")
  public Mono<String> pushToKafka(
      @PathVariable(value = "topicName") String topic,
      @RequestParam(value = "k") String key,
      @RequestParam(value = "v") String val
  ) {
    return Mono.fromFuture(kafkaTemplate.send(topic, key, val).completable())
        .map(SendResult::toString);
  }

  @PutMapping("/push")
  public Mono<String> pushToKafka(
      @RequestParam(value = "k") String key,
      @RequestParam(value = "v") String val
  ) {
    return pushToKafka("defaulttopic", key, val);
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringKafkaExampleApplication.class, args);
  }
}
