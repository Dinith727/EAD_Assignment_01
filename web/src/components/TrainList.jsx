// Train List Component
import React, { useEffect, useState } from "react";
import TrainDetail from "./TrainDetail";
import { getAllTrains } from "../utils/api/trainShedule";

const TrainList = () => {
	const [trains, setTrains] = useState([
		{
			_id: "",
			trainName: "",
			from: "",
			to: "",
			startTime: "",
			arrivalTime: "",
			price: {
				bussiness: "",
				economic: "",
			},
			active: false,
			published: false,
			_isDeleted: false,
		},
	]);

	// populate data to the list
	useEffect(() => {
		async function getTrains() {
			const _res = await getAllTrains();
			setTrains(_res.data);
		}
		getTrains();
	}, []);

	return (
		<div className="train-list">
			<table className="table">
				<thead>
					<tr>
						<th>Train Name</th>
						<th>From</th>
						<th>To</th>

						<th>Arrival Time</th>
						<th>Departure Time</th>
						<th>Edit</th>
						<th>Delete</th>
					</tr>
				</thead>
				<tbody>
					{trains.map((train, index) => (
						<TrainDetail key={index} train={train} />
					))}
				</tbody>
			</table>
		</div>
	);
};

export default TrainList;
