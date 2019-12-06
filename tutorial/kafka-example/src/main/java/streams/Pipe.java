/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package streams;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.streams.kstream.KStream;

/**
 * In this example, we implement a simple LineSplit program using the high-level Streams DSL
 * that reads from a source topic "streams-plaintext-input", where the values of messages represent lines of text,
 * and writes the messages as-is into a sink topic "streams-pipe-output".
 */
public class Pipe {

  public static void main(String[] args) throws Exception {
    // specify different Streams execution config values as defined in StreamsConfig
    Properties props = new Properties();

    // unique ID of application to distinguish from others talking to the same Kafka cluster
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");

    // specify list of host/port pairs for establishing initial connection to Kafka cluster
    // application talking to a Kafka broker running on local machine with port 9092
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

    // default serialization
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    // construct a topology
    final StreamsBuilder builder = new StreamsBuilder();

    // create a source stream from a Kafka topic named "streams-plaintext-input"
    // continuously generates records organized as String typed kv-pairs from its source Kafka topic
    KStream<String, String> source = builder.stream("streams-plaintext-input");

    // write source stream to another Kafka topic
    source.to("streams-pipe-output");

    // concat above two lines
    // builder.stream("streams-plaintext-input").to("streams-pipe-output");

    final Topology topology = builder.build();
    System.out.println(topology.describe());

    // construct a Streams client
    final KafkaStreams streams = new KafkaStreams(topology, props);

    // shutdown hook to capture a user interrupt and close client upon terminating this program
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
