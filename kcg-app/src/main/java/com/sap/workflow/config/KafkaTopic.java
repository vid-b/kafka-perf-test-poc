//package com.sap.workflow.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//
//@Configuration
//public class KafkaTopic {
//    private static Logger logger = LoggerFactory.getLogger(KafkaTopic.class);
//
//    @Bean
//    public String kafkaTopicName(Environment env) throws Exception {
//        String topicName = "test-topic-kcg";
//        logger.info("Topic name "+topicName);
//        return topicName;
//    }
//}
