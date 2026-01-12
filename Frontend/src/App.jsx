import { BrowserRouter } from "react-router-dom";
import { QueryClientProvider, QueryClient } from "@tanstack/react-query";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";

import { AuthProvider } from "./contexts/AuthContext";
import Router from "./utils/Router";

const darkTheme = createTheme({
  palette: {
    primary: {
      main: "#2196f3",
      light: "#64b5f6",
    },
    mode: "dark",
  },
});
const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <ThemeProvider theme={darkTheme}>
            <CssBaseline />
            <Router />
          </ThemeProvider>
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
