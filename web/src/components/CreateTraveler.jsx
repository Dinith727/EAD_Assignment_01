import React, { useState } from "react";
import { Form, Button, Alert, FormText } from "react-bootstrap";
import { addTraveler } from "../utils/api/traveller";

const CreateTraveler = () => {
	const [fullName, setFullName] = useState("");
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [confirmPassword, setConfirmPassword] = useState("");
	const [NIC, setNIC] = useState("");
	const [errorMessage, setErrorMessage] = useState(""); // State for error message
	const [successMessage, setSuccessMessage] = useState(""); // State for success message
	const [NICError, setNICError] = useState("");
	const [passwordError, setPasswordError] = useState("");

	const handleFullNameChange = (e) => {
		setFullName(e.target.value);
	};

	const handleEmailChange = (e) => {
		setEmail(e.target.value);
	};

	const handlePasswordChange = (e) => {
		setPassword(e.target.value);
		setPasswordError("");
	};

	const handleConfirmPasswordChange = (e) => {
		setConfirmPassword(e.target.value);
		setPasswordError("");
	};

	const handleNICChange = (e) => {
		setNIC(e.target.value);
		setNICError("");
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		const formData = {
			email: email,
			fullName: fullName,
			password: password,
			NIC: NIC,
		};
		if (
			(NIC.length !== 10 ||
				!["v", "V", "x", "X"].includes(NIC.charAt(NIC.length - 1))) &&
			NIC !== ""
		) {
			setNICError("Enter a valid NIC number");
		} else if (password !== confirmPassword) {
			setPasswordError("Confirm password does not match");
		} else if (password.length < 8) {
			setPasswordError("Password should have at least 8 charactors");
		} else if (!/[!@#$%^&*()_+{}\[\]:;<>,.?~\\]/.test(password)) {
			setPasswordError(
				"Password should contain at least one special character (@, #, %, $)"
			);
		} else {
			try {
				const _res = await addTraveler(formData);
				if (_res.status === 200) {
					setSuccessMessage("User has been created successfully!"); // Set success message
					setErrorMessage("");
				} else {
					setErrorMessage(_res.statusText);
					setSuccessMessage("");
				}

				setFullName("");
				setEmail("");
				setPassword("");
				setConfirmPassword("");
				setNIC("");
			} catch (error) {
				return error;
			}
		}
	};

	return (
		<div
			style={{
				width: "500px",
				marginTop: "100px",
				//marginBottom: "100px",
			}}
		>
			<h2
				style={{
					fontSize: "20px",
					textAlign: "left",
					marginBottom: "20px",
				}}
			>
				Create User
			</h2>
			{/* Display error message */}
			{errorMessage && (
				<Alert variant="danger" className="mb-3">
					{errorMessage}
				</Alert>
			)}
			{/* Display success message */}
			{successMessage && (
				<Alert variant="success" className="mb-3">
					{successMessage}
				</Alert>
			)}
			<Form onSubmit={handleSubmit} style={{ textAlign: "left" }}>
				{/* Full Name Input */}
				<Form.Group controlId="fullName">
					<Form.Label>Full Name:</Form.Label>
					<Form.Control
						type="text"
						placeholder="Enter full name"
						value={fullName}
						onChange={handleFullNameChange}
						required
					/>
				</Form.Group>

				{/* NIC Input */}
				<Form.Group controlId="NIC">
					<Form.Label>NIC:</Form.Label>
					<Form.Control
						type="text"
						placeholder="Enter NIC"
						value={NIC}
						onChange={handleNICChange}
						required
					/>
					{NICError && (
						<FormText className="text-danger">
							Enter a valid NIC number
						</FormText>
					)}
				</Form.Group>

				{/* Email Input */}
				<Form.Group controlId="email">
					<Form.Label>Email:</Form.Label>
					<Form.Control
						type="email"
						placeholder="Enter email"
						value={email}
						onChange={handleEmailChange}
						required
					/>
				</Form.Group>

				{/* Password Input */}
				<Form.Group controlId="password">
					<Form.Label>Password:</Form.Label>
					<Form.Control
						type="password"
						placeholder="Enter password"
						value={password}
						onChange={handlePasswordChange}
						required
					/>
				</Form.Group>

				{/* Confirm Password Input */}
				<Form.Group controlId="confirmPassword">
					<Form.Label>Confirm Password:</Form.Label>
					<Form.Control
						type="password"
						placeholder="Confirm password"
						value={confirmPassword}
						onChange={handleConfirmPasswordChange}
						required
					/>
					{passwordError && (
						<FormText className="text-danger">
							{passwordError}
						</FormText>
					)}
				</Form.Group>

				{/* Submit Button */}
				<Button variant="primary" type="submit" className="mt-4">
					Create User
				</Button>
			</Form>
		</div>
	);
};

export default CreateTraveler;
