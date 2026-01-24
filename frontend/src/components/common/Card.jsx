import React from 'react';
import styles from '@/styles/components/common/Card.module.css';

/**
 * Universal Card component for the Dashboard.
 * 
 * @param {Object} props
 * @param {string} props.title - Optional title for the card.
 * @param {string} props.riskLevel - Optional risk level ('low', 'medium', 'high', 'critical').
 * @param {React.ReactNode} props.children - Content of the card.
 * @param {string} props.className - Additional classes for the container.
 */
const Card = ({ title, riskLevel, children, className = '' }) => {
    const cardClasses = [
        styles.card,
        riskLevel ? styles[riskLevel] : '',
        className
    ].join(' ').trim();

    return (
        <div className={cardClasses}>
            {title && (
                <div className={styles.cardHeader}>
                    <h3 className={styles.title}>{title}</h3>
                </div>
            )}
            <div className={styles.cardContent}>
                {children}
            </div>
        </div>
    );
};

export default Card;
