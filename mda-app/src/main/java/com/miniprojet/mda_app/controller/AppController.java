package com.miniprojet.mda_app.controller;

import com.miniprojet.mda_app.model.Produit;
import com.miniprojet.mda_app.repository.ProduitRepository;
import com.miniprojet.mda_app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController

public class AppController {

    @Autowired private ProduitRepository produitRepo;
    @Autowired private AppService appService;

    
    @GetMapping("/")
    public String home() {
        return "<h1>Bienvenue sur l'application MDA</h1><p>Connexion MySQL : OK (admin123)</p>";
    }

    // hna te3 Consultation  //l'Authentification legere
    @GetMapping("/produits")
    public List<Produit> consulter(@RequestParam String nom, @RequestParam String service) {
        appService.enregistrerAction(nom, "CONSULTATION", "Liste des produits");
        return produitRepo.findAll();
    }

    // hna Mise à jour yste3mel l'Authentification Renforcée RH
    // ajoute (Ingénieur uniquement)
    @PostMapping("/produits")
    public String ajouter(@RequestBody Produit p, @RequestHeader String authRH) {
        if (appService.verifierRH(authRH)) {
            produitRepo.save(p);
            appService.enregistrerAction(p.getServiceOrigine(), "AJOUT", p.getNom());
            return "Produit ajouté avec succès !";
        }
        return "Erreur : Authentification RH échouée.";
    }
    //  hna MODIFICATION Ingénieur uniquement 
@PutMapping("/produits/{id}")
public String modifier(@PathVariable Long id, @RequestBody Produit nouveauxDetails, @RequestHeader String authRH) {
    if (appService.verifierRH(authRH)) {
        return produitRepo.findById(id).map(p -> {
            p.setNom(nouveauxDetails.getNom());
            p.setServiceOrigine(nouveauxDetails.getServiceOrigine());
            p.setPrix(nouveauxDetails.getPrix());
            produitRepo.save(p);
            appService.enregistrerAction(p.getServiceOrigine(), "MODIFICATION", p.getNom());
            return "Produit mis à jour avec succès !";
        }).orElse("Produit non trouvé.");
    }
    return "Erreur : Authentification RH échouée.";
}

// hna SUPPRESSION (admin)
@DeleteMapping("/produits/{id}")
public String supprimer(@PathVariable Long id, @RequestHeader String authRH) {
    if (appService.verifierRH(authRH)) {
        return produitRepo.findById(id).map(p -> {
            produitRepo.delete(p);
            appService.enregistrerAction("ADMIN", "SUPPRESSION", p.getNom());
            return "Produit supprimé avec succès !";
        }).orElse("Produit non trouvé.");
    }
    return "Erreur : Authentification RH échouée.";
}
}