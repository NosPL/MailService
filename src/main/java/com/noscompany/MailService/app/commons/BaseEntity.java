package com.noscompany.MailService.app.commons;

import lombok.Getter;

import javax.persistence.*;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    protected BaseEntity() {
    }

    public boolean isPersisted() {
        return id != null;
    }

    public boolean isNotPersisted() {
        return id == null;
    }
}
