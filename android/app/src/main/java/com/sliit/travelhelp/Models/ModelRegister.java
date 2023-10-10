//This code is for register the  travellers account
package com.sliit.travelhelp.Models;

public class ModelRegister {
    public String NIC;
    public String fullName;
    public String password;
    public String email;

    public ModelRegister(String NIC, String fullName, String password, String email) {
        this.NIC = NIC;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
    }
    //getters and setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
