package com.bips.reserve.repository;

        import com.bips.reserve.domain.entity.Admin;
        import com.bips.reserve.domain.entity.Brest;
        import com.bips.reserve.dto.brest.BrestSimpleInfoDto;
        import com.bips.reserve.repository.custom.BrestCustomRepository;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;

        import java.util.List;
        import java.util.Optional;

public interface BrestRepository extends JpaRepository<Brest, Long>, BrestCustomRepository {

    Optional<Brest> findByBrestName(String brestName);

    /**
     *  BrestSimpleInfoDto 를 이용한 모든 레스토랑의 이름, 주소 조회
     *  @return BrestSimpleInfoDto
     */
    @Query("select new com.bips.reserve.dto.brest.BrestSimpleInfoDto(h.brestName,h.address) " +
            "from Brest h " +
            "where h.enabled = true")
    List<BrestSimpleInfoDto> findAllBrestNameAndAddress();

    /**
     * 어드민이 관리하는 모든 레스토랑 정보 조회 (병원이름, 장소)
     * 어드민이 등록한 모든 레스토랑의 간단한 정보만을 조회하기 위한 쿼리
     */
    @Query("select new com.bips.reserve.dto.brest.BrestSimpleInfoDto(h.brestName, h.address) " +
            "from Brest h " +
            "where h.admin = :admin")
    List<BrestSimpleInfoDto> findAllByAdmin(Admin admin);
}