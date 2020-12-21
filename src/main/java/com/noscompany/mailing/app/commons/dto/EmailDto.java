package com.noscompany.mailing.app.commons.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailDto {
    @NonNull
    String senderName;
    @NonNull
    @Length(min = 0, max = 100)
    String subject;
    @NonNull
    @Length(min = 0, max = 2000)
    String content;
    @NonNull
    Set<ReceiverDto> receivers;
}
