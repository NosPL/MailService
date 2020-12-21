package fakes;

import com.noscompany.mailing.app.commons.dto.EmailDto;
import com.noscompany.mailing.app.synchronous.mailing.MailService;

public class AlwaysFailMailService implements MailService {

    @Override
    public MailingResult send(EmailDto emailDto) {
        throw new RuntimeException();
    }
}
