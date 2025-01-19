package com.whatsap.inc.message;

import com.whatsap.inc.chat.Chat;
import com.whatsap.inc.chat.ChatRepository;
import com.whatsap.inc.file.FileService;
import com.whatsap.inc.file.FileUtils;
import com.whatsap.inc.notification.Notification;
import com.whatsap.inc.notification.NotificationService;
import com.whatsap.inc.notification.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;;
    private final ChatRepository chatRepository;
    private final FileService fileService;
    private final NotificationService notificationService;

    public void saveMessage(MessageRequest request) {
        Chat chat = getChat(request.getChatId());
        Message message = MessageMapper.toMessage(request);
        message.setChat(chat);
        message.setState(MessageState.SENT);
        messageRepository.save(message);

        //todo notification after message sent
        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(request.getType())
                .content(request.getContent())
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getTargetChatName(message.getSenderId()))
                .build();
         notificationService.sendNotification(request.getReceiverId(),notification);

    }

    public List<MessageResponse> findChatMessages(String chatId) {
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(MessageMapper::toMessageResponse)
                .toList();
    }
    @Transactional
    public void setMessageSeen(String chatId, Authentication authentication) {
        Chat chat = getChat(chatId);
        final String recipientId = getRecipientId(chat,authentication);
        messageRepository.setMessagesToSeenByChatId(chatId,MessageState.SEEN);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .type(NotificationType.SEEN)
                .receiverId(recipientId)
                .senderId(getSenderId(chat, authentication))
                .build();

        notificationService.sendNotification(recipientId, notification);

    }

    public void uploadMediaMessage(String chatId, MultipartFile file,Authentication authentication){
        Chat chat = getChat(chatId);
        final String senderId = getSenderId(chat,authentication);
        final String recipientId = getRecipientId(chat,authentication);
        final String filePath = fileService.saveFile(file,senderId);

        Message message = new Message();
        message.setChat(chat);
        message.setReceiverId(recipientId);
        message.setType(MessageType.IMAGE);
        message.setState(MessageState.SENT);
        message.setMediaFilePath(filePath);
        messageRepository.save(message);

        //todo send notification
        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .type(NotificationType.IMAGE)
                .senderId(senderId)
                .receiverId(recipientId)
                .messageType(MessageType.IMAGE)
                .media(FileUtils.readFileFromLocation(filePath))
                .build();
        notificationService.sendNotification(recipientId, notification);
    }

    private String getSenderId(Chat chat, Authentication authentication) {
       if(chat.getSender().getId().equals(authentication.getName())){
           return chat.getSender().getId();
       }
       return chat.getRecipient().getId();
    }

    private String getRecipientId(Chat chat,Authentication authentication) {
        if(chat.getSender().getId().equals(authentication.getName())){
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }

    private Chat getChat(String chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(()->new EntityNotFoundException("chat not found"));
    }
}
