import React, { useState, useEffect } from "react";
import { Form, Button, Modal } from "react-bootstrap";
import {
	activateTraveller,
	deactivateTraveller,
	deleteTraveler,
	getAllTravelers,
} from "../utils/api/traveller";
import EditTravelerModal from "./EditTravelerModal";

const SearchComponent = () => {
	const [selectedId, setSelectedId] = useState("");
	const [selectedUserActive, setSelectedUserActive] = useState(true);
	const [travelers, setTravellers] = useState([]);
	const [showEditModal, setShowEditModal] = useState(false);
	const [selectedAction, setSelectedAction] = useState("activate");
	const [showDeleteModal, setShowDeleteModal] = useState(false);
	const [showActivateDeactivateModal, setShowActivateDeactivateModal] =
		useState(false);

	useEffect(() => {
		async function getAll() {
			const _res = await getAllTravelers();
			setTravellers(_res.data);
		}
		getAll();
	}, []);

	const handleChangeNIC = (e) => {
		const selectedIndex = e.target.value;
		if (selectedIndex !== "") {
			const selectedUser = travelers[selectedIndex];
			setSelectedId(selectedUser._id);
			setSelectedUserActive(selectedUser.active);
		}
	};

	const handleEditClick = () => {
		setShowEditModal(true);
	};

	const handleDeleteClick = () => {
		setShowDeleteModal(true);
	};

	const handleActivateDeactivateClick = () => {
		setShowActivateDeactivateModal(true);
	};

	const confirmDelete = () => {
		try {
			deleteTraveler(selectedId);
			window.location.reload();
		} catch (error) {
			return error;
		}
		setShowDeleteModal(false);
	};

	const confirmActivateDeactivate = () => {
		try {
			if (selectedUserActive) {
				deactivateTraveller(selectedId);
			} else {
				activateTraveller(selectedId);
			}
			setSelectedUserActive((prevActive) => !prevActive);
		} catch (error) {
			return error;
		}
		setShowActivateDeactivateModal(false);
	};

	return (
		<div
			className="d-flex align-items-center"
			style={{ width: "550px", marginTop: "50px", marginBottom: "50px" }}
		>
			<Form.Group className="m-2">
				<Form.Control
					as="select"
					style={{ width: "250px" }}
					onChange={handleChangeNIC}
				>
					<option value="">Select NIC</option>
					{travelers.map((user, index) => (
						<option key={user._id} value={index}>
							{user.nic}
						</option>
					))}
				</Form.Control>
			</Form.Group>

			<Button
				variant="danger"
				className="m-2"
				onClick={handleDeleteClick}
				disabled={!selectedId}
			>
				Delete
			</Button>

			<Button
				variant={selectedUserActive ? "warning" : "success"}
				className="m-2"
				disabled={!selectedId}
				onClick={handleActivateDeactivateClick}
			>
				{selectedUserActive ? "Deactivate" : "Activate"}
			</Button>

			<Modal
				show={showDeleteModal}
				onHide={() => setShowDeleteModal(false)}
			>
				<Modal.Header closeButton>
					<Modal.Title>Confirm Delete</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					Are you sure you want to delete this traveler?
				</Modal.Body>
				<Modal.Footer>
					<Button
						variant="secondary"
						onClick={() => setShowDeleteModal(false)}
					>
						Cancel
					</Button>
					<Button variant="danger" onClick={confirmDelete}>
						Delete
					</Button>
				</Modal.Footer>
			</Modal>

			<Modal
				show={showActivateDeactivateModal}
				onHide={() => setShowActivateDeactivateModal(false)}
			>
				<Modal.Header closeButton>
					<Modal.Title>Confirm Activate/Deactivate</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					{selectedUserActive
						? "Are you sure you want to deactivate this traveler?"
						: "Are you sure you want to activate this traveler?"}
				</Modal.Body>
				<Modal.Footer>
					<Button
						variant="secondary"
						onClick={() => setShowActivateDeactivateModal(false)}
					>
						Cancel
					</Button>
					<Button
						variant={selectedUserActive ? "warning" : "success"}
						onClick={confirmActivateDeactivate}
					>
						{selectedUserActive ? "Deactivate" : "Activate"}
					</Button>
				</Modal.Footer>
			</Modal>
		</div>
	);
};

export default SearchComponent;
