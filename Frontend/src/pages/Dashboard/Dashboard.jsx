import {
  Box,
  Container,
  Typography,
  Grid,
  Paper,
  Button,
  Stack,
} from "@mui/material";
import EventIcon from "@mui/icons-material/Event";
import PeopleIcon from "@mui/icons-material/People";
import AddIcon from "@mui/icons-material/Add";
import ListIcon from "@mui/icons-material/List";
import { useAuth } from "../../contexts/AuthContext";
import {
  useGetAllEvents,
  useGetEventsAfterDate,
} from "../../api/endpoints/aPIDocumentation";
import { useMemo, useState } from "react";
import { useNavigate } from "react-router";

const StatCard = ({ icon, title, value }) => (
  <Paper
    elevation={6}
    sx={{
      p: 3,
      borderRadius: 3,
      display: "flex",
      alignItems: "center",
      gap: 2,
    }}
  >
    {icon}
    <Box>
      <Typography variant="body2" color="text.secondary">
        {title}
      </Typography>
      <Typography variant="h5">{value}</Typography>
    </Box>
  </Paper>
);

export default function DashboardPage() {
  const { user } = useAuth();
  const [now] = useState(() => new Date());
  const navigate = useNavigate();

  const {
    data: events,
    isLoading: isLoadingEvents,
    error: errorEvents,
  } = useGetAllEvents();

  const {
    data: activeEvents,
    isLoading: isLoadingActiveEvents,
    error: errorActiveEvents,
  } = useGetEventsAfterDate({ date: now.toISOString() });

  console.log(events);
  console.log(activeEvents);
  console.log(errorEvents);
  return (
    <Container
      maxWidth={false}
      sx={{
        mt: 4,
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
      }}
    >
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" gutterBottom>
          Dashboard
        </Typography>
        <Typography variant="body1" color="primary.light">
          Witaj {user?.username || "użytkowniku"} 👋
        </Typography>
      </Box>

      {/* Stats */}
      <Grid
        container
        spacing={3}
        sx={{
          mb: 4,
        }}
      >
        <Grid item xs={12} md={4}>
          <StatCard
            icon={<EventIcon fontSize="large" color="primary" />}
            title="Liczba wydarzeń"
            value={events && events.length}
          />
        </Grid>

        <Grid item xs={12} md={4}>
          <StatCard
            icon={<EventIcon fontSize="large" color="success" />}
            title="Aktywne wydarzenia"
            value={activeEvents ? activeEvents.length : 0}
          />
        </Grid>
      </Grid>

      {/* Actions */}
      <Paper elevation={4} sx={{ p: 3, borderRadius: 3, width: "50%" }}>
        <Typography variant="h6" gutterBottom>
          Szybkie akcje
        </Typography>

        <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            size="large"
            onClick={() => navigate("/event/add")}
          >
            Dodaj wydarzenie
          </Button>

          <Button
            variant="outlined"
            startIcon={<ListIcon />}
            size="large"
            onClick={() => navigate("/events")}
          >
            Lista wydarzeń
          </Button>
        </Stack>
      </Paper>
    </Container>
  );
}
