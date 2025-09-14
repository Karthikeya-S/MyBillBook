package com.application.mybillbook.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
public class Users {
    @Setter
    @Id
    private String id;

    @Setter
    private String firstName;
    private String lastName;
    @Setter
    private String email;
    private String userName;
    private String password;

    public Users(){}

    public Users(String firstName, String lastName, String email, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName.trim().toLowerCase(); // Normalize userName
    }

    public String getPassword() {
        return password;
    }

}
