//auth provider 
import React, { createContext, useState, useEffect, useContext } from "react";
export const authContext = createContext();
import { useNavigate } from "react-router-dom";
import base_api from "../api/_base.api";

export default function useAuth() {
	return useContext(authContext);
}

export function AuthProvider({ children }) {
	const [user, setuser] = useState({});
	const navigate = useNavigate();

	async function login(username, password, role) {
		//window.location.reload();
		const requestBody = {
			username: username,
			password: password,
			role: role,
		};
		const _res = await base_api.post("/auth/login", requestBody); //login url

		window.localStorage.setItem("token", _res.data.token);

		const _user = await base_api.get("/admin", {
			headers: { Authorization: "Bearer " + _res.data.token },
		});
		setuser(_user.data.data);

		window.localStorage.setItem("username", _user.data.data.username);
		window.localStorage.setItem("role", _user.data.data.role);
		navigate("/dashboard");
		window.location.reload();
	}
	//logout function
	async function logout() {
		window.localStorage.removeItem("token");
		window.localStorage.removeItem("username");
		window.localStorage.removeItem("role");

		setuser({});
		navigate("/"); //redirect to main page after log out
		window.location.reload();
	}
	return (
		<authContext.Provider value={{ login, user, logout }}>
			{children}
		</authContext.Provider>
	);
}
