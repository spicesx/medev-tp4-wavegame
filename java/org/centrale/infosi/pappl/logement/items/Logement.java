/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.items;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Classe d'un logement générée automatiquement
 * @author clesp
 * 
 */
@Entity
@Table(name = "logement")
@NamedQueries({
    @NamedQuery(name = "Logement.findAll", query = "SELECT l FROM Logement l"),
    @NamedQuery(name = "Logement.findByNumeroLogement", query = "SELECT l FROM Logement l WHERE l.numeroLogement = :numeroLogement"),
    @NamedQuery(name = "Logement.findByGenreRequis", query = "SELECT l FROM Logement l WHERE l.genreRequis = :genreRequis"),
    @NamedQuery(name = "Logement.findByNbPlacesDispo", query = "SELECT l FROM Logement l WHERE l.nbPlacesDispo = :nbPlacesDispo"),
    @NamedQuery(name = "Logement.findByTypeAppartId", query = "SELECT l FROM Logement l WHERE l.typeAppartId = :typeAppartId")})
public class Logement implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "numero_logement")
    private String numeroLogement;

    @Column(name = "nb_places_dispo")
    private Integer nbPlacesDispo;

    @JoinColumn(name = "type_appart_id", referencedColumnName = "type_appart_id")
    @ManyToOne(optional = false)
    private TypeAppart typeAppartId;

    @OneToMany(mappedBy = "numeroLogement")
    private Collection<Formulaire> formulaireCollection;

    @Size(max = 255)
    @Column(name = "genre_requis")
    private String genreRequis;

    /**
     *
     */
    public Logement() {
    }

    /**
     *
     * @param numeroLogement
     */
    public Logement(String numeroLogement) {
        this.numeroLogement = numeroLogement;
    }

    /**
     *
     * @return
     */
    public String getNumeroLogement() {
        return numeroLogement;
    }

    /**
     *
     * @param numeroLogement
     */
    public void setNumeroLogement(String numeroLogement) {
        this.numeroLogement = numeroLogement;
    }

    /**
     *
     * @return
     */
    public String getGenreRequis() {
        return genreRequis;
    }

    /**
     *
     * @param genreRequis
     */
    public void setGenreRequis(String genreRequis) {
        this.genreRequis = genreRequis;
    }

    /**
     *
     * @return
     */
    public Integer getNbPlacesDispo() {
        return nbPlacesDispo;
    }

    /**
     *
     * @param nbPlacesDispo
     */
    public void setNbPlacesDispo(Integer nbPlacesDispo) {
        this.nbPlacesDispo = nbPlacesDispo;
    }

    /**
     *
     * @return
     */
    public TypeAppart getTypeAppartId() {
        return typeAppartId;
    }

    /**
     *
     * @param typeAppartId
     */
    public void setTypeAppartId(TypeAppart typeAppartId) {
        this.typeAppartId = typeAppartId;
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
        hash += (numeroLogement != null ? numeroLogement.hashCode() : 0);
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
        if (!(object instanceof Logement)) {
            return false;
        }
        Logement other = (Logement) object;
        if ((this.numeroLogement == null && other.numeroLogement != null) || (this.numeroLogement != null && !this.numeroLogement.equals(other.numeroLogement))) {
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
        return "centrale.org.pappl.mission_logement.Logement[ numeroLogement=" + numeroLogement + " ]";
    }
}