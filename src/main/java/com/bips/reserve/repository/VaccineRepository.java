package com.bips.reserve.repository;

import com.bips.reserve.domain.entity.Vaccine;
        import com.bips.reserve.repository.custom.VaccineCustomRepository;
        import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.Optional;

public interface VaccineRepository extends JpaRepository<Vaccine, Long>, VaccineCustomRepository {

}