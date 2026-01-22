import axios from 'axios';
import { setupAuthInterceptor } from '@/interceptors/auth.interceptor';

const apiUrl = import.meta.env.VITE_API_URL;

if (!apiUrl) {
  throw new Error("La variable de entorno VITE_API_URL no está definida. ");
}

const api = axios.create({
    baseURL: apiUrl,
});

setupAuthInterceptor(api);

export default api;