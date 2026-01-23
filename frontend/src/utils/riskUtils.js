/**
 * Centralized utility for risk-related calculations and formatting.
 */

export const RISK_LEVELS = {
  LOW: 'low',
  MEDIUM: 'medium',
  HIGH: 'high',
  CRITICAL: 'critical',
};

/**
 * Normalizes probability to a 0-1 range.
 * Some parts of the app might use 0.7 and others 70.
 */
export const normalizeProbability = (prob) => {
  if (prob > 1) return prob / 100;
  return prob;
};

/**
 * Returns the risk level string based on probability (0-1).
 */
export const getRiskLevel = (probability) => {
  const p = normalizeProbability(probability);
  if (p <= 0.25) return RISK_LEVELS.LOW;
  if (p <= 0.50) return RISK_LEVELS.MEDIUM;
  if (p <= 0.75) return RISK_LEVELS.HIGH;
  return RISK_LEVELS.CRITICAL;
};

/**
 * Returns the formatted risk title.
 */
export const getRiskTitle = (probability) => {
  const level = getRiskLevel(probability);
  switch (level) {
    case RISK_LEVELS.LOW: return 'Riesgo bajo (0% - 25%)';
    case RISK_LEVELS.MEDIUM: return 'Riesgo medio (26% - 50%)';
    case RISK_LEVELS.HIGH: return 'Riesgo alto (51% - 75%)';
    case RISK_LEVELS.CRITICAL: return 'Riesgo crítico (76% - 100%)';
    default: return '';
  }
};

/**
 * Determines if a client is in "Churn" state (>50% probability).
 */
export const isChurn = (probability) => {
  return normalizeProbability(probability) > 0.5;
};

/**
 * Formats a probability as a percentage string (e.g., "70.5%").
 */
export const formatProbability = (probability) => {
  const p = normalizeProbability(probability) * 100;
  return `${p.toFixed(1)}%`;
};
