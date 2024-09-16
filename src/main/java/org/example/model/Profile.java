package org.example.model;

public class Profile {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private User userId;

    public Profile(Integer id, String firstName, String lastName, String email, User userId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.email = email;
    }

    public Profile() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(User userId) {
        this.userId = userId;

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

    public User getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }


}
