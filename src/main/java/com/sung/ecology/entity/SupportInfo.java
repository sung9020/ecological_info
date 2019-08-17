package com.sung.ecology.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
@Builder
@Entity
public class SupportInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String localGovernmentCode;

    @Column
    private String supportTarget;

    @Column
    private String usage;

    @Column
    private String supportLimit;

    @Column
    private String interestSubsidy;

    @Column
    private String institutions;

    @Column
    private String management;

    @Column
    private String dealer;
    @Column
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column
    @UpdateTimestamp
    private LocalDateTime updateDateTime;




}
