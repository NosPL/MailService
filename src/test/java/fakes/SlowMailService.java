package fakes;

import com.noscompany.mailing.app.commons.dto.EmailDto;
import com.noscompany.mailing.app.synchronous.mailing.MailService;
import lombok.SneakyThrows;
import lombok.Value;

import java.util.concurrent.TimeUnit;

import static com.noscompany.mailing.app.synchronous.mailing.MailService.MailingResult.SUCCESS;

@Value
public class SlowMailService implements MailService {
    int value;
    TimeUnit timeUnit;

    @Override
    @SneakyThrows
    public MailingResult send(EmailDto emailDto) {
        timeUnit.sleep(value);
        return SUCCESS;
    }
}
