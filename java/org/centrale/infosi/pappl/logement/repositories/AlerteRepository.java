/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import org.centrale.infosi.pappl.logement.items.Alerte;
import java.util.Collection;
import java.util.Optional;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.items.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface du repository gérant les alertes
 * @author clesp
 * 
 */
@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Integer>,AlerteRepositoryCustom {

    /**
     * 
     * @param alerte_id
     * @return
     */
    public Collection<Alerte> findByAlerteId(@Param("alerteId")Integer alerte_id);

    /**
     *
     * @param formulaire_id
     * @return
     */
    public Optional<Alerte> findByFormulaireId(@Param("formulaireId")Formulaire formulaire_id);

    /**
     *
     * @param statut_id
     * @return
     */
    public Collection<Alerte> findByStatutId(@Param("statutId")Statut statut_id);
    
    /**
     * 
     * @return 
     */
    @Query(value="SELECT alerte.* FROM alerte INNER JOIN statut USING (statut_id) WHERE statut_nom ILIKE 'Non %'",nativeQuery=true)
    public Collection<Alerte> findAllToBeProcess();

    /**
     * !WARNING! this function delete all the table's data
     * !ATTENTION! cette fonction supprime toutes les donnees de cette table
     */
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE Alerte", nativeQuery = true)
    void truncateTable();
}
