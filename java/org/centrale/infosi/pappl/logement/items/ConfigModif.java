/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.items;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
 * Classe d'une configuration générée automatiquement
 * @author Quent
 * 
 */
@Entity
@Table(name = "config_modif")
@NamedQueries({
    @NamedQuery(name = "ConfigModif.findAll", query = "SELECT c FROM ConfigModif c"),
    @NamedQuery(name = "ConfigModif.findByModifId", query = "SELECT c FROM ConfigModif c WHERE c.modifId = :modifId"),
    @NamedQuery(name = "ConfigModif.findByTypeId", query = "SELECT c FROM ConfigModif c WHERE c.typeId = :typeId"),
    @NamedQuery(name = "ConfigModif.findByContenu", query = "SELECT c FROM ConfigModif c WHERE c.contenu = :contenu")})
public class ConfigModif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "modif_id")
    private Integer modifId;
    
    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    @ManyToOne(optional = false)
    private TypeModif typeId;

    @Size(max = 2147483647)
    @Column(name = "contenu")
    public String contenu;

    public ConfigModif() {
    }


    public ConfigModif(Integer modifId) {
        this.modifId = modifId;
    }

    public Integer getModifId() {
        return modifId;
    }

    public void setModifId(Integer modifId) {
        this.modifId = modifId;
    }



    public TypeModif getTypeId() {
        return typeId;
    }

    public void setTypeId(TypeModif typeId) {
        this.typeId = typeId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modifId != null ? modifId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfigModif)) {
            return false;
        }
        ConfigModif other = (ConfigModif) object;
        if ((this.modifId == null && other.modifId != null) || (this.modifId != null && !this.modifId.equals(other.modifId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.centrale.infosi.pappl.logement.items.ConfigModif[ modifId=" + modifId + " ]";
    }
    
}
