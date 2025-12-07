/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import java.util.Collection;
import jakarta.servlet.http.HttpServletRequest;
import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.repositories.ConnexionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.centrale.infosi.pappl.logement.items.MissionLogementStatus;
import org.centrale.infosi.pappl.logement.repositories.MissionLogementStatusRepository;
import org.centrale.infosi.pappl.logement.util.Util;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.servlet.ModelAndView;

/**
 * Classe permettant de gérer les services de connexion (vérification du rôle,
 * check du statut de la mission logement)
 *
 * @author samer
 */
@Service
public class ConnectionService {

    @Autowired
    private ConnexionRepository repository;

    @Autowired
    private MissionLogementStatusRepository missionStatusRepository;

    /**
     * Méthode permettant de vérifier l'accès à une page
     *
     * @param request La requête http
     * @param requiredRole Le rôle à vérifier
     * @return La connexion s'il elle est autorisée
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Connexion checkAccess(HttpServletRequest request, String requiredRole) {
        // Extract the connection ID from the request
        String code = Util.getStringFromRequest(request, "connexionId");

        if (code == null || code.isEmpty()) {
            return null; // Return null if connexionId is not provided
        }

        // Step 1: Remove all expired connections from the database
        repository.removeOld();

        // Step 2: Find the connection by the given connexionId
        Collection<Connexion> items = repository.findByConnexionId(code);

        // If no connection is found, return null
        if (items == null || items.isEmpty()) {
            return null;
        }

        // Step 3: Check if the user has the required role
        for (Connexion connexion : items) {
            Personne person = connexion.getPersonneId(); // Get associated person
            if (person == null || person.getRoleId() == null || !person.getRoleId().getRoleNom().equals(requiredRole)) {
                // If the person doesn't have the required role, return null
                return null;
            }

            // Add 30 minutes to the expiration time if the user has the required role
            Date newExpiryDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000); // Add 30 minutes
            connexion.setExpiration(newExpiryDate);
            repository.save(connexion); // Save the updated connection
            return connexion;
        }

        return null; // Return null if no valid connection found
    }

    /**
     * Méthode permettant la création d'une connexion
     *
     * @param person La personne
     * @return La connexion
     */
    @Transactional
    public Connexion createConnection(Personne person) {
        repository.removeOld();
        Optional<Connexion> existingConnectionOpt = repository.findByPersonneId(person);

        if (existingConnectionOpt.isPresent()) {
            // If an active connection exists, don't create a new one
            // You can either return the existing connection or throw an exception
            // For now, we will return null to indicate the creation of a new connection is not allowed
            return existingConnectionOpt.get();
        }
        String randomKey = UUID.randomUUID().toString(); // Generate a random key for the connection
        Date expiryDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000); // Set expiry time to 30 minutes

        Connexion newConnection = new Connexion();
        newConnection.setConnexionId(randomKey);
        newConnection.setExpiration(expiryDate);
        newConnection.setPersonneId(person);  // Set the associated person (user)

        return repository.save(newConnection); // Save and return the created connection
    }

    /**
     * Méthode permettant de checker la statue de la mission logement
     *
     * @param connection La connexion
     * @param requiredStatus Le statut
     * @return La connexion mise à jour
     */
    @Transactional
    public Connexion checkMissionStatus(Connexion connection, int requiredStatus) {
        MissionLogementStatus currentStatus = missionStatusRepository.findById(1).get();
        if (currentStatus.getStatus() != requiredStatus) {
            return null;
        }
        return connection;
    }

    /**
     * Méthode perméttant de créer le bon ModelAndView à partir de la connexion
     *
     * @param connexion La connexion
     * @param jspFileName Le nom du fichier jsp à renvoyer
     * @return Le modelAndView correspondant
     */
    @Transactional
    public ModelAndView prepareModelAndView(Connexion connexion, String jspFileName) {
        if (connexion == null) {
            return null; // Error view if connexion is null
        } else {
            ModelAndView modelAndView = new ModelAndView(jspFileName); // Provided JSP file
            modelAndView.addObject("connexion", connexion); // Add connexion to model
            modelAndView.addObject("connexionId", connexion.getConnexionId()); // Add connexionId to model
            return modelAndView;
        }
    }
}
