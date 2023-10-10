//response model
package com.sliit.travelhelp.Models;

public class ModelResponse<T> {
    public int status;
    public T data;

    public ModelResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    //getters and setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
