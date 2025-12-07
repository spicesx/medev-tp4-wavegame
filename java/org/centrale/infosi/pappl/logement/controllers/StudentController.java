/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.repositories.ConfigModifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller permettant de gérer la vue de l'application d'un étudiant (menu,
 * affichage du formulaire)
 *
 * @author clesp
 *
 */
@Controller
public class StudentController {

    @Autowired
    @Lazy
    private ConfigModifRepository configModifRepository;

    @Autowired
    private ConnectionService connectionService;

    /**
     * Gestion de la route permettant d'accéder à la page d'accueil étudiant
     *
     * @return Le ModelAndView lié à la page d'accueil
     */
    @RequestMapping(value = "studentDashboard.do", method = RequestMethod.POST)
    public ModelAndView handleStudentGet(HttpServletRequest request) {
        ModelAndView returned;
        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Eleve");

        if (connection == null) {
            Optional<ConfigModif> configInformationPopUpOpt = configModifRepository.findTopByTypeNomOrderByModifIdDesc("message_pge_connexion");
            ConfigModif configInformationPopUp = configInformationPopUpOpt.get();
            String texteInformationPopUp = configInformationPopUp.getContenu();
            
            texteInformationPopUp = texteInformationPopUp.replaceAll("\n", "<br/>");
            returned = new ModelAndView("index");
            returned.addObject("textePopUp", texteInformationPopUp);
        } else {
            returned = connectionService.prepareModelAndView(connection, "accueilEtudiant");
        }
        return returned;
    }
}
