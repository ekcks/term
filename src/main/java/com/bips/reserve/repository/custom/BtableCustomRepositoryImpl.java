package com.bips.reserve.repository.custom;

import com.bips.reserve.domain.entity.Btable;
        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.stereotype.Repository;

        import javax.persistence.EntityManager;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BtableCustomRepositoryImpl implements BtableCustomRepository{

    private final EntityManager em;

    @Override
    public Btable findBtable(Long brestId, String btableName) {
        return em.createQuery(
                        "select v " +
                                "from Btable v  join v.brest h " +
                                "where h.id = :brestId and v.btableName = :btableName and v.enabled = true", Btable.class
                )
                .setParameter("brestId", brestId)
                .setParameter("btableName", btableName)
                .getSingleResult();
    }

    @Override
    public Btable findBtableDisabled(Long brestId, String btableName) {
        return em.createQuery(
                        "select v " +
                                "from Btable v  join v.brest h " +
                                "where h.id = :brestId and v.btableName = :btableName", Btable.class
                )
                .setParameter("brestId", brestId)
                .setParameter("btableName", btableName)
                .getSingleResult();
    }
}