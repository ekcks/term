package com.bips.reserve.repository;

import com.bips.reserve.domain.entity.ReserveItem;
        import com.bips.reserve.dto.reserve.ReserveItemSimpleDto;
        import com.bips.reserve.repository.custom.ReserveItemCustomRepository;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;

        import java.util.Optional;

public interface ReserveItemRepository extends JpaRepository<ReserveItem, Long>, ReserveItemCustomRepository {

    @Query("select new com.bips.reserve.dto.reserve.ReserveItemSimpleDto(ri.id, ri.Hospital.hospitalName, ri.vaccineName, ri.reserveDate, ri.reserveTime, ri.status) " +
            "from ReserveItem  ri " +
            "where ri.user.id = :userId")
    Optional<ReserveItemSimpleDto> findByUserId(Long userId);
}