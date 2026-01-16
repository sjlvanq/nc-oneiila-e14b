import { useState } from 'react';

export function SamplePostComponent1() {
	const [username, setUsername] = useState('');
	const [password, setPassword] = useState('');
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState('');
	const [response, setResponse] = useState(null);
	const [token, setToken] = useState('');

	const getToken = async (e) => {
		e.preventDefault(); // Evita que la página se recargue

		const postBody = {
			email: username,
			password: password
		}

		console.log(postBody)

		try {
			setError('');
			setResponse('');
			setLoading(true);
			const response = await fetch('http://localhost:8080/login', {
				method: 'POST',
				headers: {
					'Content-type': 'application/json',
				},
				body: JSON.stringify(postBody)
			});

			const data = await response.json();
			setResponse(data);
			console.log(data);
			
			if (response.ok) {
				setToken(data.token);
			} else {
				console.log(data);
				// Puede traer un Json de error o no
				setError(data.message || 'Error no especificado');
			}
			
		} catch (error) {
			console.error("Sin conexión con el Backend");
			setError("Sin conexión con el Backend")
		} finally {
			setLoading(false);
		}
	}

	// Función auxiliar para buscar el mensaje de error de un campo específico
	const getFieldError = (fieldName) => {
		if (!response || !response.fields) return null;
		const fieldError = response.fields.find(f => f.field === fieldName);
		return fieldError ? fieldError.message : null;
	};

	return (
		<div className="card">
			{token && <p style={{width:"200px", overflow:"scroll"}}>{token}</p>}
			{loading && <p>Solicitando acceso...</p>}
			{error && <p style={{ color: 'red' }}>{error}</p>}
			<p>Nombre de usuario: <input type="text" 
				onChange={(e)=>setUsername(e.currentTarget.value)}/>
				<br /><span style={{color:"orange"}}>{getFieldError('email')}</span></p>
			<p>Contraseña: <input type="password" 
				onChange={(e)=>setPassword(e.currentTarget.value)}/>
				<br /><span style={{color:"orange"}}>{getFieldError('password')}</span></p>
			<p><button onClick={getToken} disabled={loading}>LetMeIn</button></p>
		</div>
	)


}
