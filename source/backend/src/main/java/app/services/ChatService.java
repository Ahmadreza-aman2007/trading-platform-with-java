package app.services;


import app.dto.user.ConversationResponse;
import app.dto.user.MessageResponse;
import app.dto.user.SendMessageRequest;
import app.entities.conversation.Conversation;
import app.entities.conversation.Message;
import app.entities.users.enums.UserRole;
import app.repository.DAOs.ConversationDAO;
import app.repository.DAOs.MessageDAO;
import app.repository.DAOs.UserDAO;
import app.utils.TokenUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ===== شروع یا پیدا کردن مکالمه =====
    public static ConversationResponse startOrGetConversation(Long adId, Long buyerId, Long sellerId, String username, String token) throws Exception {
        TokenUtil.isTokenValid(username, token, UserRole.COMMON_USER);

        if (buyerId != null && buyerId.equals(sellerId)) {
            throw new Exception("امکان گفتگو روی آگهی خودتان وجود ندارد");
        }

        if (ConversationDAO.isConversationExist(sellerId, adId, buyerId)) {
            return convertToResponse(ConversationDAO.find(sellerId, adId, buyerId));
        }

        Conversation newConv = new Conversation(adId, buyerId, sellerId, false);
        ConversationDAO.save(newConv);

        Conversation saved = ConversationDAO.find(sellerId, adId, buyerId);
        return convertToResponse(saved);
    }

    // ===== ارسال پیام =====
    public static void sendMessage(SendMessageRequest request) throws Exception {
        TokenUtil.isTokenValid(request.getUsername(), request.getToken(), UserRole.COMMON_USER);

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new Exception("❌ متن پیام نمی‌تواند خالی باشد");
        }

        String now = LocalDateTime.now().format(FORMATTER);  // ← String

        Message message = new Message(
                request.getContent(),
                now,
                request.getConversationId(),
                request.getSenderId(),
                false
        );

        MessageDAO.save(message);
    }

    // ===== دریافت پیام‌های یک مکالمه =====
    public static List<MessageResponse> getMessages(Long conversationId, String username,

                                                    String token) throws Exception {
        TokenUtil.isTokenValid(username, token, UserRole.COMMON_USER);

        List<Message> messages = MessageDAO.findByConversationId(conversationId);
        List<MessageResponse> responses = new ArrayList<>();

        for (Message m : messages) {
            String senderUsername = Objects.requireNonNull(UserDAO.loadUserById(m.getSenderId())).getUsername();
            responses.add(new MessageResponse(
                    m.getId(),
                    m.getSenderId(),
                    senderUsername,
                    m.getContent(),
                    m.getTime(),  // ← String
                    m.isRead()
            ));
        }

        return responses;
    }

    // ===== دریافت لیست مکالمه‌های یک کاربر =====
    public static List<ConversationResponse> getUserConversations(Long userId, String username, String token) throws Exception {
        TokenUtil.isTokenValid(username, token, UserRole.COMMON_USER);

        List<Conversation> conversations = ConversationDAO.findByUserId(userId);
        List<ConversationResponse> responses = new ArrayList<>();

        for (Conversation c : conversations) {
            responses.add(convertToResponse(c));
        }

        return responses;
    }

    private static ConversationResponse convertToResponse(Conversation c) {
        return new ConversationResponse(
                c.getId(),
                c.getAdId(),
                c.getBuyerId(),
                c.getSellerId(),
                c.isBlocked()
        );
    }
}