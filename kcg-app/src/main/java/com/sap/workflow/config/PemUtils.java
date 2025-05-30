package com.sap.workflow.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;

public class PemUtils {

    public static PrivateKey parsePrivateKey(String pem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    public static Certificate parseCertificate(String pem) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(new java.io.ByteArrayInputStream(pem.getBytes()));
    }

    public static Certificate[] parseCertificateChain(String fullPem) throws IOException, CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        try (InputStream is = new ByteArrayInputStream(fullPem.getBytes(StandardCharsets.UTF_8))) {
            Collection<? extends Certificate> certs = cf.generateCertificates(is);
            return certs.toArray(new Certificate[0]);
        }
    }
}
