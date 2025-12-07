/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;


import java.util.Collection;
import org.centrale.infosi.pappl.logement.items.TypeModif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interface du repository lié aux types de modification de configuration
 * @author Amolz
 */
@Repository
public interface TypeModifRepository extends JpaRepository<TypeModif, Integer>, TypeModifRepositoryCustom {
    /**
     * Renvoi la liste des elements de collection dont le nom est egale a nom
     * @param nom
     * @return 
     */
    public Collection<TypeModif> findByNom(@Param("nom") String nom);
}
