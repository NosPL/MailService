package sample.data;

import com.noscompany.mailing.app.commons.dto.EmailDto;
import com.noscompany.mailing.app.commons.dto.ReceiverDto;

import java.util.Random;
import java.util.Set;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toSet;

public class SampleData {

    private static final Random random = new Random();

    public static EmailDto sampleEmailDto() {
        return sampleMessageTo(randomReceivers());
    }

    public static EmailDto sampleMessageTo(Set<ReceiverDto> receivers) {
        return new EmailDto(
                randomUUID().toString().substring(0,5),
                "subject: " + randomUUID().toString().substring(0, 5),
                "content: " + randomUUID().toString().substring(0, 5),
                receivers);
    }

    public static Set<ReceiverDto> randomReceivers() {
        int i = random.nextInt(3);
        if (i == 0)
            return toDto(Set.of("first@o2.pl", "second@o2.pl", "third@o2.pl", "fourth@cos.pl"));
        else if (i == 1)
            return toDto(Set.of("fifth@o2.pl", "sixth@o2.pl", "seventh@o2.pl", "eith@cos.pl"));
        else
            return toDto(Set.of("neinth@o2.pl", "tenth@o2.pl", "eleventh@o2.pl", "twelfth@cos.pl"));
    }

    private static Set<ReceiverDto> toDto(Set<String> receivers) {
        return receivers
                .stream()
                .map(ReceiverDto::new)
                .collect(toSet());
    }
}