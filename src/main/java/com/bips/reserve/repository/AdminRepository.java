package com.bips.reserve.repository;

import com.bips.reserve.domain.entity.Admin;
        import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByName(String name);
}