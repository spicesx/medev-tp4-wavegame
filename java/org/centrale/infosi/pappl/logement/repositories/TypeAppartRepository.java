/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.TypeAppart;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository lié aux types d'appartements (sert aux résultats)
 * @author clesp
 */
@Repository
public interface TypeAppartRepository extends JpaRepository<TypeAppart,Integer>,TypeAppartRepositoryCustom {

    /**
     *
     * @param type_appart_id
     * @return
     */
    public Collection<TypeAppart> findByTypeAppartId(@Param("typeAppartId")Integer type_appart_id);

    /**
     *
     * @param type_appart_nom
     * @return
     */
    public Collection<TypeAppart> findByTypeAppartNom(@Param("typeAppartNom")String type_appart_nom);
}
