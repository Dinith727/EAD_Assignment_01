import base_api from "./_base.api";

export async function addTrain(trainData) {
	try {
		const _res = await base_api.post("/trainschedule/add", trainData);
		return _res;
	} catch (error) {
		return error.response;
	}
}

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

export async function deleteTrain(id) {
	try {
		const _res = await base_api.delete("/trainschedule/delete?id=" + id);
		return _res;
	} catch (error) {
		return error.response;
	}
}

export async function updateTrain(train) {
	try {
		const _res = await base_api.post("/trainschedule/update", train);
		return _res;
	} catch (error) {
		//console.log(error.response);
		return error.response;
	}
}

export async function getAllActiveTrains() {
	try {
		const _res = await base_api.get("/trainschedule/active-trains");
		return _res.data;
	} catch (error) {
		return _res;
	}
}
