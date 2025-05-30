package com.sap.workflow.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaCertificateUtility {

  private static final Map<String, Token> TOKENS = new ConcurrentHashMap<>();
  private static Logger logger = LoggerFactory.getLogger(KafkaCertificateUtility.class);

  public static String getTruststoreLocation(String certUrl, String truststorePassword)
      throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
    File certFile = File.createTempFile("kafkaRootCert", null);
    ReadableByteChannel in = Channels.newChannel(new URL(certUrl).openStream());

    try(FileOutputStream fileOutputStream = new FileOutputStream(certFile)){
      FileChannel out = fileOutputStream.getChannel();
      out.transferFrom(in, 0, Long.MAX_VALUE);
    }

    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    keystore.load(null, truststorePassword.toCharArray());
    // Add the certificate
    Map<String, Certificate> certs = loadCerts(certFile.getAbsolutePath());
    keystore.setCertificateEntry("KafkaRootCA", certs.get("current"));
    if(certs.containsKey("next"))
      keystore.setCertificateEntry("KafkaNextRootCA", certs.get("next"));
    // Save the new keystore
    File trustStoreFile = File.createTempFile("kafkaTrustStore", null);
    try(FileOutputStream os = new FileOutputStream(trustStoreFile);){
      keystore.store(os, truststorePassword.toCharArray());
      logger.info("Truststore Path is {} ", trustStoreFile.getAbsolutePath());
    }
    return trustStoreFile.getAbsolutePath();
  }

  public static String getKeystoreLocation(String clientCertPem, String clientKeyPem, String keystorePassword)
          throws Exception {

    // Create temp keystore file
    File keystoreFile = File.createTempFile("kafkaKeystore", ".p12");
    String alias = "KafkaClient";

    // Load client private key and certificate
    PrivateKey privateKey = PemUtils.parsePrivateKey(clientKeyPem);
    Certificate[] certChain = PemUtils.parseCertificateChain(clientCertPem);
    logger.debug("🔒 Certificate chain length: {}", certChain.length);

    KeyStore keystore = KeyStore.getInstance("PKCS12");
    keystore.load(null, keystorePassword.toCharArray());

    keystore.setKeyEntry(alias, privateKey, keystorePassword.toCharArray(), certChain);

    try (FileOutputStream os = new FileOutputStream(keystoreFile)) {
      keystore.store(os, keystorePassword.toCharArray());
    }

    logger.info("mTLS Keystore Path is: {}", keystoreFile.getAbsolutePath());
    return keystoreFile.getAbsolutePath();
  }

  private static Map<String, Certificate> loadCerts(String certFile) throws CertificateException, IOException {
    Map<String, Certificate> certs = new HashMap<>();
    InputStream is = null;
    try {
      is = new FileInputStream(certFile);
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      certs.put("current", cf.generateCertificate(is));
      if (is.available() > 0) {
        certs.put("next", cf.generateCertificate(is));
      }
    }finally{
      safeCloseResource(is);
    }
    return certs;
  }

  public static void safeCloseResource(Closeable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (IOException e) {
        logger.error("Error while closing resource", e);
      }
    }
  }
}
