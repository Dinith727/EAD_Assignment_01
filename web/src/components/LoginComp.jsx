//login component
import React, { useState } from "react";
import {
	Container,
	Row,
	Col,
	Form,
	Button,
	Card,
	Alert,
	Spinner,
} from "react-bootstrap";
import useAuth from "../utils/providers/AuthProvider";

const LoginComp = () => {
	//window.localStorage.removeItem("token");
	const { login } = useAuth();
	const [formData, setFormData] = useState({
		email: "",
		password: "",
	});

	const [userRole, setUserRole] = useState("backOffice");
	const [isLoading, setisLoading] = useState(false);
	const [loginError, setLoginError] = useState(null); // State to store login errors

	const handleInputChange = (e) => {
		const { name, value } = e.target;
		setFormData({
			...formData,
			[name]: value,
		});
	};
	//toggle users type
	const toggleUserRole = () => {
		setUserRole(userRole === "backOffice" ? "travelAgent" : "backOffice");
	};

	async function handleSubmit(e) {
		e.preventDefault();

		try {
			setisLoading(true);
			await login(formData.email, formData.password, userRole);
			setisLoading(false);
		} catch (error) {
			setisLoading(false);
			console.log(error.response);
			setLoginError(
				error.response.data.message ||
					error.response.message ||
					error.response.status + " - " + error.response.statusText
			);
		}
	}

	return (
		//login form
		<Container
			style={{
				paddingTop: "100px",
			}}
		>
			<Row className="justify-content-center">
				<Col md={6}>
					<Card className="mx-auto" style={{ maxWidth: "400px" }}>
						<Card.Body>
							<h2
								style={{
									textAlign: "center",
									marginBottom: "10px",
								}}
							>
								{userRole === "backOffice"
									? "Admin Login"
									: "Travel Agent Login"}
							</h2>
							{/* Display login error as an Alert */}
							{loginError && (
								<Alert variant="danger" className="mb-3">
									{loginError}
								</Alert>
							)}
							<Form onSubmit={handleSubmit}>
								<Form.Group controlId="formBasicEmail">
									<Form.Label>Email address</Form.Label>
									<Form.Control
										type="text"
										name="email"
										placeholder="Enter email"
										value={formData.email}
										onChange={handleInputChange}
									/>
								</Form.Group>

								<Form.Group controlId="formBasicPassword">
									<Form.Label>Password</Form.Label>
									<Form.Control
										type="password"
										name="password"
										placeholder="Password"
										value={formData.password}
										onChange={handleInputChange}
									/>
								</Form.Group>
								<Button
									variant="primary"
									type="submit"
									className="mt-3"
									disabled={
										formData.email === "" ||
										formData.password === "" ||
										isLoading
									}
								>
									{isLoading && (
										<Spinner
											as="span"
											animation="border"
											style={{ margin: "0 4px" }}
											size="sm"
											role="status"
											aria-hidden="true"
										/>
									)}
									Login
								</Button>
							</Form>
						</Card.Body>
						<Card.Footer>
							<p className="mb-0">
								<a
									onClick={toggleUserRole}
									className="link-button"
									style={{ cursor: "pointer" }} // Set the hover cursor
								>
									{userRole === "backOffice"
										? "Switch to Travel Agent"
										: "Switch to Back Office"}
								</a>
							</p>
						</Card.Footer>
					</Card>
				</Col>
			</Row>
		</Container>
	);
};

export default LoginComp;
