import { useState, useEffect } from 'react';

export function SampleGetComponent1() {
  const [token, setToken] = useState('');
  const [prediction, setPrediction] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [clientId, setClientId] = useState('');

  const fetchPrediction = async () => {
	if (!clientId) return;

	//const token = localStorage.getItem('token');
  // const token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJDaHVybkNoZWNrQVBJIiwiZXhwIjoxNzY4NTI4MDAyLCJzdWIiOiJhZG1pbkBkZW1vLmNvbSJ9.xBrVO9M-XEOXKsY-1w0LYnXg9YXOH2ZWork5Zv6_bbE'; 
  // Esto normalmente vendría de un AuthContext o localStorage
    
    const headers = {
      'Accept': 'application/json',
      'Authorization': `Bearer ${token}`
    }

	try {
		setLoading(true);
		setError(null);

		const response = await fetch(`http://localhost:8080/clients/${clientId}/prediction`, {
		  method: 'GET',
		  headers: headers
		});
		
    const data = await response.json();

		if (response.ok) {
        	setPrediction(data);
    } else {
          console.log(data);
        	// Manejo según ErrorStatusResponseDTO
        	setError(data.message || 'Error al obtener la predicción');
    }

	} catch (err) {
		setError(err.message);
	
  } finally {
		setLoading(false);
	
  }
  };

  return(
    <div className="card">
    <p>Token</p>
    <input type="text"
      value={token}
      onChange={(e) => setToken(e.target.value)} />

    <br />
    <p>Id de cliente</p>
    <input type="text"
      value={clientId}
      onChange={(e) => setClientId(e.target.value)} /> &nbsp;&nbsp;
    <button onClick={fetchPrediction} disabled={loading}>
    {loading ? 'Consultando...' : 'Predecir'}</button>
    
    {loading && <p>Calculando predicción...</p>}

    {error && <p style={{ color: 'red' }}><strong>Error:</strong> {error}</p>}

    {prediction && (
      <div>
        <h3>Resultado para: {prediction.clientName}</h3>
        <p><strong>Estado (Churn):</strong> {prediction.churn}</p>
        <p><strong>Probabilidad:</strong> {(prediction.probability * 100).toFixed(2)}%</p>
        <p><small>Generado el: {new Date(prediction.timestamp).toLocaleString()}</small></p>
      </div>
    )}
    </div>
  );
};
