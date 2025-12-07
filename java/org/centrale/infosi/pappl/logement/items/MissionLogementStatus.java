/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.items;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Classe d'un statut de la mission logement générée automatiquement
 *
 * @author samer
 *
 */
@Entity
@Table(name = "mission_logement_status")
@NamedQueries({
    @NamedQuery(name = "MissionLogementStatus.findAll", query = "SELECT m FROM MissionLogementStatus m"),
    @NamedQuery(name = "MissionLogementStatus.findById", query = "SELECT m FROM MissionLogementStatus m WHERE m.id = :id"),
    @NamedQuery(name = "MissionLogementStatus.findByStatus", query = "SELECT m FROM MissionLogementStatus m WHERE m.status = :status")})
public class MissionLogementStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final int MISSIONID = 1;
    
    public static int NOTSTARTED = 0;
    public static int INPROGRESS = 1;
    public static int FINISHED = 2;
    
    @Id
    private Integer id = MISSIONID;  // Always 1

    @Column(nullable = false)
    private int status; // 0: Not Started, 1: In Progress, 2: Finished

    public MissionLogementStatus() {
        this.status = 0; // Default: Not Started
    }

    public MissionLogementStatus(int status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MissionLogementStatus)) {
            return false;
        }
        MissionLogementStatus other = (MissionLogementStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.centrale.infosi.pappl.logement.items.MissionLogementStatus[ id=" + id + " ]";
    }

}
