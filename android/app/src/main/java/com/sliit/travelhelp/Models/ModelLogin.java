//login model
package com.sliit.travelhelp.Models;

public class ModelLogin {

    private String username;
    private String password;
    private String role ="traveller";

    public ModelLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }
    //getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
