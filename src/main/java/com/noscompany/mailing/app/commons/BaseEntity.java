package com.noscompany.mailing.app.commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;

    @Version
    protected Integer version;

    protected BaseEntity() { }

    public boolean isPersisted() {
        return id != null;
    }

    public boolean isNotPersisted() {
        return id == null;
    }
}

