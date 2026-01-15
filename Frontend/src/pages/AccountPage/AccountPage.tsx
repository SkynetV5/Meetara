import {
    Container,
    Paper,
    Typography,
    Stack,
    TextField,
    Button,
    CircularProgress,
  } from "@mui/material";
  import { useEffect, useState } from "react";
  import { useAuth } from "../../contexts/AuthContext";
  import {
    useGetUserByUsername,
    useCreateProfile,
    useUpdateProfile,
    useGetProfileById,
    useGetProfile,
  } from "../../api/endpoints/aPIDocumentation";
  
  export default function AccountPage() {
    const { user } = useAuth();
    const { data: userData, isLoading } = useGetUserByUsername(user.username);
    const { data: profileData} = useGetProfile(userData?.id)

    console.log(profileData)
  
    const createProfile = useCreateProfile();
    const updateProfile = useUpdateProfile();

  
    const [profile, setProfile] = useState({
      firstName: "",
      lastName: "",
      phoneNumber: "",
    });
  
    const [profileId, setProfileId] = useState(null);
    const [saving, setSaving] = useState(false);
  
    useEffect(() => {
      if (profileData) {
        setProfile({
          firstName: profileData.firstName || "",
          lastName: profileData.lastName || "",
          phoneNumber: profileData.phoneNumber || "",
        });
        setProfileId(profileData.id || null);
      }
    }, [userData, profileData]);
  
    if (isLoading) {
      return (
        <Container sx={{ mt: 6, textAlign: "center" }}>
          <CircularProgress />
        </Container>
      );
    }
  
    const handleSave = async () => {
      setSaving(true);
  
      try {
        if (profileId) {
          // UPDATE
          await updateProfile.mutateAsync({
            id: profileId,
            data: profile,
          });
        } else {
          // CREATE
          const created = await createProfile.mutateAsync({
            data: {
              ...profile,
              user: userData,
            },
          });
          setProfileId(created.id);
        }
      } finally {
        setSaving(false);
      }
    };
  
    return (
      <Container maxWidth="sm" sx={{ mt: 5 }}>
        <Paper sx={{ p: 4, borderRadius: 3 }}>
          <Typography variant="h5" gutterBottom>
            Moje konto
          </Typography>
  
          <Stack spacing={2}>
            {/* READ-ONLY */}
            <TextField label="Username" value={userData.username} disabled />
            <TextField label="Email" value={userData.email} disabled />
  
            {/* PROFILE */}
            <TextField
              label="Imię"
              value={profile.firstName}
              onChange={(e) =>
                setProfile({ ...profile, firstName: e.target.value })
              }
            />
  
            <TextField
              label="Nazwisko"
              value={profile.lastName}
              onChange={(e) =>
                setProfile({ ...profile, lastName: e.target.value })
              }
            />
  
            <TextField
              label="Numer telefonu"
              value={profile.phoneNumber}
              onChange={(e) =>
                setProfile({ ...profile, phoneNumber: e.target.value })
              }
            />
  
            <Button
              variant="contained"
              onClick={handleSave}
              disabled={saving}
            >
              {saving ? "Zapisywanie..." : "Zapisz"}
            </Button>
          </Stack>
        </Paper>
      </Container>
    );
  }