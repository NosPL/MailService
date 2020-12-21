package com.noscompany.mailing.app.commons.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReceiverDto {
    @Email @NonNull String address;
}
