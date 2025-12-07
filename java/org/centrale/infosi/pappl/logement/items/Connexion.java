/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.items;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;

/** 
 * Classe d'une connexion générée automatiquement
 * @author samer
 * 
 */
@Entity
@Table(name = "connexion")
@NamedQueries({
    @NamedQuery(name = "Connexion.findAll", query = "SELECT c FROM Connexion c"),
    @NamedQuery(name = "Connexion.findByConnexionId", query = "SELECT c FROM Connexion c WHERE c.connexionId = :connexionId"),
    @NamedQuery(name = "Connexion.findByExpiration", query = "SELECT c FROM Connexion c WHERE c.expiration = :expiration")})
public class Connexion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "connexion_id")
    private String connexionId;

    @Column(name = "expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;

    @JoinColumn(name = "personne_id", referencedColumnName = "personne_id")
    @ManyToOne(optional = false)
    private Personne personneId;

    public Connexion() {
    }

    public Connexion(String connexionId) {
        this.connexionId = connexionId;
    }

    public String getConnexionId() {
        return connexionId;
    }

    public void setConnexionId(String connexionId) {
        this.connexionId = connexionId;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public Personne getPersonneId() {
        return personneId;
    }

    public void setPersonneId(Personne personneId) {
        this.personneId = personneId;
    }

    
    public boolean isAdmin() {
        return this.getPersonneId().isAdmin();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (connexionId != null ? connexionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Connexion)) {
            return false;
        }
        Connexion other = (Connexion) object;
        if ((this.connexionId == null && other.connexionId != null) || (this.connexionId != null && !this.connexionId.equals(other.connexionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.centrale.infosi.pappl.logement.items.Connexion[ connexionId=" + connexionId + " ]";
    }
    
}
