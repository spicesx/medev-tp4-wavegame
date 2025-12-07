/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.items;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

/**
 * Classe d'une date générée automatiquement
 * @author clesp
 * 
 */
@Entity
@Table(name = "date")
@NamedQueries({
    @NamedQuery(name = "Date.findAll", query = "SELECT d FROM Date d"),
    @NamedQuery(name = "Date.findByDateId", query = "SELECT d FROM Date d WHERE d.dateId = :dateId"),
    @NamedQuery(name = "Date.findByDateDebut", query = "SELECT d FROM Date d WHERE d.dateDebut = :dateDebut"),
    @NamedQuery(name = "Date.findByDateFin", query = "SELECT d FROM Date d WHERE d.dateFin = :dateFin")})
public class Date implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "date_id")
    private Integer dateId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date_debut")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateDebut;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateFin;

    /**
     *
     */
    public Date() {
    }

    /**
     *
     * @param dateId
     */
    public Date(Integer dateId) {
        this.dateId = dateId;
    }

    /**
     *
     * @param dateId
     * @param dateDebut
     * @param dateFin
     */
    public Date(Integer dateId, java.util.Date dateDebut, java.util.Date dateFin) {
        this.dateId = dateId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    /**
     *
     * @return
     */
    public Integer getDateId() {
        return dateId;
    }

    /**
     *
     * @param dateId
     */
    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    /**
     *
     * @return
     */
    public java.util.Date getDateDebut() {
        return dateDebut;
    }

    /**
     *
     * @param dateDebut
     */
    public void setDateDebut(java.util.Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     *
     * @return
     */
    public java.util.Date getDateFin() {
        return dateFin;
    }

    /**
     *
     * @param dateFin
     */
    public void setDateFin(java.util.Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dateId != null ? dateId.hashCode() : 0);
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
        if (!(object instanceof Date)) {
            return false;
        }
        Date other = (Date) object;
        if ((this.dateId == null && other.dateId != null) || (this.dateId != null && !this.dateId.equals(other.dateId))) {
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
        return "org.centrale.infosi.pappl.logement.items.Date[ dateId=" + dateId + " ]";
    }
    
}
