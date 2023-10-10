import React from "react";
import Header from "../components/Header";
import CreateTraveler from "../components/CreateTraveler";
import SearchComponent from "../components/SearchComponent";

function TravelerManagement() {
	return (
		<>
			<Header />
			<div style={{ textAlign: "center", marginTop: "20px" }}>
				<h1>Traveler Management</h1>
			</div>
			<center>
				<CreateTraveler />
				<SearchComponent />
			</center>
		</>
	);
}

export default TravelerManagement;
