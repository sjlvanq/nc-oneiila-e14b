import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

import { createBrowserRouter, RouterProvider, Link, Outlet } from "react-router-dom";
import Inicio from './pages/Inicio';
import Perfil from './pages/Perfil';
import PageLogin from './pages/PageLogin';
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
        path: "/", // Ruta cuando estás en el inicio
        element: <Inicio />,
      },
      {
        path: "perfil", // Ruta para /perfil
        element: <Perfil />,
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
    <>
      <RouterProvider router={router} />
    </>
  );
}