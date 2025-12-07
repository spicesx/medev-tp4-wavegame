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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.util.Comparator;

/**
 * Classe d'une alerte générée automatiquement
 * @author clesp
 * 
 */
@Entity
@Table(name = "alerte")
@NamedQueries({
    @NamedQuery(name = "Alerte.findAll", query = "SELECT a FROM Alerte a"),
    @NamedQuery(name = "Alerte.findByAlerteId", query = "SELECT a FROM Alerte a WHERE a.alerteId = :alerteId"),
    @NamedQuery(name = "Alerte.findByFormulaireId", query = "SELECT a FROM Alerte a WHERE a.formulaireId = :formulaireId"),
    @NamedQuery(name = "Alerte.statutId", query = "SELECT a FROM Alerte a WHERE a.statutId = :statutId")})
public class Alerte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "alerte_id")
    private Integer alerteId;

    @JoinColumn(name = "formulaire_id", referencedColumnName = "formulaire_id")
    @ManyToOne(optional = false)
    private Formulaire formulaireId;

    @JoinColumn(name = "statut_id", referencedColumnName = "statut_id")
    @ManyToOne(optional = false)
    private Statut statutId;

    /**
     *
     */
    public Alerte() {
    }

    /**
     *
     * @param alerteId
     */
    public Alerte(Integer alerteId) {
        this.alerteId = alerteId;
    }

    /**
     *
     * @return
     */
    public Integer getAlerteId() {
        return alerteId;
    }

    /**
     *
     * @param alerteId
     */
    public void setAlerteId(Integer alerteId) {
        this.alerteId = alerteId;
    }

    /**
     *
     * @return
     */
    public Formulaire getFormulaireId() {
        return formulaireId;
    }

    /**
     *
     * @param formulaireId
     */
    public void setFormulaireId(Formulaire formulaireId) {
        this.formulaireId = formulaireId;
    }

    /**
     *
     * @return
     */
    public Statut getStatutId() {
        return statutId;
    }

    /**
     *
     * @param statutId
     */
    public void setStatutId(Statut statutId) {
        this.statutId = statutId;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alerteId != null ? alerteId.hashCode() : 0);
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
        if (!(object instanceof Alerte)) {
            return false;
        }
        Alerte other = (Alerte) object;
        if ((this.alerteId == null && other.alerteId != null) || (this.alerteId != null && !this.alerteId.equals(other.alerteId))) {
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
        return "centrale.org.pappl.mission_logement.Alerte[ alerteId=" + alerteId + " ]";
    }
    

    public int compareTo(Object object) {
        if (object == null) {
            return 1;
        } else if (!(object instanceof Alerte)) {
            return 1;
        }
        Alerte object1 = (Alerte)object;
         return this.getFormulaireId().getPersonneId().compareTo(object1.getFormulaireId().getPersonneId());
    }

    /**
     * Return Comparator for sorting tools
     *
     * @return
     */
    public static Comparator<Alerte> getComparator() {
        return new Comparator<Alerte>() {
            @Override
            public int compare(Alerte object1, Alerte object2) {
                if (object1 == null) {
                    return -1;
                } else {
                    return object1.compareTo(object2);
                }
            }
        };
    }
}
