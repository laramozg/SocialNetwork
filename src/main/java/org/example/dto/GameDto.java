package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameDto {
    private Integer id;
    private String title;
    private String genre;

    @JsonCreator
    public GameDto(@JsonProperty("id") Integer id,
                   @JsonProperty("title") String title,
                   @JsonProperty("genre") String genre) {
        this.id = id;
        this.genre = genre;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
