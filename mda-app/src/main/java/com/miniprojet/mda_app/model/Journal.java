package com.miniprojet.mda_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String utilisateur; // Qui a fait l'action
    private String action;      // Type d'opération (Consultation ou Mise à jour ay wehda)
    private String cible;       // Quel produit a été touché
    private LocalDateTime dateAction = LocalDateTime.now(); // Quand

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUtilisateur() { return utilisateur; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getCible() { return cible; }
    public void setCible(String cible) { this.cible = cible; }
    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }
}