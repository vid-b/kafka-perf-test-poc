package com.sap.workflow;

import com.sap.workflow.config.KafkaCertificateUtility;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

@Component
public class KafkaConsumerRunner implements CommandLineRunner {

    @Autowired
    private Environment env;

    //    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static Logger logger = LoggerFactory.getLogger(Consumer.class);

    private static final String TOPIC = "test-topic-kcg";
    private final int NUM_CONSUMER_GROUPS =
            Integer.parseInt(Optional.ofNullable(System.getenv("CONSUMER_GROUP_COUNT")).orElse("10"));

    private ExecutorService executor;
    private final List<KafkaConsumerRunnable> consumerRunnables = new ArrayList<>();

    @Override
    public void run(String... args) throws Exception {
        String bootstrapServers = env.getProperty("vcap.services.ac-kafka.credentials.cluster.brokers.client_ssl");
        String kafkaCertUrl = env.getProperty("vcap.services.ac-kafka.credentials.urls.certs");
        String kafkaClientCert = env.getProperty("vcap.services.ac-kafka.credentials.clientcert");
        String kafkaClientKey = env.getProperty("vcap.services.ac-kafka.credentials.clientkey");

        // Total number of consumer groups to create - get from user provided variable defined in mta.yaml
        int totalConsumers = NUM_CONSUMER_GROUPS;

        // Get instance-specific information from Cloud Foundry environment
        String instanceIndexStr = System.getenv("CF_INSTANCE_INDEX");
        if (instanceIndexStr == null) {
            logger.error("CF_INSTANCE_INDEX or CF_INSTANCE_COUNT is not set! Please ensure the app is deployed on Cloud Foundry.");
            throw new IllegalStateException("Missing Cloud Foundry instance metadata: CF_INSTANCE_INDEX");
        }

        int instanceIndex = Integer.parseInt(instanceIndexStr);
        final int instanceCount =
                Integer.parseInt(Optional.ofNullable(System.getenv("INSTANCE_COUNT")).orElse("1"));

        logger.info("Running in Cloud Foundry with instance index {} of total {}", instanceIndex, instanceCount);



        // Distribute consumers evenly across instances
        // For example, if there are 400 consumers and 4 instances, then each instance should run 100 consumers
        // In this case, we have 1 consumer per consumer group
        // Each consumer should be named test-consumer-group-<instance_index>-<consumer_count_index>
        // out of 400, instance with index 0 will run cg-0-to-99, index 1 will run cg-100-to-199,.. index 3 cg 300-399
        int consumersPerInstance = totalConsumers / instanceCount;
        int extraConsumers = totalConsumers % instanceCount;

        int start = instanceIndex * consumersPerInstance + Math.min(instanceIndex, extraConsumers);
        int end = start + consumersPerInstance + (instanceIndex < extraConsumers ? 1 : 0);

        logger.info("Instance {} creating consumers from {} to {}", instanceIndex, start, end - 1);

        executor = Executors.newFixedThreadPool(end - start);

        for (int i = start; i < end; i++) {
            String groupId = "test-consumer-group-" + instanceIndex + "-" + i;
//            String groupId = "test-consumer-group-" + i;
            KafkaConsumerRunnable consumerRunnable = new KafkaConsumerRunnable(groupId, bootstrapServers, kafkaCertUrl, kafkaClientCert, kafkaClientKey);
            consumerRunnables.add(consumerRunnable);
            executor.submit(consumerRunnable);
            Thread.sleep(100);
        }

        // Code for scenario where all instances should run all consumer groups
        // For example, if there are 400 consumers and 4 instances, each of the 4 instance should run all 400 consumers
//        executor = Executors.newFixedThreadPool(NUM_CONSUMER_GROUPS);
//
//        for (int i = 0; i < NUM_CONSUMER_GROUPS; i++) {
//            String groupId = "test-consumer-group-" + i;
//            KafkaConsumerRunnable consumerRunnable = new KafkaConsumerRunnable(groupId, bootstrapServers, kafkaCertUrl, kafkaClientCert, kafkaClientKey);
//            consumerRunnables.add(consumerRunnable);
//            executor.submit(consumerRunnable);
//            Thread.sleep(100);
//        }
    }

    // Gracefully shutdown on app exit
    @PreDestroy
    public void shutdown() {
        logger.info("Shutdown initiated, stopping consumers...");

        // Stop all consumers
        consumerRunnables.forEach(KafkaConsumerRunnable::shutdown);

        // Shutdown executor
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    logger.error("Executor did not terminate in the specified time.");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        logger.info("All consumers stopped.");
    }

    private static class KafkaConsumerRunnable implements Runnable {
        private final KafkaConsumer<String, String> consumer;
        private volatile boolean running = true;

        KafkaConsumerRunnable(String groupId, String bootstrapServers, String kafkaCertUrl, String kafkaClientCert, String kafkaClientKey) throws Exception {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
            props.put("ssl.truststore.location", KafkaCertificateUtility.getTruststoreLocation(kafkaCertUrl, "changeit"));
            props.put("ssl.keystore.location", KafkaCertificateUtility.getKeystoreLocation(kafkaClientCert, kafkaClientKey, "changeit"));
            props.put("ssl.truststore.password", "changeit");
            props.put("ssl.keystore.password", "changeit");
            props.put("ssl.key.password", "changeit"); //

            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(TOPIC));
            logger.info("### Group {} created consumer", groupId);
        }

        @Override
        public void run() {
            logger.info("Consumer group {} started consuming.", consumer.groupMetadata().groupId());
            try {
                while (running) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("Group {} Consumed message: {}",
                                consumer.groupMetadata().groupId(), record.value());
                    }
                }
            } catch (Exception e) {
                if (running) {
                    logger.error("Consumer error: {} ", e.getMessage());
                }
            } finally {
                consumer.close();
                logger.info("Consumer closed for group {}", consumer.groupMetadata().groupId());
            }
        }

        public void shutdown() {
            running = false;
            consumer.wakeup();
        }
    }
}
