package com.sung.local.enums;

import lombok.Getter;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN");

    @Getter
    private final String role;

    Role(String role){
        this.role = role;
    }
}
