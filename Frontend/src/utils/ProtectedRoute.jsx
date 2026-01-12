import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import CircularProgress from "@mui/material/CircularProgress";
import Fade from "@mui/material/Fade";

import { useAuth } from "../contexts/AuthContext.jsx";
import { Navigate, Outlet } from "react-router";

export default function ProtectedRoute({ children }) {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <Container
        maxWidth={false}
        sx={{
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          bgcolor: "background.default",
        }}
      >
        <Fade in timeout={600}>
          <Paper
            elevation={6}
            sx={{
              p: 4,
              minWidth: 280,
              textAlign: "center",
              borderRadius: 3,
            }}
          >
            <Box sx={{ mb: 2 }}>
              <CircularProgress size={48} thickness={4} />
            </Box>

            <Typography variant="h6" gutterBottom>
              Ładowanie...
            </Typography>

            <Typography variant="body2" color="text.secondary">
              Sprawdzanie autoryzacji
            </Typography>
          </Paper>
        </Fade>
      </Container>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children ? children : <Outlet />}</>;
}
