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
import org.centrale.infosi.pappl.logement.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller pour la gestion de la page d'information des étudiants
 * @author Amolz
 */
@Controller
public class InformationsElevesController {
    @Lazy
    @Autowired
    private ConfigModifRepository configModifRepository;
    
    @Autowired
    private ConnectionService connectionService;
     /**
     * Gestion de la route permettant d'afficher les informations de la mission logement
     * @param request La requête
     * @return La page d'information
     */
    @RequestMapping(value="informations.do",method=RequestMethod.POST)
    public ModelAndView handleInformation(HttpServletRequest request){
        Connexion connection = connectionService.checkAccess(request, "Eleve");
        if (connection == null){
            return new ModelAndView("redirect");
        }
        Optional<ConfigModif> configInformationTexteOpt = configModifRepository.findTopByTypeNomOrderByModifIdDesc("message_page_informations");
        ConfigModif configInformationTexte = configInformationTexteOpt.get();
        String texteInformation = configInformationTexte.getContenu();
        texteInformation = texteInformation.replaceAll("\n", "<br/>");
        
        String connectionIdStr=Util.getStringFromRequest(request, "connexionId");
        ModelAndView returned = connectionService.prepareModelAndView(connection,"informationEleves");
        returned.addObject("texteInfo", texteInformation);
        return returned;
        
    }
}
