/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import java.util.Collection;
import org.centrale.infosi.pappl.logement.items.Alerte;
import org.centrale.infosi.pappl.logement.repositories.AlerteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.repositories.ConfigModifRepository;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.centrale.infosi.pappl.logement.repositories.PersonneRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Cette classe permet l'envoie automatique du premier mail pour la connexion;
 * Controller mis en stand by car pas de serveur SMTP
 *
 * @author barbo
 *
 */
@Controller
public class MailController {

    @Lazy
    @Autowired
    private AlerteRepository alerteRepository;

    @Lazy
    @Autowired
    private FormulaireRepository formulaireRepository;
    
    @Lazy
    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private ConnectionService connectionService;

    @Lazy
    @Autowired
    private ConfigModifRepository configmodifrepository;

    private static final int MSGPREMIERCONTACT = 7;
    private static final int MSGPFIN = 9;
    private static final int MAILCONTACT = 8;
    
    //@Value("${spring.mail.username}")
    //private String usernameSMTP;

    //@Value("${spring.mail.password}")
    //private String passwordSMTP;

    //@Value("${spring.mail.host}")
    //private String hostSMTP;

    //@Value("${spring.mail.port}")
    //private int portSMTP;


    private ModelAndView envoyerMessage(HttpServletRequest request, int messageType) {
        ModelAndView returned;
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection != null) {
            // Il faut créer une liste avec l'adress mail de tous les utilisateurs.
            Optional<ConfigModif> contenuMsg = configmodifrepository.getLastTypeId(messageType);
            Optional<ConfigModif> envoyeurMsg = configmodifrepository.getLastTypeId(MAILCONTACT);
            if ((contenuMsg.isPresent()) && (envoyeurMsg.isPresent())) {
                ConfigModif messageToSend = contenuMsg.get();
                ConfigModif envoyeur = envoyeurMsg.get();

                String subject = "Mission logement connexion de compte";
                String body = messageToSend.contenu;
                
                List<String> tous_les_tokens = personneRepository.findAllTokenVague();
                Collection<String> tous_les_mails = formulaireRepository.findAllEmailsVague();
                int compteur = 0;
                for (String email : tous_les_mails) {
                    sendEmail(tous_les_tokens.get(compteur),email, subject, body);
                    compteur++;
                }
                        
                formulaireRepository.updateVague();
                
                //Renvoie sur la page accueil_admin avec un message pop_up
                List<Alerte> alertes = new ArrayList<Alerte>(alerteRepository.findAll());
                Collections.sort(alertes, Alerte.getComparator());

                returned = connectionService.prepareModelAndView(connection, "accueil_admin");
                
                returned.addObject("Alertes", alertes);
                if (tous_les_mails.size() ==0){
                    returned.addObject("confirmationMessage", "Aucun mail à envoyer");
                } else{
                    returned.addObject("confirmationMessage", "Emails envoyés avec succès !");
                }
                return returned;
            }
        }
        return new ModelAndView("redirect");
    }
    
    /**
     * Gestion de la route permettant d'envoyer les mails
     *
     * @param request La requête http
     * @return La page d'accueil admin avec un pop up
     */
    @RequestMapping(value = "envoiemail.do", method = RequestMethod.POST)
    public ModelAndView Envoi(HttpServletRequest request) {
        return envoyerMessage(request, MSGPREMIERCONTACT);
    }

    @RequestMapping(value = "envoiemailfin.do", method = RequestMethod.POST)
    public ModelAndView EnvoiFin(HttpServletRequest request) {
        return envoyerMessage(request, MSGPFIN);
    }

    /**
     * Méthode permettant d'envoyer des mails avec JavaMail
     *
     * @param token token de l'élève destinataire
     * @param recipient Adresse mail du destinataire
     * @param subject L'objet du mail
     * @param body Le contenu
     */
    public void sendEmail(String token,String recipient, String subject, String body) {
        
        // Récupération des identifiants
            //ConfigModif Configmodif = (ConfigModif) configmodifrepository.findByModifId(MAILCONTACT);
            //String username = Configmodif.contenu; // Adresse email
            //String password = "logement"; // pour se connecter à la base de données. 
            
        // A ne pas mettre dans le code en dur normalement, uniquement ici pour le test
        final String mailExpediteur = "Victor.Duforest@eleves.ec-nantes.fr";
        final String usernameSMTP = "Victor.Duforest@eleves.ec-nantes.fr";
        final String passwordSMTP = "dutzos-3Sujfu-cugves"; 
        
        final String host = "smtps.nomade.ec-nantes.fr";
        String port = "587";

        // Configuration SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        
        // A quoi servent ces deux lignes là ? 
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.fallback", "false");

        properties.put("mail.smtp.user", usernameSMTP);
        properties.put("mail.smtp.password", passwordSMTP);
        

        // Création de la session avec authentification
        
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usernameSMTP,passwordSMTP);
			}
		};

        Session session = Session.getInstance(properties, authenticator);
        
        // Active le debug pour voir la communication SMTP
        session.setDebug(true);
        try {

            // Création du message email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usernameSMTP));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            
            // Emp$êche de répondre au mail
            message.setReplyTo(InternetAddress.parse("no-reply@invalid.local"));
            
            // Objet du mail
            message.setSubject(subject);
            
            // Texte du mail
            message.setText(body);
            message.setText("Voilà votre token : "+"http://localhost:8080/MissionLogement/creationcompte.do?token="+token);

            // Envoi du message
            Transport.send(message);

            //System.out.println("Email envoyé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de l'email.");
        }
    }
}
