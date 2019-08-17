package com.sung.local.enums;

import lombok.Getter;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
public enum FileFormat {
    INDEX(0, "구분"),
    LOCAL_GOVERNMENT(1, "지차체명(기관명)"),
    SUPPORT_TARGET(2,"지원대상"),
    USAGE(3, "용도"),
    SUPPORT_LIMIT(4,"지원한도"),
    INTEREST_SUBSIDY(5, "이차보전"),
    INSTITUTIONS(6,"추천기관"),
    MANAGEMENT(7,"관리점"),
    DEALER(8,"취급점");

    @Getter
    private final int col;

    @Getter
    private final String name;

    FileFormat(int col, String name){
        this.col = col;
        this.name = name;

    }
}
