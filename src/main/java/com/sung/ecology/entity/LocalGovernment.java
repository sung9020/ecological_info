package com.sung.ecology.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
@Entity
public class LocalGovernment {

    @Id
    @Column(unique = true)
    private String localGovernmentName;

    @Column(unique = true)
    private String localGovernmentCode;
}
