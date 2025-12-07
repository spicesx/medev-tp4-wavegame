/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import org.centrale.infosi.pappl.logement.util.PasswordUtils;
import java.util.Iterator;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.centrale.infosi.pappl.logement.repositories.PersonneRepository;
import org.centrale.infosi.pappl.logement.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *Co ntroller permettant de réinitialiser son login et mot de passe
 * En stand-by car pas de possibilité d'envoyer de mails
 * @author samer

 */
@Controller
public class PasswordResetController {
    
    @Autowired
    @Lazy
    private FormulaireRepository formulaireRepository;

    @Autowired
    @Lazy
    private PersonneRepository personRepository;
    
    /**
     * Permet d'afficher la page de réinitialisation d'un mot de passe
     * @return Le nom de la page de réinitialisation
     */
    @GetMapping("passwordreset.do")
    public String showPasswordResetPage() {
        return "passwordreset"; // This should match the JSP or HTML file name
    }

    
    /**
     * Gestion de la route permettant d'afficher la page de réinitialisation de soumission d'une adresse mail
     * @param request La requête http
     * @return La page de connexion
     */
    

    @RequestMapping(value="submitpasswordreset.do", method=RequestMethod.POST)
    public ModelAndView handlePasswordReset(HttpServletRequest request) {
        ModelAndView returned = new ModelAndView("index"); 
        String email = Util.getStringFromRequest(request, "email");   
        Iterator<Formulaire> formulaires = formulaireRepository.findByMail(email).iterator();

        if (!formulaires.hasNext()) {
            returned.addObject("errorMessage", "Aucun compte associé à cet e-mail.");
            return returned;
        }
        returned=new ModelAndView("premier_connexion");
        returned.addObject("mail",email);
        //La suite concerne l'envoi de mail et la génération de token, à faire avec un serveur SMTP
        //Formulaire formulaire = formulaires.next();
        
        // Generate a reset token (example: random UUID)
        //String resetToken = PasswordUtils.generateToken();

        // Store the reset token in the database 
        /*Personne personne = formulaire.getPersonneId();
        if (personne.getLogin() != null){
            personne.setFirstConnectionToken(resetToken);
            personRepository.save(personne);
            
             // Send password reset email (you need an email service)


            // Inform the user that a reset link has been sent
        }
        */    
        return returned;
    }
    
    
    /**
     * Gestion de la réinitialisation du mot de passe
     * @param request La requête http
     * @return La vue de changement du mot de passe
     */
    @RequestMapping(value="passwordresetlink.do", method=RequestMethod.GET)
    public ModelAndView handleSetNewPassword(HttpServletRequest request){
        ModelAndView returned = null;
        String token = Util.getStringFromRequest(request, "token");
        //vérification du token
        if(verifyToken(token)){
            returned=new ModelAndView("setnewpassword");
            returned.addObject("token",token);
        }
        return returned;   
    }
    
    /**
     * Gestion de la route permettant d'afficher la page de modifiation son mot de passe 
     * @param request La requête http
     * @return La page de changement de mot de passe
     */
    
    @RequestMapping(value="submitnewpassword.do", method=RequestMethod.POST)
    public ModelAndView handleSubmitNewPassword(HttpServletRequest request) {
        String token = Util.getStringFromRequest(request, "token");
        String newPassword = Util.getStringFromRequest(request, "password");

        Optional<Personne> result = personRepository.findByFirstConnectionToken(token);

        if (result.isPresent()) {
            Personne personne = result.get();

            // Ensure the login is not null
            if (personne.getLogin() != null) {
                personne.setPassword(PasswordUtils.hashPassword(newPassword));
                personRepository.save(personne);
                deleteToken(personne);
                return new ModelAndView("index.do"); 
            }
        }

        // If token is invalid or login is null, show an error message
        ModelAndView returned = new ModelAndView("setnewpassword");
        returned.addObject("errorMessage", "Lien invalide ou expiré.");
        return returned;
    }

    private boolean verifyToken(String token) {
        Optional<Personne> result = personRepository.findByFirstConnectionToken(token);
    
        return result.isPresent() && result.get().getLogin() != null;
    }
    
    private Personne changeLoginAndPassword (Personne personne, String login,String password){
        Personne result=personRepository.update(personne,login,PasswordUtils.hashPassword(password));
        return result;
    }
    
    private void deleteToken(Personne personne){
       personRepository.setFirstConnectionTokenToNull(personne.getPersonneId());
    }

}

