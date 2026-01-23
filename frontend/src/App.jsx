import './App.css'

import { createBrowserRouter, RouterProvider, Link, Outlet } from "react-router-dom";
import styles from './styles/components/Sidebar.module.css'

import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './routes/ProtectedRoute';

//import MainLayout from './layouts/MainLayout';

import Inicio from './pages/Home';
import PageLogin from './pages/PageLogin';
import PageDashboard from './pages/PageDashboard';
import PageClientList from './pages/PageClientList';
import NotFound from './pages/NotFound';

import Sidebar from './layouts/Sidebar';

// Layout para rutas públicas (sin navbar)
function PublicLayout() {
  return (
    <main>
      <Outlet />
    </main>
  );
}

// Layout para rutas protegidas (con navbar)
function ProtectedLayout() {
  return (
    <div className={styles.layoutWrapper}>
      <Sidebar />
      <main className={styles.mainContent}>
        <Outlet />
      </main>
    </div>
  );
}

const router = createBrowserRouter([
  {
    path: "/",
    element: <PublicLayout />,
    children: [
      {
        path: "/", // Home is public
        element: <Inicio />,
      },
      {
        path: "login",
        element: <PageLogin />,
      },
      {
        path: "*", // Catch-all route for 404
        element: <NotFound />,
      },
    ],
  },
  {
    path: "/",
    element: <ProtectedRoute />,
    children: [
      {
        path: "",
        element: <ProtectedLayout />,
        children: [
          {
            path: "dashboard",
            element: <PageDashboard />,
          },
          {
            path: "clients",
            element: <PageClientList />,
          },
        ],
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