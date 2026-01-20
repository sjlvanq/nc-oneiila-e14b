import './App.css'

import { createBrowserRouter, RouterProvider, Link, Outlet } from "react-router-dom";

import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './routes/ProtectedRoute';

//import MainLayout from './layouts/MainLayout';

import Inicio from './pages/Home';
import PageLogin from './pages/PageLogin';
import PageDashboard from './pages/PageDashboard';
import NotFound from './pages/NotFound';

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

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        path: "/", // Login and Home are public
        element: <Inicio />,
      },
      {
        path: "login",
        element: <PageLogin />,
      },
      // Protected Routes
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