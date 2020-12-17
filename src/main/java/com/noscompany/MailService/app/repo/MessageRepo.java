package com.noscompany.MailService.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<MessageEntity, Long> {
}
