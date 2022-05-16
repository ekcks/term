package com.bips.reserve.dto.reserve;

import com.bips.reserve.domain.value.ReserveStatus;
        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveItemSimpleDto {

    private Long reserveItemId;

    private String hospitalName;

    private String vaccineName;

    private String reserveDate;

    private Integer reserveTime;

    private ReserveStatus reserveStatus;
}