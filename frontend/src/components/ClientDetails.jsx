import React from "react";

const ClientDetails = ({ client }) => {
  if (!client) return null;

  const probability = client.probability;
  const churnState = probability > 50 ? 1 : 0;
  
  const riskLevel =
    probability <= 25
      ? "low"
      : probability <= 50
        ? "medium"
        : probability <= 75
          ? "high"
          : "critical";


  return (
    <section className={`clientDetails ${riskLevel}`}>
      <h2>Client Details</h2>

      <div className="detailRow">
        <span>Client Name</span>
        <span>{client.name}</span>
      </div>

      <div className="detailRow">
        <span>Phone Number</span>
        <span>{client.phone}</span>
      </div>

      <div className="detailRow">
        <span>Age</span>
        <span>{client.age}</span>
      </div>

      <div className="detailRow">
        <span>Churn Probability</span>
        <span>{probability}%</span>
      </div>

      <div className="detailRow">
        <span>Churn Status</span>
        <span>{churnState === 1 ? "Churn" : "Active"}</span>
      </div>
    </section>
  );
};

export default ClientDetails;
