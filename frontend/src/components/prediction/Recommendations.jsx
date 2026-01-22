import React from "react";


const Recommendations = ({ probability }) => {
  if (probability === undefined || probability === null) return null;

  let riskTitle = "";
  let strategy = "";
  let action = "";
  let tactic = "";

  if (probability <= 25) {
    riskTitle = "Low Risk (0% - 25%)";
    strategy = "Loyalty & Upselling";
    action =
      "Do not overwhelm the client with discounts. Focus on loyalty rewards such as referrals.";
    tactic =
      "Promote premium services like SPA, cafeteria, or merchandising.";
  } else if (probability <= 50) {
    riskTitle = "Medium Risk (26% - 50%)";
    strategy = "Preventive Engagement";
    action =
      "Encourage habit formation when attendance starts to decline.";
    tactic =
      "Invite the client to group classes to increase social engagement.";
  } else if (probability <= 75) {
    riskTitle = "High Risk (51% - 75%)";
    strategy = "Active Intervention";
    action =
      "Send a personalized offer for early contract renewal.";
    tactic =
      "Offer a free personal training session to re-engage the client.";
  } else {
    riskTitle = "Critical Risk (76% - 100%)";
    strategy = "Emergency Retention";
    action =
      "Contact the client with an urgent aggressive offer valid for 48 hours.";
    tactic =
      "Offer membership freeze or collect short exit feedback.";
  }

  return (
    <section className="recommendations">
      <h2>Recommendations</h2>

      <div className="detail-row">
        <span>Risk Level</span>
        <span>{riskTitle}</span>
      </div>

      <div className="detail-row">
        <span>Churn Status</span>
        <span>{probability > 50 ? "Churn" : "Active"}</span>
      </div>

      <div className="detail-row">
        <span>Strategy</span>
        <span>{strategy}</span>
      </div>

      <div className="detail-row">
        <span>Recommended Action</span>
        <span>{action}</span>
      </div>

      <div className="detail-row">
        <span>Tactic</span>
        <span>{tactic}</span>
      </div>
    </section>
  );
};

export default Recommendations;
