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
import java.util.Comparator;

/**
 * Classe d'un pays générée automatiquement
 * @author clesp
 * 
 */
@Entity
@Table(name = "pays")
@NamedQueries({
    @NamedQuery(name = "Pays.findAll", query = "SELECT p FROM Pays p"),
    @NamedQuery(name = "Pays.findByPaysId", query = "SELECT p FROM Pays p WHERE p.paysId = :paysId"),
    @NamedQuery(name = "Pays.findByPaysNom", query = "SELECT p FROM Pays p WHERE p.paysNom = :paysNom")})
public class Pays implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pays_id")
    private Integer paysId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paysId")
    private Collection<Formulaire> formulaireCollection;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "pays_nom")
    private String paysNom;

    /**
     *
     */
    public Pays() {
    }

    /**
     *
     * @param paysId
     */
    public Pays(Integer paysId) {
        this.paysId = paysId;
    }

    /**
     *
     * @param paysId
     * @param paysNom
     */
    public Pays(Integer paysId, String paysNom) {
        this.paysId = paysId;
        this.paysNom = paysNom;
    }

    /**
     *
     * @return
     */
    public Integer getPaysId() {
        return paysId;
    }

    /**
     *
     * @param paysId
     */
    public void setPaysId(Integer paysId) {
        this.paysId = paysId;
    }

    /**
     *
     * @return
     */
    public String getPaysNom() {
        return paysNom;
    }

    /**
     *
     * @param paysNom
     */
    public void setPaysNom(String paysNom) {
        this.paysNom = paysNom;
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
        hash += (paysId != null ? paysId.hashCode() : 0);
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
        if (!(object instanceof Pays)) {
            return false;
        }
        Pays other = (Pays) object;
        if ((this.paysId == null && other.paysId != null) || (this.paysId != null && !this.paysId.equals(other.paysId))) {
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
        return "centrale.org.pappl.mission_logement.Pays[ paysId=" + paysId + " ]";
    }


    public int compareTo(Object object) {
        if (object == null) {
            return 1;
        } else if (!(object instanceof Pays)) {
            return 1;
        }
        Pays object1 = (Pays) object;
        return this.getPaysNom().toLowerCase().compareTo(object1.getPaysNom().toLowerCase());
    }

    /**
     * Return Comparator for sorting tools
     *
     * @return
     */
    public static Comparator<Pays> getComparator() {
        return new Comparator<Pays>() {
            @Override
            public int compare(Pays object1, Pays object2) {
                if (object1 == null) {
                    return -1;
                } else {
                    return object1.compareTo(object2);
                }
            }
        };
    }

    
}
