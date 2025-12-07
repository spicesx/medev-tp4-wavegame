/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Souhait;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository lié aux souhaits d'appartement
 * @author clesp
 */
@Repository
public interface SouhaitRepository extends JpaRepository<Souhait,Integer>,SouhaitRepositoryCustom{

    /**
     *
     * @param souhait_id
     * @return
     */
    public Collection<Souhait> findBySouhaitId(@Param("souhaitId")Integer souhait_id);

    /**
     *
     * @param souhait_nom
     * @return
     */
    public Collection<Souhait> findBySouhaitType(@Param("souhaitType")String souhait_nom);
}
