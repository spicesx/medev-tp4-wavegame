/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import static java.nio.file.StandardCopyOption.*;

import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.text.StringEscapeUtils;

import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.items.Genre;
import org.centrale.infosi.pappl.logement.items.Pays;
import org.centrale.infosi.pappl.logement.items.Souhait;
import org.centrale.infosi.pappl.logement.repositories.AlerteRepository;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.centrale.infosi.pappl.logement.repositories.GenreRepository;
import org.centrale.infosi.pappl.logement.repositories.PaysRepository;
import org.centrale.infosi.pappl.logement.util.Util;
import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.repositories.ConfigModifRepository;
import org.centrale.infosi.pappl.logement.repositories.PersonneRepository;
import org.centrale.infosi.pappl.logement.repositories.SouhaitRepository;
import org.centrale.infosi.pappl.logement.util.PasswordUtils;
import static org.centrale.infosi.pappl.logement.util.Util.getIntFromString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller permettant de gérer les formulaires remplis par un étudiant
 * (enregistrementFormulaire, soumission)
 *
 * @author barbo
 *
 */
@Controller
public class FormulaireController {

    @Autowired
    @Lazy
    private ConfigModifRepository configModifRepository;

    @Lazy
    @Autowired
    private FormulaireRepository formulaireRepository;

    @Lazy
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ConnectionService connectionService;

    @Lazy
    @Autowired
    private AlerteRepository alerteRepository;

    @Lazy
    @Autowired
    private PaysRepository paysRepository;

    @Autowired
    @Lazy
    private PersonneRepository personneRepository;

    @Autowired
    @Lazy
    private SouhaitRepository souhaitRepository;

    private File findBourseFile(Formulaire formulaire) {
        String[] extList = {"png", "pdf"};
        File bourseFile = null;
        for (String ext1 : extList) {
            File cible = new File(Util.buildBourseFilePath(formulaire.getNumeroScei(), ext1));
            if (cible.exists()) {
                bourseFile = cible;
                break;
            }
        }
        return bourseFile;
    }

    private ModelAndView displayFormulaire(Connexion connexion, Formulaire formulaire, boolean erreur) {
        File bourseFile = this.findBourseFile(formulaire);

        ModelAndView returned = connectionService.prepareModelAndView(connexion, "formulaire");
        returned.addObject("item", formulaire);
        returned.addObject("bourseFile", bourseFile);
        returned.addObject("error", erreur);
        returned.addObject("souhaitsList", souhaitRepository.findAll(Sort.by(Sort.Direction.ASC, "souhaitOrdre")));
        returned.addObject("genresList", genreRepository.findAll(Sort.by(Sort.Direction.ASC, "genreOrdre")));

        return returned;
    }

    /**
     * Gestion de la route permettant d'afficher le formulaire à remplir
     *
     * @param request Requête contenant l'identité de l'utilisateur
     * @return Le ModelAndView du formulaire
     */
    @RequestMapping(value = "formulaire.do", method = RequestMethod.POST)
    public ModelAndView handleFormGet(HttpServletRequest request) {
        ModelAndView returned = null;
        Connexion connexion = connectionService.checkAccess(request, "Eleve");
        Connexion connection = connectionService.checkMissionStatus(connexion, 1); // 1 corresponds to "Mission en cours"
        if (connection == null) {
            Optional<ConfigModif> configInformationPopUpOpt = configModifRepository.findTopByTypeNomOrderByModifIdDesc("message_mission_fermee");
            ConfigModif configInformationPopUp = configInformationPopUpOpt.get();
            String texteInformationPopUp = configInformationPopUp.getContenu();
            texteInformationPopUp = texteInformationPopUp.replaceAll("\n", "<br/>");

            returned = new ModelAndView("informationEleves");
            returned.addObject("texteInfo", texteInformationPopUp);
            returned.addObject("connexionId", connexion.getConnexionId());
            return returned;
        } else {
            int id = connection.getPersonneId().getPersonneId();
            Personne user = personneRepository.getReferenceById(id);
            Collection<Formulaire> form = formulaireRepository.findByPersonneId(user);
            if (form.size() == 1) {
                Formulaire formulaire = form.iterator().next();
                returned = displayFormulaire(connexion, formulaire, false);
            }
        }
        if (returned == null) {
            returned = connectionService.prepareModelAndView(connexion, "accueilEtudiant");
        }
        return returned;
    }

    private File createBourseFile(Formulaire formulaire, HttpServletRequest request) {
        File bourseFile = null;
        if (formulaire.getEstBoursier()) {
            // Save bourse file (remove old ones if necessary)
            File file = Util.getFileFromRequest(request, "preuveBourse");
            if ((file != null) && (file.exists()) && (file.isFile())) {
                String currentFileName = file.getName();
                String ext = "xxx";
                int index = currentFileName.lastIndexOf(".");
                if (index > 0) {
                    ext = currentFileName.substring(index + 1);
                }

                // Remove possible old files from bourse folder
                String[] extList = {"png", "pdf"};
                for (String ext1 : extList) {
                    File cible = new File(Util.buildBourseFilePath(formulaire.getNumeroScei(), ext1));
                    if (cible.exists()) {
                        cible.delete();
                    }
                }

                // Set new file (move downloaded file to bourse folder
                String newFilePath = Util.buildBourseFilePath(formulaire.getNumeroScei(), ext);
                try {
                    Files.move(Paths.get(file.getAbsolutePath()), Paths.get(newFilePath), REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(FormulaireController.class.getName()).log(Level.SEVERE, null, ex);
                }
                bourseFile = file;
            } else {
                // Check file exists
                bourseFile = this.findBourseFile(formulaire);
            }
        }
        return bourseFile;
    }

    /**
     * Gestion de la sauvegarde et de la validation d'un formulaire par un
     * candidat
     *
     * @param request La requête http
     * @return La page de formulaire si enregistrementFormulaire, la page
     * d'accueil si validation
     */
    @RequestMapping(value = "SauvegardeFormulaire.do", method = RequestMethod.POST)
    public ModelAndView handleEnregistrerForm(HttpServletRequest request) {
        ModelAndView returned;
        Connexion connection = connectionService.checkAccess(request, "Eleve");
        connection = connectionService.checkMissionStatus(connection, 1);
        if (connection != null) {
            int instruction = 0;
            if (Util.getStringFromRequest(request, "enregistrer") != null) {
                instruction = 1;
            } else if (Util.getStringFromRequest(request, "soumettre") != null) {
                instruction = 2;
            }

            if (instruction > 0) {
                Collection<Formulaire> formulaireObjet = formulaireRepository.findByPersonneId(connection.getPersonneId());
                Formulaire formulaire = null;
                if (!formulaireObjet.isEmpty()) {
                    formulaire = formulaireObjet.iterator().next();
                }

                if (formulaire != null) {
                    int formulaireId = formulaire.getFormulaireId();

                    Util.enregistrementFormulaire(request, formulaireId, null, formulaireRepository);
                    formulaire = formulaireRepository.getReferenceById(formulaireId);

                    // Gestion de la bourse
                    File bourseFile = this.createBourseFile(formulaire, request);

                    // Erreur choix logement
                    boolean erreur = false;
                    String souhaitStr = Util.getStringFromRequest(request, "Souhait");
                    if ((souhaitStr == null) || (souhaitStr.isEmpty())) {
                        erreur = true;
                    } else {
                        int souhait = Util.getIntFromString(souhaitStr);
                        if (souhait <= 0) {
                            erreur = true;
                        }
                    }

                    // soumission si demandée
                    if ((instruction == 1) || (erreur)) {
                        formulaireRepository.soumettre(formulaireId, false);
                    } else {
                        formulaireRepository.soumettre(formulaireId, true);
                    }

                    // Should it be checked in alert ?
                    boolean isInAlert = false;
                    if ((formulaire.getEstPmr())
                            || ((formulaire.getEstBoursier()) && (bourseFile != null))
                            || (!formulaire.getGenreId().equals(formulaire.getGenreAttendu()))) {
                        isInAlert = true;
                    }

                    if ((instruction == 2) && (isInAlert) && (!erreur)) {
                        // When ask for submission and submission is ok, we create a new line in Alerte
                        alerteRepository.create(formulaire);
                    }

                    // Retourner au formulaire
                    formulaire = formulaireRepository.getReferenceById(formulaireId);
                    returned = displayFormulaire(connection, formulaire, erreur);
                    String msg;
                    if (instruction == 1) {
                        if (erreur) {
                            msg = "Le formulaire a bien été enregistré, mais vous n'avez pas indiqué le type de logement !";
                        } else {
                            msg = "Le formulaire a bien été enregistré !";
                        }
                    } else {
                        if (erreur) {
                            msg = "Le formulaire a bien été enregistré, mais vous n'avez pas indiqué le type de logement !\nIl n'a pas été soumis !";
                        } else {
                            msg = "Le formulaire a bien été soumis vous ne pouvez plus le modifier !";
                        }
                    }

                    returned.addObject("confirmationMessage", msg);
                    return returned;
                }
            }

            returned = connectionService.prepareModelAndView(connection, "accueilEtudiant");
            return returned;
        }
        return new ModelAndView("redirect");
    }

    /*
public ModelAndView Sauvegardeformulaire(HttpServletRequest request) {
        ModelAndView returned;
        Connexion connection = connectionService.checkAccess(request, "Eleve");
        connection = connectionService.checkMissionStatus(connection, 1);
        if (connection == null) {
            return null;
        }

        int instruction = 0;
        if (Util.getStringFromRequest(request, "enregistrer") != null) {
            instruction = 1;
        } else if (Util.getStringFromRequest(request, "soumettre") != null) {
            instruction = 2;
        }

        if (instruction > 0) {
            Collection<Formulaire> formulaireObjet = formulaireRepository.findByPersonneId(connection.getPersonneId());
            Formulaire formulaire = null;
            if (!formulaireObjet.isEmpty()) {
                formulaire = formulaireObjet.iterator().next();
            }

            if (formulaire != null) {
                String msg = "";

                String mail = Util.getStringFromRequest(request, "mail");
                String genre = Util.getStringFromRequest(request, "Genre");
                String telephone = Util.getStringFromRequest(request, "tel");
                String bourse = Util.getStringFromRequest(request, "bourse");
                String souhaitAppartement = Util.getStringFromRequest(request, "Souhait");
                String infoSupplementaires = Util.getStringFromRequest(request, "infoSupplementaires");
                infoSupplementaires = StringEscapeUtils.escapeHtml4(infoSupplementaires);
                String pmr = Util.getStringFromRequest(request, "pmr");

                //mise en place du genre
                int genreId = Integer.parseInt(genre);
                Optional<Genre> genreobj = genreRepository.findById(genreId);
                Genre genreobjet = genreobj.orElse(null);
                boolean boursebool = Boolean.parseBoolean(bourse);
                boolean pmrBool = Boolean.parseBoolean(pmr);

                formulaire.setMail(mail);
                formulaire.setEstBoursier(boursebool);
                formulaire.setEstPmr(pmrBool);
                formulaire.setGenreId(genreobjet);
                formulaire.setNumeroTel(telephone);

                File bourseFile = null;
                if (boursebool) {
                    // Save bourse file (remove old one)
                    bourseFile = this.findBourseFile(formulaire);
                    
                    String[] extList = {"png", "pdf"};
                    String pathwayFichier = Util.getInfo(Util.UTILBOURSE);

                    File file = Util.getFileFromRequest(request, "preuveBourse");
                    if ((file != null) && (file.exists()) && (file.isFile())) {
                        String currentFileName = file.getName();
                        String ext = "xxx";
                        int index = currentFileName.lastIndexOf(".");
                        if (index > 0) {
                            ext = currentFileName.substring(index + 1);
                        }

                        // Remove possible old files from bourse folder
                        for (String ext1 : extList) {
                            File cible = new File(Util.buildBourseFilePath(formulaire.getNumeroScei(), ext1));
                            if (cible.exists()) {
                                cible.delete();
                            }
                        }

                        // Set new file (move downloaded file to bourse folder
                        String newFilePath = Util.buildBourseFilePath(formulaire.getNumeroScei(), ext);
                        try {
                            Files.move(Paths.get(file.getAbsolutePath()), Paths.get(newFilePath), REPLACE_EXISTING);
                        } catch (IOException ex) {
                            Logger.getLogger(FormulaireController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        bourseFile = file;
                    } else {
                        // Check file exists
                        bourseFile = this.findBourseFile(formulaire);
                    }
                }

                //Mise en place du logement 
                Souhait souhait = new Souhait();
                int logementId = Integer.parseInt(souhaitAppartement);
                souhait.setSouhaitId(logementId);
                formulaire.setSouhaitId(souhait);
                formulaire.setCommentairesEleve(infoSupplementaires);
                if (instruction == 1) {
                    formulaire.setEstValide(java.lang.Boolean.FALSE);
                } else {
                    formulaire.setEstValide(java.lang.Boolean.TRUE);
                    formulaire.setDateValidation(new Date());
                }
                formulaireRepository.saveAndFlush(formulaire);

                // Can it be submitted ?
                boolean isInAlert = true;
                if ((!pmrBool)
                        && ((!boursebool) || (bourseFile == null))
                        && (genreobjet == formulaire.getGenreId())) {
                    isInAlert = false;
                }

                if ((instruction == 2) && (isInAlert)) {
                    // When ask for submission and submission is ok, we create a new line in Alerte
                    alerteRepository.create(formulaire);
                }

                int formulaireId = formulaire.getFormulaireId();
                Collection<Formulaire> form = formulaireRepository.findByFormulaireId(formulaireId);
                if (form.size() == 1) {
                    Formulaire formulaireTemp = form.iterator().next();
                    returned = displayFormulaire(connection, formulaireTemp);
                    if (instruction == 1) {
                        returned.addObject("confirmationMessage", "Le formulaire a bien été enregistré !");
                    } else {
                        returned.addObject("confirmationMessage", "Le formulaire a bien été soumis vous ne pouvez plus le modifier !");
                    }
                    return returned;
                }
            }
        }

        returned = connectionService.prepareModelAndView(connection, "accueilEtudiant");
        return returned;
    }
     */
    private ModelAndView manageFormulaireVe(Connexion connection, Formulaire formulaire) {
        if (formulaire != null) {
            Collection<Pays> paysList = paysRepository.findAllSorted();
            ModelAndView returned = connectionService.prepareModelAndView(connection, "formulaireVe");
            returned.addObject("item", formulaire);

            returned.addObject("pays", paysList);
            returned.addObject("souhaitsList", souhaitRepository.findAll(Sort.by(Sort.Direction.ASC, "souhaitOrdre")));
            returned.addObject("genresList", genreRepository.findAll(Sort.by(Sort.Direction.ASC, "genreOrdre")));
            return returned;
        }
        return connectionService.prepareModelAndView(connection, "index");
    }

    /**
     * Controller permettant d'afficher le formulaire rempli par un eleve
     *
     * @param request
     * @return Le ModelAndView du formulaire
     */
    @RequestMapping(value = "formulaireVe.do", method = RequestMethod.POST)
    public ModelAndView handleFormVeGet(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            connection = connectionService.checkAccess(request, "Assistant");
            if (connection == null) {
                return new ModelAndView("redirect");
            }
        }

        //Récupération du formulaire
        String idStr = Util.getStringFromRequest(request, "formulaireId");
        int id = Util.getIntFromString(idStr);
        Optional<Formulaire> form = formulaireRepository.findById(id);
        Formulaire formulaire = new Formulaire();
        if (!form.isEmpty()) {
            formulaire = form.get();
        }

        returned = manageFormulaireVe(connection, formulaire);
        return returned;
    }

    /**
     * Gestion de la route de validation d'un formulaire par un assistant ou
     * administrateur
     *
     * @param request Requête http
     * @return La page d'acceuil après validation
     */
    @RequestMapping(value = "ValiderFormVe.do", method = RequestMethod.POST)
    public ModelAndView handleValiderForm(HttpServletRequest request) {
        ModelAndView returned = null;
        //Check de la connexion
        Connexion connectionAdmin = connectionService.checkAccess(request, "Admin");
        Connexion connectionAssistant = connectionService.checkAccess(request, "Assistant");
        if ((connectionAdmin != null) || (connectionAssistant != null)) {
            String formulaireIdStr = Util.getStringFromRequest(request, "id");
            int formulaireId = getIntFromString(formulaireIdStr);

            Util.enregistrementFormulaire(request, formulaireId, true, formulaireRepository);
            //TO DO : envoi du mail quand ce sera possible
            String idStr = Util.getStringFromRequest(request, "id");
            int id = Util.getIntFromString(idStr);
            alerteRepository.update(formulaireRepository.getReferenceById(id), "Traitée");
            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAllValidOrCommentaireVE());
            Collections.sort(forms, Formulaire.getComparator());

            //Redirection
            if (connectionAdmin != null) {
                returned = connectionService.prepareModelAndView(connectionAdmin, "pageDossiers");
            } else {
                returned = connectionService.prepareModelAndView(connectionAssistant, "pageDossiersAssist");
            }
            if (returned != null) {
                returned.addObject("forms", forms);
            }
            return returned;
        } else {
            return new ModelAndView("redirect");
        }
    }

    /**
     * Gestion de la route d'enregistrementFormulaire d'un formulaire par un
     * assistant ou administrateur
     *
     * @param request Requête http
     * @return La page d'acceuil après enregistrementFormulaire
     */
    @RequestMapping(value = "EnregistrerFormVe.do", method = RequestMethod.POST)
    public ModelAndView handleEnregistrerFormVe(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        int connexionType = 1;
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            connection = connectionService.checkAccess(request, "Assistant");
            connexionType = 2;
        }

        if (connection != null) {
            String formulaireIdStr = Util.getStringFromRequest(request, "id");
            int formulaireId = getIntFromString(formulaireIdStr);

            Util.enregistrementFormulaire(request, formulaireId, null, formulaireRepository);

            //Redirection
            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAllValidOrCommentaireVE());
            Collections.sort(forms, Formulaire.getComparator());

            returned = connectionService.prepareModelAndView(connection, (connexionType == 1 ? "pageDossiers" : "pageDossiersAssist"));
            if (returned != null) {
                returned.addObject("forms", forms);
            }
            return returned;
        } else {
            return new ModelAndView("redirect");
        }
    }

    /**
     * Gestion de la route de refus d'un formulaire par un assistant ou
     * administrateur
     *
     * @param request Requête http
     * @return La page d'acceuil après refus
     */
    @RequestMapping(value = "RefuserFormVe.do", method = RequestMethod.POST)
    public ModelAndView handleRefuserForm(HttpServletRequest request) {
        ModelAndView returned = null;
        //Check de la connexion
        Connexion connectionAdmin = connectionService.checkAccess(request, "Admin");
        Connexion connectionAssistant = connectionService.checkAccess(request, "Assistant");
        if (connectionAdmin != null || connectionAssistant != null) {
            String formulaireIdStr = Util.getStringFromRequest(request, "id");
            int formulaireId = getIntFromString(formulaireIdStr);

            Util.enregistrementFormulaire(request, formulaireId, false, formulaireRepository);
            //envoi du mail quand ce sera possible
            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAllValidOrCommentaireVE());
            Collections.sort(forms, Formulaire.getComparator());
            //Redirection
            if (connectionAdmin != null) {
                returned = connectionService.prepareModelAndView(connectionAdmin, "pageDossiers");
            } else {
                returned = connectionService.prepareModelAndView(connectionAssistant, "pageDossiersAssist");
            }
            if (returned != null) {
                returned.addObject("forms", forms);
            }
            return returned;
        } else {
            return new ModelAndView("redirect");
        }
    }

    /**
     * Méthode permettant de gérer le téléchargement de la bourse d'un étudiant
     *
     * @param request La requête http
     * @return Le fichier de bourse
     */
    @RequestMapping(value = "telechargerBourse.do", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> handleTelechargement(HttpServletRequest request) {
        String scei = Util.getStringFromRequest(request, "numSCEI");
        String idStr = Util.getStringFromRequest(request, "id");
        int id = Util.getIntFromString(idStr);
        Formulaire formulaire = formulaireRepository.getReferenceById(id);

        String nom = formulaire.getPersonneId().getNom();
        String prenom = formulaire.getPersonneId().getPrenom();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        String filename = "bourse_" + year + "_" + nom.replace(' ', '_') + "_" + prenom.replace(' ', '_') + "_SCEI_" + scei;

        File theFile = formulaire.getBourseFile();
        MediaType mime = null;
        if ((theFile != null) && (theFile.exists())) {
            if (theFile.getName().endsWith(".png")) {
                mime = MediaType.IMAGE_PNG;
            } else if (theFile.getName().endsWith(".pdf")) {
                mime = MediaType.APPLICATION_PDF;
            }
            return Util.sendFile(filename, theFile, mime);
        } else {
            theFile = null;
        }
        return ResponseEntity.unprocessableEntity().body((InputStreamResource) null);
    }

    @RequestMapping(value = "AnnulerPwd.do", method = RequestMethod.POST)
    public ModelAndView handleAnnulerPwd(HttpServletRequest request) {
        ModelAndView returned = null;
        //Check de la connexion
        Connexion connectionAdmin = connectionService.checkAccess(request, "Admin");
        Connexion connectionAssistant = connectionService.checkAccess(request, "Assistant");
        if (connectionAdmin != null || connectionAssistant != null) {
            String formulaireIdStr = Util.getStringFromRequest(request, "id");
            int formulaireId = getIntFromString(formulaireIdStr);

            Util.enregistrementFormulaire(request, formulaireId, null, formulaireRepository);
            Formulaire formulaire = formulaireRepository.getReferenceById(formulaireId);

            Personne personne = formulaire.getPersonneId();
            personneRepository.resetPassword(personne);

            formulaire = formulaireRepository.getReferenceById(formulaireId);
            //envoi du mail quand ce sera possible
            /*
            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAllValidOrCommentaireVE());
            Collections.sort(forms, Formulaire.getComparator());
            
            //Redirection
            if (connectionAdmin != null) {
                returned = connectionService.prepareModelAndView(connectionAdmin, "pageDossiers");
            } else {
                returned = connectionService.prepareModelAndView(connectionAssistant, "pageDossiersAssist");
            }
            if (returned != null) {
                returned.addObject("forms", forms);
            }
            */
            
            if (connectionAdmin != null) {
                returned = manageFormulaireVe(connectionAdmin, formulaire);
            } else {
                returned = manageFormulaireVe(connectionAssistant, formulaire);
            }
            return returned;
        } else {
            return new ModelAndView("redirect");
        }
    }
    

    @RequestMapping(value = "AnnulerPwd2.do", method = RequestMethod.POST)
    public ModelAndView handleAnnulerPwd2(HttpServletRequest request) {
        ModelAndView returned = null;
        //Check de la connexion
        Connexion connectionAdmin = connectionService.checkAccess(request, "Admin");
        Connexion connectionAssistant = connectionService.checkAccess(request, "Assistant");
        if (connectionAdmin != null || connectionAssistant != null) {
            String formulaireIdStr = Util.getStringFromRequest(request, "id");
            int formulaireId = getIntFromString(formulaireIdStr);
            Formulaire formulaire = formulaireRepository.getReferenceById(formulaireId);

            Personne personne = formulaire.getPersonneId();
            personneRepository.resetPassword(personne);

            formulaire = formulaireRepository.getReferenceById(formulaireId);
            if (connectionAdmin != null) {
                returned = manageFormulaireVe(connectionAdmin, formulaire);
            } else {
                returned = manageFormulaireVe(connectionAssistant, formulaire);
            }
            return returned;
        } else {
            return new ModelAndView("redirect");
        }
    }
}
