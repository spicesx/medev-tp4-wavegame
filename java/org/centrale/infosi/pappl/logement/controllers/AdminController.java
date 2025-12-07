/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import org.centrale.infosi.pappl.logement.util.Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import org.centrale.infosi.pappl.logement.items.Alerte;
import org.centrale.infosi.pappl.logement.items.Connexion;
import org.centrale.infosi.pappl.logement.repositories.AlerteRepository;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.items.Genre;
import org.centrale.infosi.pappl.logement.items.Pays;
import org.centrale.infosi.pappl.logement.items.Personne;
import org.centrale.infosi.pappl.logement.items.Role;
import org.centrale.infosi.pappl.logement.repositories.GenreRepository;
import org.centrale.infosi.pappl.logement.repositories.PaysRepository;
import org.centrale.infosi.pappl.logement.repositories.PersonneRepository;
import org.centrale.infosi.pappl.logement.repositories.RoleRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller gérant les actions de l'admin (import, export, gestion des
 * assistants)
 *
 * @author clesp
 *
 */
@Controller
public class AdminController {

    @Lazy
    @Autowired
    private AlerteRepository alerteRepository;

    @Lazy
    @Autowired
    private FormulaireRepository formulaireRepository;

    @Lazy
    @Autowired
    private PersonneRepository personneRepository;

    @Lazy
    @Autowired
    private RoleRepository roleRepository;

    @Lazy
    @Autowired
    private PaysRepository paysRepository;

    @Lazy
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ConnectionService connectionService;

    /**
     * Gestion de la route permettant d'accéder à la page d'accueil de l'admin
     *
     * @return Le ModelAndView lié à la page d'accueil admin
     */
    @RequestMapping(value = "adminDashboard.do", method = RequestMethod.POST)
    public ModelAndView handleAdminGet(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            return new ModelAndView("redirect");
        }
        //Transmission de la liste des dossiers en alerte
        List<Alerte> alertes = new ArrayList<Alerte>(alerteRepository.findAll());
        Collections.sort(alertes, Alerte.getComparator());

        returned = connectionService.prepareModelAndView(connection, "accueil_admin");
        if (returned != null) {
            returned.addObject("Alertes", alertes);
        }
        return returned;
    }

    /**
     * Gestion de la route permettant d'accéder à la liste des dossiers validés
     * par un candidat
     *
     * @return Le ModelAndView lié à la page des dossiers
     */
    @RequestMapping(value = "dossiers.do", method = RequestMethod.POST)
    public ModelAndView handleDossierGet(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            return new ModelAndView("redirect");
        }
        //Ajout de la liste des formulaires
        List<Formulaire> forms = new ArrayList<Formulaire>(formulaireRepository.findAllValidOrCommentaireVE());
        Collections.sort(forms, Formulaire.getComparator());

        returned = connectionService.prepareModelAndView(connection, "pageDossiers");
        if (returned != null) {
            returned.addObject("forms", forms);
        }

        return returned;
    }

    /**
     * Gestion de la route permettant d'accéder à la liste des assistants de la
     * mission logement
     *
     * @return Le ModelAndView lié à la liste des assistants
     */
    @RequestMapping(value = "pageAssistants.do", method = RequestMethod.POST)
    public ModelAndView handlePageAssist(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            return new ModelAndView("redirect");
        }
        //Ajout de la liste des assistants
        Role assist = roleRepository.getReferenceById(3);
        List<Personne> assistants = new ArrayList<Personne>(personneRepository.findByRoleId(assist));
        Collections.sort(assistants, Personne.getComparator());

        returned = connectionService.prepareModelAndView(connection, "pageAssistant");
        //returned=new ModelAndView("pageAssistant");
        if (returned != null) {
            returned.addObject("assistants", assistants);
        }
        return returned;
    }

    /**
     * Gestion de la route permettant d'éditer le profil d'un assistant
     *
     * @param request La requête http
     * @return La page d'édition d'un assistant
     */
    @RequestMapping(value = "editAssistant.do")
    public ModelAndView handlePageAssistant(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            return new ModelAndView("redirect");
        }

        //Edit de la personne
        if (Util.getStringFromRequest(request, "edit") != null) {
            String personId = Util.getStringFromRequest(request, "id");
            int id = Integer.parseInt(personId);
            Optional<Personne> personObjet = personneRepository.findById(id);
            Personne personne = personObjet.orElse(null);
            returned = connectionService.prepareModelAndView(connection, "changementassistant");
            returned.addObject("user", personne);
            return returned;
        }

        //Supression de la personne
        if (Util.getStringFromRequest(request, "delete") != null) {
            String personId = Util.getStringFromRequest(request, "id");
            int id = Integer.parseInt(personId);
            personneRepository.delete(id);
            Role assist = roleRepository.getReferenceById(3);

            List<Personne> assistants = new ArrayList<Personne>(personneRepository.findByRoleId(assist));
            Collections.sort(assistants, Personne.getComparator());

            returned = connectionService.prepareModelAndView(connection, "pageAssistant");
            returned.addObject("assistants", assistants);
            return returned;
        }

        //Affichage de la page des assistants en cas d'erreur
        Role assist = roleRepository.getReferenceById(3);
        List<Personne> assistants = new ArrayList<Personne>(personneRepository.findByRoleId(assist));
        Collections.sort(assistants, Personne.getComparator());

        returned = connectionService.prepareModelAndView(connection, "pageAssistant");
        returned.addObject("assistants", assistants);
        return returned;
    }

    /**
     * Gestion de la route de sauvegarde d'un assistant et le retour au menu
     *
     * @param request La requête http
     * @return La vue du menu des assistants après la sauvegarde
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "saveassistant.do")
    public ModelAndView sauverAssistant(HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView returned;

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            return new ModelAndView("redirect");
        }
        request.setCharacterEncoding("UTF-8");

        //Sauvegarde de la personne
        String idStr = Util.getStringFromRequest(request, "id");
        String firstName = Util.getStringFromRequest(request, "FirstName");
        String lastName = Util.getStringFromRequest(request, "LastName");
        String Login = Util.getStringFromRequest(request, "Login");
        String Password = Util.getStringFromRequest(request, "Password");

        List<Personne> existant = new ArrayList<Personne>(personneRepository.findByLogin(Login));
        Collections.sort(existant, Personne.getComparator());

        int id = Integer.parseInt(idStr);
        if (id > 0) {
            if ((personneRepository.getReferenceById(id).getLogin().equals(Login)) || (existant.isEmpty())) {
                personneRepository.update(id, firstName, lastName, Login, Password);
            }
        }
        if (id < 0) {
            if (existant.isEmpty()) {
                personneRepository.create(firstName, lastName, Login, Password);
            }
        }

        // Return view
        //Ajout de la liste des assistants
        Role assist = roleRepository.getReferenceById(3);
        List<Personne> assistants = new ArrayList<Personne>(personneRepository.findByRoleId(assist));
        Collections.sort(assistants, Personne.getComparator());

        returned = connectionService.prepareModelAndView(connection, "pageAssistant");
        returned.addObject("assistants", assistants);
        return returned;
    }

    /**
     * Gestion de la route de création d'un assistant
     *
     * @param request La requête http
     * @return La page de création d'un assistant
     */
    @RequestMapping(value = "creatuser.do")
    public ModelAndView handlePostCreateassistant(HttpServletRequest request) {
        ModelAndView returned;

        //Check de la connexion 
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            return new ModelAndView("redirect");
        }

        //Envoi de la page
        Personne newPerson = new Personne();
        returned = connectionService.prepareModelAndView(connection, "changementassistant");
        returned.addObject("user", newPerson);

        return returned;
    }

    /**
     * Méthode traitant la route d'export
     *
     * @return Le fichier d'export téléchargé
     */
    @RequestMapping(value = "export.do", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> handleExport(HttpServletRequest request) {

        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection == null) {
            ResponseEntity.unprocessableEntity().body((InputStreamResource) null);
        }

        //Création du fichier d'export
        List<Formulaire> formulairesExport = new ArrayList<Formulaire>(formulaireRepository.findByEstConforme(true));
        Collections.sort(formulairesExport, Formulaire.getComparator());

        String result = Util.export(formulairesExport);

        //Téléchargement du fichier sur le client
        File theFile = new File(result);
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH+mm+ss!"); //Nom du fichier : date de l'export
        String dateStr = date.format(formatter);
        dateStr = dateStr.replace("+", "h");
        dateStr = dateStr.replace("!", "s");
        String fileName = "Export_" + dateStr + ".csv";

        return Util.sendFile(fileName, theFile, MediaType.TEXT_PLAIN);
    }

    /**
     * Gestion de la route récupérant le fichier csv importé et créant les
     * entités dans la bdd
     *
     * @param request La requête http
     * @return Le fichier d'erreur d'import s'il existe, rien sinon
     */
    @RequestMapping(value = "importEleves.do", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> handleImportEleves(HttpServletRequest request) {
        //Check de la connexion
        Connexion connection = connectionService.checkAccess(request, "Admin");
        if (connection != null) {
            ArrayList<String> noms = new ArrayList();
            ArrayList<String> prenoms = new ArrayList();
            ArrayList<String> numsScei = new ArrayList();
            ArrayList<Genre> genres = new ArrayList();
            ArrayList<String> villes = new ArrayList();
            ArrayList<Date> datesNaissance = new ArrayList();
            ArrayList<String> codePostaux = new ArrayList();
            ArrayList<Pays> pays = new ArrayList();
            ArrayList<String> mails = new ArrayList();

            File Fichier = Util.getFileFromRequest(request, "file");
            try {
                importerCSV(Fichier, noms, prenoms, numsScei, genres, villes, datesNaissance, codePostaux, pays, mails);
                for (int k = 0; k < noms.size(); k++) {
                    //on vérifie que l'élève n'existe pas déja en comparant son num scei
                    Collection<Formulaire> listeFormNumScei = formulaireRepository.findByNumeroScei(numsScei.get(k));
                    if (listeFormNumScei.isEmpty()) {
                        Personne eleve = personneRepository.createEleve(noms.get(k), prenoms.get(k));
                        if (eleve != null) {
                            formulaireRepository.createNewForm(
                                    eleve, villes.get(k), mails.get(k), numsScei.get(k),
                                    datesNaissance.get(k), codePostaux.get(k), genres.get(k), pays.get(k));
                            Util.EcritureErreurImport("Creation " + eleve.getNomPrenom());
                        }
                    }
                }
            } catch (Exception e) {
                Util.EcritureErreurImport("Import terminé !");
            }
        } else {
            Util.EcritureErreurImport("Vous ne disposez pas des droits nécessaires !");
        }
        String fileName = Util.EcritureErreurImport("Import terminé !");
        return telechargeFichierErreur(fileName);
    }

    private static final int ENTETE_Genre = 0;
    private static final int ENTETE_Pays = 1;
    private static final int ENTETE_Nom = 2;
    private static final int ENTETE_Prenom = 3;
    private static final int ENTETE_NumeroSCEI = 4;
    private static final int ENTETE_Ville = 5;
    private static final int ENTETE_DateDeNaissance = 6;
    private static final int ENTETE_CodePostal = 7;
    private static final int ENTETE_Mail = 8;
    private static final int ENTETE_Count = 9;

    private Charset detectCharset(File fichier) {
        Charset defaultValue = null;

        String testCharsets[] = {"UTF-8", "ISO-8859-1", "x-MacRoman"};
        for (String testCSstr : testCharsets) {
            try {
                Charset testCS = Charset.forName(testCSstr);
                BufferedReader fichier_source = new BufferedReader(new FileReader(fichier.getAbsolutePath(), testCS));
                String chaine = fichier_source.readLine();
                if (chaine != null) {
                    int index = chaine.toLowerCase().indexOf("pr");
                    char test = chaine.charAt(index + 2);
                    if (test == 'é') {
                        // Found it
                        defaultValue = testCS;
                        break;
                    }
                }
                fichier_source.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (defaultValue == null) {
            defaultValue = Charset.defaultCharset();
        }

        return defaultValue;
    }

    /**
     * lit le fichier csv et rempli les différentes listes
     *
     * @param Fichier Le fichier d'import
     * @param noms Les noms
     * @param prenoms Les prénoms
     * @param numsScei Les numéros SCEI
     * @param genres Les genres
     * @param villes Les villes
     * @param datesNaissance Les dates de naissance
     * @param codePostaux Les codes postaux
     * @param pays Les pays
     * @param mails Les mails
     */
    private void importerCSV(File fichier, ArrayList<String> noms, ArrayList<String> prenoms,
            ArrayList<String> numsScei, ArrayList<Genre> genres, ArrayList<String> villes,
            ArrayList<Date> datesNaissance, ArrayList<String> codePostaux, ArrayList<Pays> pays,
            ArrayList<String> mails) {

        if (fichier != null) {
            //String[] listeNoms = {"Genre", "Pays", "Nom", "Prénom", "Numero SCEI", "Ville", "Date de naissance", "Code Postal", "Mail"};
            List<String> arrayNoms = new ArrayList<String>();
            arrayNoms.add("Genre");
            arrayNoms.add("Pays");
            arrayNoms.add("Nom");
            arrayNoms.add("Prénom");
            arrayNoms.add("Numero SCEI");
            arrayNoms.add("Ville");
            arrayNoms.add("DateDeNaissance");
            arrayNoms.add("CodePostal");
            arrayNoms.add("Mail");

            List<Integer> ordreCol = new ArrayList<Integer>();
            for (String s : arrayNoms) {
                ordreCol.add(-1);
            }

            Util.EcritureErreurImport(fichier.getName() + " : Lecture de l'entête");
            try {
                String separateur = detecterSeparateur(fichier);
                if (separateur == null) {
                    Util.EcritureErreurImport(fichier.getName() + " : le separateur du fichier csv n'est pas reconnu, annulation de l'import");
                    return;
                }

                Charset usedCharset = detectCharset(fichier);

                BufferedReader fichier_source = new BufferedReader(new FileReader(fichier.getAbsolutePath(), usedCharset));
                String chaine;

                //on cherche quelle colonne correspond à quel champ
                if ((chaine = fichier_source.readLine()) != null) {
                    String[] entete = chaine.split(separateur);
                    if (entete[0].startsWith("\uFEFF")) {
                        entete[0] = entete[0].substring(1);
                    }

                    // Check columns
                    for (int indexEntete = 0; indexEntete < entete.length; indexEntete++) {
                        String nomEntete = entete[indexEntete].trim().toLowerCase();
                        if (nomEntete.length() > 1) {
                            nomEntete = nomEntete.substring(0, 1).toUpperCase() + nomEntete.substring(1);
                        }
                        entete[indexEntete] = nomEntete;

                        int indice = -1;
                        String testSTR = nomEntete.toLowerCase();
                        switch (testSTR) {
                            case "genre":
                            case "sexe":
                                indice = ENTETE_Genre;
                                break;
                            case "pays":
                                indice = ENTETE_Pays;
                                break;
                            case "nom":
                                indice = ENTETE_Nom;
                                break;
                            case "prénom":
                            case "prÈnom":
                            case "prenom":
                            case "Pr�nom":
                                indice = ENTETE_Prenom;
                                break;
                            case "numero scei":
                            case "numeroscei":
                            case "scei":
                                indice = ENTETE_NumeroSCEI;
                                break;
                            case "ville":
                                indice = ENTETE_Ville;
                                break;
                            case "date de naissance":
                            case "datedenaissance":
                            case "naissance":
                                indice = ENTETE_DateDeNaissance;
                                break;
                            case "code postal":
                            case "codepostal":
                            case "code":
                                indice = ENTETE_CodePostal;
                                break;
                            case "mail":
                            case "email":
                            case "e-mail":
                                indice = ENTETE_Mail;
                                break;
                        }
                        // Pb de l'accent du prénom
                        if ((indice < 0) && (testSTR.startsWith("pr")) && (testSTR.endsWith("nom"))) {
                            indice = ENTETE_Prenom;
                        }

                        if (indice >= 0) {
                            ordreCol.set(indice, indexEntete);
                            arrayNoms.set(indice, "");
                        }
                    }

                    // Count missing columns
                    String missingColumns = "";
                    int countMissing = 0;
                    for (String s : arrayNoms) {
                        if (!s.isEmpty()) {
                            countMissing++;
                            if (!missingColumns.isEmpty()) {
                                missingColumns += ", ";
                            }
                            missingColumns += s;
                        }
                    }
                    if (countMissing > 0) {
                        // Some columns are missing
                        if (countMissing == 1) {
                            Util.EcritureErreurImport(fichier.getName() + " : la colonne " + missingColumns + " n'a pas ete trouvée, annulation de l'import");
                        } else {
                            Util.EcritureErreurImport(fichier.getName() + " : les colonnes " + missingColumns + " n'ont pas ete trouvées, annulation de l'import");
                        }
                        return;
                    }
                }

                // On lit chaque ligne
                Util.EcritureErreurImport(fichier.getName() + " : Lecture des données");
                int numLigne = 1;
                while ((chaine = fichier_source.readLine()) != null) {
                    numLigne++;
                    String[] ligne = chaine.split(";");
                    int lineSize = ligne.length;

                    // on verifie que les champs non nul dans la bdd, ne le sont pas
                    int[] colonneNnNulle = {0, 1, 2, 3, 4};
                    boolean canGoOn = true;

                    Genre genre = null;
                    Pays paysOrigine = null;
                    Date dateN = null;
                    String code = null;

                    if (canGoOn) {
                        for (int col : colonneNnNulle) {
                            int colIndex = ordreCol.get(col);
                            if ((lineSize <= colIndex) || (ligne[colIndex] == null) || (ligne[colIndex].trim().isEmpty())) {
                                Util.EcritureErreurImport(fichier.getName() + " : ligne " + numLigne + ", la case " + arrayNoms.get(col) + " n'est pas définie, annulation de l'import de cette ligne");
                                canGoOn = false;
                                break;
                            }
                        }
                    }

                    if (canGoOn) {
                        String data;

                        //On verifie si le genre est connu
                        if (lineSize > 0) {
                            data = ligne[ordreCol.get(0)].trim();
                            genre = verifierGenre(data);
                            if (genre == null) {
                                Util.EcritureErreurImport(fichier.getName() + " : ligne " + numLigne + ", le genre (" + data + ") n'est pas connu, annulation de l'import de cette ligne");
                                canGoOn = false;
                            }
                        }

                        //On verifie si le pays est connu
                        if (lineSize > 1) {
                            data = ligne[ordreCol.get(1)].trim();
                            paysOrigine = verifierPays(data);
                            if (paysOrigine == null) {
                                Util.EcritureErreurImport(fichier.getName() + " : ligne " + numLigne + ", le pays (" + data + ") n'est pas connu, annulation de l'import de cette ligne");
                                canGoOn = false;
                            }
                        }

                        //On verifie si la date est connue
                        if (lineSize > 6) {
                            data = ligne[ordreCol.get(6)];
                            if ((data != null) && (!data.trim().isEmpty())) {
                                data = data.trim();
                                dateN = Util.isDate(data);
                                if (dateN == null) {
                                    Util.EcritureErreurImport(fichier.getName() + " : ligne " + numLigne + ", la date (" + data + ") n'est pas lisible, annulation de l'import de cette ligne");
                                    canGoOn = false;
                                }
                            }
                        } else {
                            Util.EcritureErreurImport(fichier.getName() + " : ligne " + numLigne + ", la date n'est pas lisible, annulation de l'import de cette ligne");
                            canGoOn = false;
                        }

                        //On verifie si le code postal est connu
                        if (lineSize > 7) {
                            data = ligne[ordreCol.get(7)].trim();
                            if ((data != null) && (!data.trim().isEmpty())) {
                                data = data.trim();
                                code = verifierCodePostal(paysOrigine, data);
                                if (code == null) {
                                    Util.EcritureErreurImport(fichier.getName() + " : ligne " + numLigne + ", le code Postal (" + data + ") n'est pas lisible, annulation de l'import de cette ligne");
                                    canGoOn = false;
                                }
                            }
                        }

                        if (lineSize <= 8) {
                            Util.EcritureErreurImport(fichier.getName() + " : ligne " + numLigne + ", l'adresse mail n'est pas lisible, annulation de l'import de cette ligne");
                            canGoOn = false;
                        }
                    }

                    if (canGoOn) {
                        genres.add(genre);
                        pays.add(paysOrigine);
                        noms.add((lineSize > 2 ? ligne[ordreCol.get(2)] : ""));
                        prenoms.add((lineSize > 3 ? ligne[ordreCol.get(3)] : ""));
                        numsScei.add((lineSize > 4 ? ligne[ordreCol.get(4)] : ""));
                        villes.add((lineSize > 5 ? ligne[ordreCol.get(5)] : ""));
                        datesNaissance.add(dateN);
                        codePostaux.add(code);
                        mails.add((lineSize > 8 ? ligne[ordreCol.get(8)].trim().toLowerCase() : ""));
                    }
                }
                Util.EcritureErreurImport("Le fichier " + fichier.getName() + " a été lu. " + numLigne + " lignes analysées.");
                fichier_source.close();
            } catch (FileNotFoundException e) {
                Util.EcritureErreurImport("Le fichier " + fichier.getName() + " est introuvable !");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private ResponseEntity<InputStreamResource> telechargeFichierErreur(String fileName) {
        File theFile = new File(fileName);
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH+mm-ss!");
        String dateStr = date.format(formatter);
        dateStr = dateStr.replace("+", "h");
        dateStr = dateStr.replace("!", "s");
        return Util.sendFile("rapport_erreur_" + dateStr + ".csv", theFile, MediaType.TEXT_PLAIN);
    }

    /**
     * lit un fichier csv et renvoi le séparateur de ce fichier (; / tabulation
     * §)
     *
     * @param fichier Le fichier d'import
     * @return Le séparateur du fichier
     */
    private String detecterSeparateur(File fichier) throws FileNotFoundException, IOException {

        String[] separateurs = {null, ";", "\t", ",", "§", "/", "~"};

        BufferedReader fichierCsv = new BufferedReader(new FileReader(fichier.getAbsolutePath()));
        String chaine = fichierCsv.readLine();

        int apparitionMax = 0;
        int indexMax = 0;
        int lgr = chaine.length();
        for (int ind = 1; ind < separateurs.length; ind++) {
            String chaine2 = chaine.replace(separateurs[ind], "");
            int nbr = lgr - chaine2.length();
            if (nbr > apparitionMax) {
                apparitionMax = nbr;
                indexMax = ind;
            }
        }
        fichierCsv.close();
        return separateurs[indexMax];
    }

    /**
     * Recupère un string et renvoi le pays de même nom de la base de donnée
     *
     * @param paysStr Le pays
     * @return null si pas de pays compatible
     */
    private Pays verifierPays(String paysStr) {
        Collection<Pays> myList = paysRepository.findByPaysNom(paysStr);
        if (myList.isEmpty()) {
            return null;
        }
        Iterator<Pays> iterator = myList.iterator();
        return iterator.next();
    }

    /**
     * Recupère un string et renvoi le genre de même nom de la bdd
     *
     * @param genreStr Le genre
     * @return null si pas de genre compatible
     */
    private Genre verifierGenre(String genreStr) {
        Collection<Genre> myList = new ArrayList<>();
        switch (genreStr.toLowerCase()) {
            case "m":
            case "m.":
            case "mr":
            case "masculin":
                myList = genreRepository.findByGenreId(Genre.MASCULIN);
                break;

            case "mme":
            case "mlle":
            case "féminin":
                myList = genreRepository.findByGenreId(Genre.FEMININ);
                break;

            case "-":
                myList = genreRepository.findByGenreId(Genre.NSP);
                break;

            default:
                myList = genreRepository.findByGenreId(Genre.AUTRE);
                break;
        }

        if (myList.isEmpty()) {
            return null;
        }

        return myList.iterator().next();
    }

    /**
     * Récupère un string, le convertit en integer et renvoi null si pas
     * possible
     *
     * @param CodeStr Le code postal
     * @return Le code postal vérifié
     */
    private String verifierCodePostal(Pays pays, String codeStr) {
        if (!pays.getPaysNom().equals("France")) {
            return "00000";
        }

        if (codeStr == null) {
            return null;
        }

        codeStr = codeStr.replace(" ", "");
        codeStr = codeStr.replace("-", "");
        int lg = codeStr.length();

        if (lg > 5 || lg < 4) {
            return null;
        }
        for (int longeurReel = lg; longeurReel < 5; longeurReel++) {
            codeStr = "0" + codeStr;
        }
        return codeStr;
    }
}
