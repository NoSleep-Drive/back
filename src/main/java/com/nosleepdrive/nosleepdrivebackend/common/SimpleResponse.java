package com.nosleepdrive.nosleepdrivebackend.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponse {
    private int status;
    private String message;
}
