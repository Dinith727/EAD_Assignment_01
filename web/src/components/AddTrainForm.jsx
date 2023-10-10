import React, { useState } from "react";
import { Form, Button, Row, Col, Alert, FormText } from "react-bootstrap";
import { addTrain } from "../utils/api/trainShedule";

const AddTrainForm = () => {
	const [trainData, setTrainData] = useState({
		trainName: "",
		from: "",
		to: "",
		startTime: "",
		arrivalTime: "",
		price: { bussiness: "", economic: "" },
	});

	const [errorMessage, setErrorMessage] = useState("");
	const [successMessage, setSuccessMessage] = useState("");
	const [addTrainError, setAddTrainError] = useState("");

	const handleChange = (e) => {
		setAddTrainError("");
		const { name, value } = e.target;
		if (name === "bussiness" || name === "economic") {
			setTrainData({
				...trainData,
				price: {
					...trainData.price,
					[name]: value,
				},
			});
		} else {
			setTrainData({
				...trainData,
				[name]: value,
			});
		}
	};

	// const handleSubmit = async (e) => {
	// 	e.preventDefault();
	// 	try {
	// 		const _res = await addTrain(trainData);
	// 		if (_res.status === 200) {
	// 			setSuccessMessage("Train has been added successfully!");
	// 			setErrorMessage("");
	// 		} else {
	// 			setErrorMessage(_res.statusText);
	// 			setSuccessMessage("");
	// 		}
	// 		window.location.reload();
	// 	} catch (error) {
	// 		return error;
	// 	}
	// };
	const handleSubmit = async (e) => {
		e.preventDefault();

		// Convert the departure and arrival times to Date objects
		const departureTime = new Date(trainData.startTime);
		const arrivalTime = new Date(trainData.arrivalTime);

		// Check if departure time is greater than arrival time
		if (departureTime >= arrivalTime) {
			setAddTrainError("Departure time must be before arrival time.");
			setSuccessMessage("");
			setErrorMessage("");
			return;
		}

		try {
			const _res = await addTrain(trainData);
			if (_res.status === 200) {
				setSuccessMessage("Train has been added successfully!");
				setErrorMessage("");
				setAddTrainError(""); // Clear the error if the submission is successful
			} else {
				setErrorMessage(_res.statusText);
				setSuccessMessage("");
			}
			window.location.reload();
		} catch (error) {
			return error;
		}
	};

	return (
		<Form onSubmit={handleSubmit} style={{ textAlign: "left" }}>
			<h2 style={{ textAlign: "center" }}>Add Train</h2>
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
			<Form.Group controlId="trainName">
				<Form.Label>Train Name</Form.Label>
				<Form.Control
					type="text"
					name="trainName"
					value={trainData.trainName}
					onChange={handleChange}
					required
				/>
			</Form.Group>

			<Row>
				<Col>
					<Form.Group controlId="from">
						<Form.Label>Departure Station</Form.Label>
						<Form.Control
							type="text"
							name="from"
							value={trainData.from}
							onChange={handleChange}
							required
						/>
					</Form.Group>
				</Col>
				<Col>
					<Form.Group controlId="to">
						<Form.Label>Arrival Station</Form.Label>
						<Form.Control
							type="text"
							name="to"
							value={trainData.to}
							onChange={handleChange}
							required
						/>
					</Form.Group>
				</Col>
			</Row>

			<Row>
				<Col>
					<Form.Group controlId="startTime">
						<Form.Label>Departure Time</Form.Label>
						<Form.Control
							type="datetime-local"
							name="startTime"
							value={trainData.startTime}
							onChange={handleChange}
							required
						/>
					</Form.Group>
				</Col>
				<Col>
					<Form.Group controlId="arrivalTime">
						<Form.Label>Arrival Time</Form.Label>
						<Form.Control
							type="datetime-local"
							name="arrivalTime"
							value={trainData.arrivalTime}
							onChange={handleChange}
							required
						/>
					</Form.Group>
				</Col>
				{addTrainError && (
					<FormText className="text-danger">{addTrainError}</FormText>
				)}
			</Row>
			<Row>
				<Col>
					<Form.Group controlId="priceBussiness">
						<Form.Label>Price-Business</Form.Label>
						<Form.Control
							type="number"
							name="bussiness"
							value={trainData.price.bussiness}
							onChange={handleChange}
							required
						/>
					</Form.Group>
				</Col>
				<Col>
					<Form.Group controlId="priceEconomic">
						<Form.Label>Price-Economy</Form.Label>
						<Form.Control
							type="number"
							name="economic"
							value={trainData.price.economic}
							onChange={handleChange}
							required
						/>
					</Form.Group>
				</Col>
			</Row>

			<Button
				variant="primary"
				type="submit"
				style={{ marginTop: "20px" }}
			>
				Add Train
			</Button>
		</Form>
	);
};

export default AddTrainForm;
