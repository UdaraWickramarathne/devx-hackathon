package com.zenvest.devx.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionType {
    DEPOSIT,
    WITHDRAWAL;

    @JsonCreator
    public static TransactionType fromString(String value) {
        return TransactionType.valueOf(value.toUpperCase());
    }
}
