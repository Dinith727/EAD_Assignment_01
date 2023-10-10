// import base_api from "./_base.api";

// export async function login(username, password, role) {
// 	try {
// 		const requestBody = {
// 			username: username,
// 			password: password,
// 			role: role,
// 		};

// 		const _res = await base_api.post("/auth/login", requestBody);
// 		localStorage.setItem("token", _res.data.token);
// 		return _res;
// 	} catch (error) {
// 		console.log(error);
// 		return error;
// 	}
// }
