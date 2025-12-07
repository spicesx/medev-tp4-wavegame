/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Connexion;
import java.util.Collection;
import java.util.Optional;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface du repository des connexions
 * @author clesp
 */
@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, String>,ConnexionRepositoryCustom{

    /**
     *
     * @param connexionId
     * @return
     */

    @Query("SELECT c FROM Connexion c WHERE c.connexionId = :connexionId")
    Collection<Connexion> findByConnexionId(@Param("connexionId") String connexionId);

    /**
     *
     * @param expiration
     * @return
     */
    public Collection<Connexion> findByExpiration(@Param("expiration") String expiration);

    /**
     *
     * @param personne
     * @return
     */
    public Optional<Connexion> findByPersonneId(@Param("personneId") Personne personne);
    
    /**
     * Delet all the table's data except the role selected
     * @param personneId La personne à supprimer
     */
    @Modifying
    @jakarta.transaction.Transactional
    @Query("DELETE FROM Connexion c WHERE c.personneId <> :personneId")
    public void deleteAllExceptConnectedId(@Param("personneId") Personne personneId);
    
    /**
     * Suppression d'une connexion trop vieille
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Connexion c WHERE c.expiration < CURRENT_TIMESTAMP")
    void removeOld();
}
