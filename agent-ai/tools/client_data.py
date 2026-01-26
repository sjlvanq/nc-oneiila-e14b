"""
MCP tools for accessing client data and churn analysis
"""

import pandas as pd
from typing import Annotated
from datetime import datetime

def get_client_by_id(client_id: Annotated[str, "Client ID (DNI-XXXX)"]) -> str:
    """
    Search client in the CSV and return complete information
    
    Args:
        client_id: Client ID (e.g: DNI-1012)
        
    Returns:
        Detailed client information or not found message
    """
    try:
        df = pd.read_csv('resources/data.csv')
        client = df[df['dni'] == client_id.upper()]
        
        if not client.empty:
            row = client.iloc[0]
            age = calculate_age(row['birth_date'])
            partner_name = get_partner_name(row['partner_id'])
            tenure_months = get_tenure_months(row['registration_date'])
            
            return f"""**Client {row['dni']} - {row['name']}**

**Personal Data:**
• Phone: {row['phone']}
• Gender: {row['gender']}
• Age: {age} years
• Birth date: {row['birth_date']}

**Membership Information:**
• Partner: {partner_name}
• Friends promotion: {'Yes' if row['promo_friends'] else 'No'}
• Registration date: {row['registration_date']}
• Contract start date: {row['contract_start_date']}
• Contract duration: {row['contract_period']} months
• Antigüedad: {tenure_months} months

**Features:**
• Near location: {'Yes' if row['near_location'] else 'No'}
• Group visits: {'Yes' if row['group_visit'] else 'No'}
• Status: {'Active' if row['active'] else 'Inactive'}"""
        else:
            return f"Client {client_id} not found in the database"
            
    except Exception as e:
        return f"Error searching client {client_id}: {str(e)}"

def analyze_churn_risk(client_id: Annotated[str, "Client ID to analyze"]) -> str:
    """
    Analyze churn risk based on real data from the CSV
    
    Args:
        client_id: Client ID (e.g: DNI-1012)
        
    Returns:
        Complete churn risk analysis with recommendations
    """
    try:
        df = pd.read_csv('resources/data.csv')
        client = df[df['dni'] == client_id.upper()]
        
        if client.empty:
            return f"Client {client_id} not found for risk analysis"
        
        row = client.iloc[0]
        risk_score = calculate_risk_score(row)
        risk_level = get_risk_level(risk_score)
        tenure_months = get_tenure_months(row['registration_date'])
        
        # Detailed risk factor analysis
        factors_analysis = analyze_risk_factors(row, tenure_months)
        
        return f"""**Churn Risk Analysis**
**Client:** {row['dni']} - {row['name']}

**Risk Level:** {risk_level} ({risk_score}%)

{factors_analysis}

**Specific Recommendations:**
{get_recommendations(risk_level, row, tenure_months)}

**Key Metrics:**
• Churn probability: {risk_score}%
• Primary risk factor: {get_primary_risk_factor(row, tenure_months)}
• Recommended action: {get_recommended_action(risk_level)}"""
        
    except Exception as e:
        return f"Error analyzing risk for client {client_id}: {str(e)}"

def list_clients() -> str:
    """
    List all clients available in the database
    
    Returns:
        Formatted list of all clients
    """
    try:
        df = pd.read_csv('resources/data.csv')
        
        if df.empty:
            return "No clients found in the database"
        
        clients_list = "**All Clients Available:**\n\n"
        
        for _, row in df.iterrows():
            risk_score = calculate_risk_score(row)
            risk_level = get_risk_level(risk_score)
            status_emoji = "Active" if row['active'] else "Inactive"
            risk_emoji = {"LOW": "🟢", "MEDIUM": "🟡", "HIGH": "🔴"}.get(risk_level, "⚪")
            
            clients_list += f"{status_emoji} **{row['dni']}** - {row['name']} {risk_emoji}({risk_level})\n"
        
        clients_list += f"\n**Total:** {len(df)} active clients"
        return clients_list
        
    except Exception as e:
        return f"Error listing clients: {str(e)}"

# Functions auxiliares
def calculate_age(birth_date):
    """Calculate age from birth date"""
    try:
        birth = datetime.strptime(birth_date, '%Y-%m-%d')
        today = datetime.now()
        age = today.year - birth.year - ((today.month, today.day) < (birth.month, birth.day))
        return age
    except:
        return "Unknown"

def get_tenure_months(registration_date):
    """Calculate tenure in months"""
    try:
        reg_date = datetime.strptime(registration_date, '%Y-%m-%d')
        today = datetime.now()
        months = (today.year - reg_date.year) * 12 + (today.month - reg_date.month)
        return max(0, months)
    except:
        return 0

def get_partner_name(partner_id):
    """Return partner name"""
    partners = {
        1: "Gym Corp International",
        2: "Wellness Solutions"
    }
    return partners.get(partner_id, "Unassigned")

def calculate_risk_score(row):
    """Calculate risk score (0-100)"""
    score = 0
    
    # Tenure (most important factor)
    tenure = get_tenure_months(row['registration_date'])
    if tenure < 3:
        score += 35
    elif tenure < 6:
        score += 25
    elif tenure < 12:
        score += 15
    
    # Contract type
    if row['contract_period'] < 6:
        score += 30
    elif row['contract_period'] < 12:
        score += 20
    
    # Location
    if not row['near_location']:
        score += 15
    
    # Group visits
    if not row['group_visit']:
        score += 10
    
    # Partner
    if pd.isna(row['partner_id']):
        score += 5
    
    # Promoción friends
    if not row['promo_friends']:
        score += 5
    
    return min(score, 100)

def get_risk_level(score):
    """Determine risk level"""
    if score >= 70:
        return "HIGH"
    elif score >= 40:
        return "MEDIUM"
    else:
        return "LOW"

def analyze_risk_factors(row, tenure_months):
    """Analyze risk factors"""
    factors = []
    
    if tenure_months < 6:
        factors.append(f"**Tenure low:** Only {tenure_months} months (high initial risk)")
    
    if row['contract_period'] < 12:
        factors.append(f"**Contract short:** {row['contract_period']} months (lower commitment)")
    
    if not row['near_location']:
        factors.append("**Location far:** Access barrier")
    
    if not row['group_visit']:
        factors.append("**No group visits:** No participates in groups")
    
    if pd.isna(row['partner_id']):
        factors.append("**No partner:** Lack of corporate benefits")
    
    if not row['promo_friends']:
        factors.append("**No friends promotion:** Lower social commitment")
    
    if not factors:
        factors.append("**Stable profile:** No risk factors")
    
    return "\n".join(factors)

def get_recommendations(risk_level, row, tenure_months):
    """Get specific recommendations based on risk level"""
    if risk_level == "HIGH":
        return f"""**Immediate Actions (24-48h):**
• Personalized phone contact
• Special offer: 30% discount + free class
• Assign buddy or mentor
• Review access barriers

**Follow-up:**
• Weekly check-in during first month
• Personalized reengagement plan"""
    
    elif risk_level == "MEDIUM":
        return f"""**Preventive Actions:**
• Email with added value
• Invitation to special group class
• Upgrade offer with discount
• Satisfaction survey

**Follow-up:**
• Monthly contact
• Loyalty program"""
    
    else:  # BAJO
        return f"""**Maintenance and Retention:**
• Exclusive content newsletter
• Referral program (+10% discount)
• Premium workshops access
• Tri-annual satisfaction survey

**Opportunities:**
• Upsell premium services
• Brand ambassador program"""

def get_primary_risk_factor(row, tenure_months):
    """Identify primary risk factor"""
    factors = []
    
    if tenure_months < 6:
        factors.append(("Tenure low", 35))
    if row['contract_period'] < 12:
        factors.append(("Contract short", 30))
    if not row['near_location']:
        factors.append(("Location far", 15))
    
    if factors:
        return max(factors, key=lambda x: x[1])[0]
    return "No significant factor"

def get_recommended_action(risk_level):
    """Get recommended action based on risk level"""
    actions = {
        "HIGH": "Immediate and personalized intervention",
        "MEDIUM": "Preventive contact with value offer",
        "LOW": "Maintenance and loyalty programs"
    }
    return actions.get(risk_level, "Evaluate specific case")