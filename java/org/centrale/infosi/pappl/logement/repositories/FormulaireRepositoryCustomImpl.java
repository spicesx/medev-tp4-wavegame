/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.items.Genre;
import org.centrale.infosi.pappl.logement.items.Pays;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.items.Souhait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.stereotype.Repository;

/**
 * Repository custom des formulaires
 *
 * @author clesp
 */
@Repository
public class FormulaireRepositoryCustomImpl implements FormulaireRepositoryCustom {

    @Autowired
    @Lazy
    private FormulaireRepository repository;

    @Autowired
    @Lazy
    private PaysRepository paysRepository;

    @Autowired
    @Lazy
    private SouhaitRepository souhaitRepository;

    @Autowired
    @Lazy
    private GenreRepository genreRepository;

    @Autowired
    @Lazy
    private PersonneRepository personneRepository;

    /**
     * Cree un nouveau formulaire à l'import des nouvelles donnees
     *
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
    @Override
    public Formulaire createNewForm(
            Personne personne, String ville, String mail, String scei,
            Date dateDeNaissance, String codePostal, Genre genreId, Pays paysId) {

        if ((personne != null) && (scei != null) && (genreId != null) && (paysId != null)) {

            Formulaire item = new Formulaire();

            item.setPersonneId(personne);
            item.setNumeroScei(scei);
            item.setGenreId(genreId);
            item.setGenreAttendu(genreId);
            item.setPaysId(paysId);

            if (ville != null) {
                ville = ville.trim();
                if (! ville.isEmpty()) {
                    item.setVille(ville);
                }
            }
            if (mail != null) {
                mail = mail.trim().toLowerCase();
                if (! mail.isEmpty()) {
                    item.setMail(mail);
                }
            }
            if (dateDeNaissance != null) {
                item.setDateDeNaissance(dateDeNaissance);
            }
            if (codePostal != null) {
                codePostal = codePostal.trim();
                item.setCodePostal(codePostal);
            }

            repository.saveAndFlush(item);

            Optional<Formulaire> result = repository.findById(item.getFormulaireId());
            if (result.isPresent()) {
                return result.get();
            }
        }
        return null;
    }

    /**
     * Met à jour un formulaire
     *
     * @param id Son identifiant
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
     * @param validation Sa validation
     * @return Le formulaire mis à jour
     */
    @Override
    public Formulaire update(int id, String nom, String prenom, Date dateNaissance, String ville, String codePostal, int pays, String mail, int genre, String numTelephone,
            String boursier, int souhait, String pmr, String commentaireVe, String commentaireEleve, Boolean validation) {
        Formulaire formulaire = repository.getReferenceById(id);
        if (formulaire != null) {
            personneRepository.updateNoms(formulaire.getPersonneId().getPersonneId(), nom, prenom);

            if (dateNaissance != null) {
                formulaire.setDateDeNaissance(dateNaissance);
            }
            if (ville != null) {
                ville = ville.trim();
                if (!ville.isEmpty()) {
                    formulaire.setVille(ville);
                }
            }
            if (codePostal != null) {
                codePostal = codePostal.trim();
                formulaire.setCodePostal(codePostal);
            }
            Pays paysId = paysRepository.getReferenceById(pays);
            if (paysId != null) {
                formulaire.setPaysId(paysId);
                // La référence inverse n'est pas utilisée donc pas mise à jour
            }
            if (mail != null) {
                mail = mail.trim().toLowerCase();
                formulaire.setMail(mail);
            }
            
            Genre genreId = genreRepository.getReferenceById(genre);
            if (genreId != null) {
                formulaire.setGenreId(genreId);
            }

            if (numTelephone != null) {
                numTelephone = numTelephone.trim();
                formulaire.setNumeroTel(numTelephone);
            }
            if (souhait > 0) {
                Souhait souhaitId = souhaitRepository.getReferenceById(souhait);
                if (souhaitId != null) {
                    formulaire.setSouhaitId(souhaitId);
                }
            }
            formulaire.setCommentairesVe(commentaireVe);
            formulaire.setCommentairesEleve(commentaireEleve);

            switch (pmr) {
                case "true":
                    formulaire.setEstPmr(true);
                    break;
                case "false":
                    formulaire.setEstPmr(false);
                    break;
                default:
                    formulaire.setEstPmr(null);
                    break;
            }

            switch (boursier) {
                case "true":
                    formulaire.setEstBoursier(true);
                    break;
                case "false":
                    formulaire.setEstBoursier(false);
                    break;
                default:
                    formulaire.setEstBoursier(null);
                    break;
            }

            if (validation != null) {
                // Validé ou refusé
                formulaire.setEstConforme(validation);
                formulaire.setEstValide(validation);
            }

            repository.saveAndFlush(formulaire);
            return formulaire;
        }
        return null;
    }

    /**
     * Vide et valide un formulaire (en cas de problème pour remplir un
     * formulaire par un élève)
     *
     * @param id l'identifiant du formulaire
     * @return Le formulaire mis à jour
     */
    @Override
    public Formulaire viderEtValider(int id) {
        //on recuperre le formulaire
        Formulaire formulaire = repository.getReferenceById(id);
        if (formulaire != null) {
            //on enleve les champs problematiques
            formulaire.setGenreId(formulaire.getGenreAttendu());
            formulaire.setNumeroTel(null);
            formulaire.setSouhaitId(null);
            formulaire.setEstBoursier(null);
            formulaire.setEstPmr(null);
            Date dateValidation = new Date();
            formulaire.setCommentairesEleve(null);
            formulaire.setDateValidation(dateValidation);
            
            //On transmet
            formulaire.setEstValide(true);
            //On sauvegarde
            repository.saveAndFlush(formulaire);
            return formulaire;
        }
        return null;
    }

    @Override
    public void soumettre(int id, boolean soumission) {
        Formulaire formulaire = repository.getReferenceById(id);
        if (formulaire != null) {
            if (soumission) {
                Calendar aCalendar = Calendar.getInstance();
                Date now = aCalendar.getTime();
                formulaire.setEstValide(true);
                formulaire.setDateValidation(now);
            } else {
                formulaire.setEstValide(false);
                formulaire.setDateValidation(null);
            }
            repository.saveAndFlush(formulaire);
        }
    }

    @Override
    public Formulaire getByEmail(String mail) {
        if ((mail != null) && (! mail.isEmpty())) {
            Collection<Formulaire> result = repository.findByEmailNoCase(mail);
            if ((result != null) && (result.size() == 1)) {
                return result.iterator().next();
            }
        }
        return null;
    }
    
    @Override
    public Formulaire getBySCEI(String scei) {
        if ((scei != null) && (! scei.isEmpty())) {
            Collection<Formulaire> result = repository.findByNumeroScei(scei);
            if ((result != null) && (result.size() == 1)) {
                return result.iterator().next();
            }
        }
        return null;
    }
}
