package com.DevOOPS.barrier.DTO;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class WallDTO {
    private boolean isCommandWall;
    private int stnId;

    public WallDTO(boolean isCommandWall, int stnId) {
        this.isCommandWall = isCommandWall;
        this.stnId = stnId;
    }


}
