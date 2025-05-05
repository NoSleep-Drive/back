package com.nosleepdrive.nosleepdrivebackend.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Component
public class Token {
    private static String SECRET_KEY; // 보안에 주의하세요!
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Token(@Value("${security.token.key}") String key) {
        Token.SECRET_KEY = key;
    }

    public static String generateToken(String uid, long expirationEpochSeconds) {
        try{
            Map<String, Object> payload = new HashMap<>();
            payload.put("uid", uid);
            payload.put("exp", expirationEpochSeconds);

            String payloadJson = objectMapper.writeValueAsString(payload);
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes());

            String signature = sign(encodedPayload, SECRET_KEY);

            return encodedPayload + "." + signature;
        }
            catch (Exception e) {
            throw new CustomError(BAD_REQUEST.value(), Message.ERR_IN_TOKEN.getMessage());
        }
    }

    public static boolean verifyToken(String token) {
        try{
            String[] parts = token.split("\\.");
            if (parts.length != 2) return false;

            String encodedPayload = parts[0];
            String receivedSignature = parts[1];

            String expectedSignature = sign(encodedPayload, SECRET_KEY);
            if (!expectedSignature.equals(receivedSignature)) return false;

            String payloadJson = new String(Base64.getUrlDecoder().decode(encodedPayload));
            Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);

            long exp = ((Number) payload.get("exp")).longValue();
            return System.currentTimeMillis() / 1000 < exp;
        }
            catch (Exception e) {
            throw new CustomError(BAD_REQUEST.value(), Message.ERR_IN_TOKEN.getMessage());
        }
    }

    private static String sign(String data, String secret) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            hmac.init(key);
            byte[] hash = hmac.doFinal(data.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        }
        catch (Exception e) {
            throw new CustomError(BAD_REQUEST.value(), Message.ERR_IN_TOKEN.getMessage());
        }
    }
}
