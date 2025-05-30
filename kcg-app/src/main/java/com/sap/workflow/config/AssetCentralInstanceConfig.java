package com.sap.workflow.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

import static com.sap.workflow.config.Constant.*;


@Configuration
public class AssetCentralInstanceConfig {

  @Value("${ASSET_CENTRAL_INSTANCE}")
  private String serviceName;
  private static Logger logger = LoggerFactory.getLogger(AssetCentralInstanceConfig.class);


    @Bean
    public Map<String,String> assetServiceConfig(Environment env){
        Map<String,String> instanceMap =  new HashMap<String, String>();
        instanceMap.put(ASSET_CENTRAL_CLIENT_ID, env.getProperty(VCAP_SERVICES + serviceName + ".credentials.uaa.clientid", ""));
        instanceMap.put(ASSET_CENTRAL_CLIENT_SECRET, env.getProperty(VCAP_SERVICES + serviceName + ".credentials.uaa.clientsecret", ""));
        instanceMap.put(ASSET_CENTRAL_IDENTITY_ZONE, env.getProperty(VCAP_SERVICES + serviceName + ".credentials.uaa.identityzone", ""));
        instanceMap.put(ASSET_CENTRAL_ENDPOINT, env.getProperty(VCAP_SERVICES + serviceName + ".credentials.endpoints.asset-central-service", "") );
        instanceMap.put(ASSET_CENTRAL_AUTHENTICATION_URL, env.getProperty(VCAP_SERVICES + serviceName + ".credentials.uaa.url", "") + "/oauth/token");

        for (Map.Entry<String, String> assetCentralEntity : instanceMap.entrySet()) {
            if (assetCentralEntity.getValue().equals("")) {
                logger.error("Error in reading asset central instance");
            }
        }
        return instanceMap;
    }

}
