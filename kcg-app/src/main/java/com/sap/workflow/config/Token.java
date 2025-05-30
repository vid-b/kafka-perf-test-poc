package com.sap.workflow.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

public class Token {

	private final String token;
    private final Instant expiryTime;
    private static final int TOKEN_EXPIRY_OFFSET_IN_MINUTES = 10;

    public Token(String token, Instant expiryTime) {
       this.token = token;
       this.expiryTime = expiryTime;
    }

    public String getToken() {
       return token;
    }

    public Instant expiry() {
       return expiryTime;
    }

	public static Token getToken(String tokenUrl, String user, String password) throws IOException {
       String userCredentials = user + ":" + password;
       String authHeaderValue = "Basic " + Base64.encodeBase64String(userCredentials.getBytes());
       String bodyParams = "grant_type=client_credentials";
       byte[] postData = bodyParams.getBytes(StandardCharsets.UTF_8);
       String resp = null;
       HttpURLConnection conn = null;;
       try {
          URL url = new URL(tokenUrl);
          conn = (HttpURLConnection) url.openConnection();
          conn.setRequestProperty("Authorization", authHeaderValue);
          conn.setRequestMethod("POST");
          conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
          conn.setRequestProperty("charset", "utf-8");
          conn.setRequestProperty("Content-Length", "" + postData.length);
          conn.setUseCaches(false);
          conn.setDoInput(true);
          conn.setDoOutput(true);
          try (DataOutputStream os = new DataOutputStream(conn.getOutputStream())) {
            os.write(postData);
          }

          try (DataInputStream is = new DataInputStream(conn.getInputStream());
        	BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            resp = br.lines().collect(Collectors.joining("\n"));
          }
       }
       catch (Exception ex) {
          throw ex;
       } finally {
          if (null != conn) {
            conn.disconnect();
          }
       }
       JsonObject token = new Gson().fromJson(resp, JsonObject.class);
       String accessToken = token.get("access_token").getAsString();
       Instant expiryTime = Instant.now().plus(Duration.ofSeconds(token.get("expires_in").getAsLong())
                                  .minusMinutes(TOKEN_EXPIRY_OFFSET_IN_MINUTES));
       return new Token(accessToken, expiryTime);
	}

}
