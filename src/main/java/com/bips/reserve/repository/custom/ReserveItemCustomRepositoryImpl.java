package com.bips.reserve.repository.custom;

import com.bips.reserve.domain.entity.Btable;
import com.bips.reserve.domain.entity.AvailableDate;
import com.bips.reserve.domain.entity.AvailableTime;
import com.bips.reserve.domain.entity.ReserveItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReserveItemCustomRepositoryImpl implements ReserveItemCustomRepository {

    private final EntityManager em;

    @Override
    public List<AvailableDate> findAvailableDatesByBrestId(Long id) {
        return em.createQuery(
                        "select d " +
                                "from AvailableDate d " +
                                "where d.brest.id = :id and d.enabled = true", AvailableDate.class
                )
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<AvailableTime> findAvailableTimesByAvailableDateId(Long id) {
        return em.createQuery(
                        "select t " +
                                "from AvailableTime  t " +
                                "where t.availableDate.id = :id and t.enabled = true" , AvailableTime.class
                )
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Btable> findAvailableBtables(Long brestId) {
        return em.createQuery(
                        "select v " +
                                "from Btable v " +
                                "where v.brest.id = :brestId and v.quantity > 0 and v.enabled = true", Btable.class
                )
                .setParameter("brestId", brestId)
                .getResultList();
    }

    @Override
    public List<ReserveItem> findAllReserveItem(Long brestId){
        return em.createQuery(
                        "select distinct ri " +
                                "from ReserveItem ri " +
                                "join fetch ri.user u " +
                                "where ri.Brest.id = :brestId"
                        ,ReserveItem.class)
                .setParameter("brestId",brestId)
                .getResultList();
    }
}