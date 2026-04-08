package com.skillstack.devhub.model;

public enum Difficulty {
    EASY(1),
    MEDIUM(3),
    HARD(5);

    private int points;

    Difficulty(int points) {
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }

}
