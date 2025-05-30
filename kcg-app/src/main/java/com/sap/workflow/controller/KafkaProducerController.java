package com.sap.workflow.controller;

import com.sap.workflow.config.KafkaCertificateUtility;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;

import static com.sap.workflow.config.Constant.*;

@RestController
@RequestMapping("/produce")
public class KafkaProducerController {

    @Autowired
    private Environment env;

//    @Value("${KAFKA_CERT_URL}")
//    private String kafkaCertUrl;
//    @Value("${KAFKA_BOOTSTRAP_SERVER}")
//    private String bootstrapServers;
//    @Value("${KEY_STORE_PASS}")
//    private String keyStorePass;
//    @Value("${KAFKA_CLIENT_CERTIFICATE}")
//    private String kafkaClientCert;
//    @Value("${KAFKA_CLIENT_KEY}")
//    private String kafkaClientKey;


//    private final String kafkaServiceName = env.getProperty(KAFKA_SERVICE_NAME);
//    private final String bootstrapServers = env.getProperty(VCAP_SERVICES + kafkaServiceName + ".credentials.cluster.brokers.client_ssl", "");



    private static final String TOPIC = "test-topic-kcg";
//    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    private static Logger logger = LoggerFactory.getLogger(Consumer.class);

    @PostMapping("/{count}")
    public String sendMessages(@PathVariable int count) throws Exception {
        //NOTE: Properties to be added are different for spring kafka and apache kafka
        //TODO: Add properties in application properties
        String bootstrapServers = env.getProperty("vcap.services.ac-kafka.credentials.cluster.brokers.client_ssl");
        String kafkaCertUrl = env.getProperty("vcap.services.ac-kafka.credentials.urls.certs");
        String kafkaClientCert = env.getProperty("vcap.services.ac-kafka.credentials.clientcert");
        String kafkaClientKey = env.getProperty("vcap.services.ac-kafka.credentials.clientkey");
        Properties props = new Properties();
        logger.info("bootstrapServers {}", bootstrapServers);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(KAFKA_BOOTSTRAP_SERVER, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put("ssl.truststore.location", KafkaCertificateUtility.getTruststoreLocation(kafkaCertUrl, "changeit"));
        props.put("ssl.keystore.location", KafkaCertificateUtility.getKeystoreLocation(kafkaClientCert, kafkaClientKey, "changeit"));
        props.put("ssl.truststore.password", "changeit");
        props.put("ssl.keystore.password", "changeit");
        props.put("ssl.key.password", "changeit"); //

//        props.put(KAFKA_GROUP_ID, kafkaConfig.get(KAFKA_GROUP_NAME));

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 1; i <= count; i++) {
                String key = "key-" + i;
                String value = "Message number " + i;
                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key, value);

                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        logger.error("Sent: {} to partition {} offset {} %n", value, metadata.partition(), metadata.offset());
                    } else {
                        exception.printStackTrace();
                    }
                });
            }
        }

        return "Sent " + count + " messages to Kafka topic '" + TOPIC + "'";
    }
}

