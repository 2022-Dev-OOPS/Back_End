package com.DevOOPS.barrier.DTO;

public class ToIoTDTO {
    private int status;
    private String message;
    private Object data;

    public ToIoTDTO(int status, String message, Object data) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public ToIoTDTO(int status, String message) {
        this.status = status;
        this.message = message;

        this.status = status;
    }
}