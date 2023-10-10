import React, { useState } from "react";
import { Modal, Button, Form, Row, Col } from "react-bootstrap";
import { updateAdmin } from "../utils/api/admin";

const EditAdminModal = ({ show, onHide, Id, username, role }) => {
	const [userType, setUserType] = useState(role);
	const [email, setEmail] = useState(username);

	const handleUserTypeChange = (e) => {
		setUserType(e.target.value);
	};
	const handleEmailChange = (e) => {
		setEmail(e.target.value);
	};
	const handleSave = () => {
		updateAdmin(Id, email, userType);
		onHide();
		window.location.reload();
	};

	return (
		<Modal show={show} onHide={onHide} centered>
			<Modal.Header closeButton>
				<Modal.Title>Edit User</Modal.Title>
			</Modal.Header>
			<Modal.Body>
				<Form style={{ textAlign: "left" }}>
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
						<Form.Label>Email:</Form.Label>
						<Form.Control
							type="email"
							placeholder="Enter email"
							value={email}
							onChange={handleEmailChange}
							required
						/>
					</Form.Group>
				</Form>
			</Modal.Body>
			<Modal.Footer>
				<Button variant="secondary" onClick={onHide}>
					Close
				</Button>
				<Button variant="primary" onClick={handleSave}>
					Save Changes
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default EditAdminModal;
