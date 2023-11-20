package com.project.Restaurant.Member;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("관리자"),
    SELLER("영업주"),
    CUSTOMER("소비자");

    MemberRole(String value) {
        this.value = value;
    }

    private String value;
}