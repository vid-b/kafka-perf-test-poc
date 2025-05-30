//package com.sap.workflow.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class KafkaServiceConfigMap {
//
//    @Autowired
//    private Environment env;
//
//    private Map<String, String> kafkaConfigMap;
//
//    @PostConstruct
//    public void init() {
//        String kafkaServiceName = env.getProperty("KAFKA_SERVICE_NAME");
//        Map<String, String> map = new HashMap<>();
//        map.put("KAFKA_CLIENT_CERTIFICATE", env.getProperty("vcap.services." + kafkaServiceName + ".credentials.clientcert", ""));
//        map.put("KAFKA_CLIENT_KEY", env.getProperty("vcap.services." + kafkaServiceName + ".credentials.clientkey", ""));
//        map.put("KAFKA_ZK", env.getProperty("vcap.services." + kafkaServiceName + ".credentials.cluster.zk", ""));
//        map.put("KAFKA_CERT_URL", env.getProperty("vcap.services." + kafkaServiceName + ".credentials.urls.certs", ""));
//        map.put("KAFKA_BROKER_DEFAULT", env.getProperty("vcap.services." + kafkaServiceName + ".credentials.cluster.brokers", ""));
//        map.put("KAFKA_BROKER_CLIENT_SSL", env.getProperty("vcap.services." + kafkaServiceName + ".credentials.cluster.brokers.client_ssl", ""));
//        map.put("KAFKA_GROUP_NAME", env.getProperty("KAFKA_GROUP_NAME"));
//        map.put("KEY_STORE_PASS", env.getProperty("KAFKA_KEYSTORE_PASS", ""));
//        this.kafkaConfigMap = map;
//    }
//
//    public Map<String, String> getKafkaConfigMap() {
//        return kafkaConfigMap;
//    }
//}
//
