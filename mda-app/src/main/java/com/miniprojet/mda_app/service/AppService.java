package com.miniprojet.mda_app.service;

import com.miniprojet.mda_app.model.Journal;
import com.miniprojet.mda_app.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    @Autowired private JournalRepository journalRepo;

    // Simulation de l'Authentification Renforcée RH
    public boolean verifierRH(String mdpRH) {
        return "admin123".equals(mdpRH); 
    }

    // Méthode pour la Journalisation Systématique
    public void enregistrerAction(String qui, String quoi, String cible) {
        Journal log = new Journal();
        log.setUtilisateur(qui);
        log.setAction(quoi);
        log.setCible(cible);
        journalRepo.save(log); // Sauvegarde dans MySQL pour la non-répudiation
    }
}