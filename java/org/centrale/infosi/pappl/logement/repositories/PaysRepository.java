/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Pays;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository lié aux pays
 * @author clesp
 */
@Repository
public interface PaysRepository extends JpaRepository<Pays,Integer>,PaysRepositoryCustom{

    /**
     *
     * @param paysId
     * @return
     */
    public Collection<Pays> findByPaysId(@Param("paysId")Integer paysId);

    /**
     *
     * @param paysNom
     * @return
     */
    public Collection<Pays> findByPaysNom(@Param("paysNom")String paysNom);
    
    
    /**
     * Liste triée des pays
     * @return La liste des pays triée
     */
    @Query(value="SELECT * FROM pays ORDER BY pays_id",nativeQuery=true)
    public Collection<Pays> findAllSorted();
}
