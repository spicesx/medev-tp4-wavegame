/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Logement;
import java.util.Collection;
import org.centrale.infosi.pappl.logement.items.TypeAppart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository lié au logement (sert à l'affichage des résultats)
 * @author clesp
 */
@Repository
public interface LogementRepository extends JpaRepository<Logement,String>,LogementRepositoryCustom {

    /**
     * 
     * @param numero_logement
     * @return
     */
    public Collection<Logement> findByNumeroLogement(@Param ("numeroLogement") String numero_logement);

    /**
     *
     * @param genre_requis
     * @return
     */
    public Collection<Logement> findByGenreRequis(@Param("genreRequis")Integer genre_requis);

    /**
     *
     * @param nb_places_dispo
     * @return
     */
    public Collection<Logement> findByNbPlacesDispo(@Param("nbPlacesDispo")Integer nb_places_dispo);

    /**
     *
     * @param type_appart_id
     * @return
     */
    public Collection<Logement> findByTypeAppartId(@Param("typeAppartId")TypeAppart type_appart_id);
}
