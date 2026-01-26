import api from './api';

export const sendChatMessage = async (message, conversationId = null) => {
  try {
    const response = await api.post('/chat', {
      message,
      conversation_id: conversationId
    });
    
    return response.data;
  } catch (error) {
    console.error('Error sending chat message:', error);
    throw error;
  }
};

export const getChatHealth = async () => {
  try {
    const response = await api.get('/chat/health');
    return response.data;
  } catch (error) {
    console.error('Error checking chat health:', error);
    throw error;
  }
};
