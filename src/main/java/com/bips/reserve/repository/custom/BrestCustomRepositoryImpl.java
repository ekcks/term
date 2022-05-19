package com.bips.reserve.repository.custom;

        import com.bips.reserve.domain.entity.Brest;
        import com.bips.reserve.dto.brest.BrestListDto;
        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.data.repository.query.Param;
        import org.springframework.stereotype.Repository;

        import javax.persistence.EntityManager;
        import java.util.List;
        import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BrestCustomRepositoryImpl implements BrestCustomRepository{

    private final EntityManager em;

    @Override
    public List<BrestListDto> findAllBrestInfo(Long id) {
        return em.createQuery(
                "select new com.bips.reserve.dto.brest.BrestListDto(h.id, h.brestName, h.address, h.totalQuantity) " +
                        "from Brest h " +
                        "where h.admin.id = :id"
                , BrestListDto.class).setParameter("id",id).getResultList();
    };

    /**
     * 레스토랑 아이디로 레스토랑 정보 조회
     */
    @Override
    public Optional<Brest> findBrestDetail(Long id){
        return Optional.of(em.createQuery(
                        "select distinct h from Brest h " +
                                "join fetch h.admin a " +
                                "join fetch h.Btables v " +
                                "where h.id= :id", Brest.class)
                .setParameter("id",id).getSingleResult());
    }

    /**
     * 예약가능 레스토랑 조회 + 페이징
     */
    @Override
    public List<BrestListDto> findBrestListPaging(int offset, int limit) {
        return em.createQuery(
                        "select new com.bips.reserve.dto.brest.BrestListDto(h.id, h.brestName, h.address, h.totalQuantity) " +
                                "from Brest h " +
                                "where h.enabled = true", BrestListDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 주소로 예약가능 레스토랑 조회 + 페이징
     */
    @Override
    public List<BrestListDto> findBrestListByAddressPaging(int offset, int limit, @Param("address") String address) {
        return em.createQuery(
                        "select new com.bips.reserve.dto.brest.BrestListDto(h.id, h.brestName, h.address, h.totalQuantity) " +
                                "from Brest h " +
                                "where h.enabled = true and h.address like '%'||:address||'%'", BrestListDto.class)
                .setParameter("address", address)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 주소, admin으로 레스토랑 조회
     */
    @Override
    public List<BrestListDto> findBrestListByAddressAndAdmin(@Param("address") String address, Long adminId) {
        return em.createQuery(
                        "select new com.bips.reserve.dto.brest.BrestListDto(h.id, h.brestName, h.address, h.totalQuantity) " +
                                "from Brest h " +
                                "where h.admin.id= :adminId and h.address like '%'||:address||'%'", BrestListDto.class)
                .setParameter("address", address)
                .setParameter("adminId",adminId)
                .getResultList();
    }
}