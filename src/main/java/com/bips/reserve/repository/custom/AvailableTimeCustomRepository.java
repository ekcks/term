package com.bips.reserve.repository.custom;

import com.bips.reserve.domain.entity.AvailableTime;

public interface AvailableTimeCustomRepository {

    AvailableTime findAvailableTimeById(Long timeId);

    AvailableTime findAvailableTimeByTimeAndDateId(Integer time, Long dateId);
}