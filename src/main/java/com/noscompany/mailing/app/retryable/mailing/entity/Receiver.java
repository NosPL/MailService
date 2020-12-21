package com.noscompany.mailing.app.retryable.mailing.entity;

import com.noscompany.mailing.app.commons.BaseEntity;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.Email;

import static lombok.AccessLevel.PROTECTED;

@Entity
@EqualsAndHashCode(of = "address", callSuper = false)
public class Receiver extends BaseEntity {

    @Getter
    @Email(message = "Incorrect email format")
    private String address;

    protected Receiver() {}

    public Receiver(@NonNull String address) {
        this.address = address.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", getId())
                .append("version", getVersion())
                .append("email", address)
                .toString();
    }
}
