import { useState } from 'react';
import styles from '../styles/components/ChatMessage.module.css';

export default function ChatMessage({ message }) {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <div 
      className={`${styles.message} ${styles[message.sender]}`}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <div className={styles.messageContent}>
        <p>{message.text}</p>
        <div className={styles.messageFooter}>
          <span className={styles.messageTime}>{message.timestamp}</span>
          {isHovered && message.sender === 'bot' && (
            <div className={styles.messageActions}>
              <button 
                className={styles.actionButton}
                title="Copiar mensaje"
                onClick={() => navigator.clipboard.writeText(message.text)}
              >
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                  <path d="M16 1H4C2.9 1 2 1.9 2 3V17H4V3H16V1ZM19 5H8C6.9 5 6 5.9 6 7V21C6 22.1 6.9 23 8 23H19C20.1 23 21 22.1 21 21V7C21 5.9 20.1 5 19 5ZM19 21H8V7H19V21Z" fill="currentColor"/>
                </svg>
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
