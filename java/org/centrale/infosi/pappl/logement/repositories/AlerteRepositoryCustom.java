/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import org.centrale.infosi.pappl.logement.items.Alerte;
import org.centrale.infosi.pappl.logement.items.Formulaire;

/**
 * Interface du repository custom gérant les alertes
 * @author clesp
 * 
 */
public interface AlerteRepositoryCustom {
    /**
     * Création d'une alerte
     * @param formulaire Le formulaire correspondant
     * @return L'alerte créée si elle existe bien
     */
    public Alerte create(Formulaire formulaire);
    
    /**
     * Mise à jour de l'état d'une alerte
     * @param formulaire Le formulaire correspondant
     * @param etat L'état voulu
     * @return L'alerte mise à jour si elle a bien eu lieu, null sinon
     */
    public Alerte update(Formulaire formulaire, String etat);
    
    
    /**
     * Mise à jour de l'état d'une alerte
     * @param formulaire Le formulaire correspondant
     * @param etat L'état voulu (true=traité, false=non traité)
     * @return L'alerte mise à jour si elle a bien eu lieu, null sinon
     */
    public Alerte update(Formulaire formulaire, boolean etat);
}
