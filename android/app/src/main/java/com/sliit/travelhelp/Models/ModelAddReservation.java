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
    //date getter
    public String getDate() {
        return date;
    }
    //agentNote getter
    public String getAgentNote() {
        return agentNote;
    }
    //date setter
    public void setDate(String date) {
        this.date = date;
    }
    //train getter
    public String getTrain() {
        return train;
    }

    //train setter
    public void setTrain(String train) {
        this.train = train;
    }

    //travel agent getter
    public String getTravelAgent() {
        return travelAgent;
    }
    //travel agent setter
    public void setTravelAgent(String travelAgent) {
        this.travelAgent = travelAgent;
    }


    //trainclass getter
    public String getTrainClass() {
        return trainClass;
    }
    //trainclass settter
    public void setTrainClass(String trainClass) {
        this.trainClass = trainClass;
    }
}
