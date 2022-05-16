package com.bips.reserve.repository.custom;

import com.bips.reserve.domain.entity.AvailableDate;

public interface AvailableDateCustomRepository {

    AvailableDate findAvailableDateByHospitalIdAndDate(Long hospitalId, String date);
}