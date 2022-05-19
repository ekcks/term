package com.bips.reserve.repository.custom;

import com.bips.reserve.domain.entity.AvailableDate;
        import com.bips.reserve.domain.entity.AvailableTime;
import com.bips.reserve.domain.entity.Btable;
import com.bips.reserve.domain.entity.ReserveItem;

import java.util.List;

public interface ReserveItemCustomRepository {

    /**
     * 예약가능 날짜 조회
     */
    List<AvailableDate> findAvailableDatesByBrestId(Long id);

    /**
     * 예약가능 시간 조회
     */
    List<AvailableTime> findAvailableTimesByAvailableDateId(Long id);


    /**
     * 예약가능 테이블 이름 조회
     */
    List<Btable> findAvailableBtables(Long brestId);

    /**
     * 예약 현황 조회
     */
    List<ReserveItem> findAllReserveItem(Long brestId);


}