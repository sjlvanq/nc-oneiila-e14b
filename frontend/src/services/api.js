import axios from 'axios';
import { setupAuthInterceptor } from '@/interceptors/auth.interceptor';

const api = axios.create({
    baseURL: 'http://localhost:8080', // Base URL
});

setupAuthInterceptor(api);

export default api;