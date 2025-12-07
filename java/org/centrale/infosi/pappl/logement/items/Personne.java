/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.items;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Comparator;

/**
 * Classe d'une personne générée automatiquement
 * @author samer
 *
 */
@Entity
@Table(name = "personne")
@NamedQueries({
    @NamedQuery(name = "Personne.findAll", query = "SELECT p FROM Personne p"),
    @NamedQuery(name = "Personne.findByPersonneId", query = "SELECT p FROM Personne p WHERE p.personneId = :personneId"),
    @NamedQuery(name = "Personne.findByRoleId", query = "SELECT p FROM Personne p WHERE p.roleId = :roleId"),
    @NamedQuery(name = "Personne.findByNom", query = "SELECT p FROM Personne p WHERE p.nom = :nom"),
    @NamedQuery(name = "Personne.findByPrenom", query = "SELECT p FROM Personne p WHERE p.prenom = :prenom"),
    @NamedQuery(name = "Personne.findByLogin", query = "SELECT p FROM Personne p WHERE p.login = :login"),
    @NamedQuery(name = "Personne.findByPassword", query = "SELECT p FROM Personne p WHERE p.password = :password"),
    @NamedQuery(name = "Personne.findByFirstConnectionToken", query = "SELECT p FROM Personne p WHERE p.firstConnectionToken = :firstConnectionToken"),
    @NamedQuery(name="Personne.findAllTokenVague", query = "SELECT p.firstConnectionToken FROM Personne p JOIN p.formulaireCollection f WHERE f.vague=false"),
    @NamedQuery(name = "Personne.findByFirstConnectionTokenExpiry", query = "SELECT p FROM Personne p WHERE p.firstConnectionTokenExpiry = :firstConnectionTokenExpiry")})
public class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "personne_id")
    private Integer personneId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nom")
    private String nom;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "prenom")
    private String prenom;
    
    @Size(max = 255)
    @Column(name = "login")
    private String login;
    
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    
    @Size(max = 255)
    @Column(name = "first_connection_token")
    private String firstConnectionToken;
    
    @Column(name = "first_connection_token_expiry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstConnectionTokenExpiry;

    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(optional = false)
    private Role roleId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personneId")
    private Collection<Connexion> connexionCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personneId")
    private Collection<Formulaire> formulaireCollection;

    public Personne() {
    }

    public Personne(Integer personneId) {
        this.personneId = personneId;
    }

    public Personne(Integer personneId, String nom, String prenom) {
        this.personneId = personneId;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Integer getPersonneId() {
        return personneId;
    }

    public void setPersonneId(Integer personneId) {
        this.personneId = personneId;
    }


    public String getFirstConnectionToken() {
        return firstConnectionToken;
    }

    public void setFirstConnectionToken(String firstConnectionToken) {
        this.firstConnectionToken = firstConnectionToken;
    }

    public Date getFirstConnectionTokenExpiry() {
        return firstConnectionTokenExpiry;
    }

    public void setFirstConnectionTokenExpiry(Date firstConnectionTokenExpiry) {
        this.firstConnectionTokenExpiry = firstConnectionTokenExpiry;
    }

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
        this.roleId = roleId;
    }

    public Collection<Connexion> getConnexionCollection() {
        return connexionCollection;
    }

    public void setConnexionCollection(Collection<Connexion> connexionCollection) {
        this.connexionCollection = connexionCollection;
    }

    public void setFormulaireCollection(Collection<Formulaire> formulaireCollection) {
        this.formulaireCollection = formulaireCollection;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getNomPrenom() {
        return (nom + " " + prenom).trim();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isAdmin() {
        Role role = this.getRoleId();
        if (role == null) {
            return false;
        } else {
            return role.isAdmin();
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personneId != null ? personneId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personne)) {
            return false;
        }
        Personne other = (Personne) object;
        if ((this.personneId == null && other.personneId != null) || (this.personneId != null && !this.personneId.equals(other.personneId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "centrale.org.pappl.mission_logement.Personne[ personneId=" + personneId + " ]";
    }
    public Collection<Formulaire> getFormulaireCollection() {
        return formulaireCollection;
    }

    public int compareTo(Object object) {
        if (object == null) {
            return 1;
        } else if (! (object instanceof Personne)) {
            return 1;
        } else {
            return this.getNomPrenom().toUpperCase().compareTo(((Personne)object).getNomPrenom().toUpperCase());
        }
    }

    /**
     * Return Comparator for sorting tools 
     * @return
     */
    public static Comparator<Personne> getComparator() {
        return new Comparator<Personne>() {
            @Override
            public int compare(Personne object1, Personne object2) {
                if (object1 == null) {
                    return -1;
                } else {
                    return object1.compareTo(object2);
                }
            }
        };
    }
}
