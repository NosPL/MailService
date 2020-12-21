package com.noscompany.mailing.app.retryable.mailing.service;

import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.async.mailing.service.MailServiceCreator;
import com.noscompany.mailing.app.async.mailing.service.MailingFuture;
import com.noscompany.mailing.app.commons.dto.EmailDto;
import com.noscompany.mailing.app.retryable.mailing.entity.Email;
import com.noscompany.mailing.app.retryable.mailing.entity.NewEmail;
import com.noscompany.mailing.app.retryable.mailing.entity.EmailRepo;
import com.noscompany.mailing.app.retryable.mailing.properties.AttemptLimit;
import com.noscompany.mailing.app.retryable.mailing.properties.BatchSize;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.noscompany.mailing.app.retryable.mailing.entity.Email.Status.FOR_RESENDING;
import static com.noscompany.mailing.app.retryable.mailing.entity.Email.Status.NEW;
import static com.noscompany.mailing.app.retryable.mailing.service.EmailMapper.asDto;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class RetryableMailingService {
    private EmailRepo emailRepo;
    private AsyncMailingService asyncMailingService;
    private BatchSize batchSize;
    @Getter @Setter
    private AttemptLimit attemptLimit;
    private Clock clock;

    public Long send(EmailDto dto) {
        LocalDateTime creationDate = LocalDateTime.now(clock);
        Email mail = emailRepo.save(NewEmail.createFrom(dto, creationDate));
        return mail.getId();
    }

    @Transactional
    public BatchResult sendPendingBatch() {
        List<AsyncMailingHandler> handlers = sendAsync(oldestPendingEmails());
//        no need to save processed emails - JPA Dirty checking (@Transactional)
        List<Email> processedEmails = waitForAllToFinish(handlers);
        return BatchResult.from(processedEmails);
    }

    private List<Email> waitForAllToFinish(List<AsyncMailingHandler> handlers) {
        return handlers.stream()
                .map(AsyncMailingHandler::waitForFinish)
                .collect(toList());
    }

    private List<AsyncMailingHandler> sendAsync(List<Email> emailsToSend) {
        return emailsToSend.stream()
                .flatMap(this::sendAsync)
                .collect(toList());
    }

    private Stream<AsyncMailingHandler> sendAsync(Email email) {
        Option<MailingFuture> opFuture = asyncMailingService.sendAsync(asDto(email));
        return opFuture
                .map(mailingFuture -> new AsyncMailingHandler(email, mailingFuture, attemptLimit))
                .toJavaStream();
    }

    private List<Email> oldestPendingEmails() {
        return emailRepo
                .findByStatusOrderByCreationDate(Set.of(FOR_RESENDING, NEW), PageRequest.of(0, getBatchSize()));
    }

    public void setMailServiceCreator(MailServiceCreator creator) {
        asyncMailingService.setMailServiceCreator(creator);
    }

    /**
     * batch size can be <= asyncMailingService.threadPoolMaxSize()
     * @param batchSize
     * @return actual batch size
     */
    public int trySetBatchSize(BatchSize batchSize) {
        this.batchSize = batchSize;
        int actualBatchSize = getBatchSize();
        return actualBatchSize;
    }

    public int getBatchSize() {
        return Math.min(batchSize.value(), asyncMailingService.threadPoolMaxSize());
    }
}