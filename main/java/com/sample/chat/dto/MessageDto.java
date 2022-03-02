package com.sample.chat.dto;

import com.sample.chat.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String text;

    private Attachment attachment;

    private UserDto sender;
}
