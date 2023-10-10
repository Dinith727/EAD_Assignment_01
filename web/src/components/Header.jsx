import React from "react";
import { Navbar, Container, Nav, Image, Button } from "react-bootstrap";
import useAuth from "../utils/providers/AuthProvider";

const Header = () => {
	const userEmail = window.localStorage.getItem("username");
	const userAvatarUrl =
		"https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1780&q=80";

	const { logout } = useAuth();
	const handleLogout = () => {
		logout();
	};
	return (
		<Navbar bg="dark" variant="dark" expand="lg">
			<Container>
				<Navbar.Brand>Dashboard</Navbar.Brand>
				<Navbar.Toggle aria-controls="basic-navbar-nav" />
				<Navbar.Collapse id="basic-navbar-nav">
					<Nav className="me-auto">
						<Nav.Link disabled>{userEmail}</Nav.Link>
					</Nav>
					<Nav>
						<Image
							src={userAvatarUrl}
							alt="Profile"
							roundedCircle
							width="30"
							height="30"
							className="mx-4"
						/>
						<Button variant="outline-light" onClick={handleLogout}>
							Logout
						</Button>
					</Nav>
				</Navbar.Collapse>
			</Container>
		</Navbar>
	);
};

export default Header;
