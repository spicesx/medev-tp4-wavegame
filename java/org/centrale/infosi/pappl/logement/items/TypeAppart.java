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
 * Classe d'un type de logement générée automatiquement
 * @author clesp
 * 
 */
@Entity
@Table(name = "type_appart")
@NamedQueries({
    @NamedQuery(name = "TypeAppart.findAll", query = "SELECT t FROM TypeAppart t"),
    @NamedQuery(name = "TypeAppart.findByTypeAppartId", query = "SELECT t FROM TypeAppart t WHERE t.typeAppartId = :typeAppartId"),
    @NamedQuery(name = "TypeAppart.findByTypeAppartNom", query = "SELECT t FROM TypeAppart t WHERE t.typeAppartNom = :typeAppartNom")})
public class TypeAppart implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "type_appart_id")
    private Integer typeAppartId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "type_appart_nom")
    private String typeAppartNom;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeAppartId")
    private Collection<Logement> logementCollection;

    /**
     *
     */
    public TypeAppart() {
    }

    /**
     *
     * @param typeAppartId
     */
    public TypeAppart(Integer typeAppartId) {
        this.typeAppartId = typeAppartId;
    }

    /**
     *
     * @param typeAppartId
     * @param typeAppartNom
     */
    public TypeAppart(Integer typeAppartId, String typeAppartNom) {
        this.typeAppartId = typeAppartId;
        this.typeAppartNom = typeAppartNom;
    }

    /**
     *
     * @return
     */
    public Integer getTypeAppartId() {
        return typeAppartId;
    }

    /**
     *
     * @param typeAppartId
     */
    public void setTypeAppartId(Integer typeAppartId) {
        this.typeAppartId = typeAppartId;
    }

    /**
     *
     * @return
     */
    public String getTypeAppartNom() {
        return typeAppartNom;
    }

    /**
     *
     * @param typeAppartNom
     */
    public void setTypeAppartNom(String typeAppartNom) {
        this.typeAppartNom = typeAppartNom;
    }

    /**
     *
     * @return
     */
    public Collection<Logement> getLogementCollection() {
        return logementCollection;
    }

    /**
     *
     * @param logementCollection
     */
    public void setLogementCollection(Collection<Logement> logementCollection) {
        this.logementCollection = logementCollection;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeAppartId != null ? typeAppartId.hashCode() : 0);
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
        if (!(object instanceof TypeAppart)) {
            return false;
        }
        TypeAppart other = (TypeAppart) object;
        if ((this.typeAppartId == null && other.typeAppartId != null) || (this.typeAppartId != null && !this.typeAppartId.equals(other.typeAppartId))) {
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
        return "centrale.org.pappl.mission_logement.TypeAppart[ typeAppartId=" + typeAppartId + " ]";
    }

}
