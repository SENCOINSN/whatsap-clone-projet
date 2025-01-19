package com.whatsap.inc.chat;

import com.whatsap.inc.common.StringResponse;
import com.whatsap.inc.constants.ApiPath;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.whatsap.inc.constants.ApiPath.BASE_PATH;
import static com.whatsap.inc.constants.ApiPath.CHAT;

@RestController
@RequestMapping(BASE_PATH+CHAT)
@RequiredArgsConstructor
@Tag(name = "Chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<StringResponse> createChat(
            @RequestParam(name = "sender-id") String senderId,
            @RequestParam(name = "receiver-id") String receiverId
    ) {
        final String chatId = chatService.createChat(senderId, receiverId);
        StringResponse response = StringResponse.builder()
                .response(chatId)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatsByReceiver(Authentication authentication) {
        return ResponseEntity.ok(chatService.allChats(authentication));
    }

}
