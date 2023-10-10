import ButtonGroup from "../components/ButtonGroup";
import Header from "../components/Header";

function Dashboard() {
	const dashboardStyle = {
		backgroundImage: `url("https://images.pexels.com/photos/258455/pexels-photo-258455.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")`,
		backgroundSize: "cover",
		backgroundPosition: "center",
		minHeight: "100vh",
		color: "white",
		overflow: "hidden",
	};
	const buttonStyles = {
		marginLeft: "100px",
		marginTop: "250px",
	};
	const role = window.localStorage.getItem("role");
	//console.log(role == "travelAgent");

	return (
		<>
			<div style={dashboardStyle}>
				<Header />
				<div style={buttonStyles}>
					<ButtonGroup
						btn1="Users"
						btn2="Ticket bookings"
						url1="user-management"
						restrict1={role === "travelAgent"}
						restrict2={role === "backOffice"}
						url2="ticket-booking"
					/>
					<br></br>
					<br></br>
					<ButtonGroup
						btn1="Travelers"
						btn2="Trains"
						url1="traveler-management"
						url2="trains"
						restrict2={role === "travelAgent"}
						restrict1={false}
					/>
				</div>
			</div>
		</>
	);
}

export default Dashboard;
