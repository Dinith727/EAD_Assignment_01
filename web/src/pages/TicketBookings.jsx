/*
This page shows the reservation form and the reservation
table that has been implemented inside the components. those
components will be called inside this TicketBookings function
*/

import { useEffect, useState } from "react";
import Header from "../components/Header";
import ReservationTable from "../components/ReservationTable";
import TrainTicketReservationForm from "../components/TrainTicketReservationForm";
import { getResevations } from "../utils/api/reservation";

function TicketBookings() {
	const [reservations, setReservations] = useState([]);
	const [reservationsData, setReservationsData] = useState([
		{
			_id: "",
			date: "",
			status: "",
			train: [
				{
					from: "",
					to: "",
				},
			],
		},
	]);
//fetch reservation data
	useEffect(() => {
		async function getReservationsData() {
			const _res = await getResevations();
			if (_res.data) {
				//console.log(_res.data);
				const res1 = _res.data.replaceAll("ObjectId(", "");
				const res2 = res1.replaceAll("ISODate(", "");
				const res3 = res2.replaceAll(")", "");
				const res4 = res3.replaceAll(")", "");
				setReservationsData(JSON.parse(res4));
			}
		}
		getReservationsData();
	}, []);
//delete a reservation
	const deleteReservation = (index) => {
		const updatedReservations = [...reservations];
		updatedReservations.splice(index, 1);
		setReservations(updatedReservations);
	};

	return (
		<>
			<Header />
			<center>
				<div style={{ width: "500px" }}>
					<TrainTicketReservationForm />
				</div>

				<div
					style={{
						width: "1000px",
						marginTop: "20px",
						marginBottom: "50px",
					}}
				>
					<h2 style={{ marginBottom: "20px" }}>Reservations</h2>
					<ReservationTable
						reservations={reservationsData}
						onDelete={deleteReservation}
					/>
				</div>
			</center>
		</>
	);
}

export default TicketBookings;
