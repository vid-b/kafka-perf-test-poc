//package com.sap.workflow.service;
//
////import com.sap.workflow.destination.DestinationHandler;
////import com.sap.workflow.model.EventWorkflow;
////import com.sap.workflow.model.Workflow;
////import com.sap.workflow.model.WorkflowInstance;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//
//@Component
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//public class ConsumerAction {
//    private static Logger logger = LoggerFactory.getLogger(ConsumerAction.class);
//
////    @Autowired
////    private WorkflowService workflowService;
//
////    @Autowired
////    private DestinationHandler destinationHandler;
//
//    @Async("threadPoolTaskExecutor")
//    public CompletableFuture<String> processEquipmentMessage(JSONObject context){
//        try{
//            String clientId = null;
//            try{
//                clientId = context.getString("clientID");
//            }catch(JSONException jsex){
//                logger.error("########## Json exception in getting client ID #####");
//            }catch (Exception ex){
//                logger.error("###### Expection in parsing client ID form message ###");
//            }
//            String source = context.getString("source");
//            String type = context.getString("type");
//
////            EventWorkflow workflowIds = workflowService.getAllWorkflowByEventType(clientId,source,type);
////            if(workflowIds.getWorkflowIds().size()>0){
////                Map<String,String> tenantDetails = destinationHandler.getTenantDetailsByClientId(clientId);
////                String tenantID =  tenantDetails.get("tenantId");
////                List<Workflow> workflows = new ArrayList<>();
////                for(int i=0;i<workflowIds.getWorkflowIds().size();i++){
////                    Workflow workflow = new Workflow();
////                    workflow.setId(workflowIds.getWorkflowIds().get(i));
////                    workflows.add(workflow);
////                }
////                List<WorkflowInstance> workflowInstances = workflowService.startWorkFlowForTenant(workflows,tenantID,context);
////                workflowInstances.forEach(instance -> {instance.setEventTypeID(workflowIds.getEventId());instance.setId(UUID.randomUUID().toString());});
////                workflowService.saveWorkflowInstance(workflowInstances);
////
////            }else{
////				logger.info(" No Workflow Bound for client {} and event type {}",clientId,type);
////			}
//
//
//        }catch(Exception ex){
//            logger.error("Error while creating workflow "+ex.getMessage());
//        }
//        return CompletableFuture.completedFuture("done");
//    }
//}
