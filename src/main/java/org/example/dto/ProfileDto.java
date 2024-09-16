package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer userId;

    @JsonCreator
    public ProfileDto(@JsonProperty("id") Integer id,
                      @JsonProperty("firstName") String firstName,
                      @JsonProperty("lastName") String lastName,
                      @JsonProperty("email") String email,
                      @JsonProperty("userId") Integer userId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;

    }

    public ProfileDto() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
