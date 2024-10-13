package org.example.dto;


public class UserDto {
    private Integer id;
    private String username;
    private String password;

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
