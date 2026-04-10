package com.skillstack.devhub.dto;
import java.util.List;
public class UserResponseDTO {
      
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private List<String> preferences;
    private int answeredQuestions;
    private int totalScore;
    public UserResponseDTO() {

    }
    
    public UserResponseDTO(String id, String firstName, String lastName, String username, String email, 
                          String phone, List<String> preferences, int answeredQuestions, int totalScore) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.preferences = preferences;
        this.answeredQuestions = answeredQuestions;
        this.totalScore = totalScore;
    }

    public String getId() { 
        return id; 
    }

    public void setId(String id) {
         this.id = id; 
    }

    public String getFirstName() { 
        return firstName; 
    }

    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public String getLastName() { 
        return lastName;
     }

    public void setLastName(String lastName) {
        this.lastName = lastName; 
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

    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone;
    }

    public List<String> getPreferences() { 
        return preferences; 
    }

    public void setPreferences(List<String> preferences) { 
        this.preferences = preferences; 
    }

    public int getAnsweredQuestions() {
         return answeredQuestions;
    }
    public void setAnsweredQuestions(int answeredQuestions) {
         this.answeredQuestions = answeredQuestions;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    
}

