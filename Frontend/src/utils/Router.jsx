import { useEffect } from "react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Routes, Route, useLocation } from "react-router-dom";
import Title from "../components/Title/Title.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";
import LoginPage from "../pages/LoginPage/LoginPage.jsx";
export default function Router() {
  const { pathname } = useLocation();
  const ScrollOnTop = () => {
    useEffect(() => {
      window.scrollTo(0, 0);
    }, [pathname]);

    return null;
  };

  return (
    <Routes>
      <Route element={<ProtectedRoute />}>
        <Route path="/" element={<Title title="Dashboard"></Title>} />
      </Route>
      //Login and Register routes
      <Route
        path="/login"
        element={
          <Title title="Logowanie">
            <LoginPage />
          </Title>
        }
      />
      <Route path="/register" element={<Title title="Rejestracja"></Title>} />
    </Routes>
  );
}
