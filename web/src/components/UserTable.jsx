import React, { useState, useEffect } from "react";
import { Table, Button, Alert } from "react-bootstrap";
import { activateAdmin, deleteAdmin, getAllAdmins } from "../utils/api/admin";
import EditAdminModal from "./EditAdminModal";
import ConfirmDeleteModal from "./ConfirmDeleteModal";
import ActivateDeactivateModal from "./ActivateDeactivateModal";

const UserTable = () => {
	const [users, setUsers] = useState([]);
	const [error, setError] = useState(null);
	const [showEditModal, setShowEditModal] = useState(false);
	const [selectedUserId, setSelectedUserId] = useState("");
	const [isDeleteModalVisible, setDeleteModalVisible] = useState(false);
	const [
		isActivateDeactivateModalVisible,
		setActivateDeactivateModalVisible,
	] = useState(false);
	const [activateDeactivateAction, setActivateDeactivateAction] =
		useState("Activate");

	const onDeleteClick = (userId) => {
		setSelectedUserId(userId);
		setDeleteModalVisible(true);
	};

	const onActivateDeactivateClick = (userId, isActive) => {
		setSelectedUserId(userId);
		setActivateDeactivateAction(isActive ? "Deactivate" : "Activate");
		setActivateDeactivateModalVisible(true);
	};

	const handleDeleteConfirm = async () => {
		try {
			await deleteAdmin(selectedUserId);
			window.location.reload();
		} catch (error) {
			setError("Error deleting user. Please try again later.");
		}
		setDeleteModalVisible(false);
	};

	const handleActivateDeactivateConfirm = async () => {
		try {
			const isActive = activateDeactivateAction === "Activate";
			await activateAdmin(selectedUserId, !isActive);
			window.location.reload();
		} catch (error) {
			setError("Error toggling user activation. Please try again later.");
		}
		setActivateDeactivateModalVisible(false);
	};

	useEffect(() => {
		async function getAll() {
			try {
				const _res = await getAllAdmins();
				if (_res && _res.data && Array.isArray(_res.data)) {
					setUsers(_res.data);
				} else {
					setError("Error fetching user data. Invalid response.");
				}
			} catch (error) {
				setError("Error fetching user data. Please try again later.");
			}
		}
		getAll();
	}, []);

	return (
		<div>
			{error && <Alert variant="danger">{error}</Alert>}
			<Table striped bordered hover>
				<thead>
					<tr>
						<th>Username</th>
						<th>Role</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					{users.map((user) => (
						<tr key={user._id}>
							<td>{user.username}</td>
							<td>{user.role}</td>
							<td>
								<Button
									variant="danger"
									onClick={() => onDeleteClick(user._id)}
									style={{ marginRight: "20px" }}
								>
									Delete
								</Button>
								<Button
									variant={user.active ? "success" : "danger"} // Set button variant based on user.active
									onClick={() =>
										onActivateDeactivateClick(
											user._id,
											user.active
										)
									}
									style={{ marginRight: "20px" }}
								>
									{user.active ? "Deactivate" : "Activate"}
								</Button>
								<Button
									variant="primary"
									onClick={() => setShowEditModal(true)}
									style={{ marginRight: "20px" }}
								>
									Edit
								</Button>
								<EditAdminModal
									show={showEditModal}
									onHide={() => setShowEditModal(false)}
									Id={user._id}
									username={user.username}
									role={user.role}
								/>
							</td>
						</tr>
					))}
				</tbody>
			</Table>
			<ConfirmDeleteModal
				show={isDeleteModalVisible}
				onHide={() => setDeleteModalVisible(false)}
				onDelete={handleDeleteConfirm}
			/>
			<ActivateDeactivateModal
				show={isActivateDeactivateModalVisible}
				onHide={() => setActivateDeactivateModalVisible(false)}
				onConfirm={handleActivateDeactivateConfirm}
				action={activateDeactivateAction}
			/>
		</div>
	);
};

export default UserTable;
