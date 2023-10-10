import base_api from "./_base.api";

export async function addUser(formData) {
	try {
		const _res = await base_api.post("/admin/add", formData);
		return _res;
		//console.log(_res);
	} catch (error) {
		//console.log(error.response.data);
		return error.response;
	}
}

export async function getAllAdmins() {
	try {
		const _res = await base_api.get("/admin/all?active=true");
		return _res.data;
	} catch (error) {
		return error;
	}
}

export async function deleteAdmin(id) {
	try {
		const _res = await base_api.delete("/admin/delete?id=" + id);
	} catch (error) {
		return error;
	}
}

export async function activateAdmin(id, state) {
	console.log(id, state);
	try {
		const _res = await base_api.post(
			"admin/activate?id=" + id + "&active=" + !state
		);
		console.log(_res);
		return _res;
	} catch (error) {
		//console.log(error);
		return error;
	}
}

export async function updateAdmin(id, username, role) {
	const reqBody = {
		_id: id,
		username: username,
		role: role,
	};
	try {
		const _res = await base_api.post("/admin/update", reqBody);
		console.log(_res);
	} catch (error) {
		return error;
	}
}
export async function getAdmin() {
	try {
		const _res = await base_api.get("/admin");
		return _res.data;
	} catch (error) {
		return _res;
	}
}
