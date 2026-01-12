import {
  Box,
  Button,
  Container,
  Paper,
  TextField,
  Typography,
  CircularProgress,
  Alert,
} from "@mui/material";
import { useState } from "react";
import { useAuth } from "../../contexts/AuthContext";
import { Navigate, useNavigate } from "react-router-dom";

export default function LoginPage() {
  const { login, isAuthenticated, isLoading, error } = useAuth();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [localError, setLocalError] = useState(null);
  const navigate = useNavigate();

  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLocalError(null);

    if (!username || !password) {
      setLocalError("Podaj login i hasło");
      return;
    }

    const result = await login(username, password);

    if (!result.success) {
      if (result.error.includes("404")) {
        setLocalError("Nie znaleziono użytkownika");
        return;
      }
    }
    navigate("/");
  };

  return (
    <Container
      maxWidth={false}
      sx={{
        minHeight: "100vh",
        minWidth: "100vw",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        bgcolor: "background.default",
      }}
    >
      <Paper
        elevation={8}
        sx={{
          p: 4,
          width: "100%",
          maxWidth: 400,
          borderRadius: 3,
        }}
      >
        {/* Header */}
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            mb: 3,
          }}
        >
          <Typography variant="h4" color="primary.main">
            Meetara
          </Typography>
          <Typography variant="h5">Logowanie</Typography>
          <Typography variant="body2" color="primary.light" sx={{ mt: 2 }}>
            Zaloguj się do panelu
          </Typography>
        </Box>

        {/* Errors */}
        {(localError || error) && (
          <Alert severity="error" sx={{ mb: 2 }} variant="outlined">
            {localError || error}
          </Alert>
        )}

        {/* Form */}
        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            label="Login"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoFocus
          />

          <TextField
            label="Hasło"
            type="password"
            fullWidth
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <Button
            type="submit"
            fullWidth
            size="large"
            variant="contained"
            sx={{ mt: 3 }}
            disabled={isLoading}
          >
            {isLoading ? (
              <CircularProgress size={24} color="inherit" />
            ) : (
              "Zaloguj się"
            )}
          </Button>
        </Box>

        {/* Footer */}
        <Typography
          variant="body2"
          color="text.secondary"
          align="center"
          sx={{ mt: 3 }}
        >
          © {new Date().getFullYear()} Meetara
        </Typography>
      </Paper>
    </Container>
  );
}
