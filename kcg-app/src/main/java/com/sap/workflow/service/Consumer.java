//
//package com.sap.workflow.service;
//
//
////import com.sap.workflow.repository.EventTypeRepository;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.TopicPartition;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.listener.ConsumerSeekAware;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//
//@Component
//public class Consumer implements ConsumerSeekAware {
//
//    private static Logger logger = LoggerFactory.getLogger(Consumer.class);
//
//    @Autowired
//    private ConsumerAction consumerAction;
//
//
//
//    public Consumer(){
//        logger.info("WORKFLOW CONSUMER STARTED !!");
//    }
//
////    @Autowired
////    private EventTypeRepository eventTypeRepository;
//    @KafkaListener(topicPattern = "#{kafkaTopicName}")
//    public void processMessage(@Payload ConsumerRecord<String, Object> record,
//                               @Header(KafkaHeaders.OFFSET) Long offset,
//                               @Header(KafkaHeaders.CONSUMER) KafkaConsumer<String, String> consumer,
//                               @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partitionId)  {
//
//        String message = new String((byte[]) record.value());
//        logger.info("### Topic {} Received Message : {}",record.topic() , message);
//        if(message.length()>0){
//            String clientID = getClientId(record.topic());
//            JSONObject context = extractMessage(message);
//            context.put("clientID",clientID);
//            try{
//                consumerAction.processEquipmentMessage(context).thenApplyAsync(s -> {
//                    logger.info("##############Workflow process completed");
//                    return  "done";
//                });
//            }catch(Exception ex){
//                logger.error("Error while processing kafka message for client ",clientID);
//            }
//            logger.info("#########Workflow process next ##############");
//        }
//
//
//    }
//
//    public String getTenantId(String kafkaTopic) {
//        int firstIndex = kafkaTopic.lastIndexOf(".");
//        String tenantId = kafkaTopic.substring(0, firstIndex);
//        return tenantId;
//    }
//    public String getClientId(String kafkaTopic) {
//        int lastIndex = kafkaTopic.lastIndexOf(".");
//        String clientId = kafkaTopic.substring(lastIndex+1);
//        return clientId;
//    }
//
//    public JSONObject extractMessage(String encodedMessage){
//        JSONObject messageJson = null;
//        try{
//            messageJson = new JSONObject(encodedMessage);
//        }catch (JSONException ex){
//            logger.error("JSON Parsing error on consumer");
//        }catch(Exception ex){
//            logger.error("Error on JSON message read"+ex.getMessage());
//        }
//        return messageJson;
//    }
//
//
//
//    @Override
//    public void registerSeekCallback(ConsumerSeekCallback callback) {
//
//    }
//
//
//    @Override
//    public void onIdleContainer(Map<TopicPartition, Long> arg0, ConsumerSeekCallback arg1) {
//
//    }
//
//    @Override
//    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
//        logger.info("Partition Assignment:");
//        assignments.forEach((t, o) -> logger.info("Topic Patition " + t.topic() + "-" + t.partition() + " at offset " + o));
//        assignments.forEach((t, o) -> callback.seek(t.topic(), t.partition(), (o!=0?o-1:o)));
//    }
//
//}
//
