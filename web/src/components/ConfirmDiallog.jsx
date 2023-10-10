import React from "react";
import { Button } from "react-bootstrap";

function ConfirmDiallog({ handleConfirm, handleCancel }) {
    const dialogStyle = {
        backgroundColor: "white",
        padding: "20px",
        borderRadius: "5px",
        boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.5)",
        zIndex: 1001, // Slightly higher z-index to appear above the background
    };
    const backgroundStyle = {
        position: "fixed",
        top: "0",
        left: "0",
        width: "100%",
        height: "100%",
        background: "rgba(1, 0, 0, 0.1)", // Semi-transparent black
        backdropFilter: "blur(5px)", // Apply a blur effect
        zIndex: 1000, // Higher z-index to cover other content
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
    };

    return (
        <div style={backgroundStyle}>
            <div style={dialogStyle}>
                <p>Are you sure you want to perform this action?</p>
                <Button
                    variant="danger"
                    onClick={handleConfirm}
                    style={{ margin: "10px" }}
                >
                    Confirm
                </Button>
                <Button onClick={handleCancel} style={{ margin: "10px" }}>
                    Cancel
                </Button>
            </div>
        </div>
    );
}

export default ConfirmDiallog;
