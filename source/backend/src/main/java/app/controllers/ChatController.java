package app.controllers;


import app.dto.user.ConversationResponse;
import app.dto.user.CreateConversationRequest;
import app.dto.user.MessageResponse;
import app.dto.user.SendMessageRequest;
import app.services.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @PostMapping("/conversations/start")
    public ResponseEntity<ConversationResponse> startConversation(@RequestBody CreateConversationRequest request) {
        try {
            ConversationResponse response = ChatService.startOrGetConversation(
                    request.getAdId(),
                    request.getBuyerId(),
                    request.getSellerId(),
                    request.getUsername(),
                    request.getToken()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/messages/send")
    public ResponseEntity<String> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            ChatService.sendMessage(request);
            return ResponseEntity.ok("✅ پیام با موفقیت ارسال شد");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ " + e.getMessage());
        }
    }

    @GetMapping("/messages/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long conversationId,
                                                             @RequestParam String username,
                                                             @RequestParam String token) {
        try {
            List<MessageResponse> messages = ChatService.getMessages(conversationId, username, token);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ConversationResponse>> getUserConversations(@PathVariable Long userId,
                                                                           @RequestParam String username,
                                                                           @RequestParam String token) {
        try {
            List<ConversationResponse> conversations = ChatService.getUserConversations(userId, username, token);
            return ResponseEntity.ok(conversations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}