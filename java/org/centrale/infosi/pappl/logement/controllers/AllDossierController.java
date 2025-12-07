/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.repositories.AlerteRepository;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.centrale.infosi.pappl.logement.repositories.PaysRepository;
import org.centrale.infosi.pappl.logement.util.Util;
import static org.centrale.infosi.pappl.logement.util.Util.getIntFromString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller gérant la liste de tous les dossiers (dont ceux non transmis)
 *
 * @author Quent
 *
 */
@Controller
public class AllDossierController {

    @Autowired
    private ConnectionService connectionService;

    @Lazy
    @Autowired
    private FormulaireRepository formulaireRepository;

    @Lazy
    @Autowired
    private PaysRepository paysRepository;

    /**
     * Gestion de la route d'affichage de tous les dossiers
     *
     * @param request La requête http
     * @return La vue de la liste de TOUS les dossiers, même ceux non envoyés
     */
    @RequestMapping(value = "allDossiers.do", method = RequestMethod.POST)
    public ModelAndView handleAllDossier(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            connection = connectionService.checkAccess(request, "Assistant");
        }
        if (connection == null) {
            return null;
        }

        //Ajout de la liste des formulaires
        List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAll());
        Collections.sort(forms, Formulaire.getComparator());

        returned = connectionService.prepareModelAndView(connection, "allDossiers");
        if (returned != null) {
            returned.addObject("forms", forms);
        }
        return returned;
    }

    /**
     * Gestion de la route de retour à la route des dossiers transmis
     *
     * @param request La requête http
     * @return La vue de la liste des dossiers transmis
     */
    @RequestMapping(value = "returnToDossiers.do", method = RequestMethod.POST)
    public ModelAndView handleReturn(HttpServletRequest request) {
        ModelAndView returned;
        returned = null;

        //Check de la connexion
        Connexion connectionAssist = connectionService.checkAccess(request, "Assistant");
        Connexion connectionAdmin = connectionService.checkAccess(request, "Admin");
        if (connectionAssist != null || connectionAdmin != null) {
            //On renvoie à la page Dossier Assistant
            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAllValidOrCommentaireVE());
            Collections.sort(forms, Formulaire.getComparator());

            if (connectionAdmin != null) {
                returned = connectionService.prepareModelAndView(connectionAdmin, "pageDossiers");
            } else {
                returned = connectionService.prepareModelAndView(connectionAssist, "pageDossiersAssist");
            }

            if (returned != null) {
                returned.addObject("forms", forms);
            }
        }
        return returned;

    }

    /**
     * Gestion de la route permettant d'accéder à la page d'un dossier non
     * transmis
     *
     * @param request La requête http
     * @return La page du dossier non transmis (sans ses informations)
     */
    @RequestMapping(value = "dossierNnTransmit.do", method = RequestMethod.POST)
    public ModelAndView handleDossierNnTransmit(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            connection = connectionService.checkAccess(request, "Assistant");
        }
        if (connection == null) {
            return new ModelAndView("redirect");
        }

        //Page du formulaire
        int formId = Util.getIntFromString(request.getParameter("formulaireId"));
        Optional<Formulaire> form = formulaireRepository.findById(formId);

        returned = connectionService.prepareModelAndView(connection, "dossierNnTransmit");

        if (!form.isEmpty() && returned != null) {
            Formulaire formulaire = form.get();
            returned.addObject("form", formulaire);
            returned.addObject("pays", paysRepository.findAll());

            Date dateD = formulaire.getDateDeNaissance();
            returned.addObject("date", dateD);
        }

        return returned;
    }

    /**
     * Enregistre puis renvoie sur la page All Dossiers
     *
     * @param request Requête http
     * @return La page de la liste des dossiers
     */
    @RequestMapping(value = "EnregistrerDossierNnTransmit.do", method = RequestMethod.POST)
    public ModelAndView handleEnregistrerFormNnTransmit(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            connection = connectionService.checkAccess(request, "Assistant");
        }

        if (connection != null) {
            String formulaireIdStr = Util.getStringFromRequest(request, "id");
            int formulaireId = getIntFromString(formulaireIdStr);

            Util.enregistrementFormulaire(request, formulaireId, null, formulaireRepository);

            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAll());
            Collections.sort(forms, Formulaire.getComparator());

            returned = connectionService.prepareModelAndView(connection, "allDossiers");
            if (returned != null) {
                returned.addObject("forms", forms);
            }
            return returned;
        }
        return new ModelAndView("redirect");
    }

    /**
     * Sauvegarde les changement Vide les paramètres vides, Renvoi la page All
     * Dossier
     *
     * @param request La requête http
     * @return La pgae des dossiers après le vidage et la transmission
     */
    @RequestMapping(value = "ViderEtTransmettre.do", method = RequestMethod.POST)
    public ModelAndView handleViderEtTransmettre(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connectionAdmin = connectionService.checkAccess(request, "Admin");
        Connexion connectionAssistant = connectionService.checkAccess(request, "Assistant");
        if (connectionAdmin != null || connectionAssistant != null) {
            String formulaireIdStr = Util.getStringFromRequest(request, "id");
            int formulaireId = getIntFromString(formulaireIdStr);

            Util.enregistrementFormulaire(request, formulaireId, null, formulaireRepository); //On sauvegarde
            formulaireRepository.viderEtValider(formulaireId);

            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAll());
            Collections.sort(forms, Formulaire.getComparator());

            if (connectionAdmin != null) {
                returned = connectionService.prepareModelAndView(connectionAdmin, "allDossiers");
            } else {
                returned = connectionService.prepareModelAndView(connectionAssistant, "allDossiers");
            }

            if (returned != null) {
                returned.addObject("forms", forms);
            }
            return returned;
        }
        return new ModelAndView("redirect");
    }
}
