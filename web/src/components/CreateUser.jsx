import React, { useState, useEffect } from "react";
import { Form, Button, Col, Row, Alert } from "react-bootstrap";
import { addUser } from "../utils/api/admin";

const CreateUser = () => {
	const [userType, setUserType] = useState("backOffice");
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [confirmPassword, setConfirmPassword] = useState("");
	const [passwordMatchError, setPasswordMatchError] = useState(false);
	const [isFormValid, setIsFormValid] = useState(false);
	const [successMessage, setSuccessMessage] = useState("");
	const [errorMessage, setErrorMessage] = useState(""); // State to store error message

	useEffect(() => {
		setIsFormValid(
			email.trim() !== "" &&
				password.trim() !== "" &&
				confirmPassword.trim() !== "" &&
				!passwordMatchError
		);
	}, [email, password, confirmPassword, passwordMatchError]);

	const handleUserTypeChange = (e) => {
		setUserType(e.target.value);
	};

	const handleEmailChange = (e) => {
		setEmail(e.target.value);
	};

	const handlePasswordChange = (e) => {
		setPassword(e.target.value);

		// Check password for length and special characters
		const hasSpecialCharacters = /[!@#$%^&*()_+{}\[\]:;<>,.?~\\]/.test(
			e.target.value
		);
		setPasswordMatchError(
			e.target.value !== confirmPassword ||
				e.target.value.length < 8 ||
				!hasSpecialCharacters
		);
	};

	const handleConfirmPasswordChange = (e) => {
		setConfirmPassword(e.target.value);
		setPasswordMatchError(e.target.value !== password);
	};

	async function handleSubmit(e) {
		e.preventDefault();

		// Basic validation checks
		if (!email || !password) {
			setErrorMessage("Please fill in both email and password fields.");
			return;
		}

		if (
			password !== confirmPassword ||
			password.length < 8 ||
			!/[!@#$%^&*()_+{}\[\]:;<>,.?~\\]/.test(password)
		) {
			setErrorMessage(
				"Please make sure the password is at least 8 characters long and contains at least one special character (@, #, %, $)."
			);
			setPasswordMatchError(true);
			return;
		}

		try {
			const _res = await addUser({
				username: email,
				password: password,
				role: userType,
			});
			setUserType("back-office");
			if (_res.status === 200) {
				setErrorMessage("");
				setSuccessMessage("User Created!");
				setEmail("");
				setPassword("");
				setConfirmPassword("");
				setPasswordMatchError(false);
			} else {
				setSuccessMessage("");
				setErrorMessage(_res.statusText);
			}
		} catch (error) {
			setErrorMessage(
				"An error occurred while adding the user. Please try again later."
			);
		}
	}

	return (
		<div
			style={{
				width: "500px",
				marginTop: "100px",
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
			{/* Display success or error alert */}
			{successMessage && (
				<Alert variant="success" className="mb-3">
					{successMessage}
				</Alert>
			)}
			{errorMessage && (
				<Alert variant="danger" className="mb-3">
					{errorMessage}
				</Alert>
			)}
			<Form onSubmit={handleSubmit} style={{ textAlign: "left" }}>
				<Form.Group controlId="userType">
					<Form.Label>User Type:</Form.Label>
					<Row>
						<Col>
							<Form.Check
								type="radio"
								label="Back Office"
								name="userType1"
								value="backOffice"
								checked={userType === "backOffice"}
								onChange={handleUserTypeChange}
								id="backOffice"
							/>
						</Col>
						<Col>
							<Form.Check
								type="radio"
								label="Travel Agent"
								name="userType2"
								value="travelAgent"
								checked={userType === "travelAgent"}
								onChange={handleUserTypeChange}
								id="travelAgent"
							/>
						</Col>
					</Row>
				</Form.Group>

				<Form.Group controlId="email">
					<Form.Label>Username:</Form.Label>
					<Form.Control
						type="username"
						placeholder="Enter Username"
						value={email}
						onChange={handleEmailChange}
						required
					/>
				</Form.Group>

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

				<Form.Group controlId="confirmPassword">
					<Form.Label>Confirm Password:</Form.Label>
					<Form.Control
						type="password"
						placeholder="Confirm password"
						value={confirmPassword}
						onChange={handleConfirmPasswordChange}
						required
					/>
					{passwordMatchError && confirmPassword != "" && (
						<Form.Text className="text-danger">
							Passwords do not match or do not meet the
							requirements.
						</Form.Text>
					)}
				</Form.Group>

				<Button
					variant="primary"
					type="submit"
					className="mt-4"
					disabled={!isFormValid}
				>
					Create User
				</Button>
			</Form>
		</div>
	);
};

export default CreateUser;
