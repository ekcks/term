package com.bips.reserve.repository.custom;

        import com.bips.reserve.domain.entity.Brest;
        import com.bips.reserve.dto.brest.BrestListDto;
        import org.springframework.data.repository.query.Param;

        import java.util.List;
        import java.util.Optional;

public interface BrestCustomRepository {

    List<BrestListDto> findAllBrestInfo(Long id);

    Optional<Brest> findBrestDetail(Long id);

    List<BrestListDto> findBrestListPaging(int offset, int limit);

    List<BrestListDto> findBrestListByAddressPaging(int offset, int limit, @Param("address") String address);

    List<BrestListDto> findBrestListByAddressAndAdmin(@Param("address") String address, Long adminId);

}