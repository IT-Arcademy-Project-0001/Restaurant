package com.project.Restaurant.Member;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    SELLER("ROLE_SELLER)"),
    CUSTOMER("ROLE_CUSTOMER");

    MemberRole(String value) {
        this.value = value;
    }

    private String value;
}
