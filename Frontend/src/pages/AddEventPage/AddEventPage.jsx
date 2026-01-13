import {
  Container,
  Box,
  Typography,
  Paper,
  TextField,
  Button,
  Stack,
  TextareaAutosize,
  InputLabel,
} from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  useCreateEvent,
  useCreateParticipation,
  useGetAllUsers,
  useGetUserByUsername,
} from "../../api/endpoints/aPIDocumentation";
import { Autocomplete, CircularProgress } from "@mui/material";
import { useAuth } from "../../contexts/AuthContext";

export default function AddEventPage() {
  const now = new Date();
  const navigate = useNavigate();
  const { user } = useAuth();
  const createEvent = useCreateEvent();
  const createParticipation = useCreateParticipation();
  const {
    data: users,
    isLoading: isLoadingUsers,
    error: errorUsers,
  } = useGetAllUsers();
  const {
    data: creatorUser,
    isLoading: isLoadingCreatorUser,
    error: errorCreatorUser,
  } = useGetUserByUsername(user.username);
  const [formData, setFormData] = useState({
    name: "",
    eventDate: "",
    location: "",
    description: "",
    users: [],
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  console.log(users);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const createdEvent = await createEvent.mutateAsync({
        data: {
          title: formData.name,
          description: formData.description,
          eventDate: formData.eventDate,
          location: formData.location,
          user: creatorUser,
          comments: [],
        },
      });

      console.log(createdEvent);

      for (const user of formData.users) {
        await createParticipation.mutateAsync({
          data: {
            event: createdEvent,
            user: user,
            status: "REGISTERED",
            registeredAt: now.toISOString(),
          },
        });
      }

      navigate("/events");
    } catch (err) {
      console.error("Błąd tworzenia wydarzenia:", err);
      setError("Nie udało się utworzyć wydarzenia");
      setLoading(false);
    }
  };

  console.log(formData);

  return (
    <Container maxWidth="sm" sx={{ mt: 5 }}>
      <Paper sx={{ p: 3, borderRadius: 3 }}>
        <Typography variant="h5" mb={2}>
          Nowe wydarzenie
        </Typography>

        <form onSubmit={handleSubmit}>
          <Stack spacing={2}>
            <TextField
              label="Nazwa wydarzenia"
              name="name"
              value={formData.name}
              onChange={handleChange}
              fullWidth
              required
            />

            <TextField
              label="Data wydarzenia"
              name="eventDate"
              type="datetime-local"
              value={formData.eventDate}
              onChange={handleChange}
              fullWidth
              required
              InputLabelProps={{ shrink: true }}
            />

            <TextField
              label="Miejsce"
              name="location"
              value={formData.location}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextareaAutosize
              id="description"
              name="description"
              minRows={3}
              placeholder="Opis wydarzenia"
              style={{
                width: "100%",
                paddingLeft: 10,
                paddingTop: 10,
                fontSize: "15px",
                borderRadius: 4,
                borderColor: "#ccc",
              }}
              value={formData.description}
              onChange={handleChange}
            />
            {isLoadingUsers ? (
              <CircularProgress size={24} />
            ) : (
              <Autocomplete
                multiple
                options={users || []} // dane użytkowników z backendu
                getOptionLabel={(option) => option.username}
                value={formData.users}
                onChange={(event, newValue) => {
                  setFormData((prev) => ({
                    ...prev,
                    users: newValue, // zapisujemy całą tablicę obiektów użytkowników
                  }));
                }}
                renderInput={(params) => (
                  <TextField
                    {...params}
                    label="Uczestnicy"
                    placeholder="Wybierz użytkowników"
                  />
                )}
              />
            )}

            {error && (
              <Typography color="error" variant="body2">
                {error}
              </Typography>
            )}

            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={loading}
            >
              {loading ? "Tworzenie..." : "Zapisz wydarzenie"}
            </Button>
          </Stack>
        </form>
      </Paper>
    </Container>
  );
}
