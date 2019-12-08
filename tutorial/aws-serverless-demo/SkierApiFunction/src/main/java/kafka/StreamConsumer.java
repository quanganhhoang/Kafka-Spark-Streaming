package kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class StreamConsumer {
  public static void main(String[] args) {
    Consumer<String, Long> consumer = StreamConsumer.createConsumer();
    int noMessageFound = 0;
    String mostUsedLiftId = "";
    long maxUsage = 0;

    while (true) {
      ConsumerRecords<String, Long> consumerRecords = consumer.poll(Duration.ofSeconds(5));
      if (consumerRecords.count() == 0) {
        noMessageFound++;
        if (noMessageFound > 100) {
          break;
        } else {
          continue;
        }
      }

      for (ConsumerRecord<String, Long> record : consumerRecords) {
//        System.out.println("Record key: " + record.key());
//        System.out.println("Record value: " + record.value());
//        System.out.println("Record partition: " + record.partition());
//        System.out.println("Record offset: " + record.offset());

        long liftUsage = record.value();
        if (liftUsage > maxUsage) {
          maxUsage = liftUsage;
          mostUsedLiftId = record.key();
          System.out.println("Most frequently used lift: " + mostUsedLiftId);
        }
      }

      consumer.commitAsync();
    }

    consumer.close();
  }

  public static Consumer<String, Long> createConsumer() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfig.CLIENT_ID);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "6000");
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongDeserializer");
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
    props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000");
//    props.put("zookeeper.sync.time.ms","2000");
//    props.put("consumer.timeout.ms", "-1");

    Consumer<String, Long> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList(KafkaConfig.OUTPUT_TOPIC));

    return consumer;
  }
}
