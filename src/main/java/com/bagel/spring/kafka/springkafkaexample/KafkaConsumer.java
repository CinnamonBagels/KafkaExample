package com.bagel.spring.kafka.springkafkaexample;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafka
@Configuration
public class KafkaConsumer {

  private Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

  @KafkaListener(topicPattern = ".*")
  public void onTopicPatternKafkaMessage(ConsumerRecord<String, String> record) {
    log.info(record.toString());
  }
}
