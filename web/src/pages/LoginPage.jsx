//login page ui
import React from "react";
import LoginComp from "../components/LoginComp";

function LoginPage() {
	const backgroundImageUrl =
		"https://images.pexels.com/photos/716834/pexels-photo-716834.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"; //background image

	//styles
	const containerStyle = {
		backgroundImage: `url(${backgroundImageUrl})`,
		backgroundSize: "cover",
		backgroundPosition: "center",
		minHeight: "100vh",
		paddingTop: "80px",
	};

	const headingStyle = {
		textAlign: "center",
		color: "white",
	};

	return (
		<div style={containerStyle}>
			<h1 style={headingStyle}>Booking Management System</h1>
			<LoginComp topic="Admin" />
		</div>
	);
}

export default LoginPage;
