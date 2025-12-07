/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import java.util.Optional;
import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.items.TypeModif;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * Repository custom des configModif
 * @author Amolz
 */
@Repository
public class ConfigModifRepositoryCustomImpl implements ConfigModifRepositoryCustom {
    @Autowired
    @Lazy
    ConfigModifRepository configRepository;
    
    
    /**
     * Création d'une modification de configuration
     * @param type Son type
     * @param contenu Son contenu
     * @return La configModif créée
     */
    @Override
    public ConfigModif create(TypeModif type, String contenu) {
        if(contenu==null){
            contenu="";
        }
        if(type != null){
            ConfigModif item = new ConfigModif();
            item.setTypeId(type);
            item.setContenu(contenu);
            configRepository.saveAndFlush(item);

            Optional<ConfigModif> result = configRepository.findById(item.getModifId());
            if (result.isPresent()) {
                return result.get();
            }
        }
        return null;
    }
    
}
