package com.nosleepdrive.nosleepdrivebackend.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Component
public class Token {
    private static String SECRET_KEY;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Token(@Value("${security.token.key}") String key) {
        Token.SECRET_KEY = key;
    }

    public static String generateToken(String uid, long expirationEpochSeconds) {
        try{
            Map<String, Object> payload = new HashMap<>();
            payload.put("uid", uid);
            payload.put("exp", expirationEpochSeconds+System.currentTimeMillis()/1000);

            String payloadJson = objectMapper.writeValueAsString(payload);
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes());

            String signature = sign(encodedPayload, SECRET_KEY);

            return encodedPayload + "." + signature;
        }
            catch (Exception e) {
            throw new CustomError(BAD_REQUEST.value(), Message.ERR_IN_TOKEN.getMessage());
        }
    }

    public static long verifyToken(String token) {
        try{
            Map<String, Object> payload = readData(token);

            long exp = ((Number) payload.get("exp")).longValue();
            if(System.currentTimeMillis() / 1000 >= exp){
                throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
            }
            return Long.parseLong(payload.get("uid").toString());
        }
        catch(CustomError e){
            throw e;
        }
        catch (Exception e) {
            throw new CustomError(BAD_REQUEST.value(), Message.ERR_IN_TOKEN.getMessage());
        }
    }

    public static String verifyTokenHardware(String token) {
        try{
            Map<String, Object> payload = readData(token);

            long exp = ((Number) payload.get("exp")).longValue();
            if(System.currentTimeMillis() / 1000 >= exp){
                throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
            }
            return payload.get("uid").toString();
        }
        catch(CustomError e){
            throw e;
        }
        catch (Exception e) {
            throw new CustomError(BAD_REQUEST.value(), Message.ERR_IN_TOKEN.getMessage());
        }
    }

    private static Map readData(String token) throws JsonProcessingException {
        String[] parts = token.split("\\.");
        if (parts.length != 2) throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());

        String encodedPayload = parts[0];
        String receivedSignature = parts[1];

        String expectedSignature = sign(encodedPayload, SECRET_KEY);
        if (!expectedSignature.equals(receivedSignature)) throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());

        String payloadJson = new String(Base64.getUrlDecoder().decode(encodedPayload));
        return objectMapper.readValue(payloadJson, Map.class);
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
