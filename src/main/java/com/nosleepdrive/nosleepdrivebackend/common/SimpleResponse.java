package com.nosleepdrive.nosleepdrivebackend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleResponse<T> {
    private String message;
    private T data;
    public static <T> SimpleResponse<T> withoutData(String message) {
        return new SimpleResponse<>(message, null);
    }

    public static <T> SimpleResponse<T> withData(String message, T data) {
        return new SimpleResponse<>(message, data);
    }
}
