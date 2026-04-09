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
    private Integer preguntasResueltas;
    private Integer totalPreguntas;
    private Integer puntosAcumulados;
    public UserResponseDTO() {

    }
    
    public UserResponseDTO(String id, String firstName, String lastName, String username, String email, 
                          String phone, List<String> preferences, Integer preguntasResueltas, 
                          Integer totalPreguntas, Integer puntosAcumulados) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.preferences = preferences;
        this.preguntasResueltas = preguntasResueltas;
        this.totalPreguntas = totalPreguntas;
        this.puntosAcumulados = puntosAcumulados;
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

    public Integer getPreguntasResueltas() {
         return preguntasResueltas; 
    }
    public void setPreguntasResueltas(Integer preguntasResueltas) {
         this.preguntasResueltas = preguntasResueltas; 
    }

    public Integer getTotalPreguntas() { 
        return totalPreguntas; 
    }
    public void setTotalPreguntas(Integer totalPreguntas) { 
        this.totalPreguntas = totalPreguntas; 
    }

    public Integer getPuntosAcumulados() { 
        return puntosAcumulados; 
    }

    public void setPuntosAcumulados(Integer puntosAcumulados) { 
        this.puntosAcumulados = puntosAcumulados; 
    }
    
}

