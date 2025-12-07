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
import java.io.File;
import java.util.Comparator;
import org.centrale.infosi.pappl.logement.util.Util;

/**
 * Classe d'un formulaire générée automatiquement
 *
 * @author clesp
 *
 */
@Entity
@Table(name = "formulaire")
@NamedQueries({
    @NamedQuery(name = "Formulaire.findAll", query = "SELECT f FROM Formulaire f"),
    @NamedQuery(name = "Formulaire.findByFormulaireId", query = "SELECT f FROM Formulaire f WHERE f.formulaireId = :formulaireId"),
    @NamedQuery(name = "Formulaire.findByPersonneId", query = "SELECT f FROM Formulaire f WHERE f.personneId = :personneId"),
    @NamedQuery(name = "Formulaire.findByGenreId", query = "SELECT f FROM Formulaire f WHERE f.genreId = :genreId"),
    @NamedQuery(name = "Formulaire.findByPaysId", query = "SELECT f FROM Formulaire f WHERE f.paysId = :paysId"),
    @NamedQuery(name = "Formulaire.findByNumeroScei", query = "SELECT f FROM Formulaire f WHERE f.numeroScei = :numeroScei"),
    @NamedQuery(name = "Formulaire.findByDateDeNaissance", query = "SELECT f FROM Formulaire f WHERE f.dateDeNaissance = :dateDeNaissance"),
    @NamedQuery(name = "Formulaire.findByVille", query = "SELECT f FROM Formulaire f WHERE f.ville = :ville"),
    @NamedQuery(name = "Formulaire.findByCodePostal", query = "SELECT f FROM Formulaire f WHERE f.codePostal = :codePostal"),
    @NamedQuery(name = "Formulaire.findByMail", query = "SELECT f FROM Formulaire f WHERE f.mail = :mail"),
    @NamedQuery(name = "Formulaire.findByNumeroTel", query = "SELECT f FROM Formulaire f WHERE f.numeroTel = :numeroTel"),
    @NamedQuery(name = "Formulaire.findByCommentairesVe", query = "SELECT f FROM Formulaire f WHERE f.commentairesVe = :commentairesVe"),
    @NamedQuery(name = "Formulaire.findByCommentairesEleve", query = "SELECT f FROM Formulaire f WHERE f.commentairesEleve = :commentairesEleve"),
    @NamedQuery(name = "Formulaire.findByEstBoursier", query = "SELECT f FROM Formulaire f WHERE f.estBoursier = :estBoursier"),
    @NamedQuery(name = "Formulaire.findByEstPmr", query = "SELECT f FROM Formulaire f WHERE f.estPmr = :estPmr"),
    @NamedQuery(name = "Formulaire.findByNumeroLogement", query = "SELECT f FROM Formulaire f WHERE f.numeroLogement = :numeroLogement"),
    @NamedQuery(name = "Formulaire.findBySouhaitId", query = "SELECT f FROM Formulaire f WHERE f.souhaitId = :souhaitId"),
    @NamedQuery(name = "Formulaire.findAllEmails", query = "SELECT f.mail FROM Formulaire f"),
    @NamedQuery(name = "Formulaire.findByEstValide", query = "SELECT f FROM Formulaire f WHERE f.estValide = :estValide"),
    @NamedQuery(name = "Formulaire.findAllEMailsVague", query = "SELECT f.mail FROM Formulaire f where f.vague= false"),
    @NamedQuery(name = "Formulaire.findMailByToken", query = "SELECT f.mail FROM Formulaire f JOIN f.personneId p WHERE p.firstConnectionToken = ?1"),
    @NamedQuery(name = "Formulaire.findSCEIByToken", query = "SELECT f.numeroScei FROM Formulaire f JOIN f.personneId p WHERE p.firstConnectionToken = ?1"),
    @NamedQuery(name = "Formulaire.updateVague", query="UPDATE Formulaire f SET f.vague=true where f.vague=false"),
    @NamedQuery(name = "Formulaire.findByEstConforme", query = "SELECT f FROM Formulaire f WHERE f.estConforme = :estConforme")})

public class Formulaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "formulaire_id")
    private Integer formulaireId;

    @Column(name = "date_de_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateDeNaissance;

    @Column(name = "est_boursier")
    private Boolean estBoursier;

    @Column(name = "est_pmr")
    private Boolean estPmr;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formulaireId")
    private Collection<Alerte> alerteCollection;

    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    @ManyToOne(optional = false)
    private Genre genreId;

    @JoinColumn(name = "numero_logement", referencedColumnName = "numero_logement")
    @ManyToOne
    private Logement numeroLogement;

    @JoinColumn(name = "pays_id", referencedColumnName = "pays_id")
    @ManyToOne(optional = false)
    private Pays paysId;

    @JoinColumn(name = "personne_id", referencedColumnName = "personne_id")
    @ManyToOne(optional = false)
    private Personne personneId;

    @JoinColumn(name = "souhait_id", referencedColumnName = "souhait_id")
    @ManyToOne
    private Souhait souhaitId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "numero_scei")
    private String numeroScei;

    @Size(max = 255)
    @Column(name = "ville")
    private String ville;

    @Size(max = 255)
    @Column(name = "mail")
    private String mail;

    @Size(max = 16)
    @Column(name = "numero_tel")
    private String numeroTel;

    @Size(max = 2147483647)
    @Column(name = "commentaires_ve")
    private String commentairesVe;

    @Size(max = 2147483647)
    @Column(name = "commentaires_eleve")
    private String commentairesEleve;

    @Basic(optional = false)
    @NotNull
    @Column(name = "est_valide")
    private boolean estValide;

    @Basic(optional = false)
    @NotNull
    @Column(name = "est_conforme")
    private boolean estConforme;

    @Size(max = 2147483647)
    @Column(name = "code_postal")
    private String codePostal;

    @JoinColumn(name = "genre_attendu", referencedColumnName = "genre_id")
    @ManyToOne
    private Genre genreAttendu;

    @Column(name = "date_validation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;

    @Basic(optional = false)
    @NotNull
    @Column(name = "vague")
    private boolean vague;
    
    /**
     *
     */
    public Formulaire() {
    }

    /**
     *
     * @param formulaireId
     */
    public Formulaire(Integer formulaireId) {
        this.formulaireId = formulaireId;
    }

    /**
     *
     * @param formulaireId
     * @param numeroScei
     */
    public Formulaire(Integer formulaireId, String numeroScei) {
        this.formulaireId = formulaireId;
        this.numeroScei = numeroScei;
    }

    /**
     *
     * @return
     */
    public Integer getFormulaireId() {
        return formulaireId;
    }

    /**
     *
     * @param formulaireId
     */
    public void setFormulaireId(Integer formulaireId) {
        this.formulaireId = formulaireId;
    }

    /**
     *
     * @return
     */
    public String getNumeroScei() {
        return numeroScei;
    }

    /**
     *
     * @param numeroScei
     */
    public void setNumeroScei(String numeroScei) {
        this.numeroScei = numeroScei;
    }

    /**
     *
     * @return
     */
    public Date getDateDeNaissance() {
        return dateDeNaissance;
    }

    /**
     *
     * @param dateDeNaissance
     */
    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    /**
     *
     * @return
     */
    public String getNumeroTel() {
        return numeroTel;
    }

    /**
     *
     * @param numeroTel
     */
    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    /**
     *
     * @return
     */
    public String getCommentairesVe() {
        return commentairesVe;
    }

    /**
     *
     * @param commentairesVe
     */
    public void setCommentairesVe(String commentairesVe) {
        this.commentairesVe = commentairesVe;
    }

    /**
     *
     * @return
     */
    public String getCommentairesEleve() {
        return commentairesEleve;
    }

    /**
     *
     * @param commentairesEleve
     */
    public void setCommentairesEleve(String commentairesEleve) {
        this.commentairesEleve = commentairesEleve;
    }

    /**
     *
     * @return
     */
    public Boolean getEstBoursier() {
        return estBoursier;
    }

    /**
     *
     * @param estBoursier
     */
    public void setEstBoursier(Boolean estBoursier) {
        this.estBoursier = estBoursier;
    }

    /**
     *
     * @return
     */
    public Boolean getEstPmr() {
        return estPmr;
    }

    /**
     *
     * @param estPmr
     */
    public void setEstPmr(Boolean estPmr) {
        this.estPmr = estPmr;
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
    public Genre getGenreId() {
        return genreId;
    }

    /**
     *
     * @param genreId
     */
    public void setGenreId(Genre genreId) {
        this.genreId = genreId;
    }

    /**
     *
     * @return
     */
    public Logement getNumeroLogement() {
        return numeroLogement;
    }

    /**
     *
     * @param numeroLogement
     */
    public void setNumeroLogement(Logement numeroLogement) {
        this.numeroLogement = numeroLogement;
    }

    /**
     *
     * @return
     */
    public Pays getPaysId() {
        return paysId;
    }

    /**
     *
     * @param paysId
     */
    public void setPaysId(Pays paysId) {
        this.paysId = paysId;
    }

    /**
     *
     * @return
     */
    public Personne getPersonneId() {
        return personneId;
    }

    /**
     *
     * @param personneId
     */
    public void setPersonneId(Personne personneId) {
        this.personneId = personneId;
    }

    /**
     *
     * @return
     */
    public Souhait getSouhaitId() {
        return souhaitId;
    }

    /**
     *
     * @param souhaitId
     */
    public void setSouhaitId(Souhait souhaitId) {
        this.souhaitId = souhaitId;
    }

    public Alerte getAlerte() {
        if (this.getAlerteCollection().size() == 1) {
            return this.getAlerteCollection().iterator().next();
        }
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "centrale.org.pappl.mission_logement.Formulaire[ formulaireId=" + formulaireId + " ]";
    }

    /**
     *
     * @return
     */
    public boolean getEstValide() {
        return estValide;
    }

    /**
     *
     * @param estValide
     */
    public void setEstValide(boolean estValide) {
        this.estValide = estValide;
    }

    public boolean getEstConforme() {
        return estConforme;
    }

    public void setEstConforme(boolean estConforme) {
        this.estConforme = estConforme;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Genre getGenreAttendu() {
        return genreAttendu;
    }

    public void setGenreAttendu(Genre genreAttendu) {
        this.genreAttendu = genreAttendu;
    }

    public File getBourseFile() {
        String[] extList = {"png", "pdf"};
        File bourseFile = null;
        for (String ext1 : extList) {
            File cible = new File(Util.buildBourseFilePath(this.getNumeroScei(), ext1));
            if (cible.exists()) {
                bourseFile = cible;
                break;
            }
        }
        if ((bourseFile == null) || (! bourseFile.exists())) {
            return null;
        } else {
            return bourseFile;
        }
    }

    public boolean hasBourseFile() {
        return (this.getBourseFile() != null);
    }

    public boolean getVague() {
        return vague;
    }

    public void setVague(boolean vague) {
        this.vague = vague;
    }

    
    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formulaireId != null ? formulaireId.hashCode() : 0);
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
        if (!(object instanceof Formulaire)) {
            return false;
        }
        Formulaire other = (Formulaire) object;
        if ((this.formulaireId == null && other.formulaireId != null) || (this.formulaireId != null && !this.formulaireId.equals(other.formulaireId))) {
            return false;
        }
        return true;
    }

    public int compareTo(Object object) {
        if (object == null) {
            return 1;
        } else if (!(object instanceof Formulaire)) {
            return 1;
        }
        Formulaire object1 = (Formulaire) object;
        return this.getPersonneId().compareTo(object1.getPersonneId());
    }

    /**
     * Return Comparator for sorting tools
     *
     * @return
     */
    public static Comparator<Formulaire> getComparator() {
        return new Comparator<Formulaire>() {
            @Override
            public int compare(Formulaire object1, Formulaire object2) {
                if (object1 == null) {
                    return -1;
                } else {
                    return object1.compareTo(object2);
                }
            }
        };
    }
}
