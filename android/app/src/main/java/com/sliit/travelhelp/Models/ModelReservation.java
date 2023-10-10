package com.sliit.travelhelp.Models;

import com.fasterxml.jackson.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.bson.BsonDateTime;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@JsonDeserialize(using = ObjectIdDeserializer.class)
public class ModelReservation {
    @JsonProperty("_id")
    private BsonObjectId id;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private BsonDateTime date;

    @JsonProperty("status")
    private String status;

    @JsonProperty("train")
    private ModelTrain train;

    public BsonObjectId getId() {
        return id;
    }

    public void setId(BsonObjectId id) {
        this.id = id;
    }

    public BsonDateTime getDate() {
        return date;
    }

    public void setDate(BsonDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ModelTrain getTrain() {
        return train;
    }

    public void setTrain(ModelTrain train) {
        this.train = train;
    }

    // getters and setters
}
