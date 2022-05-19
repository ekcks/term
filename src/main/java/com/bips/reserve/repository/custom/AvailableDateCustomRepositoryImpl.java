package com.bips.reserve.repository.custom;


import com.bips.reserve.domain.entity.AvailableDate;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Repository;

        import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AvailableDateCustomRepositoryImpl implements AvailableDateCustomRepository{

    private final EntityManager em;

    @Override
    public AvailableDate findAvailableDateByBrestIdAndDate(Long brestId, String date) {
        return em.createQuery(
                        "select d " +
                                "from AvailableDate d " +
                                "where d.brest.id = :brestId and d.date = :date", AvailableDate.class
                )
                .setParameter("brestId", brestId)
                .setParameter("date", date)
                .getSingleResult();

    }
}