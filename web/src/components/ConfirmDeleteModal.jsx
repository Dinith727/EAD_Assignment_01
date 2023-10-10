//Delete Confirmation
import React from "react";
import { Modal, Button } from "react-bootstrap";

const ConfirmDeleteModal = ({ show, onHide, onDelete }) => {
	return (
		<Modal show={show} onHide={onHide}>
			<Modal.Header closeButton>
				<Modal.Title>Confirm Delete</Modal.Title>
			</Modal.Header>
			<Modal.Body>Are you sure you want to delete this user?</Modal.Body>
			<Modal.Footer>
				<Button variant="secondary" onClick={onHide}>
					Cancel
				</Button>
				<Button variant="danger" onClick={onDelete}>
					Delete
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default ConfirmDeleteModal;
