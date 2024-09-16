package org.example.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    private Integer id;
    private String username;
    private String password;

    @JsonCreator
    public UserDto(@JsonProperty("id") Integer id,
                   @JsonProperty("username") String username,
                   @JsonProperty("password") String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserDto() {
    }

    public String getUsername() {
        return username;
    }


    public void setId(Integer userId) {
        this.id = userId;
    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
