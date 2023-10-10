import AddTrainForm from "../components/AddTrainForm";
import Header from "../components/Header";
import TrainList from "../components/TrainList";

function Trains() {
	return (
		<>
			<Header />
			<center>
				<div style={{ width: "500px", marginTop: "50px" }}>
					<AddTrainForm />
				</div>
				<div style={{ margin: "20px", width: "1100px" }}>
					<TrainList />
				</div>
			</center>
		</>
	);
}

export default Trains;
