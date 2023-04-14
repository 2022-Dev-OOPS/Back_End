package com.DevOOPS.barrier.DTO;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class WallDTO {
    private boolean wallAlertActivation;
    private boolean wallAlertDeactivation;
    private int stnId;

    public WallDTO(boolean wallAlertActivation, boolean wallAlertDeactivation, int stnId) {
        this.wallAlertActivation = wallAlertActivation;
        this.wallAlertDeactivation = wallAlertDeactivation;
        this.stnId = stnId;
    }
}
