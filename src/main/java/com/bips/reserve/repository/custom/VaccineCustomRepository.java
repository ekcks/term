package com.bips.reserve.repository.custom;

import com.bips.reserve.domain.entity.Vaccine;

public interface VaccineCustomRepository {

    Vaccine findVaccine(Long hospitalId, String vaccineName);

    Vaccine findVaccineDisabled(Long hospitalId, String vaccineName);
}