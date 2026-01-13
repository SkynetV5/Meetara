import {
  Box,
  Container,
  Typography,
  Paper,
  Button,
  Stack,
  TextField,
} from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import AddIcon from "@mui/icons-material/Add";
import { useState, useEffect } from "react";
import {
  useDeleteEvent,
  useGetAllEvents,
  useSearchEventsByKeyword,
} from "../../api/endpoints/aPIDocumentation";
import { useNavigate } from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";
import InputAdornment from "@mui/material/InputAdornment";
import DeleteIcon from "@mui/icons-material/Delete";

export default function EventsPage() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [selectedEventIds, setSelectedEventIds] = useState([]);
  const navigate = useNavigate();
  const deleteEvents = useDeleteEvent();

  const {
    data: allEvents,
    isLoading: isLoadingEvents,
    error,
  } = useGetAllEvents();

  const {
    data: eventSearch,
    isLoading: isLoadingEventSearch,
    error: errorEventSearch,
  } = useSearchEventsByKeyword({ keyword: search });

  console.log(eventSearch);

  console.log(selectedEventIds);

  useEffect(() => {
    if (allEvents && search === "") {
      const formattedEvents = allEvents.map((event, index) => ({
        id: event.id ?? index,
        name: event.title || "-",
        eventDate: event.eventDate || "-",
        location: event.location || "-",
      }));
      setEvents(formattedEvents);
      setLoading(false);
    } else if (eventSearch) {
      const eventsArray = Array.isArray(eventSearch)
        ? eventSearch
        : [eventSearch];
      const formattedEvent = eventsArray
        .filter(Boolean) // usuwa undefined/null
        .map((event, index) => ({
          id: event.id ?? index,
          name: event.title || "-",
          eventDate: event.eventDate || "-",
          location: event.location || "-",
        }));
      setEvents(formattedEvent);
      setLoading(false);
    }

    if (error) {
      console.error("Błąd pobierania wydarzeń:", error);
      setLoading(false);
    }
  }, [allEvents, error, eventSearch, search]);

  const columns = [
    { field: "id", headerName: "ID", width: 70 },
    { field: "name", headerName: "Nazwa", flex: 1 },
    {
      field: "eventDate",
      headerName: "Data wydarzenia",
      flex: 1,
    },
    { field: "location", headerName: "Miejsce", flex: 1 },
  ];

  const handleBulkDelete = async () => {
    if (!selectedEventIds.length) return;

    if (
      !window.confirm(
        `Czy na pewno chcesz usunąć ${selectedEventIds.length} wydarzeń?`
      )
    ) {
      return;
    }

    try {
      await Promise.all(
        selectedEventIds.map((id) => deleteEvents.mutateAsync({ id: id }))
      );

      setEvents((prev) =>
        prev.filter((event) => !selectedEventIds.includes(event.id))
      );

      setSelectedEventIds([]);
    } catch (err) {
      console.error("Błąd usuwania wydarzeń:", err);
    }
  };

  return (
    <Container maxWidth={false} sx={{ mt: 0, maxWidth: "90%" }}>
      <Box sx={{ mb: 3 }}>
        <Typography variant="h4">Wydarzenia</Typography>
        <Typography variant="body2" color="text.secondary">
          Lista wszystkich wydarzeń
        </Typography>
      </Box>

      <Paper sx={{ p: 2, mb: 3, borderRadius: 3 }}>
        <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => navigate("/event/add")}
          >
            Dodaj wydarzenie
          </Button>
          <Button
            variant="contained"
            color="error"
            startIcon={<DeleteIcon />}
            disabled={selectedEventIds.length == 0}
            onClick={handleBulkDelete}
          >
            Usuń ({selectedEventIds.length})
          </Button>
        </Stack>
      </Paper>

      <Paper sx={{ p: 2, mb: 3, borderRadius: 3 }}>
        <TextField
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Szukaj wydarzeń..."
          size="small"
          maxWidth={false}
          sx={{ minWidth: "50%" }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon color="action" />
              </InputAdornment>
            ),
          }}
        />
      </Paper>

      <Paper sx={{ autoHeight: true, borderRadius: 3 }}>
        <DataGrid
          rows={events}
          columns={columns}
          pageSize={10}
          rowsPerPageOptions={[5, 10, 20]}
          loading={loading}
          disableSelectionOnClick
          autoHeight
          checkboxSelection
          disableRowSelectionOnClick
          onRowSelectionModelChange={(selection) => {
            const idsArray = Array.from(selection.ids);
            setSelectedEventIds(idsArray);
          }}
        />
      </Paper>
    </Container>
  );
}
