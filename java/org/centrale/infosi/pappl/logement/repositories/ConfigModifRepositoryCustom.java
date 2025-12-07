/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.items.TypeModif;

/**
 * Interface du repository custom correspondant aux configurations
 * @author Amolz
 */
public interface ConfigModifRepositoryCustom {
    
    /**
     * Création d'une modification de configuration
     * @param type Son type
     * @param contenu Son contenu
     * @return La configModif créée
     */
    public ConfigModif create(TypeModif type, String contenu);
}
