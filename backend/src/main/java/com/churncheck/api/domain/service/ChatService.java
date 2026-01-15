package com.churncheck.api.domain.service;

public interface ChatService {
    String startConversation();
    String sendMessage(String conversationId, String message);
    String getConversationStatus(String conversationId);
}
