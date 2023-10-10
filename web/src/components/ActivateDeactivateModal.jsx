//Activate Deactivate Confirmation
import React from "react";
import { Modal, Button } from "react-bootstrap";

const ActivateDeactivateModal = ({ show, onHide, onConfirm, action }) => {
	return (
		<Modal show={show} onHide={onHide}>
			<Modal.Header closeButton>
				<Modal.Title>Confirm {action}</Modal.Title>
			</Modal.Header>
			<Modal.Body>
				Are you sure you want to {action.toLowerCase()} this user?
			</Modal.Body>
			<Modal.Footer>
				<Button variant="secondary" onClick={onHide}>
					Cancel
				</Button>
				<Button variant="primary" onClick={onConfirm}>
					{action}
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default ActivateDeactivateModal;
