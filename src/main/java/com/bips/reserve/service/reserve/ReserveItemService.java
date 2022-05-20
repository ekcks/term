package com.bips.reserve.service.reserve;


import com.bips.reserve.dto.brest.BrestListDto;
import com.bips.reserve.dto.btable.BtableReserveDto;
        import com.bips.reserve.dto.reserve.AvailableDateDto;
        import com.bips.reserve.dto.reserve.AvailableTimeDto;
        import com.bips.reserve.dto.reserve.ReserveItemSimpleDto;

        import java.util.List;

public interface ReserveItemService {

    List<BrestListDto> getAllBrestInfo(int offset, int limit);

    List<BrestListDto> getAllBrestInfoSearchByAddress(String address, int offset, int limit);

    List<AvailableDateDto> getAvailableDates(Long brestId);

    List<AvailableTimeDto> getAvailableTimes(Long id);

    List<BtableReserveDto> getAvailableBtableNameList(Long brestId);

    Long reserve(String username, Long brestId, String btableName, Long dateId, Long timeId);

    ReserveItemSimpleDto getReserveResult(String username);

    void validateDuplicateUser(String username);

    void cancelReserveItem(Long reserveItemId);
}