import React, { useState } from "react";
import { Table, Button, Alert } from "react-bootstrap"; // Import Alert component
import EditReservationModal from "./EditReservationModal";
import ConfirmDeleteModal from "./ConfirmDeleteModal";
import { deleteReservation } from "../utils/api/reservation";

const ReservationTable = ({ reservations }) => {
	const [showEditModal, setShowEditModal] = useState(false);
	const [selectedReservation, setSelectedReservation] = useState(null);
	const [showDeleteModal, setShowDeleteModal] = useState(false);
	const [reservationToDelete, setReservationToDelete] = useState(null);
	const [deleteError, setDeleteError] = useState(null); // State to track delete errors

	const handleEditClick = (reservation) => {
		setSelectedReservation(reservation);
		setShowEditModal(true);
	};

	const handleEditModalClose = () => {
		setShowEditModal(false);
		setSelectedReservation(null);
	};

	const handleDeleteClick = (reservation) => {
		setReservationToDelete(reservation);
		setShowDeleteModal(true);
	};

	const handleDeleteModalClose = () => {
		setShowDeleteModal(false);
		setReservationToDelete(null);
		//setDeleteError(null); // Clear the delete error when closing the modal
	};

	const handleDelete = async () => {
		if (reservationToDelete) {
			try {
				const _res = await deleteReservation(reservationToDelete._id);
				// Handle the response as needed
				//console.log(_res);

				if (_res.response.status === 400) {
					//console.log("Deleted Reservation:", _res.response.status);
					setDeleteError("This train can not be deleted");
				}
				// Close the modal after deleting
				handleDeleteModalClose();
			} catch (error) {
				// Handle delete errors and set the deleteError state

				setDeleteError(
					"Error deleting reservation. Please try again later."
				);
			}
		}
	};

	return (
		<div>
			{
				<Alert variant="danger" show={deleteError != null}>
					{deleteError}
				</Alert>
			}

			<Table striped bordered hover>
				<thead>
					<tr>
						<th>#</th>
						<th>Departure Station</th>
						<th>Arrival Station</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					{reservations.map((reservation, index) => (
						<tr key={index}>
							<td>{index + 1}</td>
							<td>{reservation.train[0].from}</td>
							<td>{reservation.train[0].to}</td>
							<td>
								<Button
									variant="primary"
									onClick={() => handleEditClick(reservation)}
								>
									Edit
								</Button>{" "}
								<Button
									variant="danger"
									onClick={() =>
										handleDeleteClick(reservation)
									}
								>
									Delete
								</Button>
							</td>
						</tr>
					))}
				</tbody>
			</Table>
			<EditReservationModal
				show={showEditModal}
				reservation={selectedReservation}
				onSave={(editedReservation) => {
					// Handle saving the edited reservation data here
					console.log("Edited Reservation Data:", editedReservation);
					// Close the modal after saving
					handleEditModalClose();
				}}
				onClose={handleEditModalClose}
			/>
			{/* Render the ConfirmDeleteModal */}
			<ConfirmDeleteModal
				show={showDeleteModal}
				onHide={handleDeleteModalClose}
				onDelete={handleDelete}
			/>
		</div>
	);
};

export default ReservationTable;
