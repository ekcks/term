package com.bips.reserve.dto.reserve;

import com.bips.reserve.domain.entity.ReserveItem;
        import com.bips.reserve.domain.value.ReserveStatus;
        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReserveItemWithUsernameDto {

    private Long reserveItemId;

    private String username;

    private String brestName;

    private String btableName;

    private String reserveDate;

    private Integer reserveTime;

    private ReserveStatus reserveStatus;

    public ReserveItemWithUsernameDto(ReserveItem reserveItem){
        this.reserveItemId = reserveItem.getId();
        this.username = reserveItem.getUser().getName();
        this.brestName = reserveItem.getBrest().getBrestName();
        this.btableName = reserveItem.getBtableName();
        this.reserveDate = reserveItem.getReserveDate();
        this.reserveTime = reserveItem.getReserveTime();
        this.reserveStatus = reserveItem.getStatus();
    }

}