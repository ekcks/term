package com.bips.reserve.domain.value;

import lombok.Getter;

@Getter
public enum ReserveStatus {

    예약완료("예약완료"), 예약취소("예약취소");

    private String description;

    ReserveStatus(String description) {
        this.description = description;
    }
}