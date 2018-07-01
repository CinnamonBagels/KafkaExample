# Spring boot 2.0 Kafka example

This demo application contains boilerplate code for Kafka streams, producers, and consumers.

## Running
* Setup a kafka cluster
  - [Docker](https://github.com/wurstmeister/kafka-docker)
  - [Kafka quickstart docs](https://kafka.apache.org/quickstart)
* `./gradlew bootRun`

## Pushing messages to the Kafka server
Through your favorite API environment, you can push messages to the kafka server
via a PUT
```
http://127.0.0.1:8080/push/topic/<sometopic>?k=<somekey>&v=<somevalue>
```
Example:
```
http://127.0.0.1:8080/push/topic/applicationlogs?k=myapplicationname&v=somelogvalue
```
The response is simply a stringified [SendResult](https://docs.spring.io/spring-kafka/api/org/springframework/kafka/support/SendResult.html)
containing information about the message sent to kafka.

## Kafka consumer
The kafka consumer is automatically configured to listen to all topics using the `topicPattern`
property on the `@KafkaListener` annotation, accepting any spring regex expression.
Specifc topics can be listened to through the `topics` annotation.

This demo application logs consumer records
```
ConsumerRecord(
    topic = mytopic,
    partition = 0,
    offset = 9,
    CreateTime = 1530424154199,
    serialized key size = 7,
    serialized value size = 9,
    headers = RecordHeaders(headers = [], isReadOnly = false),
    key = somekey,
    value = somevalue
)
```

## Kafka streams
Test out the kafka stream by pushing a message to the kafka server with certain parameters:
```
http://127.0.0.1:8080/push/topic/streamtopic?k=somekey&v=keepme
```

Verify the stream is working by inspecting the logs for an entry from the `KafkaConsumer`
```
ConsumerRecord(
  topic = filteredstreamtopic,
  partition = 0,
  offset = 5,
  CreateTime = 1530425204820,
  serialized key size = 7,
  serialized value size = 6,
  headers = RecordHeaders(headers = [], isReadOnly = false),
  key = somekey,
  value = new value
)
```

### Kafka streams with JSON
Kafka streams support Jackson JsonNode serialization and deserialization.
You can test it by sending any json string through the API
```
http://127.0.0.1:8080/push/topic/jsontopic?k=somekey&v={}
```

And verify that messages are produced to the `filteredjsontopic` kafka topic