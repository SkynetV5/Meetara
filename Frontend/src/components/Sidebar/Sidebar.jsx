import {
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Divider,
  Toolbar,
} from "@mui/material";
import DashboardIcon from "@mui/icons-material/Dashboard";
import EventIcon from "@mui/icons-material/Event";
import LogoutIcon from "@mui/icons-material/Logout";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";

export default function Sidebar({ open, onClose, variant, drawerWidth }) {
  const navigate = useNavigate();
  const { logout } = useAuth();

  const handleNavigate = (path) => {
    navigate(path);
    onClose?.();
  };

  return (
    <Drawer
      open={variant === "temporary" ? open : true}
      onClose={onClose}
      variant={variant}
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: {
          width: drawerWidth,
          boxSizing: "border-box",
        },
      }}
    >
      <Toolbar />
      <List>
        <ListItemButton onClick={() => handleNavigate("/")}>
          <ListItemIcon>
            <DashboardIcon />
          </ListItemIcon>
          <ListItemText primary="Dashboard" />
        </ListItemButton>

        <ListItemButton onClick={() => handleNavigate("/events")}>
          <ListItemIcon>
            <EventIcon />
          </ListItemIcon>
          <ListItemText primary="Wydarzenia" />
        </ListItemButton>
        <ListItemButton onClick={() => handleNavigate("/account")}>
          <ListItemIcon>
            <AccountCircleIcon />
          </ListItemIcon>
          <ListItemText primary="Konto" />
        </ListItemButton>
      </List>

      <Divider />

      <List>
        <ListItemButton onClick={logout}>
          <ListItemIcon>
            <LogoutIcon color="error" />
          </ListItemIcon>
          <ListItemText primary="Wyloguj" />
        </ListItemButton>
      </List>
    </Drawer>
  );
}
