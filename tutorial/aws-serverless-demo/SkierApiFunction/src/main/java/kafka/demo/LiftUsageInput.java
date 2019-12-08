package kafka.demo;

import java.util.Properties;
import java.util.Random;
import kafka.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;

public class LiftUsageInput {

  public static void main(String[] args) {
    Random random = new Random();
    Properties props = new Properties();

//    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-input");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
    props.put("client.id", "lift-usage-producer");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("batch.size", 16384);
    props.put("linger.ms", 1);
    props.put("buffer.memory", 33554432);

//    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConfig.OFFSET_RESET_EARLIEST);
    Producer<String, String> producer = new KafkaProducer<>(props);
    for (int i = 0; i < 100; i++) {
      int liftId = random.nextInt(100);
      producer.send(new ProducerRecord<String, String>(KafkaConfig.INPUT_TOPIC, Integer.toString(liftId)));
    }
    producer.close();
  }
}
