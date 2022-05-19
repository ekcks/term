
package com.bips.reserve.dto.brest;

        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

/**
 * 레스토랑 조회 결과를 위한 dto
 * json 포맷을 빈 값 없이 맞추기 위해 BRestRequestDto와 따로 사용
 */

@Data
@NoArgsConstructor
public class BrestResponseDto {
    private String brestName;
    private List<String> availableDates;
    private String address;
    private String detailAddress;

    private Map<String, Integer> btableInfoMap = new HashMap<>();

    public BrestResponseDto createDto(String brestName, List<String> availableDates,
                                      String address, String detailAddress, Map<String, Integer> btableInfoMap) {
        this.brestName = brestName;
        this.availableDates = availableDates;
        this.address = address;
        this.detailAddress = detailAddress;
        this.btableInfoMap = btableInfoMap;

        return this;
    }
}