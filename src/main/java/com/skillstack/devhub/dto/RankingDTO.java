package com.skillstack.devhub.dto;

public class RankingDTO {
    private int position;
    private String username;
    private String email;
    private int totalScore;

    public RankingDTO() {}

    public RankingDTO(int position, String username, String email, int totalScore) {
        this.position = position;
        this.username = username;
        this.email = email;
        this.totalScore = totalScore;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
