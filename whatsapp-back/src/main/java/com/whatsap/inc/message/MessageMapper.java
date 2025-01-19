package com.whatsap.inc.message;

import com.whatsap.inc.file.FileUtils;

public class MessageMapper {
    private MessageMapper() {}

    public static MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .type(message.getType())
                .state(message.getState())
                .createdAt(message.getCreatedDate())
                .media(FileUtils.readFileFromLocation(message.getMediaFilePath()))
                .build();
    }

    public static Message toMessage(MessageRequest request) {
        Message message = new Message();
        message.setContent(request.getContent());
        message.setSenderId(request.getSenderId());
        message.setReceiverId(request.getReceiverId());
        message.setType(request.getType());
        return message;

    }
}
