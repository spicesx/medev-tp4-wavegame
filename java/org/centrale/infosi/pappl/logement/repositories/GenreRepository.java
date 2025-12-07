/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Genre;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository lié au genre
 * @author clesp
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre,Integer>,GenreRepositoryCustom {

    /**
     *
     * @param genre_id
     * @return
     */
    public Collection<Genre> findByGenreId(@Param("genreId")Integer genre_id);

    /**
     *
     * @param genreNom
     * @return
     */
    public Collection<Genre> findByGenreNom(@Param("genreNom")String genreNom);
}
