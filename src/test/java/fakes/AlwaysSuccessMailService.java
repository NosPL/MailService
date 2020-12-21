package fakes;

import com.noscompany.mailing.app.commons.dto.EmailDto;
import com.noscompany.mailing.app.synchronous.mailing.MailService;

import static com.noscompany.mailing.app.synchronous.mailing.MailService.MailingResult.SUCCESS;

public class AlwaysSuccessMailService implements MailService {

    @Override
    public MailingResult send(EmailDto emailDto) {
        return SUCCESS;
    }
}
