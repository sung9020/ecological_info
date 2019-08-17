package com.sung.local.enums;

import lombok.Getter;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
public enum Unit {
    억원("억원",100000000),
    천만원("천만원",10000000),
    백만원("백만원", 1000000);

    @Getter
    private final String unit;

    @Getter
    private final int won;

    Unit(String unit, int won){
        this.unit = unit;
        this.won = won;

    }
}
