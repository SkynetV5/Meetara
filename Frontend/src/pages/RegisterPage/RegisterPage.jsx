import {
  Box,
  Button,
  Container,
  Paper,
  TextField,
  Typography,
  CircularProgress,
  Alert,
  Link,
} from "@mui/material";
import { useState } from "react";
import { useAuth } from "../../contexts/AuthContext";
import { Navigate, useNavigate } from "react-router-dom";
import { registerUser } from "../../api/endpoints/aPIDocumentation";

export default function RegisterPage() {
  const { isAuthenticated, isLoading, error } = useAuth();

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");
  const [localError, setLocalError] = useState(null);
  const [isRegisterSuccesfully, setIsRegisterSuccesfully] = useState(false);
  const navigate = useNavigate();

  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLocalError(null);

    if (!username || !password || !email || !repeatPassword) {
      setLocalError("Podaj potrzebne dane!");
      return;
    }

    if (password !== repeatPassword) {
      setLocalError("Hasła nie są takie same");
      return;
    }

    try {
      await registerUser({ email, username, password });
      setIsRegisterSuccesfully(true);
    } catch (error) {
      console.log(error);
      setLocalError(
        error.response?.data?.error || "Wystąpił błąd podczas rejestracji"
      );
    }
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
        {!isRegisterSuccesfully ? (
          <>
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
              <Typography variant="h5">Rejestracja</Typography>
              <Typography variant="body2" color="primary.light" sx={{ mt: 2 }}>
                Rejestracja się do panelu
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
                label="Email"
                fullWidth
                margin="normal"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                autoFocus
              />
              <TextField
                label="Nazwa użytkownika"
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
              <TextField
                label="Powtórz hasło"
                type="password"
                fullWidth
                margin="normal"
                value={repeatPassword}
                onChange={(e) => setRepeatPassword(e.target.value)}
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
                  "Zarejestruj się"
                )}
              </Button>
            </Box>

            <Typography
              variant="body2"
              color="text.secondary"
              align="start"
              sx={{ mt: 3 }}
            >
              Masz już konto? <Link href="/login">Zaloguj się</Link>
            </Typography>
          </>
        ) : (
          <>
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
              <Typography variant="h5">Rejestracja</Typography>

              <Alert variant="filled" severity="success" sx={{ mt: 2 }}>
                Rejestracja przebiegła pomyślnie
              </Alert>

              <Typography variant="body2" color="primary.light" sx={{ mt: 2 }}>
                Możesz się teraz zalogować
              </Typography>
              <Button
                type="submit"
                fullWidth
                size="large"
                variant="contained"
                sx={{ mt: 3 }}
                disabled={isLoading}
                onClick={() => navigate("/login")}
              >
                Zaloguj się
              </Button>
            </Box>
          </>
        )}
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
