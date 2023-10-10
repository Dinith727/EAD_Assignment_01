package com.sliit.travelhelp.Models;

import com.google.gson.annotations.SerializedName; // You may need to import this annotation if you're using GSON

public class ModelAddReservation {
    private String date;

    private String train;

    private String travelAgent;

    private String trainClass;

    private String agentNote = "";

    public ModelAddReservation(String date, String train, String travelAgent, String trainClass) {
        this.date = date;
        this.train = train;
        this.travelAgent = travelAgent;
        this.trainClass = trainClass;
    }


    // Getters and setters

    public String getDate() {
        return date;
    }

    public String getAgentNote() {
        return agentNote;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public String getTravelAgent() {
        return travelAgent;
    }

    public void setTravelAgent(String travelAgent) {
        this.travelAgent = travelAgent;
    }



    public String getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(String trainClass) {
        this.trainClass = trainClass;
    }
}
