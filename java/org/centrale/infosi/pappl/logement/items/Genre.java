/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.items;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Classe d'un genre générée automatiquement
 * @author clesp
 * 
 */
@Entity
@Table(name = "genre")
@NamedQueries({
    @NamedQuery(name = "Genre.findAll", query = "SELECT g FROM Genre g"),
    @NamedQuery(name = "Genre.findByGenreId", query = "SELECT g FROM Genre g WHERE g.genreId = :genreId"),
    @NamedQuery(name = "Genre.findByGenreNom", query = "SELECT g FROM Genre g WHERE g.genreNom = :genreNom"),
    @NamedQuery(name = "Genre.findByGenreOrdre", query = "SELECT g FROM Genre g WHERE g.genreOrdre = :genreOrdre")})
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final int MASCULIN = 1;
    public static final int FEMININ = 2;
    public static final int AUTRE = 3;
    public static final int NSP = 4;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "genre_id")
    private Integer genreId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "genre_nom")
    private String genreNom;

    @Column(name = "genre_ordre")
    private String genreOrdre;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genreId")
    private Collection<Formulaire> formulaireCollection;

    /**
     *
     */
    public Genre() {
    }

    /**
     *
     * @param genreId
     */
    public Genre(Integer genreId) {
        this.genreId = genreId;
    }

    /**
     *
     * @param genreId
     * @param genreNom
     */
    public Genre(Integer genreId, String genreNom) {
        this.genreId = genreId;
        this.genreNom = genreNom;
    }

    /**
     *
     * @return
     */
    public Integer getGenreId() {
        return genreId;
    }

    /**
     *
     * @param genreId
     */
    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    /**
     *
     * @return
     */
    public String getGenreNom() {
        return genreNom;
    }

    /**
     *
     * @param genreNom
     */
    public void setGenreNom(String genreNom) {
        this.genreNom = genreNom;
    }

    public String getGenreOrdre() {
        return genreOrdre;
    }

    public void setGenreOrdre(String genreOrdre) {
        this.genreOrdre = genreOrdre;
    }

    /**
     *
     * @return
     */
    public Collection<Formulaire> getFormulaireCollection() {
        return formulaireCollection;
    }

    /**
     *
     * @param formulaireCollection
     */
    public void setFormulaireCollection(Collection<Formulaire> formulaireCollection) {
        this.formulaireCollection = formulaireCollection;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genreId != null ? genreId.hashCode() : 0);
        return hash;
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Genre)) {
            return false;
        }
        Genre other = (Genre) object;
        if ((this.genreId == null && other.genreId != null) || (this.genreId != null && !this.genreId.equals(other.genreId))) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "centrale.org.pappl.mission_logement.Genre[ genreId=" + genreId + " ]";
    }
  
}