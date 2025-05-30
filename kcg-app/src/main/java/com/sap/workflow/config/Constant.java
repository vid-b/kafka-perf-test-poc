package com.sap.workflow.config;

public final class Constant {
    private Constant(){

    }
    public static final String VCAP_SERVICES = "vcap.services.";
    public static final String VCAP_APPLICATION = "vcap.application.";
    public static final String SPRING_PROFILES_ACTIVE = "SPRING_PROFILES_ACTIVE";
    public static final String PROFILE_CLOUD = "cloud";

    //Kafka Service Config keys
    public static final String KAFKA_CLIENT_CERTIFICATE = "KAFKA_CLIENT_CERTIFICATE";
    public static final String KAFKA_CLIENT_KEY = "KAFKA_CLIENT_KEY";
    public static final String KAFKA_ZK = "KAFKA_ZK";
    public static final String KAFKA_TOKEN_URL = "KAFKA_TOKEN_URL";
    public static final String KAFKA_CERT_URL = "KAFKA_CERT_URL";
    public static final String KAFKA_BROKER_DEFAULT = "KAFKA_BROKER_DEFAULT";
    public static final String KAFKA_BROKER_CLIENT_SSL = "KAFKA_BROKER_CLIENT_SSL";
    public static final String KAFKA_GROUP_NAME = "KAFKA_GROUP_NAME";
    public static final String KAFKA_SERVICE_NAME = "KAFKA_SERVICE_NAME";
    public static final String KEY_STORE_PASS = "KEY_STORE_PASS";

    //kafka key store
    public static final String KAFKA_KEYSTORE = "spring.kafka.properties.ssl.keystore.location";
    public static final String KAFKA_TRUSTSTORE = "spring.kafka.properties.ssl.truststore.location";
    public static final String KAFKA_BOOTSTRAP_SERVER = "spring.kafka.bootstrap-servers";
    public static final String KAFKA_GROUP_ID = "spring.kafka.consumer.group-id";
    public static final String KAFKA_KEYSTORE_PASS = "spring.kafka.properties.ssl.keystore.password";

    // Asset Central
    public static final String ASSET_CENTRAL_CLIENT_ID = "ASSET_CENTRAL_CLIENT_ID";
    public static final String ASSET_CENTRAL_CLIENT_SECRET = "ASSET_CENTRAL_CLIENT_SECRET";
    public static final String ASSET_CENTRAL_IDENTITY_ZONE = "ASSET_CENTRAL_IDENTITY_ZONE";
    public static final String ASSET_CENTRAL_ENDPOINT = "ASSET_CENTRAL_ENDPOINT";
    public static final String ASSET_CENTRAL_AUTHENTICATION_URL = "ASSET_CENTRAL_AUTHENTICATION_URL";

}
