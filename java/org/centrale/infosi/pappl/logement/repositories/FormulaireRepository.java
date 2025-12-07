/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import java.util.Collection;
import java.util.List;
import org.centrale.infosi.pappl.logement.items.Genre;
import org.centrale.infosi.pappl.logement.items.Logement;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.items.Souhait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
/**
 * Interface du repotory des formulaires
 * @author clesp
 */
@Repository
public interface FormulaireRepository extends JpaRepository<Formulaire,Integer>,FormulaireRepositoryCustom {

    /**
     *
     * @param formulaireId
     * @return
     */
    public Collection<Formulaire> findByFormulaireId(@Param("formulaireId")Integer formulaireId);

    /**
     *
     * @param personne_id
     * @return
     */
    public Collection<Formulaire> findByPersonneId(@Param("personneId") Personne personne_id);

    /**
     *
     * @param numeroScei
     * @return
     */
    public Collection<Formulaire> findByNumeroScei(@Param("numeroScei")String numeroScei);


    /**
     *
     * @param genre_id
     * @return
     */
    public Collection<Formulaire> findByGenreId(@Param("genreId")Genre genre_id);

    /**
     *
     * @param date_de_naissance
     * @return
     */
    public Collection<Formulaire> findByDateDeNaissance(@Param("dateDeNaissance")String date_de_naissance);

    /**
     *
     * @param ville
     * @return
     */
    public Collection<Formulaire> findByVille(@Param("ville")String ville);

    /**
     *
     * @param code_postal
     * @return
     */
    public Collection<Formulaire> findByCodePostal(@Param("codePostal")String code_postal);

    /**
     *
     * @param pays_id
     * @return
     */
    public Collection<Formulaire> findByPaysId(@Param("paysId")Integer pays_id);

    /**
     *
     * @param mail
     * @return
     */
    public Collection<Formulaire> findByMail(@Param("mail")String mail);

    /**
     *
     * @param numero_tel
     * @return
     */
    public Collection<Formulaire> findByNumeroTel(@Param("numeroTel")String numero_tel);

    /**
     *
     * @param commentaires_ve
     * @return
     */
    public Collection<Formulaire> findByCommentairesVe(@Param("commentairesVe")String commentaires_ve);

    /**
     *
     * @param commentaires_eleve
     * @return
     */
    public Collection<Formulaire> findByCommentairesEleve(@Param("commentairesEleve")String commentaires_eleve);

    /**
     *
     * @param est_boursier
     * @return
     */
    public Collection<Formulaire> findByEstBoursier(@Param("estBoursier")Boolean est_boursier);

    /**
     *
     * @param est_pmr
     * @return
     */
    public Collection<Formulaire> findByEstPmr(@Param("estPmr")Boolean est_pmr);

    /**
     *
     * @param numero_logement
     * @return
     */
    public Collection<Formulaire> findByNumeroLogement(@Param("numeroLogement")Logement numero_logement);

    /**
     *
     * @param souhait_id
     * @return
     */
    public Collection<Formulaire> findBySouhaitId(@Param("souhaitId")Souhait souhait_id);

    /**
     *
     * @param est_valide
     * @return
     */
    public Collection<Formulaire> findByEstValide(@Param("estValide")boolean est_valide);
    
    /**
     * Trouve tous les mails
     * @return Les mails
     */
    @Query("SELECT f.mail FROM Formulaire f")
    Collection<String> findAllEmails();
    
    /**
     * Trouve tous les mails qui n'ont pas reçu de lien de première connexion
     * @return Les mails selon la vague d'envoi
     */
    @Query("SELECT f.mail FROM Formulaire f where f.vague=false")
    Collection<String> findAllEmailsVague();
    
    /**
     * Trouve le mail associé au token cherché
     * @param token le token de l'élève recherché
     * @return mail de l'élève
     */
    @Query("SELECT f.mail FROM Formulaire f JOIN f.personneId p WHERE p.firstConnectionToken = ?1")
    List<String> findMailByToken(String token);
    
    /**
     * Trouve le num SCEI associé au token cherché
     * @param token le token de l'élève recherché
     * @return numSCEI de l'élève
     */
    @Query("SELECT f.numeroScei FROM Formulaire f JOIN f.personneId p WHERE p.firstConnectionToken = ?1")
    List<String> findSCEIByToken(String token);
    
    /**
     * Update l'attribut vague, méthode appelée uniquement dans MailController lorsque les mails de première connexion ont été envoyées
     */
    @Modifying
    @Transactional
    @Query("UPDATE Formulaire f SET f.vague=true where f.vague=false")
    int updateVague();
    
    /**
     * 
     * @param est_conforme
     * @return 
     */
    public Collection<Formulaire> findByEstConforme(@Param("estConforme")boolean est_conforme);
  
     /** Méthode permettant de trouver les formulaires liés à un token et un numéro SCEI
     * @param token Le token
     * @param numSCEI Le numéro SCEI
     * @return La liste des personnes correspondantes
     */
    @Query(value="SELECT * FROM formulaire NATURAL JOIN personne WHERE first_connection_token=?1 AND numero_SCEI=?2",nativeQuery=true)
    public Collection<Formulaire> findByNumeroSCEIAndToken(String token, String numSCEI);

     /** Méthode permettant de trouver les formulaires liés à un mail et un numéro SCEI
     * @param mail Le mail
     * @param numSCEI Le numéro SCEI
     * @return La liste des personnes correspondantes
     */
    @Query(value="SELECT * FROM formulaire NATURAL JOIN personne WHERE LOWER(mail)=LOWER(?1) AND UPPER(numero_SCEI)=UPPER(?2)",nativeQuery=true)
    public Collection<Formulaire> findByNumeroSceiAndMail(String mail, String numSCEI);
    
    /**
     * Méthode permettant de sélectionner les formulaires transmis une fois (validé ou refusé avec un commentaire)
     * @return Les dossiers correspondants
     */
    @Query(value="SELECT * FROM formulaire WHERE est_valide=true OR commentaires_ve IS NOT NULL",nativeQuery=true)
    public Collection<Formulaire> findAllValidOrCommentaireVE();

    /**
     * Find by email
     * @param mail
     * @return 
     */
    @Query(value="SELECT * FROM formulaire WHERE mail IS NOT NULL AND LOWER(mail)=LOWER(?1)",nativeQuery=true)
    public Collection<Formulaire> findByEmailNoCase(String mail);
}


