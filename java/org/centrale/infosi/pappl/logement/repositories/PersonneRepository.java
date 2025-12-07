/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Personne;
import java.util.Collection;
import java.util.Optional;
import jakarta.transaction.Transactional;
import java.util.List;
import org.centrale.infosi.pappl.logement.items.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository lié aux personnes
 * @author clesp
 */
@Repository
public interface PersonneRepository extends JpaRepository<Personne,Integer>,PersonneRepositoryCustom {

    /**
     *
     * @param personne_id
     * @return
     */
    public Collection<Personne> findByPersonneId(@Param("personneId")Integer personne_id);

    /**
     *
     * @param nom
     * @return
     */
    public Collection<Personne> findByNom(@Param("nom")String nom);

    /**
     *
     * @param prenom
     * @return
     */
    public Collection<Personne> findByPrenom(@Param("prenom")String prenom);

    /**
     *
     * @param login
     * @return
     */
    public Collection<Personne> findByLogin(@Param("login")String login);

    /**
     *
     * @param password
     * @return
     */
    public Collection<Personne> findByPassword(@Param("password")String password);
    
    
    /**
     * Recherche par token de première connexion
     * @param token Le token
     * @return  La personne liée au token
     */
    @Query("SELECT p FROM Personne p WHERE p.firstConnectionToken = :firstConnectionToken")
    Optional<Personne> findByFirstConnectionToken(@Param("firstConnectionToken") String token);
    
    /**
     * Recherche de tous les tokens des élèves à qui on va envoyé un mail
     * @return la liste des tokens
     */
    @Query("SELECT p.firstConnectionToken FROM Personne p JOIN p.formulaireCollection f WHERE f.vague=false")
    List<String> findAllTokenVague();
    
    /**
     *
     * @param role_id
     * @return
     */
    public Collection<Personne> findByRoleId(@Param("roleId")Role role_id);
    
    /**
     * Delet all the table's data except the role selected
     * @param roleId 
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Personne p WHERE p.roleId <> :roleId")
    public void deleteAllExceptRoleId(@Param("roleId") Role roleId);
    
    /**
     * Delet all the table's data except the role selected
     * @param roleId 
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Personne p WHERE p.roleId = :roleId")
    public void deleteAllRoleId(@Param("roleId") Role roleId);
    
    /**
     * Test d'existence d'un token
     * @param token Le token
     * @return Le booleen d'existence
     */
    boolean existsByFirstConnectionToken(String token); 
    
    
     /**
     * find personne
     * @param nom nom de la personne
     * @param prenom prénom de la personne
     * @return 
     */
    @Query("SELECT p FROM Personne p WHERE p.nom = :nom AND p.prenom = :prenom")
    public Collection<Personne> findByPersonFirstAndLastName(@Param("nom")String nom, @Param("prenom")String prenom);

    /**
     * Trouve une personne liée à un login et un mot de passe
     * @param login Le login
     * @param password Le mot de passe
     * @return La personne si elle existe
     */
    @Query("SELECT p FROM Personne p WHERE p.login = :login AND p.password = :password")
    public Collection<Personne> findByLoginAndPassword(@Param("login") String login, @Param("password") String password);
    
    
    /**
     * Destruction des tokens
     * @param id L'identifiant de la personne
     */
    @Modifying
    @Transactional
    @Query(value="UPDATE personne SET first_connection_token = NULL WHERE personne_id = ?1",nativeQuery=true)
    public void setFirstConnectionTokenToNull(Integer id);
}
