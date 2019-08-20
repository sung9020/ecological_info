package com.sung.local.repository;

import com.sung.local.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
