import { useEffect } from "react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Routes, Route, useLocation } from "react-router-dom";
import Title from "../components/Title/Title.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";
import LoginPage from "../pages/LoginPage/LoginPage.jsx";
import RegisterPage from "../pages/RegisterPage/RegisterPage.jsx";
import DashboardPage from "../pages/Dashboard/Dashboard.jsx";
import AppLayout from "../layouts/AppLayout.jsx";
import EventsPage from "../pages/EventsPage/EventsPage.jsx";
import AddEventPage from "../pages/AddEventPage/AddEventPage.jsx";
import EventDetailsPage from "../pages/EventDetailsPage/EventDetailsPage.jsx";
import AccountPage from "../pages/AccountPage/AccountPage.js";
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
        <Route element={<AppLayout />}>
          <Route
            path="/"
            element={
              <Title title="Dashboard">
                <DashboardPage />
              </Title>
            }
          />
          <Route
            path="/events"
            element={
              <Title title="Wydarzenia">
                <EventsPage />
              </Title>
            }
          />
          <Route
            path="/event/add"
            element={
              <Title title="Wydarzenia">
                <AddEventPage />
              </Title>
            }
          />
          <Route
            path="/event/:id"
            element={
              <Title title="Wydarzenie">
                <EventDetailsPage />
              </Title>
            }
          />
          <Route
            path="/account"
            element={
              <Title title="Konto">
                <AccountPage />
              </Title>
            }
          />
        </Route>
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
      <Route
        path="/register"
        element={
          <Title title="Rejestracja">
            <RegisterPage />
          </Title>
        }
      />
    </Routes>
  );
}
