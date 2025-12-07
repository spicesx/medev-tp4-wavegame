/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;


import org.centrale.infosi.pappl.logement.items.MissionLogementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository lié au statut de la mission logement (sert à l'affichage des résultats)
 * @author samer
 */
@Repository
public interface MissionLogementStatusRepository extends JpaRepository<MissionLogementStatus,Integer>,MissionLogementStatusRepositoryCustom {

}
