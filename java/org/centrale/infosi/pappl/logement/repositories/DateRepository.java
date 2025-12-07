/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Date;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository des dates
 * @author clesp
 */
@Repository
public interface DateRepository extends JpaRepository<Date,Integer>,DateRepositoryCustom {

    /**
     *
     * @param date_id
     * @return
     */
    public Collection<Date> findByDateId(@Param("dateId")Integer date_id);

    /**
     *
     * @param date_debut
     * @return
     */
    public Collection<Date> findByDateDebut(@Param("dateDebut")java.util.Date date_debut);
}
