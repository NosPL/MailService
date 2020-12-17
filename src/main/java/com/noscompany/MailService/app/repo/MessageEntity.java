package com.noscompany.MailService.app.repo;

import com.noscompany.MailService.app.commons.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class MessageEntity extends BaseEntity {
    private String subject;
    private String content;
    private String uuid;
    private LocalDateTime createdAt;

    public MessageEntity(String subject, String content, String uuid) {
        this.subject = subject;
        this.content = content;
        this.uuid = UUID.randomUUID().toString();
    }

    @OneToMany(mappedBy = "mail")
    private Set<Address> addresses;
}
