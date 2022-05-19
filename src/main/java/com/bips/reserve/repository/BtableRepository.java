package com.bips.reserve.repository;

        import com.bips.reserve.domain.entity.Btable;
        import com.bips.reserve.repository.custom.BtableCustomRepository;
        import org.springframework.data.jpa.repository.JpaRepository;

public interface BtableRepository extends JpaRepository<Btable, Long>, BtableCustomRepository {

}