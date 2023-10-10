import base_api from "./_base.api";

export async function addTraveler(formData) {
	try {
		const _res = await base_api.post("/traveller/add", formData);
		window.location.reload();
		return _res;
	} catch (error) {
		return error.response;
	}
}

export async function getAllTravelers() {
	try {
		const _res = await base_api.get("/traveller/all?active=true");
		return _res.data;
	} catch (error) {
		return error;
	}
}

export async function deleteTraveler(_id) {
	try {
		const _res = await base_api.delete("/traveller/delete?id=" + _id);
		return _res;
	} catch (error) {
		return error;
	}
}

export async function getTraveler(_id) {
	try {
		const _res = await base_api.get("/traveller?id=" + _id);
		return _res;
	} catch (error) {
		return error;
	}
}

export async function activateTraveller(_id) {
	//console.log(_id);
	try {
		const _res = await base_api.post(
			"/traveller/activate?id=" + _id + "&active=true"
		);
		return _res;
	} catch (error) {
		return error;
	}
}
export async function deactivateTraveller(_id) {
	//console.log(_id);
	try {
		const _res = await base_api.post(
			"/traveller/activate?id=" + _id + "&active=false"
		);
		return _res;
	} catch (error) {
		return error;
	}
}
