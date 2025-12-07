/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import org.centrale.infosi.pappl.logement.items.Personne;

/**
 * Interface du repository custom lié aux personnes
 *
 * @author clesp
 */
public interface PersonneRepositoryCustom {

    /**
     * Permet d'obtenir la personne lié à un login et un password
     *
     * @param login Le login
     * @param password Le mot de passe
     * @return L'utilisateur identifié (s'il existe)
     */
    public Personne getByLoginAndPassword(String login, String password);

    /**
     * Mise à jour d'une personne
     *
     * @param id L'identifiant de la personne
     * @param firstName Le prénom de la personne
     * @param lastName Le nom de la personne
     * @param Login Le login de la personne
     * @param Password Le mot de passe de la personne
     * @return La personne mise à jour si elle a réussie
     */
    public Personne update(int id, String firstName, String lastName, String Login, String Password);

    /**
     * Suppression d'une personne
     *
     * @param id Son identifiant
     */
    public void delete(int id);

    /**
     * Création d'une personne
     *
     * @param firstName Prénom de la personne
     * @param lastName Nom de la personne
     * @param Login Le login de la personne
     * @param Password Mot de passe
     * @return La personne créée
     */
    public Personne create(String firstName, String lastName, String Login, String Password);

    /**
     * Crée un Eleve et rempli la table personne de la base de donnee associee
     *
     * @param nom le nom de l'eleve
     * @param prenom le prenom de l'eleve
     * @return la personne cree
     */
    public Personne createEleve(String nom, String prenom);

    /**
     * Met à jour une personne
     *
     * @param personne La personne
     * @param login Le nouveau login
     * @param password Le nouveau mot de passe
     * @return La personne si la mise à jour a eu lieu, null sinon
     */
    public Personne update(Personne personne, String login, String password);

    /**
     * Met à jour une personne
     *
     * @param personne La personne
     * @return La personne si la mise à jour a eu lieu, null sinon
     */
    public Personne resetPassword(Personne personne);

    /**
     * Change le nom et le prenom de la personne
     *
     * @param Id L'identifiant de la personne
     * @param nom Le nom de la personne
     * @param prenom Le prénom de la personne
     * @return La personne mise à jour
     */
    public Personne updateNoms(int Id, String nom, String prenom);

    /**
     * Set new token
     * @param personne
     * @return 
     */
    public Personne setToken(Personne personne);

    /**
     * Remove token
     * @param personne
     * @return 
     */
    public Personne resetToken(Personne personne);
    
    /**
     * Get by Login
     * @param login
     * @return 
     */
    public Personne getByLogin(String login);
    
}
