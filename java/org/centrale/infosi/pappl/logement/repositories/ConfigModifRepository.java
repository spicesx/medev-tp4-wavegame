/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;

import java.util.List;
import java.util.Optional;
import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.items.TypeModif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interface du Repository des modifications de configuration
 * @author Amolz
 */
@Repository
public interface ConfigModifRepository extends JpaRepository<ConfigModif, Integer>, ConfigModifRepositoryCustom {
    
    /**
     * Renvoi le dernier element de typeModif dont le tipeId correspond au paramètre
     * @param modifId La modification voulue
     * @return Les modifications correspondantes
     */

    @Query(name="ConfigModif.findByModifId")
    List<ConfigModif> findByModifId(@Param("modifId") Integer modifId);


    @Query(name="ConfigModif.findByTypeId")
    List<ConfigModif> findByTypeId(@Param("typeId") TypeModif typeId);
    
    /**
     * 
     * @param tipeId
     * @return 
     */
    @Query(value="SELECT * FROM config_modif NATURAL JOIN type_modif WHERE type_modif.typeId=?1 ORDER BY modif_id DESC LIMIT 1", nativeQuery=true)
    public Optional<ConfigModif> findTopByTypeIdOrderByModifIdDesc(TypeModif tipeId);
    
    /**
     * Renvoi le dernier element de configuration modifiée dont le nom correspond au paramètre 
     * @param nomModif Le nom de la modification
     * @return La dernière modif correspondante
     */
    @Query(value="SELECT * FROM config_modif NATURAL JOIN type_modif WHERE type_modif.nom= ?1 ORDER BY modif_id DESC LIMIT 1", nativeQuery=true)
    public Optional<ConfigModif> findTopByTypeNomOrderByModifIdDesc(String nomModif);

    @Query(value="SELECT * FROM config_modif WHERE type_id=?1 ORDER BY modif_id DESC LIMIT 1", nativeQuery=true)
    public Optional<ConfigModif> getLastTypeId(int typeId);
}
