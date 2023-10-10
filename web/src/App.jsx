import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import "bootstrap/dist/css/bootstrap.min.css";
import Dashboard from "./pages/Dashboard";
import UserManagement from "./pages/UserManagement";
import TravelerManagement from "./pages/TravelerManagement";
import TicketBookings from "./pages/TicketBookings";
import Trains from "./pages/Trains";
import { AuthProvider } from "./utils/providers/AuthProvider";

function App() {
	return (
		<>
			<BrowserRouter>
				<AuthProvider>
					<Routes>
						<Route path="/" element={<LoginPage />} />
						<Route path="/dashboard" element={<Dashboard />} />
						<Route
							path="/user-management"
							element={<UserManagement />}
						/>
						<Route
							path="/traveler-management"
							element={<TravelerManagement />}
						/>
						<Route
							path="/ticket-booking"
							element={<TicketBookings />}
						/>
						<Route path="/trains" element={<Trains />} />
					</Routes>
				</AuthProvider>
			</BrowserRouter>
		</>
	);
}

export default App;
