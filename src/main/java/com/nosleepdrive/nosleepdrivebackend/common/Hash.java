package com.nosleepdrive.nosleepdrivebackend.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class Hash {
    private static String salt;
    private static final SecureRandom random = new SecureRandom();

    public Hash(@Value("${security.password.salt}") String salt) {
        Hash.salt = salt;
    }

    public static String generateRandomSHA256Hash() {
        byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(randomBytes);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomError(HttpStatus.BAD_REQUEST.value(),"해쉬 값 생성 과정에 오류가 생겼습니다.");
        }
    }

    public String HashEncode(String input) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update((input+salt).getBytes());
            byte[] pwdsalt = md.digest();

            StringBuffer sb = new StringBuffer();
            for (byte b : pwdsalt) {
                sb.append(String.format("%02x", b));
            }

            result=sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new CustomError(HttpStatus.BAD_REQUEST.value(),"입력 형식이 올바르지 않습니다.");
        }

        return result;
    }

    public boolean Match(String input, String hash) {
        return HashEncode(input).equals(hash);
    }
}
