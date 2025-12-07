/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import java.util.Collection;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller gérant la vue des assistants de l'application
 *
 * @author clesp
 *
 */
@Controller
public class AssistantController {

    @Autowired
    @Lazy
    private FormulaireRepository formulaireRepository;

    @Autowired
    private ConnectionService connectionService;

    /**
     * Gestion de la rotue affichant la page d'acceuil d'un assistant (la liste
     * des formulaires)
     *
     * @return Le ModelAndView lié à la page d'acceuil d'un assistant
     */
    @RequestMapping(value = "dossiersAssist.do", method = RequestMethod.POST)
    public ModelAndView handleDossiersAssist(HttpServletRequest request) {
        ModelAndView returned;
        Connexion connection = connectionService.checkAccess(request, "Assistant");
        if (connection != null) {
            //Ajout de la liste des formulaires
            List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAllValidOrCommentaireVE());
            Collections.sort(forms, Formulaire.getComparator());

            returned = connectionService.prepareModelAndView(connection, "pageDossiersAssist");
            if (returned != null) {
                returned.addObject("forms", forms);
            }
            return returned;
        }
        return new ModelAndView("redirect");
    }
}
