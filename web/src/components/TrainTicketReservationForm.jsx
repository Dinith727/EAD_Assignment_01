import React, { useState, useEffect } from "react";
import { Form, Button, Row, Col, Alert } from "react-bootstrap"; // Import Alert component
import { getAllTravelers } from "../utils/api/traveller";
import { getAllActiveTrains } from "../utils/api/trainShedule";
import useAuth from "../utils/providers/AuthProvider";
import { getAdmin } from "../utils/api/admin";
import { addReservation } from "../utils/api/reservation";

const TrainTicketReservationForm = () => {
	const [formData, setFormData] = useState({
		date: "",
		train: "",
		userID: "",
		travelAgent: "",
		agentNote: "",
	});

	const [users, setUsers] = useState([]);
	const [trains, setTrains] = useState([]);
	const [successMessage, setSuccessMessage] = useState(""); // State for success message

	useEffect(() => {
		async function getUsers() {
			const _res = await getAllTravelers();
			if (_res.data) {
				setUsers(_res.data);
			}
		}
		async function getTrains() {
			const _res = await getAllActiveTrains();
			setTrains(_res.data);
		}
		async function getTravelAgent() {
			const _res = await getAdmin();
			setFormData((prevData) => ({
				...prevData,
				travelAgent: _res.data._id,
			}));
		}
		getTravelAgent();
		getUsers();
		getTrains();
	}, []);

	const handleChange = (e) => {
		const { name, value } = e.target;
		setFormData({
			...formData,
			[name]: value,
		});
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		const _res = await addReservation(formData);
		if (_res.data) {
			// If the reservation is added successfully, display the success message
			setSuccessMessage("Ticket reserved successfully!");
			// You can optionally reset the form here
			setFormData({
				date: "",
				train: "",
				userID: "",
				travelAgent: "",
				agentNote: "",
			});
		}
	};

	return (
		<Form onSubmit={handleSubmit} style={{ textAlign: "left" }}>
			<h2
				style={{
					marginBottom: "50px",
					textAlign: "center",
					marginTop: "20px",
				}}
			>
				Train Ticket Reservation
			</h2>

			{/* Display the success message */}
			{successMessage && (
				<Alert variant="success">{successMessage}</Alert>
			)}

			<Row>
				<Col>
					<Form.Group controlId="date">
						<Form.Label>Booking Date</Form.Label>
						<Form.Control
							type="date"
							name="date"
							value={formData.date}
							onChange={handleChange}
							required
						/>
					</Form.Group>
				</Col>
				<Col>
					<Form.Group controlId="train">
						<Form.Label>Select Train</Form.Label>
						<Form.Control
							as="select"
							name="train"
							value={formData.train}
							onChange={handleChange}
							required
						>
							<option value="">Select Train</option>
							{trains.map((train) => (
								<option key={train._id} value={train._id}>
									{train.trainName}
								</option>
							))}
						</Form.Control>
					</Form.Group>
				</Col>
			</Row>

			<Form.Group controlId="userID">
				<Form.Label>Select User</Form.Label>
				<Form.Control
					as="select"
					name="userID"
					value={formData.userID}
					onChange={handleChange}
					required
				>
					<option value="">Select User</option>
					{users.map((user) => (
						<option key={user._id} value={user._id}>
							{user.nic}
						</option>
					))}
				</Form.Control>
			</Form.Group>

			<Button
				variant="primary"
				type="submit"
				style={{ marginTop: "20px" }}
			>
				Reserve Ticket
			</Button>
		</Form>
	);
};

export default TrainTicketReservationForm;
