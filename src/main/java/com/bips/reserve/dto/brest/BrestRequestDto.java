
package com.bips.reserve.dto.brest;

import com.bips.reserve.domain.entity.Brest;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 어드민으로부터 레스토랑 등록 요청을 처리하기 위한 DTO
 */

@Data
@NoArgsConstructor
public class BrestRequestDto {
    @NotEmpty(message = "레스토랑 이름을 입력해주세요.")
    private String brestName;

    @NotEmpty(message = "예약가능 시작날짜를 선택해주세요.")
    private String startDate;
    @NotEmpty(message = "예약가능 종료날짜를 선택해주세요.")
    private String endDate;
    @NotNull(message = "일일 최대 예약가능 인원을 입력해주세요.")
    private Integer dateAccept;

    @NotNull(message = "예약가능 시작시간을 선택해주세요.")
    private String startTime;
    @NotNull(message = "예약가능 종료시간을 선택해주세요.")
    private String endTime;
    @NotNull(message = "시간당 최대 예약가능 인원을 입력해주세요.")
    private Integer timeAccept;

    @NotEmpty(message = "레스토랑 주소를 입력해주세요.")
    private String address;
    @NotEmpty(message = "레스토랑 상세주소를 입력해주세요.")
    private String detailAddress;



    private List<String> btableNames = new ArrayList<>();

    private List<Integer> btableQuantities = new ArrayList<>();

    @NotNull(message = "테이블 수를 입력해주세요.")
    private Integer seat2;
    @NotNull(message = "테이블 수를 입력해주세요.")
    private Integer seat4;
    @NotNull(message = "테이블 수를 입력해주세요.")
    private Integer seat6;
    @NotNull(message = "테이블 수를 입력해주세요.")
    private Integer seat8;

    // 테이블마다 잔여좌석을 달리하기 위해 Map 사용 (key:테이블이름, value:잔여좌석)
    private Map<String, Integer> btableInfoMap = new HashMap<>();

    public Brest toBRestEntity() {
        return Brest.createBRest()
                .brestName(this.brestName)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .dateAccept(dateAccept)
                .timeAccept(timeAccept)
                .build();
    }

    @Builder(builderMethodName = "createBrestRequestDto")
    public BrestRequestDto(String brestName, String startDate, String endDate, Integer dateAccept,
                           String startTime, String endTime, Integer timeAccept, String address,
                           String detailAddress, Integer seat2,
                           Integer seat4, Integer seat6, Integer seat8) {
        this.brestName = brestName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateAccept = dateAccept;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeAccept = timeAccept;
        this.address = address;
        this.detailAddress = detailAddress;
        this.seat2 = seat2;
        this.seat4 = seat4;
        this.seat6 = seat6;
        this.seat8 = seat8;

    }

}