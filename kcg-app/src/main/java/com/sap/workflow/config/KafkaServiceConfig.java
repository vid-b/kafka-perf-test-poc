//Commenting as we migrate from spring kafka to direct use of apache kafka
//In case if we need to bring back spring kafka usage, this config class needs to be added to spring application listener
//Refer: assetnetworks/iam-workflow/blob/master/iam-workflow-api/src/main/resources/META-INF/spring.factories

//Authentication - mTLS

//package com.sap.workflow.config;
//
//
//import org.apache.kafka.clients.CommonClientConfigs;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.Environment;
//import org.springframework.core.env.MutablePropertySources;
//import org.springframework.core.env.PropertiesPropertySource;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//import static com.sap.workflow.config.Constant.*;
//
//@Configuration
//public class KafkaServiceConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
//    private static Logger logger = LoggerFactory.getLogger(KafkaServiceConfig.class);
//    public Map<String,String> kafkaConfig(Environment env){
//        Map<String,String> kafkaConfigMap =  new HashMap<String, String>();
//        String kafkaServiceName = env.getProperty(KAFKA_SERVICE_NAME);
//        kafkaConfigMap.put(KAFKA_CLIENT_CERTIFICATE, env.getProperty(VCAP_SERVICES + kafkaServiceName + ".credentials.clientcert", ""));
//        kafkaConfigMap.put(KAFKA_CLIENT_KEY, env.getProperty(VCAP_SERVICES + kafkaServiceName + ".credentials.clientkey", ""));
//        kafkaConfigMap.put(KAFKA_ZK, env.getProperty(VCAP_SERVICES + kafkaServiceName + ".credentials.cluster.zk", ""));
//        kafkaConfigMap.put(KAFKA_CERT_URL, env.getProperty(VCAP_SERVICES + kafkaServiceName + ".credentials.urls.certs", ""));
//        kafkaConfigMap.put(KAFKA_BROKER_DEFAULT, env.getProperty(VCAP_SERVICES + kafkaServiceName + ".credentials.cluster.brokers", ""));
//        kafkaConfigMap.put(KAFKA_BROKER_CLIENT_SSL, env.getProperty(VCAP_SERVICES + kafkaServiceName + ".credentials.cluster.brokers.client_ssl", ""));
//        kafkaConfigMap.put(KAFKA_GROUP_NAME, env.getProperty(KAFKA_GROUP_NAME));
//        kafkaConfigMap.put(KEY_STORE_PASS,env.getProperty(KAFKA_KEYSTORE_PASS, ""));
//        return kafkaConfigMap;
//    }
//
//    @Override
//    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
//
//        ConfigurableEnvironment env = event.getEnvironment();
//        Properties props = new Properties();
//        MutablePropertySources propertySources = env.getPropertySources();
//        String profile  =  env.getProperty(SPRING_PROFILES_ACTIVE);
//         if(profile.equals(PROFILE_CLOUD)){
//            Map<String,String> kafkaConfig = this.kafkaConfig(env);
//            try{
//                props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//                props.put(KAFKA_TRUSTSTORE, KafkaCertificateUtility.getTruststoreLocation(kafkaConfig.get(KAFKA_CERT_URL), kafkaConfig.get(KEY_STORE_PASS)));
//                props.put(KAFKA_KEYSTORE, KafkaCertificateUtility.getKeystoreLocation(kafkaConfig.get(KAFKA_CLIENT_CERTIFICATE), kafkaConfig.get(KAFKA_CLIENT_KEY), kafkaConfig.get(KEY_STORE_PASS)));
//                props.put(KAFKA_BOOTSTRAP_SERVER, kafkaConfig.get(KAFKA_BROKER_CLIENT_SSL));
//                props.put(KAFKA_GROUP_ID, kafkaConfig.get(KAFKA_GROUP_NAME));
//                propertySources.addFirst(new PropertiesPropertySource("kafkaProps", props));
//                logger.info("Inside kafka properties");
//            }catch(Exception ex){
//                logger.error("Error in setting kafka properties : " + ex.getMessage());
//            }
//        } else if (profile.equalsIgnoreCase("local")) {
//             try {
//                 props.put(KAFKA_BOOTSTRAP_SERVER, "localhost:9092");
//                 props.put(KAFKA_GROUP_ID, "local-consumer-group");
//                 propertySources.addFirst(new PropertiesPropertySource("kafkaProps", props));
//                 logger.info("Loaded Kafka properties for Local profile.");
//             } catch (Exception ex) {
//                 logger.error("Error setting Kafka properties (Local): " + ex.getMessage());
//             }
//         }
//
//    }
//}
//
//
