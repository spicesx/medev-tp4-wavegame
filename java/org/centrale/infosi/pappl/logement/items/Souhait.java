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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.Comparator;

/**
 * Classe d'un souhait générée automatiquement
 * @author clesp
 * 
 */
@Entity
@Table(name = "souhait")
@NamedQueries({
    @NamedQuery(name = "Souhait.findAll", query = "SELECT s FROM Souhait s"),
    @NamedQuery(name = "Souhait.findBySouhaitId", query = "SELECT s FROM Souhait s WHERE s.souhaitId = :souhaitId"),
    @NamedQuery(name = "Souhait.findBySouhaitType", query = "SELECT s FROM Souhait s WHERE s.souhaitType = :souhaitType"),
    @NamedQuery(name = "Souhait.findBySouhaitOrdre", query = "SELECT s FROM Souhait s WHERE s.souhaitOrdre = :souhaitOrdre")})
public class Souhait implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "souhait_id")
    private Integer souhaitId;

    @Size(max = 255)
    @Column(name = "souhait_type")
    private String souhaitType;

    @Column(name = "souhait_ordre")
    private Integer souhaitOrdre;

    @OneToMany(mappedBy = "souhaitId")
    private Collection<Formulaire> formulaireCollection;

    /**
     *
     */
    public Souhait() {
    }

    /**
     *
     * @param souhaitId
     */
    public Souhait(Integer souhaitId) {
        this.souhaitId = souhaitId;
    }

    /**
     *
     * @return
     */
    public Integer getSouhaitId() {
        return souhaitId;
    }

    /**
     *
     * @param souhaitId
     */
    public void setSouhaitId(Integer souhaitId) {
        this.souhaitId = souhaitId;
    }

    /**
     *
     * @return
     */
    public String getSouhaitType() {
        return souhaitType;
    }

    /**
     *
     * @param souhaitType
     */
    public void setSouhaitType(String souhaitType) {
        this.souhaitType = souhaitType;
    }

    /**
     * 
     * @return 
     */
    public Integer getSouhaitOrdre() {
        return souhaitOrdre;
    }

    /**
     * 
     * @param souhaitOrdre 
     */
    public void setSouhaitOrdre(Integer souhaitOrdre) {
        this.souhaitOrdre = souhaitOrdre;
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
        hash += (souhaitId != null ? souhaitId.hashCode() : 0);
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
        if (!(object instanceof Souhait)) {
            return false;
        }
        Souhait other = (Souhait) object;
        if ((this.souhaitId == null && other.souhaitId != null) || (this.souhaitId != null && !this.souhaitId.equals(other.souhaitId))) {
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
        return "centrale.org.pappl.mission_logement.Souhait[ souhaitId=" + souhaitId + " ]";
    }

    public int compareTo(Object object) {
        if (object == null) {
            return 1;
        } else if (! (object instanceof Souhait)) {
            return 1;
        } else {
            return this.getSouhaitOrdre() - ((Souhait)object).getSouhaitOrdre();
        }
    }

    /**
     * Return Comparator for sorting tools 
     * @return
     */
    public static Comparator<Souhait> getComparator() {
        return new Comparator<Souhait>() {
            @Override
            public int compare(Souhait object1, Souhait object2) {
                if (object1 == null) {
                    return -1;
                } else {
                    return object1.compareTo(object2);
                }
            }
        };
    }
    
}
