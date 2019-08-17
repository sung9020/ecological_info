package com.sung.local.repository;

import com.sung.local.entity.SupportInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Repository
public interface SupportInfoRepository extends JpaRepository<SupportInfo, Integer> {
    SupportInfo findByRegionCode(String regionCode);
}
