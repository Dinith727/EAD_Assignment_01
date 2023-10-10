//Trains model
package com.sliit.travelhelp.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelTrain {
    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    public ModelTrain(String from, String to) {
        this.from = from;
        this.to = to;
    }

    //getters and setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    
}
