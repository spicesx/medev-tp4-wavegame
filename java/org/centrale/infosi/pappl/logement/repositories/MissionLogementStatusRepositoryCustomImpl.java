/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import org.centrale.infosi.pappl.logement.items.MissionLogementStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * Repository custom lié au statut de la mission logement
 * @author samer
 */
@Repository
public class MissionLogementStatusRepositoryCustomImpl implements MissionLogementStatusRepositoryCustom{
    
    @Lazy
    @Autowired
    private MissionLogementStatusRepository repository;
    
    @Override
    public MissionLogementStatus updateStatus(int newStatus) {
        MissionLogementStatus status = repository.findById(1).get();
        status.setStatus(newStatus);
        repository.saveAndFlush(status);
        return status;
        
    }
    
}
