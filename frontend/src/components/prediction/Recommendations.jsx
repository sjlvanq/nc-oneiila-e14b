import React from "react";

const Recommendations = ({ probability }) => {
  if (probability === undefined || probability === null) return null;

  if (probability <= 25) {
    return (
      <section className="recommendation low">
        <h3>Low Risk</h3>
        <ul>
          <li>Loyalty rewards strategy</li>
          <li>Referral-based promotions</li>
          <li>Premium services upselling</li>
        </ul>
      </section>
    );
  }

  if (probability <= 50) {
    return (
      <section className="recommendation medium">
        <h3>Medium Risk</h3>
        <ul>
          <li>Motivational engagement reminders</li>
          <li>Group class invitations</li>
          <li>Habit reinforcement actions</li>
        </ul>
      </section>
    );
  }

  if (probability <= 75) {
    return (
      <section className="recommendation high">
        <h3>High Risk</h3>
        <ul>
          <li>Direct personalized contact</li>
          <li>Early contract renewal offers</li>
          <li>Complimentary personal training session</li>
        </ul>
      </section>
    );
  }

  return (
    <section className="recommendation critical">
      <h3>Critical Risk</h3>
      <ul>
        <li>Limited-time aggressive discount</li>
        <li>Urgent personal follow-up</li>
        <li>Membership freeze option</li>
        <li>Exit feedback collection</li>
      </ul>
    </section>
  );
};

export default Recommendations;
