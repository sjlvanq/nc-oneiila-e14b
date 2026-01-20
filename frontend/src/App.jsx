import './App.css'

import { createBrowserRouter, RouterProvider, Link, Outlet } from "react-router-dom";

import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './routes/ProtectedRoute';

import Inicio from './pages/Inicio';
import PageLogin from './pages/PageLogin';
import PageDashboard from './pages/PageDashboard';
import Navbar from './layouts/Navbar';

function Layout() {
  return (
    <>
      <Navbar />
      <main>
        <Outlet />
      </main>
    </>
  );
}



// 1. Definimos las rutas
const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />, // El Layout envuelve a los hijos
    children: [
      {
        path: "/", // Login e Inicio desprotegidas
        element: <Inicio />,
      },
      {
        path: "login",
        element: <PageLogin />,
      },
      // Protegidas
      {
        element: <ProtectedRoute />, 
        children: [
          {
            path: "dashboard",
            element: <PageDashboard />,
          },
        ],
      },
      {
        path: "login", // Ruta para /login
        element: <PageLogin />,
      },
    ],
  },
]);



// 3. Aplicación principal
export default function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  );
}