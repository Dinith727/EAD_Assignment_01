import React from "react";
import { Button, Row, Col } from "react-bootstrap";

const ButtonGroup = ({ btn1, btn2, url1, url2, restrict1, restrict2 }) => {
	return (
		<Row className="justify-content-center ">
			<Col md={3}>
				<Button
					variant="primary"
					size="lg"
					style={{ width: "200px" }}
					href={url1}
					disabled={restrict1}
				>
					{btn1}
				</Button>
			</Col>
			<Col md={3}>
				<Button
					variant="primary"
					size="lg"
					style={{ width: "200px" }}
					href={url2}
					disabled={restrict2}
				>
					{btn2}
				</Button>
			</Col>
		</Row>
	);
};

export default ButtonGroup;
