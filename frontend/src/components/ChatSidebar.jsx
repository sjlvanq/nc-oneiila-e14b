import { useState, useEffect, useRef } from 'react';
import styles from '../styles/components/ChatSidebar.module.css';
import { sendChatMessage } from '../services/chatService';
import ChatMessage from './ChatMessage';
import ChatInput from './ChatInput';

export default function ChatSidebar() {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [conversationId, setConversationId] = useState(null);
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const toggleChat = () => {
    setIsOpen(!isOpen);
  };

  const handleSendMessage = async (message) => {
    if (!message.trim() || isLoading) return;

    const userMessage = {
      id: Date.now(),
      text: message,
      sender: 'user',
      timestamp: new Date().toLocaleTimeString()
    };

    setMessages(prev => [...prev, userMessage]);
    setIsLoading(true);

    try {
      const response = await sendChatMessage(message, conversationId);
      
      const botMessage = {
        id: Date.now() + 1,
        text: response.response,
        sender: 'bot',
        timestamp: new Date().toLocaleTimeString()
      };

      setMessages(prev => [...prev, botMessage]);
      
      if (response.conversation_id) {
        setConversationId(response.conversation_id);
      }
    } catch (error) {
      const errorMessage = {
        id: Date.now() + 1,
        text: 'Lo siento, hubo un error al procesar tu mensaje. Intenta de nuevo.',
        sender: 'bot',
        timestamp: new Date().toLocaleTimeString()
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      {/* Overlay */}
      {isOpen && (
        <div className={styles.overlay} onClick={toggleChat} />
      )}
      
      {/* Chat Sidebar */}
      <div className={`${styles.chatSidebar} ${isOpen ? styles.open : styles.closed}`}>
        {/* Header */}
        <div className={styles.chatHeader}>
          <div className={styles.chatInfo}>
            <div className={styles.chatAvatar}>
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 1L9 7V9C9 10.1 9.9 11 11 11H13V22C13 22.6 13.4 23 14 23C14.6 23 15 22.6 15 22V11H17C18.1 11 19 10.1 19 9H21Z" fill="currentColor"/>
              </svg>
            </div>
            <div>
              <h3>Asistente Churn</h3>
              <span className={styles.status}>En línea</span>
            </div>
          </div>
          <button className={styles.closeButton} onClick={toggleChat}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
            </svg>
          </button>
        </div>

        {/* Messages */}
        <div className={styles.messagesContainer}>
          {messages.length === 0 ? (
            <div className={styles.welcomeMessage}>
              <div className={styles.welcomeIcon}>
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 1L9 7V9C9 10.1 9.9 11 11 11H13V22C13 22.6 13.4 23 14 23C14.6 23 15 22.6 15 22V11H17C18.1 11 19 10.1 19 9H21Z" fill="currentColor"/>
                </svg>
              </div>
              <h4>¡Hola! Soy tu asistente de análisis de churn</h4>
              <p>Puedo ayudarte a analizar riesgos de clientes, identificar factores de abandono y proporcionar recomendaciones de retención.</p>
              <p>¿En qué puedo ayudarte hoy?</p>
            </div>
          ) : (
            messages.map((message) => (
              <ChatMessage key={message.id} message={message} />
            ))
          )}
          
          {isLoading && (
            <div className={`${styles.message} ${styles.bot}`}>
              <div className={styles.messageContent}>
                <div className={styles.typingIndicator}>
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>
          )}
          
          <div ref={messagesEndRef} />
        </div>

        {/* Input */}
        <ChatInput onSendMessage={handleSendMessage} isLoading={isLoading} />
      </div>

      {/* Floating Button */}
      <button 
        className={`${styles.floatingButton} ${isOpen ? styles.hidden : ''}`}
        onClick={toggleChat}
        title="Abrir chat"
      >
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M21 15C21 15.5 20.8 16 20.4 16.4L18.4 18.4C18 18.8 17.5 19 17 19C16.5 19 16 18.8 15.6 18.4L13.2 16H10.8L8.4 18.4C8 18.8 7.5 19 7 19C6.5 19 6 18.8 5.6 18.4L3.6 16.4C3.2 16 3 15.5 3 15C3 14.5 3.2 14 3.6 13.6L5.6 11.6V9.2L3.6 6.8C3.2 6.4 3 5.9 3 5.4C3 4.9 3.2 4.4 3.6 4L5.6 2C6 1.6 6.5 1.4 7 1.4C7.5 1.4 8 1.6 8.4 2L10.8 4.4H13.2L15.6 2C16 1.6 16.5 1.4 17 1.4C17.5 1.4 18 1.6 18.4 2L20.4 4C20.8 4.4 21 4.9 21 5.4C21 5.9 20.8 6.4 20.4 6.8L18.4 9.2V11.6L20.4 13.6C20.8 14 21 14.5 21 15Z" fill="currentColor"/>
        </svg>
        {messages.length > 0 && (
          <span className={styles.notificationBadge}>{messages.length}</span>
        )}
      </button>
    </>
  );
}
