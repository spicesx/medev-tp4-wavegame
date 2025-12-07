/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.repositories.ConfigModifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *Controller permettant de gérer la page d'attente de l'application 
 * @author Amolz
 * 
 */
public class AttenteController {
    @Autowired
    @Lazy
    private ConfigModifRepository configModifRepository;
    
    /**
     * Permet d'afficher le bon message selon si la misssion n'a pas encore commencée ou si elle s'est terminée
     * @return La page d'attente
     */
    @RequestMapping(value="attente.do",method=RequestMethod.POST)
    public ModelAndView handleAttente(){
        Optional<ConfigModif> dateDebutObjOpt = configModifRepository.findTopByTypeNomOrderByModifIdDesc("date_debut");
        ConfigModif dateDebutObj = dateDebutObjOpt.get();
        String dateDebutStr = dateDebutObj.getContenu();
        LocalDateTime dateDebut = LocalDateTime.parse(dateDebutStr);
        
        Optional<ConfigModif> dateFinObjOpt = configModifRepository.findTopByTypeNomOrderByModifIdDesc("date_debut");
        ConfigModif dateFinObj = dateFinObjOpt.get();
        String dateFinStr = dateFinObj.getContenu();
        LocalDateTime dateFin = LocalDateTime.parse(dateFinStr);
        
        LocalDateTime dateHeureNantes = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        
        ModelAndView returned = new ModelAndView("pageAttente");
        
        //Vérification de dates
        if (dateHeureNantes.isBefore(dateDebut)){
            Optional<ConfigModif> messageAttenteOpt = configModifRepository.findTopByTypeNomOrderByModifIdDesc("message_page_attente");
            ConfigModif messageAttente = messageAttenteOpt.get();
            String messageAttenteTexte = messageAttente.getContenu();
            
            returned.addObject("message",messageAttenteTexte);
        } 
        else if (dateHeureNantes.isAfter(dateFin)){
            Optional<ConfigModif> messageAttenteOpt = configModifRepository.findTopByTypeNomOrderByModifIdDesc("message_mission_fermee");
            ConfigModif messageAttente = messageAttenteOpt.get();
            String messageAttenteTexte = messageAttente.getContenu();
            
            returned.addObject("message",messageAttenteTexte);
        }
        
        return returned;     
    }
}
