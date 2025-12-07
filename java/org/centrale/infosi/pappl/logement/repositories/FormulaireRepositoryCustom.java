/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;


import java.util.Date;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.items.Genre;
import org.centrale.infosi.pappl.logement.items.Pays;
import org.centrale.infosi.pappl.logement.items.Personne;


/**
 * Interface du repository custom des formulaires
 * @author clesp
 */
public interface FormulaireRepositoryCustom {

    //public Formulaire enregistrer(Formulaire form);

    
    /**
     * Cree un nouveau formulaire à l'import des nouvelles donnees
     * @param personne
     * @param ville
     * @param mail
     * @param scei
     * @param dateDeNaissance
     * @param codePostal
     * @param genreId
     * @param paysId
     * @return le formulaire cree
     */
    public Formulaire createNewForm(
            Personne personne, String ville, String mail, String scei, Date dateDeNaissance, 
            String codePostal, Genre genreId, Pays paysId);

    /**
     * Met à jour un formulaire
     * @param Id Son identifiant
     * @param nom Le nom de la personne
     * @param prenom le prénom de la personne
     * @param dateNaissance Sa date de naissance
     * @param ville Sa ville
     * @param codePostal Son code postal
     * @param pays Son pays
     * @param mail Son mail
     * @param genre Son genre
     * @param numTelephone Son numéro de téléphone
     * @param boursier Son statut de boursier
     * @param souhait Son souhait d'appartement
     * @param pmr Son statut pmr
     * @param commentaireVe Ses commentaires de l'admin
     * @param commentaireEleve
     * @param validation Sa validation
     * @return Le formulaire mis à jour
     */
    public Formulaire update(int Id, String nom, String prenom, Date dateNaissance ,String ville, String codePostal, int pays, String mail, int genre, String numTelephone,
            String boursier, int souhait, String pmr, String commentaireVe, String commentaireEleve, Boolean validation);
    
    /**
     * Vide et valide un formulaire (en cas de problème pour remplir un formulaire par un élève)
     * @param Id l'identifiant du formulaire
     * @return Le formulaire mis à jour
     */
    public Formulaire viderEtValider(int Id);
    
    /**
     * Soumettre un formulaire (par un candidat)
     * @param id
     * @param soumission 
     */
    public void soumettre(int id, boolean soumission);
    
    /**
     * Get by mail
     * @param mail
     * @return 
     */
    public Formulaire getByEmail(String mail);
    
    /**
     * Get by scei
     * @param scei
     * @return 
     */
    public Formulaire getBySCEI(String scei);
}
