package com.sung.local.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String password;
}
