package com.bips.reserve.service.admin;


import com.bips.reserve.dto.brest.*;
        import com.bips.reserve.dto.reserve.ReserveItemWithUsernameDto;

        import java.text.ParseException;
        import java.util.List;

public interface AdminService {

    /**
     * 레스토랑등록
     */
    Long addBrest(BrestRequestDto brestAddDto,String adminName) throws Exception;


    /**
     * 래스토랑 이름으로 레스토랑 단건 조회
     */
    BrestResponseDto getBrestInfo(String brestName);

    /**
     * 어드민이 관리하는 레스토랑 리스트를 보여주기 위한 메서드
     */
    List<BrestSimpleInfoDto> getAllSimpleBrestInfo(String name);

    List<BrestListDto> getBrestList(String name,String address);

    /**
     * 레스토랑 상세 정보 조회 후 dto로 변환
     */
    BrestUpdateDto getBrest(Long id);

    /**
     * 레스토랑 update
     */
    Long brestUpdate(BrestUpdateDto dto) throws ParseException;

    /**
     * 예약 현황 정보
     */
    List<ReserveItemWithUsernameDto> getReserveItemCondition(Long brestId);
}