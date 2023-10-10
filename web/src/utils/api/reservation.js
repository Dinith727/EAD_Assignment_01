/*
API requests for reservations
*/

import base_api from "./_base.api";
// API route for get all the resetvations
export async function getResevations() {
	try {
		const _res = await base_api.get("/reservation/all?type=draft");
		return _res;
	} catch (error) {
		return error.response;
	}
}
// API request for adding a reservation
export async function addReservation(formData) {
	try {
		const _res = await base_api.post("/reservation/add", formData);
		window.location.reload();
		return _res;
	} catch (error) {
		return data;
	}
}
// API request for delete a reservation
export async function deleteReservation(_id) {
	try {
		const _res = await base_api.delete("/reservation/delete?id=" + _id);
		window.location.reload();
		return _res;
	} catch (error) {
		return error;
	}
}
//API request for adding a reservation
export async function updateReservation(editedData) {
	try {
		const _res = await base_api.post("/reservation/update", editedData);
		return _res;
	} catch (error) {
		return error;
	}
}
