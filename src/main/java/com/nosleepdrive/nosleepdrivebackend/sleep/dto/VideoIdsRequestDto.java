package com.nosleepdrive.nosleepdrivebackend.sleep.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VideoIdsRequestDto {
    private List<Long> ids;
}
