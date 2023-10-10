import React, { useState } from "react";
import { Button, Modal, Form, Alert } from "react-bootstrap";
import { updateTrain } from "../utils/api/trainShedule";

const EditTrainModal = ({ isOpen, onClose, train }) => {
	const [editedTrainData, setEditedTrainData] = useState({ ...train });
	const [errorMessage, setErrorMessage] = useState("");
	const [successMessage, setSuccessMessage] = useState("");

	const handleSave = async () => {
		try {
			const _res = await updateTrain(editedTrainData);
			if (_res.status === 200) {
				setErrorMessage("");
				//console.log(_res);
				setSuccessMessage("Train has been updated successfully!");
			} else {
				setSuccessMessage("");
				setErrorMessage(_res.statusText);
			}
			window.location.reload();
		} catch (error) {
			return error;
		}
	};

	const handlePublish = async () => {
		try {
			const _res = await updateTrain({
				...editedTrainData,
				published: true,
				active: true,
			});

			if (_res.status === 200) {
				setErrorMessage("");
				setSuccessMessage("Train has been published successfully!");
				onClose();
				window.location.reload();
			} else {
				setSuccessMessage("");
				setErrorMessage(_res.statusText);
			}
		} catch (error) {
			return error;
		}
	};

	const handleChange = (e) => {
		const { name, value } = e.target;
		setEditedTrainData({
			...editedTrainData,
			[name]: value,
		});
	};

	const handleClose = () => {
		setErrorMessage("");
		setSuccessMessage("");
		onClose();
	};

	return (
		<Modal show={isOpen} onHide={handleClose}>
			<Modal.Header closeButton>
				<Modal.Title>Edit Train</Modal.Title>
			</Modal.Header>
			<Modal.Body>
				{errorMessage && (
					<Alert variant="danger" className="mb-3">
						{errorMessage}
					</Alert>
				)}
				{successMessage && (
					<Alert variant="success" className="mb-3">
						{successMessage}
					</Alert>
				)}
				<Button
					variant="primary"
					style={{ marginBottom: "30px", backgroundColor: "green" }}
					onClick={handlePublish}
					disabled={editedTrainData.published}
				>
					Publish Train
				</Button>
				<Form>
					<Form.Group controlId="editTrainName">
						<Form.Label>Train Name</Form.Label>
						<Form.Control
							type="text"
							name="trainName"
							value={editedTrainData.trainName}
							onChange={handleChange}
						/>
					</Form.Group>
					<Form.Group controlId="editFrom">
						<Form.Label>From</Form.Label>
						<Form.Control
							type="text"
							name="from"
							value={editedTrainData.from}
							onChange={handleChange}
						/>
					</Form.Group>
					<Form.Group controlId="editTo">
						<Form.Label>To</Form.Label>
						<Form.Control
							type="text"
							name="to"
							value={editedTrainData.to}
							onChange={handleChange}
						/>
					</Form.Group>
				</Form>
			</Modal.Body>
			<Modal.Footer>
				<Button variant="secondary" onClick={handleClose}>
					Close
				</Button>
				<Button variant="primary" onClick={handleSave}>
					Save Changes
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default EditTrainModal;
