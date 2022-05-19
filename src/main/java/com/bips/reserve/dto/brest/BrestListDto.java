
package com.bips.reserve.dto.brest;

        import lombok.AllArgsConstructor;
        import lombok.Data;

@AllArgsConstructor
@Data
public class BrestListDto {

    private Long id;

    private String brestName;

    private String address;

    private Integer qty;
}