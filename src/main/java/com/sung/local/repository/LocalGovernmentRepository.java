package com.sung.local.repository;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */

import com.sung.local.entity.LocalGovernment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalGovernmentRepository extends JpaRepository<LocalGovernment, String> {
    LocalGovernment findByRegion(String region);
    LocalGovernment findByRegionCode(String regionCode);

}
