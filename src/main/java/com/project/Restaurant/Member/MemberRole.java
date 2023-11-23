package com.project.Restaurant.Member;

import lombok.Getter;

@Getter
public enum MemberRole {

    ADMIN("관리자"),
    OWNER("사장님"),
    CUSTOMER("소비자");

    MemberRole(String value) {
        this.value = value;
    }

    private String value;
}