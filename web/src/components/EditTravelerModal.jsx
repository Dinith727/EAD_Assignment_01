//Edit Traveller Profile
import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import { getTraveler } from "../utils/api/traveller";

const EditTravelerModal = ({ show, onHide, id, nic }) => {
	//console.log(id);
	const [NIC, setNIC] = useState("");
	const [travelerData, setTravelerData] = useState({
		_id: "",
		nic: "",
		email: "",
		fullName: "",
		password: "",
		active: true,
		_isDeleted: false,
	});

	const handleNICChange = (e) => {
		setNIC(e.target.value);
	};

	const handleUpdate = () => {
		// Construct the updated traveler data
		const updatedTraveler = {
			NIC: NIC,
		};

		// Call the onUpdate callback to update the traveler
		onUpdate(updatedTraveler);

		// Close the modal
		onHide();
	};

	return (
		<Modal show={show} onHide={onHide} centered>
			<Modal.Header closeButton>
				<Modal.Title>Edit Traveler</Modal.Title>
			</Modal.Header>
			<Modal.Body>
				<Form>
					<Form.Group controlId="fullName">
						<Form.Label>NIC</Form.Label>
						<Form.Control
							type="text"
							value={NIC}
							onChange={handleNICChange}
						/>
					</Form.Group>
				</Form>
			</Modal.Body>
			<Modal.Footer>
				<Button variant="secondary" onClick={onHide}>
					Close
				</Button>
				<Button variant="primary" onClick={handleUpdate}>
					Save Changes
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default EditTravelerModal;
