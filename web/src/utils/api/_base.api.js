import axios from "axios";
//const VITE_BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
const VITE_BACKEND_URL = "http://localhost:5299/api";
const base_api = axios.create({
	baseURL: VITE_BACKEND_URL,
	headers: {
		Authorization: "Bearer " + window.localStorage.getItem("token"),
	},
});

export default base_api;
