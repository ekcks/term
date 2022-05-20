package com.bips.reserve.dto.brest;


import com.bips.reserve.domain.entity.Brest;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class BrestUpdateDto {

    private Long id;

    private String brestName;

    private String startDate;

    private String endDate;

    @NotNull(message = "일일 최대 예약가능 인원을 입력해주세요.")
    private Integer dateAccept;

    private String startTime;

    private String endTime;
    @NotNull(message = "시간당 최대 예약가능 인원을 입력해주세요.")
    private Integer timeAccept;

    private String address;
    private String detailAddress;


    @NotNull(message = "좌석 수를 입력해주세요.")
    private Integer seat2;
    @NotNull(message = "좌석 수을 입력해주세요.")
    private Integer seat4;
    @NotNull(message = "좌석 수을 입력해주세요.")
    private Integer seat6;
    @NotNull(message = "좌석 수을 입력해주세요.")
    private Integer seat8;

    // 테이블마다 잔여수량을 달리하기 위해 Map 사용 (key:테이블이름, value:잔여 좌석)
    private Map<String, Integer> btableInfoMap = new HashMap<>();

    public Brest toBrestEntity() {
        return Brest.createBRest()
                .brestName(this.brestName)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .build();
    }

    @Builder(builderMethodName = "createBrestUpdateDto")
    public BrestUpdateDto(Long id,String brestName, String startDate, String endDate, Integer dateAccept,
                          String startTime, String endTime, Integer timeAccept, String address,
                          String detailAddress, Integer seat2,
                          Integer seat4, Integer seat6, Integer seat8) {
        this.id=id;
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