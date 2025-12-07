/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import org.centrale.infosi.pappl.logement.items.MissionLogementStatus;

/**
 * Inteface du Repository custom lié au statut de la mission logement
 * @author samer
 */
public interface MissionLogementStatusRepositoryCustom {
    /**
     * Met à jour le statut
     * @param newStatus Le nouveau statut
     * @return Le statut mis à jour
     */
    public MissionLogementStatus updateStatus(int newStatus);
}
