package com.noscompany.mailing.app.retryable.mailing.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface EmailRepo extends JpaRepository<Email, Long> {

    @Query(value = "select m from Email m left join fetch m.receivers where m.status = :status")
    Set<Email> findAllByStatus(@Param("status") Email.Status status);

    @Query(value = "select m from Email m left join fetch m.receivers")
    Set<Email> findAllEager();

    @Query(value = "select m from Email m left join fetch m.receivers where m.status in :set order by m.createdOn")
    List<Email> findByStatusOrderByCreationDate(@Param("set") Set<Email.Status> statuses, Pageable pageable);
}