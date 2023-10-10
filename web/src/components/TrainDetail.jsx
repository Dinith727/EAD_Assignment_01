// train details component
import React, { useState } from "react";
import { Button, Alert, Badge } from "react-bootstrap";
import { deleteTrain } from "../utils/api/trainShedule";
import EditTrainModal from "./EditTrainModal";
import ConfirmDeleteModal from "./ConfirmDeleteModal"; // Import the ConfirmDeleteModal component

const TrainDetail = ({ train }) => {
	const [editModalOpen, setEditModalOpen] = useState(false);
	const [deleteModalOpen, setDeleteModalOpen] = useState(false); // State to control the delete confirmation modal
	const [editedTrainData, setEditedTrainData] = useState({ ...train });
	const [errorMessage, setErrorMessage] = useState("");
	const [successMessage, setSuccessMessage] = useState("");

	// handle edits 
	const handleEdit = () => {
		setEditModalOpen(true);
	};

	const handleEditModalClose = () => {
		setEditModalOpen(false);
	};

	const handleDelete = async () => {
		setDeleteModalOpen(true);
	};

	const handleDeleteModalClose = () => {
		setDeleteModalOpen(false);
	};

	// delete train funtion
	const handleConfirmDelete = async () => {
		try {
			const _res = await deleteTrain(train._id);
			if (_res.status === 200) {
				setSuccessMessage("Train has been deleted successfully!");
				setErrorMessage("");
				window.location.reload();
			} else {
				setErrorMessage("This train cannot be deleted!");
				setSuccessMessage("");
			}
		} catch (error) {
			setErrorMessage("Error deleting the train.");
			setSuccessMessage("");
		}
		setDeleteModalOpen(false);
	};

	return (
		<>
			{errorMessage && (
				<Alert
					variant="danger"
					className="mt-2"
					onClose={() => setErrorMessage("")}
					dismissible
				>
					{errorMessage}
				</Alert>
			)}
			{successMessage && (
				<Alert
					variant="success"
					className="mt-2"
					onClose={() => setSuccessMessage("")}
					dismissible
				>
					{successMessage}
				</Alert>
			)}
			<tr className="train-detail">
				<td>
					{train.published ? (
						<Badge bg="success" style={{ marginRight: "5px" }}>
							Y
						</Badge>
					) : (
						<Badge bg="warning" style={{ marginRight: "5px" }}>
							N
						</Badge>
					)}
					<span>{train.trainName}</span>
				</td>
				<td>{train.from}</td>
				<td>{train.to}</td>
				<td>{new Date(train.startTime).toLocaleString()}</td>
				<td>{new Date(train.arrivalTime).toLocaleString()}</td>
				<td>
					<Button variant="primary" onClick={handleEdit}>
						Edit
					</Button>
				</td>
				<td>
					<Button variant="danger" onClick={handleDelete}>
						Delete
					</Button>
				</td>
				<EditTrainModal
					isOpen={editModalOpen}
					onClose={handleEditModalClose}
					trainData={editedTrainData}
					train={train}
				/>
			</tr>
			{/* Render the ConfirmDeleteModal */}
			<ConfirmDeleteModal
				show={deleteModalOpen}
				onClose={handleDeleteModalClose}
				onDelete={handleConfirmDelete}
			/>
		</>
	);
};

export default TrainDetail;
