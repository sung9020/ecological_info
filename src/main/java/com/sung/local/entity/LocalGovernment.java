package com.sung.local.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
@Setter
@Entity
public class LocalGovernment {

    @Id
    @Column(unique = true)
    private String region;

    @Column(unique = true)
    private String regionCode;
}
