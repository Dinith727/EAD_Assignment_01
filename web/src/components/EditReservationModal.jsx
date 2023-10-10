/*
Form to edit a reservation
*/
import React, { useEffect, useState } from "react";
import { Modal, Button, Form, Row, Col } from "react-bootstrap";
import { getAdmin } from "../utils/api/admin";
import { getAllActiveTrains } from "../utils/api/trainShedule";
import { getAllTravelers } from "../utils/api/traveller";
import { updateReservation } from "../utils/api/reservation";

const EditReservationModal = ({ show, reservation, onSave, onClose }) => {
	const [editedReservation, setEditedReservation] = useState({
		...reservation,
	});
// function to update the editedReservation state.
	const handleChange = (e) => {
		const { name, value } = e.target;
		setEditedReservation({
			...editedReservation,
			[name]: value,
		});
	};

	const handleSave = async () => {
		// onSave(editedReservation);
		const _res = await updateReservation(editedReservation);
		console.log(_res);
		onClose();
	};

	const [users, setUsers] = useState([]);
	const [trains, setTrains] = useState([]);
//fetch all travellers
	useEffect(() => {
		async function getUsers() {
			const _res = await getAllTravelers();
			if (_res.data) {
				setUsers(_res.data);
			}
		}
		//fetch all trains
		async function getTrains() {
			const _res = await getAllActiveTrains();
			setTrains(_res.data);
		}
		async function getTravelAgent() {
			const _res = await getAdmin();
			setEditedReservation(() => ({
				...editedReservation,
				travelAgent: _res.data._id,
			}));
			//console.log(_res.data._id);
		}
		getTravelAgent();
		getUsers();
		getTrains();
	}, []);
	//console.log(editedReservation);
//modal for the reservation edit
	return (
		<Modal show={show} onHide={onClose}>
			<Modal.Header closeButton>
				<Modal.Title>Edit Reservation</Modal.Title>
			</Modal.Header>
			<Modal.Body>
				<Form>
					<Row>
						<Col>
							<Form.Group controlId="date">
								<Form.Label>Date</Form.Label>
								<Form.Control
									type="date"
									name="date"
									required
									value={editedReservation.date}
									onChange={handleChange}
								/>
							</Form.Group>
						</Col>
						<Col>
							<Form.Group controlId="train">
								<Form.Label>Select Train</Form.Label>
								<Form.Control
									as="select"
									name="train"
									value={editedReservation._id}
									onChange={handleChange}
									required
								>
									<option value="">Select Train</option>
									{trains.map((train) => (
										<option
											key={train._id}
											value={train._id}
										>
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
							value={editedReservation.userID}
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
				</Form>
			</Modal.Body>
			<Modal.Footer>
				<Button variant="secondary" onClick={onClose}>
					Close
				</Button>
				<Button variant="primary" onClick={handleSave}>
					Save Changes
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default EditReservationModal;
