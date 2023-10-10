import base_api from "./_base.api";

// Add train API Function
export async function addTrain(trainData) {
	try {
		const _res = await base_api.post("/trainschedule/add", trainData);
		return _res;
	} catch (error) {
		return error.response;
	}
}

// Get All Trains Function
export async function getAllTrains() {
	try {
		const _res = await base_api.get(
			"/trainschedule/all-trains?active=false"
		);
		return _res.data;
	} catch (error) {
		return error;
	}
}

// Delete Tarin Function
export async function deleteTrain(id) {
	try {
		const _res = await base_api.delete("/trainschedule/delete?id=" + id);
		return _res;
	} catch (error) {
		return error.response;
	}
}

// Update Train Function
export async function updateTrain(train) {
	try {
		const _res = await base_api.post("/trainschedule/update", train);
		return _res;
	} catch (error) {
		//console.log(error.response);
		return error.response;
	}
}

// function to get all active trains
export async function getAllActiveTrains() {
	try {
		const _res = await base_api.get("/trainschedule/active-trains");
		return _res.data;
	} catch (error) {
		return _res;
	}
}
