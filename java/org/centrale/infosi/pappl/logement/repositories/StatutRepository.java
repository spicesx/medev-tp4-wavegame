/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Statut;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository liés aux statuts d'alerte
 * @author clesp
 */
@Repository
public interface StatutRepository extends JpaRepository<Statut,Integer>,StatutRepositoryCustom{

    /**
     *
     * @param statut_id
     * @return
     */
    public Collection<Statut> findByStatutId(@Param("statutId")Integer statut_id);

    /**
     *
     * @param statut_nom
     * @return
     */
    public Collection<Statut> findByStatutNom(@Param("statutNom")String statut_nom);
}
