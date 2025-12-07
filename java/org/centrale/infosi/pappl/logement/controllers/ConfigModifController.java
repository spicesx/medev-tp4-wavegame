/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.centrale.infosi.pappl.logement.items.Alerte;
import org.centrale.infosi.pappl.logement.items.ConfigModif;
import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.items.MissionLogementStatus;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.items.Role;
import org.centrale.infosi.pappl.logement.items.TypeModif;
import org.centrale.infosi.pappl.logement.repositories.AlerteRepository;
import org.centrale.infosi.pappl.logement.repositories.ConfigModifRepository;
import org.centrale.infosi.pappl.logement.repositories.ConnexionRepository;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.centrale.infosi.pappl.logement.repositories.MissionLogementStatusRepository;
import org.centrale.infosi.pappl.logement.repositories.PersonneRepository;
import org.centrale.infosi.pappl.logement.repositories.RoleRepository;
import org.centrale.infosi.pappl.logement.repositories.TypeModifRepository;
import org.centrale.infosi.pappl.logement.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller permettant de gérer la configuration de l'application côté admin
 *
 * @author Amolz
 *
 */
@Controller
public class ConfigModifController {

    @Autowired
    @Lazy
    private ConfigModifRepository configRepository;

    @Autowired
    @Lazy
    private TypeModifRepository typeRepository;

    @Lazy
    @Autowired
    private MissionLogementStatusRepository missionStatusRepository;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    @Lazy
    private ConnexionRepository connexionRepository;

    @Autowired
    @Lazy
    private PersonneRepository personneRepository;

    @Autowired
    @Lazy
    private RoleRepository roleRepository;

    @Lazy
    @Autowired
    private AlerteRepository alerteRepository;

    @Lazy
    @Autowired
    private FormulaireRepository formulaireRepository;

    @Lazy
    @Autowired
    private TypeModifRepository typeModifRepository;

    /**
     * Save config elements
     *
     * @param request
     */
    private void saveConfig(HttpServletRequest request) {
        if (Util.hasRequestParameter(request, "newStatus")) {
            String newStatusStr = Util.getStringFromRequest(request, "newStatus");
            int newStatus = Util.getIntFromString(newStatusStr);
            missionStatusRepository.updateStatus(newStatus);
        }

        Collection<TypeModif> listeLignes = typeModifRepository.findAll();
        for (TypeModif typeModif : listeLignes) {
            String item = typeModif.getNom();

            if (Util.hasRequestParameter(request, item)) {
                String value = Util.getStringFromRequest(request, item);

                Optional<ConfigModif> lastItem = configRepository.getLastTypeId(typeModif.getTypeId());
                if (lastItem.isPresent()) {
                    ConfigModif data = lastItem.get();
                    if (!data.getContenu().equals(value)) {
                        configRepository.create(typeModif, value);
                    }
                } else {
                    configRepository.create(typeModif, value);
                }
            }
        }
    }

    private boolean canEmptyData() {
        boolean returned = false;
        int status = missionStatusRepository.findById(MissionLogementStatus.MISSIONID).get().getStatus();
        if (status == MissionLogementStatus.FINISHED) {
            returned = true;
            /*
            Collection<Alerte> toBeProccessed = alerteRepository.findAllToBeProcess();
            if (toBeProccessed.isEmpty()) {
                returned = true;
            }
            */
        }
        return returned;
    }

    /**
     * Create screen
     *
     * @param connection
     * @return
     */
    private ModelAndView buildConfigScreen(Connexion connection) {
        boolean canEmptyData = canEmptyData();
        int status = missionStatusRepository.findById(MissionLogementStatus.MISSIONID).get().getStatus();

        ModelAndView returned = connectionService.prepareModelAndView(connection, "configuration");
        if (returned != null) {
            Collection<TypeModif> listeLignes = typeModifRepository.findAll(Sort.by(Sort.Direction.ASC, "typeId"));
            returned.addObject("typeModif", listeLignes);
            returned.addObject("missionStatus", status);
            returned.addObject("canEmptyData", canEmptyData);

            for (TypeModif typeModif : listeLignes) {
                String value = "";
                Optional<ConfigModif> lastItem = configRepository.getLastTypeId(typeModif.getTypeId());
                if (lastItem.isPresent()) {
                    value = lastItem.get().getContenu();
                }
                returned.addObject(typeModif.getNom(), value);
            }
        }
        return returned;
    }

    /**
     * Méthode permettant de vider la base de données
     *
     * @return Le booléen de confirmation
     */
    private boolean viderDataBase(Connexion connection) {
        // On récupère mes rôles
        Collection<Role> roleEleveColl = roleRepository.findByRoleNom("Eleve");
        if ((roleEleveColl == null) || (roleEleveColl.size() != 1)) {
            return false;
        }
        Role roleEleveId = roleEleveColl.iterator().next();

        Collection<Role> roleAdminColl = roleRepository.findByRoleNom("Admin");
        if ((roleAdminColl == null) || (roleAdminColl.size() != 1)) {
            return false;
        }
        Role roleAdminId = roleAdminColl.iterator().next();

        // On déconnecte
        connexionRepository.deleteAllExceptConnectedId(connection.getPersonneId());
        
        // On supprime les étudiants
        alerteRepository.truncateTable();
        formulaireRepository.deleteAll();
        personneRepository.deleteAllRoleId(roleEleveId);

        // Pour l'instant, on ne retire pas les assistants
        // personneRepository.deleteAllExceptRoleId(roleAdminId);

        return true;
    }

    /**
     * Ouverture de la page html de config et remplissage des champs
     *
     * @return La page de configuration
     */
    @RequestMapping(value = "configuration.do", method = RequestMethod.POST)
    public ModelAndView handleConfig(HttpServletRequest request) {
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection != null) {
            /*
            String dateDeb = getLast("date_debut");
            String dateFin = getLast("date_fin");
            String connexion = getLast("message_pge_connexion");
            String info = getLast("message_page_informations");
            String attente = getLast("message_page_attente");
            String missionFermee = getLast("message_mission_fermee");
            String contact = getLast("message_contact");
            String messageMail = getLast("adresse_mail_envoyeur");

            int status = missionStatusRepository.findById(MissionLogementStatus.MISSIONID).get().getStatus();
            boolean canEmptyData = false;
            if (status == MissionLogementStatus.INPROGRESS) {
                List<Alerte> toBeProccessed = new ArrayList<Alerte>(alerteRepository.findAllToBeProcess());
                Collections.sort(toBeProccessed, Alerte.getComparator());
                if (toBeProccessed.isEmpty()) {
                    canEmptyData = true;
                }
            }
             */

            return buildConfigScreen(connection);
            /*
            connectionService.prepareModelAndView(connection, "configuration");
            if (returned != null) {
                returned.addObject("dateDebut", dateDeb);
                returned.addObject("dateFin", dateFin);

                returned.addObject("missionStatus", status);

                returned.addObject("messageConnexion", connexion);
                returned.addObject("messageInfo", info);
                returned.addObject("messageAttente", attente);
                returned.addObject("messageMissionFermee", missionFermee);
                returned.addObject("messageAuthentification", contact);
                returned.addObject("mailEnvoyeur", messageMail);
                returned.addObject("canEmptyData", canEmptyData);
                return returned;
            }
             */
        }
        return new ModelAndView("redirect");
    }

    /**
     * Gestion de la sauvegarde des paramètres admin
     *
     * @param request La requête http
     * @return La page de configuration après mise à jour
     */
    @RequestMapping(value = "saveConfig.do", method = RequestMethod.POST)
    public ModelAndView handleSaveConfig(HttpServletRequest request) {
        //
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection != null) {
            saveConfig(request);
        }
        return handleConfig(request);
    }

    /**
     * Méthode permettant de gérer l'initialisation du statut de la mission
     * logement (ouverte ou fermée)
     */
    @PostConstruct
    public void initializeStatus() {
        if (missionStatusRepository.count() == 0) {
            missionStatusRepository.save(new MissionLogementStatus(0)); // Default: Avant mission
        }
    }

    /**
     * Renvoi le contenu de la dernière modif du type en paramètre
     *
     * @param TypeModifName Le type de la modif
     * @return "" si aucun champ trouvé
     */
    private String getLast(String TypeModifName) {

        Collection<TypeModif> types = typeRepository.findByNom(TypeModifName);
        if (types.isEmpty()) {
            return "";
        }
        Iterator<TypeModif> iteratorType = types.iterator();
        TypeModif type = iteratorType.next();

        Optional<ConfigModif> optionalConfig = configRepository.findTopByTypeIdOrderByModifIdDesc(type);
        if (optionalConfig.isPresent()) {
            ConfigModif config = optionalConfig.get();
            return config.getContenu();
        }
        return "";
    }

    /**
     * Gestion de la route permettant de supprimer les données de la base de
     * données
     *
     * @param request La requête http
     * @return La confirmation de supression
     */
    @RequestMapping(value = "supprimerDonnees.do", method = RequestMethod.POST)
    public ModelAndView SupprimerDonnes(HttpServletRequest request) {
        Connexion connection = connectionService.checkAccess(request, "Admin");
        //connection = connectionService.checkMissionStatus(connection, 2);
        if (connection != null) {
            if (this.canEmptyData()) {
                viderDataBase(connection);
            }
        }

        return handleConfig(request);
    }
}
