export function getChurnStatus(probability) {
  return probability > 50 ? "Churn" : "Active";
}

export function getRiskLevel(probability) {
  if (probability <= 25) {
    return {
      level: "Low Risk",
      color: "green",
      icon: "🟢",
      strategy: "Loyalty & Upselling",
      action:
        "Reward loyalty instead of offering discounts. Encourage referrals using promoFriends.",
      tactic:
        "Promote premium services like SPA, cafeteria, or merchandising."
    };
  }

  if (probability <= 50) {
    return {
      level: "Medium Risk",
      color: "yellow",
      icon: "🟡",
      strategy: "Preventive Engagement",
      action:
        "Encourage habit formation. Send motivational reminders when attendance drops.",
      tactic:
        "Invite clients to group classes to increase social engagement."
    };
  }

  if (probability <= 75) {
    return {
      level: "High Risk",
      color: "red",
      icon: "🔴",
      strategy: "Active Intervention",
      action:
        "Send a personalized offer for early contract renewal.",
      tactic:
        "Offer a free personal training session."
    };
  }

  return {
    level: "Critical Risk",
    color: "darkred",
    icon: "🔴",
    strategy: "Emergency Retention",
    action:
      "Urgent call or message with a strong discount valid for 48 hours.",
    tactic:
      "Offer membership freeze or send a short exit survey."
  };
}
