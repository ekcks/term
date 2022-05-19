package com.bips.reserve.dto.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReserveItemRequestDto {
    private Long brestId;
    private String btableName;
    private Long reserveDateId;
    private Long reserveTimeId;
}