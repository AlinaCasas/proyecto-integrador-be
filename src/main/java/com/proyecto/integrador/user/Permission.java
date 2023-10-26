package com.proyecto.integrador.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MOD_READ("mod:read"),
    MOD_UPDATE("mod:update"),
    MOD_CREATE("mod:create"),
    MOD_DELETE("mod:delete")

    ;

    @Getter
    private final String permission;
}
