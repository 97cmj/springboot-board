package com.cmj.myproject.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

        ADMIN("ADMIN", "관리자"),
        MEMBER("MEMBER", "회원");

        private final String key;
        private final String title;
}