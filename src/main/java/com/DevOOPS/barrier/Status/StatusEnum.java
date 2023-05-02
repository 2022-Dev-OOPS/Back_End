package com.DevOOPS.barrier.Status;


import jdk.jfr.EventType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum StatusEnum {

    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

    int statusCode;
    String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getCode() {
        return code;
    }

    private static final Map<Integer, StatusEnum> lookup = new HashMap<>();

    static {
        for(StatusEnum e : EnumSet.allOf(StatusEnum.class)) {
            lookup.put(e.getStatusCode(), e);
        }
    }

    public static StatusEnum of(int code) {
        return lookup.get(code);
    }

}
