package kafka.demo;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import kafka.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

/**
 * In this example, we implement a simple Lift-Usage-Count program using the high-level Streams DSL
 * that reads from a source topic "lift-usage-input", where the values of messages represent lines of text,
 * compute the lift usage histogram, write the continuous updated histogram
 * into a topic "lift-usage-count-output" where each record is an updated count of a liftId
 */
public class LiftUsageCount {

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-count");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConfig.OFFSET_RESET_EARLIEST);

    final StreamsBuilder builder = new StreamsBuilder();

    builder.<String, String>stream(KafkaConfig.INPUT_TOPIC)
        .groupBy((key, value) -> value)
        .count(Materialized.as("count-store"))
        .toStream()
        .to(KafkaConfig.OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));

    final Topology topology = builder.build();
    final KafkaStreams streams = new KafkaStreams(topology, props);
    final CountDownLatch latch = new CountDownLatch(1);

    // attach shutdown handler to catch control-c
    Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
      @Override
      public void run() {
        streams.close();
        latch.countDown();
      }
    });

    try {
      streams.start();
      latch.await();
    } catch (Throwable e) {
      System.exit(1);
    }
    System.exit(0);
  }
}
