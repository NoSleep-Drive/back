package com.nosleepdrive.nosleepdrivebackend.common;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    @Min(0)
    private Integer pageIdx = 0;
    @Min(1)
    private Integer pageSize = 5;
}
