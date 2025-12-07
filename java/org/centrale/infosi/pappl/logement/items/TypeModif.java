/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.items;

import java.io.Serializable;
import jakarta.persistence.Basic;
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
import jakarta.validation.constraints.Size;

/**
 * Classe d'un type de modification générée automatiquement
 * @author Quent
 * 
 */
@Entity
@Table(name = "type_modif")
@NamedQueries({
    @NamedQuery(name = "TypeModif.findAll", query = "SELECT t FROM TypeModif t"),
    @NamedQuery(name = "TypeModif.findByTypeId", query = "SELECT t FROM TypeModif t WHERE t.typeId = :typeId"),
    @NamedQuery(name = "TypeModif.findByNom", query = "SELECT t FROM TypeModif t WHERE t.nom = :nom")})
public class TypeModif implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "type_id")
    private Integer typeId;

    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId")
    private Collection<ConfigModif> configModifCollection;


//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeModif")
//    private Collection<ConfigModif> configModifCollection;

    public TypeModif() {
    }

    public TypeModif(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Collection<ConfigModif> getConfigModifCollection() {
        return configModifCollection;
    }

    public void setConfigModifCollection(Collection<ConfigModif> configModifCollection) {
        this.configModifCollection = configModifCollection;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


//    public Collection<ConfigModif> getConfigModifCollection() {
//        return configModifCollection;
//    }
//
//    public void setConfigModifCollection(Collection<ConfigModif> configModifCollection) {
//        this.configModifCollection = configModifCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeId != null ? typeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeModif)) {
            return false;
        }
        TypeModif other = (TypeModif) object;
        if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.centrale.infosi.pappl.logement.items.TypeModif[ typeId=" + typeId + " ]";
    }
    
}
