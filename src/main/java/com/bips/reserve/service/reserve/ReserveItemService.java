package com.bips.reserve.service.reserve;


import com.bips.reserve.dto.hospital.HospitalListDto;
        import com.bips.reserve.dto.reserve.AvailableDateDto;
        import com.bips.reserve.dto.reserve.AvailableTimeDto;
        import com.bips.reserve.dto.reserve.ReserveItemSimpleDto;
        import com.bips.reserve.dto.Vaccine.VaccineReserveDto;

        import java.util.List;

public interface ReserveItemService {

    List<HospitalListDto> getAllHospitalInfo(int offset, int limit);

    List<HospitalListDto> getAllHospitalInfoSearchByAddress(String address, int offset, int limit);

    List<AvailableDateDto> getAvailableDates(Long hospitalId);

    List<AvailableTimeDto> getAvailableTimes(Long id);

    List<VaccineReserveDto> getAvailableVaccineNameList(Long hospitalId);

    Long reserve(String username, Long hospitalId, String vaccineName, Long dateId, Long timeId);

    ReserveItemSimpleDto getReserveResult(String username);

    void validateDuplicateUser(String username);

    void cancelReserveItem(Long reserveItemId);
}