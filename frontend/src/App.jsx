import './App.css'

import { createBrowserRouter, RouterProvider, Link, Outlet } from "react-router-dom";

import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './routes/ProtectedRoute';

import MainLayout from './layouts/MainLayout';

import Inicio from './pages/Home';
import PageLogin from './pages/PageLogin';
import PageDashboard from './pages/PageDashboard';
import NotFound from './pages/NotFound';

// Define the routes
const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout />,
    children: [
      {
        path: "/", // Login and Home are public
        element: <Inicio />,
      },
      {
        path: "login",
        element: <PageLogin />,
      },
      {
        path: "dashboard",
        element: <PageDashboard />,
      },
      // Protected Routes
      {
        path: "login", // Login is public
        element: <PageLogin />,
      },
      {
        path: "*", // Catch-all route for 404
        element: <NotFound />,
      },
    ],
  },
]);



// Main App
export default function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  );
}