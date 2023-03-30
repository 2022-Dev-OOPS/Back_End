package com.DevOOPS.barrier.DTO;

public class ToIoTDataDTO {
    private int status;
    private String message;
    private Object data;

    public ToIoTDataDTO(int status, String message, Object data) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public ToIoTDataDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    ToIoTDataDTO(int status){
        this.status = status;
    }
}
