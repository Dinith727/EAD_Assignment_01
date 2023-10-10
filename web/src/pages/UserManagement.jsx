import React from "react";
import Header from "../components/Header";
import CreateUser from "../components/CreateUser";
import UserTable from "../components/UserTable";

function UserManagement() {
	return (
		<>
			<Header />
			<div style={{ textAlign: "center", marginTop: "20px" }}>
				<h1>User Management</h1>
			</div>
			<center>
				<CreateUser />
				<div
					style={{
						width: "900px",
						marginTop: "30px",
						marginBottom: "50px",
					}}
				>
					<UserTable />
				</div>
			</center>
		</>
	);
}

export default UserManagement;
