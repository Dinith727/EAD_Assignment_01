//users model
package com.sliit.travelhelp.Models;

public class ModelUser {
    private String id;
    private String email;
    private String NIC;
    private String fullName;

    public ModelUser(String id, String email, String NIC, String fullName) {
        this.id = id;
        this.email = email;
        this.NIC = NIC;
        this.fullName = fullName;
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
