import {
  Container,
  Box,
  Typography,
  Paper,
  Stack,
  Divider,
  Button,
  TextField,
  Chip,
  IconButton,
  CircularProgress,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import SaveIcon from "@mui/icons-material/Save";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import { useNavigate, useParams } from "react-router-dom";
import { useState } from "react";

import {
  useGetEventById,
  useUpdateEvent,
  useDeleteComment,
  useGetAllUsers,
  useGetParticipationByEventId,
  useGetCommentsByEventId,
  useDeleteParticipation,
  useCreateComment,
  useCreateParticipation,
  useGetUserByUsername,
  useDeleteEvent,
} from "../../api/endpoints/aPIDocumentation";
import { useAuth } from "../../contexts/AuthContext";

export default function EventDetailsPage() {
  const { id } = useParams();
  const [now] = useState(() => new Date());
  const navigate = useNavigate();

  const { data: event, isLoading, refetch: refetchEvent } = useGetEventById(id);
  const { data: participants = [], refetch: refetchParticipants } =
    useGetParticipationByEventId(id);
  const { data: comments = [], refetch: refetchComments } =
    useGetCommentsByEventId(id);
  const { data: users = [] } = useGetAllUsers();
  const { user: username } = useAuth();
  const { data: user } = useGetUserByUsername(username.username);

  console.log(event);
  console.log(user);
  console.log(comments);
  console.log(participants);

  const updateEvent = useUpdateEvent(id);
  const addParticipant = useCreateParticipation();
  const removeParticipant = useDeleteParticipation();
  const addComment = useCreateComment();
  const deleteComment = useDeleteComment();
  const deleteEvent = useDeleteEvent();

  const [editMode, setEditMode] = useState(false);
  const [editedEvent, setEditedEvent] = useState({});
  const [newComment, setNewComment] = useState("");

  if (isLoading || !event) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 6 }}>
        <CircularProgress />
      </Box>
    );
  }

  /* =========================
       EVENT UPDATE
    ========================== */
  const handleEdit = () => {
    setEditedEvent(event);
    setEditMode(true);
  };

  const handleSave = async () => {
    await updateEvent.mutateAsync({ id: editedEvent.id, data: editedEvent });
    refetchEvent();
    setEditMode(false);
  };

  const handleBulkDelete = async () => {
    if (!window.confirm(`Czy na pewno chcesz usunąć ${event.title}?`)) {
      return;
    }

    try {
      await deleteEvent.mutateAsync({ id: event.id });
      navigate("/events");
    } catch (err) {
      console.error("Błąd usuwania wydarzeń:", err);
    }
  };

  /* =========================
       COMMENTS
    ========================== */
  const handleAddComment = async () => {
    if (!newComment.trim()) return;

    await addComment.mutateAsync({
      data: { text: newComment, event: event, user: user },
    });
    setNewComment("");
    refetchComments();
  };

  console.log(editedEvent);

  const participantUserIds = new Set(
    participants.map((p) => p.user?.id).filter(Boolean)
  );

  const availableUsers = users.filter((u) => !participantUserIds.has(u.id));

  return (
    <Container maxWidth="md" sx={{ mt: 5 }}>
      {/* EVENT INFO */}
      <Paper sx={{ p: 3, borderRadius: 3, mb: 3 }}>
        <Stack
          direction="row"
          justifyContent="space-between"
          alignItems="center"
        >
          <Typography variant="h4">
            {editMode ? (
              <TextField
                value={editedEvent.title}
                onChange={(e) =>
                  setEditedEvent({ ...editedEvent, title: e.target.value })
                }
                fullWidth
              />
            ) : (
              event.title
            )}
          </Typography>
          <Typography>
            <IconButton onClick={editMode ? handleSave : handleEdit}>
              {editMode ? <SaveIcon /> : <EditIcon />}
            </IconButton>
            <IconButton onClick={handleBulkDelete} color="error">
              {" "}
              <DeleteIcon />
            </IconButton>
          </Typography>
        </Stack>

        <Divider sx={{ my: 2 }} />

        <Stack spacing={2}>
          <TextField
            label="Data"
            type="datetime-local"
            disabled={!editMode}
            value={editedEvent.eventDate || event.eventDate}
            onChange={(e) =>
              setEditedEvent({ ...editedEvent, eventDate: e.target.value })
            }
            InputLabelProps={{ shrink: true }}
          />

          <TextField
            label="Miejsce"
            disabled={!editMode}
            value={editedEvent.location || event.location}
            onChange={(e) =>
              setEditedEvent({ ...editedEvent, location: e.target.value })
            }
          />

          <TextField
            label="Opis"
            multiline
            rows={3}
            disabled={!editMode}
            value={editedEvent.description || event.description}
            onChange={(e) =>
              setEditedEvent({ ...editedEvent, description: e.target.value })
            }
          />
        </Stack>
      </Paper>

      {/* PARTICIPANTS */}
      <Paper sx={{ p: 3, borderRadius: 3, mb: 3 }}>
        <Typography variant="h6">Uczestnicy</Typography>

        <Stack direction="row" spacing={1} flexWrap="wrap" mt={2}>
          {participants.map((p) => (
            <Chip
              key={p.id}
              label={p.user.username}
              onDelete={async () => {
                await removeParticipant.mutateAsync({ id: p.id });
                refetchParticipants();
              }}
              color="primary"
            />
          ))}
        </Stack>

        <Stack direction="row" spacing={1} mt={2}>
          {availableUsers.length === 0 ? (
            <Typography variant="body2" color="text.secondary">
              Wszyscy użytkownicy są już uczestnikami
            </Typography>
          ) : (
            availableUsers.map((u) => (
              <IconButton
                key={u.id}
                onClick={async () => {
                  await addParticipant.mutateAsync({
                    data: {
                      event,
                      user: u,
                      status: "REGISTERED",
                      registeredAt: new Date().toISOString(),
                    },
                  });
                  refetchParticipants();
                }}
                sx={{ display: "flex", gap: 1 }}
              >
                <PersonAddIcon />
                <Typography variant="subtitle2">{u.username}</Typography>
              </IconButton>
            ))
          )}
        </Stack>
      </Paper>

      {/* COMMENTS */}
      <Paper sx={{ p: 3, borderRadius: 3 }}>
        <Typography variant="h6">Komentarze</Typography>

        <Stack spacing={2} mt={2}>
          {comments.map((c) => (
            <Paper key={c.id} sx={{ p: 2 }}>
              <Stack
                direction="row"
                justifyContent="space-between"
                alignItems="center"
              >
                <Typography variant="subtitle2">
                  {c.user.username} : {c.text}{" "}
                </Typography>

                <IconButton
                  size="small"
                  onClick={async () => {
                    await deleteComment.mutateAsync({ id: c.id });
                    refetchComments();
                  }}
                >
                  <DeleteIcon fontSize="small" />
                </IconButton>
              </Stack>

              <Typography variant="body2">{c.content}</Typography>
            </Paper>
          ))}
        </Stack>

        <Stack direction="row" spacing={2} mt={3}>
          <TextField
            fullWidth
            label="Dodaj komentarz"
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
          />
          <Button variant="contained" onClick={handleAddComment}>
            Dodaj
          </Button>
        </Stack>
      </Paper>
    </Container>
  );
}
