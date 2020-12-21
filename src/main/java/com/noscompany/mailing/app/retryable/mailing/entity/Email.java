package com.noscompany.mailing.app.retryable.mailing.entity;

import com.noscompany.mailing.app.commons.BaseEntity;
import com.noscompany.mailing.app.retryable.mailing.properties.AttemptLimit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.noscompany.mailing.app.retryable.mailing.entity.Email.Status.*;

@Entity
@EqualsAndHashCode(of = "uuid", callSuper = false)
@DynamicUpdate
@Getter
public class Email extends BaseEntity {

    public enum Status {
        NEW, FOR_RESENDING, SENT, DISCARDED
    }

    private String uuid = UUID.randomUUID().toString();

    private String sender;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Enumerated
    @Column(nullable = false)
    private Status status = NEW;

    private Integer sendingAttempts = 0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "email_id")
    private Set<Receiver> receivers = new HashSet<>();

    protected Email() {
    }

    Email(String sender, String subject, String content, LocalDateTime createdAt, Set<Receiver> receivers) {
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.createdOn = createdAt;
        this.receivers = receivers;
    }

    public void markAsSent() {
        sendingAttempts += 1;
        this.status = SENT;
    }

    public void failedToSend(AttemptLimit limit) {
        sendingAttempts += 1;
        if (sendingAttempts >= limit.value())
            this.status = DISCARDED;
        else
            this.status = FOR_RESENDING;
    }

    public boolean isNew() {
        return this.status == NEW;
    }

    public boolean isSent() {
        return this.status == SENT;
    }

    public boolean isDiscarded() {
        return this.status == DISCARDED;
    }

    public boolean isScheduledForResending() {
        return this.status == FOR_RESENDING;
    }

    void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", getId())
                .append("version", getVersion())
                .append("uuid", uuid)
                .append("subject", subject)
                .append("content", content)
                .append("createdAt", createdOn)
                .append("status", status)
                .append("sendAttempts", sendingAttempts)
                .append("receivers", receivers)
                .toString();
    }
}