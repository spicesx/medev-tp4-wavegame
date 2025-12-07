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
 * Classe d'un statut d'alerte générée automatiquement
 * @author clesp
 *  
 */
@Entity
@Table(name = "statut")
@NamedQueries({
    @NamedQuery(name = "Statut.findAll", query = "SELECT s FROM Statut s"),
    @NamedQuery(name = "Statut.findByStatutId", query = "SELECT s FROM Statut s WHERE s.statutId = :statutId"),
    @NamedQuery(name = "Statut.findByStatutNom", query = "SELECT s FROM Statut s WHERE s.statutNom = :statutNom")})
public class Statut implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final int NONTRAITE = 1;
    public static final int TRAITE = 2;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "statut_id")
    private Integer statutId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "statut_nom")
    private String statutNom;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statutId")
    private Collection<Alerte> alerteCollection;

    /**
     *
     */
    public Statut() {
    }

    /**
     *
     * @param statutId
     */
    public Statut(Integer statutId) {
        this.statutId = statutId;
    }

    /**
     *
     * @param statutId
     * @param statutNom
     */
    public Statut(Integer statutId, String statutNom) {
        this.statutId = statutId;
        this.statutNom = statutNom;
    }

    /**
     *
     * @return
     */
    public Integer getStatutId() {
        return statutId;
    }

    /**
     *
     * @param statutId
     */
    public void setStatutId(Integer statutId) {
        this.statutId = statutId;
    }

    /**
     *
     * @return
     */
    public String getStatutNom() {
        return statutNom;
    }

    /**
     *
     * @param statutNom
     */
    public void setStatutNom(String statutNom) {
        this.statutNom = statutNom;
    }

    /**
     *
     * @return
     */
    public Collection<Alerte> getAlerteCollection() {
        return alerteCollection;
    }

    /**
     *
     * @param alerteCollection
     */
    public void setAlerteCollection(Collection<Alerte> alerteCollection) {
        this.alerteCollection = alerteCollection;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statutId != null ? statutId.hashCode() : 0);
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
        if (!(object instanceof Statut)) {
            return false;
        }
        Statut other = (Statut) object;
        if ((this.statutId == null && other.statutId != null) || (this.statutId != null && !this.statutId.equals(other.statutId))) {
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
        return "centrale.org.pappl.mission_logement.Statut[ statutId=" + statutId + " ]";
    }



}
