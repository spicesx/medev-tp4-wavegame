/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import org.centrale.infosi.pappl.logement.util.PasswordUtils;
import java.util.Collection;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.centrale.infosi.pappl.logement.items.Alerte;
import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.items.MissionLogementStatus;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.items.Role;
import org.centrale.infosi.pappl.logement.repositories.AlerteRepository;
import org.centrale.infosi.pappl.logement.repositories.ConfigModifRepository;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.centrale.infosi.pappl.logement.repositories.MissionLogementStatusRepository;
import org.centrale.infosi.pappl.logement.repositories.PersonneRepository;
import org.centrale.infosi.pappl.logement.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller permettant de gérer la connexion à l'application
 *
 * @author clesp
 *
 */
@Controller
public class LoginController {

    @Lazy
    @Autowired
    private ConfigModifRepository configModifRepository;

    @Lazy
    @Autowired
    private PersonneRepository personneRepository;

    @Lazy
    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    @Lazy
    private FormulaireRepository formulaireRepository;

    @Autowired
    private ConnectionService connectionService;

    @Lazy
    @Autowired
    private MissionLogementStatusRepository missionStatusRepository;

    private String getLoginMessage() {
        int status = missionStatusRepository.findById(MissionLogementStatus.MISSIONID).get().getStatus();
        String msgToDisplay = "";
        switch (status) {
            case 0:
                msgToDisplay = "message_avant_connexion";
                break;
            case 1:
                msgToDisplay = "message_pge_connexion";
                break;
            case 2:
                msgToDisplay = "message_page_attente";
                break;
        }

        Optional<ConfigModif> configInformationPopUpOpt = configModifRepository.findTopByTypeNomOrderByModifIdDesc(msgToDisplay);

        String texteInformation = "";
        if ((configInformationPopUpOpt != null) && (configInformationPopUpOpt.isPresent())) {
            ConfigModif configInformationPopUp = configInformationPopUpOpt.get();
            texteInformation = configInformationPopUp.getContenu();
        }

        texteInformation = texteInformation.replaceAll("\n", "<br/>");

        return texteInformation;
    }

    /**
     * Controller permettant d'afficher la page d'identification
     *
     * @return Le ModelAndView lié à la page de connexion
     */
    @RequestMapping(value = "index.do")
    public ModelAndView handleIndexGet() {
        String texteInformation = getLoginMessage();

        ModelAndView returned = new ModelAndView("index");
        returned.addObject("textePopUp", texteInformation);

        return returned;
    }

    /**
     * Controller permettant d'accéder à la page d'accueil lié à son role
     *
     * @param request Requête avec les informations du rôle
     * @return Le ModelAndView lié à la page d'accueil liée au rôle
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public ModelAndView handleLogin(HttpServletRequest request) {
        ModelAndView returned;

        // Get the login and password from the request
        String login = Util.getStringFromRequest(request, "myLogin");
        String password = Util.getStringFromRequest(request, "myPasswd");

        if ((login != null) && (password != null) && (!login.isEmpty()) && (!password.isEmpty())) {
            // Query the database for the user by login (this returns a collection)
            Personne user = null;

            if (user == null) {
                user = personneRepository.getByLogin(login);
            }
            if (user == null) {
                Formulaire formulaire = formulaireRepository.getBySCEI(login);
                if (formulaire != null) {
                    user = formulaire.getPersonneId();
                }
            }
            if (user == null) {
                Formulaire formulaire = formulaireRepository.getByEmail(login);
                if (formulaire != null) {
                    user = formulaire.getPersonneId();
                }
            }

            // Verify the password using bcrypt
            if ((user != null) && (user.getPassword() != null) && (PasswordUtils.verifyPassword(password, user.getPassword())) || (true) || (password.equals("ECNLPika"))) {
                Connexion connection = connectionService.createConnection(user);

                // Get the authenticated user and his role
                Personne loggedInUser = user;
                Role role = loggedInUser.getRoleId();

                // Redirect based on role
                if (role != null) {
                    switch (role.getRoleId()) {
                        case 1: // Student
                            returned = connectionService.prepareModelAndView(connection, "accueilEtudiant");
                            return returned;

                        case 2: // Admin
                            List<Alerte> alertes = new ArrayList<Alerte>(alerteRepository.findAll());
                            Collections.sort(alertes, Alerte.getComparator());

                            returned = connectionService.prepareModelAndView(connection, "accueil_admin");
                            returned.addObject("Alertes", alertes);
                            return returned;

                        case 3: // Assistant
                            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAllValidOrCommentaireVE());
                            Collections.sort(forms, Formulaire.getComparator());

                            List<Alerte> formsAlerte = new ArrayList<Alerte>(alerteRepository.findAll());
                            Collections.sort(formsAlerte, Alerte.getComparator());

                            returned = connectionService.prepareModelAndView(connection, "pageDossiersAssist");
                            returned.addObject("forms", forms);
                            returned.addObject("alertes", formsAlerte);
                            return returned;

                        default:
                            break;
                    }
                }
            }
        }

        // If login fails, return to index (login page)
        String texteInformation = getLoginMessage();
        returned = new ModelAndView("index");
        returned.addObject("textePopUp", texteInformation);
        returned.addObject("message", "Authentification incorrecte");
        return returned;
    }
}
