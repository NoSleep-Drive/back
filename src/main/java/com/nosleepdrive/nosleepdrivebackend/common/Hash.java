package com.nosleepdrive.nosleepdrivebackend.common;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Hash {
    private static String salt;

    public Hash(@Value("${security.password.salt}") String salt) {
        this.salt = salt;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"입력 형식이 올바르지 않습니다.");
        }

        return result;
    }

    public boolean Match(String input, String hash) {
        return HashEncode(input).equals(HashEncode(input));
    }
}
