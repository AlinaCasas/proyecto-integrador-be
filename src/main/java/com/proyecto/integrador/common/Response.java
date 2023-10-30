package com.proyecto.integrador.common;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class Response<T> {
    T data;
    String message;
    boolean success;

    public Response(T data, String message, boolean success) {
        this.data = data;
        this.success = success;
        this.message = message;
    }
}
