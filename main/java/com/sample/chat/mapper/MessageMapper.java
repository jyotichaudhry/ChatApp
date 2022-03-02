package com.sample.chat.mapper;

import com.sample.chat.entity.Attachment;
import com.sample.chat.entity.Message;
import com.sample.chat.dto.MessageDto;
import com.sample.chat.entity.User;
import com.sample.chat.model.MessageRequest;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public static Message toMessage(MessageRequest request, User sender, User recipient, Attachment attachment) {
        return Message.builder()
                .attachment(attachment)
                .recipientUser(recipient)
                .senderUser(sender)
                .text(request.getText())
                .build();
    }

    public static MessageDto toDto(Message message) {
        return MessageDto.builder()
                .attachment(message.getAttachment())
                .sender(UserMapper.toDto(message.getSenderUser()))
                .text(message.getText())
                .build();
    }
}
