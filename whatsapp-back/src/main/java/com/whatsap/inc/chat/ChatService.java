package com.whatsap.inc.chat;

import com.whatsap.inc.user.User;
import com.whatsap.inc.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<ChatResponse> allChats(Authentication currentUser) {
        final String userId = currentUser.getName();
        return chatRepository.findChatsBySenderId(userId)
                .stream()
                .map(c -> ChatMapper.toChatResponse(c, userId)).toList();
    }

    public String createChat(String senderId, String recipientId) {
        Optional<Chat> existingChat = chatRepository.findChatByReceiverAndSender(senderId, recipientId);
        if (existingChat.isPresent()) {
            return existingChat.get().getId();
        }
        User sender=userRepository.findByPublicId(senderId).orElseThrow(
                () -> new EntityNotFoundException(format("sender with id %s not found",senderId))
        );
        User recipient=userRepository.findByPublicId(recipientId).orElseThrow(
                () -> new EntityNotFoundException(format("recipient with id %s not found",senderId))
        );
        Chat chat = new Chat();
        chat.setRecipient(recipient);
        chat.setSender(sender);
        chatRepository.save(chat);
        return chat.getId();


    }

}
