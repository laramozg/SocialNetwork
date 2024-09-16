package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PostDto {
    private Integer id;
    private Integer profileId;
    private String content;
    private LocalDateTime createdAt;

    @JsonCreator
    public PostDto(@JsonProperty("id") Integer id,
                   @JsonProperty("profileId") Integer profileId,
                   @JsonProperty("content") String content,
                   @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.content = content;
        this.profileId = profileId;
    }

    public PostDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
