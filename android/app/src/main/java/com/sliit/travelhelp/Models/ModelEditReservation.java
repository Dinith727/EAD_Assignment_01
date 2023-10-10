//This code is for edit reservation by travellers
package com.sliit.travelhelp.Models;

public class ModelEditReservation {
    private String _id;

    private String date;

    private String train;

    private String travelAgent;

    private String trainClass;

    private String status;

    private String agentNote;


    //getters and setters
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
