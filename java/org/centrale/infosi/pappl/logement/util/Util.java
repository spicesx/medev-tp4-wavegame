/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.util;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.centrale.infosi.pappl.logement.controllers.StudentController;
import org.centrale.infosi.pappl.logement.items.Formulaire;
import org.centrale.infosi.pappl.logement.repositories.FormulaireRepository;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.text.StringEscapeUtils;
import org.centrale.infosi.pappl.logement.controllers.AdminController;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Une classe permettant d'implementer des méthodes outils statiques
 *
 * @author Quent
 */
public class Util {

    public static final String UTILERREUR = "EmplacementFichierErreur";
    public static final String UTILNOMRAPPORT = "NomRapportException";
    public static final String UTILEXPORT = "EmplacementFichierExport";
    public static final String UTILTEMP = "EmplacementFichierTemp";
    public static final String UTILBOURSE = "EmplacementFichierBourse";

    private static final String CONFIGFILENAME = "config";
    private static final String MULTIPART = "MULTIPART";

    public static String getInfo(String typeFile) {
        String packageClassName = Util.class.getPackage().getName();
        String resourceName = packageClassName + ".config." + CONFIGFILENAME;
        ResourceBundle theResource = ResourceBundle.getBundle(resourceName);

        return theResource.getString(typeFile);
    }

    public static String buildBourseFileName(String scei, String ext) {
        return "bourse_" + scei + "." + ext;
    }

    public static String buildBourseFilePath(String scei, String ext) {
        return Util.getInfo(Util.UTILBOURSE) + "/" + Util.buildBourseFileName(scei, ext);
    }

    public static File tryToBuild(String scei, String ext) {
        File theFile = null;
        String fileName = Util.buildBourseFileName(scei, ext);
        String pathwayFichier = Util.getInfo(Util.UTILBOURSE);
        theFile = new File(pathwayFichier + "/" + fileName);
        return theFile;
    }

    public static File tryToGetBourseFile(String scei) {
        File theFile = tryToBuild(scei, "png");
        if (!theFile.exists()) {
            theFile = tryToBuild(scei, "pdf");
        }
        return theFile;
    }

    /**
     * Ecrit la date et le message d'erreur dans le fichier designe
     *
     * @param erreur le message a ecrire
     * @return
     */
    public static String EcritureErreurImport(String erreur) {
        String pathwayFichier = getInfo(UTILERREUR) + File.separator + getInfo(UTILNOMRAPPORT) + ".txt";

        try {
            FileWriter fileWriter = new FileWriter(pathwayFichier, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            erreur = now.format(formatter) + " - " + erreur;

            writer.write(erreur);
            writer.newLine();

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathwayFichier;
    }

    /**
     * Méthode permettant de convertir un fichier multipart en fichier simple
     *
     * @param multipartFile Le fichier multipart
     * @return Le fichier converti
     * @throws IOException
     */
    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("temp", multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }

        return file;
    }

    private static Date checkDateFormats(String aTime, LinkedList<String> possibleFormats) {
        Date returnedValue = null;
        if (aTime != null) {
            if (!aTime.equals("")) {
                String format = null;
                while ((returnedValue == null) && (!possibleFormats.isEmpty())) {
                    format = possibleFormats.removeFirst();

                    String separator = "-";
                    if (format.contains("/")) {
                        separator = "/";
                    }
                    String f1[] = format.split(separator);
                    String f2[] = aTime.split(separator);

                    boolean canDoIt = true;
                    if ((canDoIt) && (f1.length != f2.length)) {
                        canDoIt = false;
                    }
                    if (canDoIt) {
                        for (int i = 0; i < f1.length; i++) {
                            if (f1[i].length() != f2[i].length()) {
                                canDoIt = false;
                                break;
                            }
                        }
                    }

                    if (canDoIt) {
                        try {
                            // Hour, Minustes and seconds
                            SimpleDateFormat aFormater = new SimpleDateFormat(format);
                            returnedValue = aFormater.parse(aTime);
                        } catch (ParseException ex) {
                        }
                    }
                }

                if (returnedValue != null) {
                    Calendar aCalendar = Calendar.getInstance();
                    aCalendar.setTime(returnedValue);
                    possibleFormats.addFirst(format);
                }
            }
        }

        return returnedValue;
    }

    public static Date isDate(String aDate) {
        Date returnedValue = null;
        if (aDate != null) {
            aDate = aDate.replaceAll(" ", "");
            aDate = aDate.trim();
            if (!aDate.isEmpty()) {
                LinkedList<String> possibleFormats = new LinkedList<String>();
                possibleFormats.add("dd/MM/yyyy");
                possibleFormats.add("dd-MM-yyyy");
                possibleFormats.add("yyyy-MM-dd");
                possibleFormats.add("yyyy-dd-MM");
                possibleFormats.add("ddMMyyyy");

                returnedValue = checkDateFormats(aDate, possibleFormats);
            }
        }
        return returnedValue;
    }

    public static Date getDateFromString(String aDate, String format) {
        Date returnValue = null;
        if (aDate != null) {
            aDate = aDate.replaceAll(" ", "");
            aDate = aDate.trim();
            if (!aDate.isEmpty()) {
                try {
                    SimpleDateFormat aFormater = new SimpleDateFormat(format);
                    returnValue = aFormater.parse(aDate);
                } catch (ParseException e) {
                }
                if (returnValue != null) {
                    Calendar aCalendar = Calendar.getInstance();
                    aCalendar.setTime(returnValue);
                }
            }
        }
        return returnValue;
    }

    public static String getStringFromDate(Date date) {
        return getStringFromDate(date, "dd/MM/yyyy");
    }

    public static String getStringFromDate(Date date, String dateFormat) {
        String dateStr = "";
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            dateStr = formatter.format(date);
        }
        return dateStr;
    }

    private static void writeString(BufferedWriter bufferedWriter, String s) throws IOException {
        if (s != null) {
            s = s.replace('\n', '.');
            s = s.replace("\r", "");
            s = s.replace("\"", "\\\"");
            s = s.replace("Ã¨", "è");
            s = s.replace("Ã©", "é");
            s = StringEscapeUtils.unescapeHtml4(s);
            bufferedWriter.write("\"" + s + "\"" + ";");
        } else {
            bufferedWriter.write(";");
        }
    }

    /**
     * Méthode assurant l'export d'un formulaire dans le fichier csv
     *
     * @param formulaire Un formulaire
     * @param bufferedWriter Le fichier en écriture
     * @throws IOException
     */
    public static void exportFormulaire(Formulaire formulaire, BufferedWriter bufferedWriter) throws IOException {
        writeString(bufferedWriter, formulaire.getPersonneId().getNom());
        writeString(bufferedWriter, formulaire.getPersonneId().getPrenom());

        String s;
        writeString(bufferedWriter, getStringFromDate(formulaire.getDateDeNaissance()));
        writeString(bufferedWriter, formulaire.getNumeroScei());

        if (formulaire.getGenreId() != null) {
            writeString(bufferedWriter, formulaire.getGenreId().getGenreNom());
        } else {
            bufferedWriter.write(";");
        }

        writeString(bufferedWriter, formulaire.getVille());
        writeString(bufferedWriter, formulaire.getCodePostal());
        if (formulaire.getPaysId() != null) {
            writeString(bufferedWriter, formulaire.getPaysId().getPaysNom());
        } else {
            bufferedWriter.write(";");
        }

        writeString(bufferedWriter, formulaire.getMail());
        char dummy = (char) 160;
        writeString(bufferedWriter, dummy + formulaire.getNumeroTel());

        if (formulaire.getSouhaitId() != null) {
            writeString(bufferedWriter, formulaire.getSouhaitId().getSouhaitType());
        } else {
            bufferedWriter.write(";");
        }

        if (formulaire.getEstBoursier() != null) {
            writeString(bufferedWriter, (formulaire.getEstBoursier() ? "Oui" : "Non"));
        } else {
            bufferedWriter.write(";");
        }

        if (formulaire.getEstPmr() != null) {
            writeString(bufferedWriter, (formulaire.getEstPmr() ? "Oui" : "Non"));
        } else {
            bufferedWriter.write(";");
        }

        writeString(bufferedWriter, getStringFromDate(formulaire.getDateValidation()));

        writeString(bufferedWriter, formulaire.getCommentairesEleve());
        writeString(bufferedWriter, formulaire.getCommentairesVe());
        bufferedWriter.newLine();
    }

    /**
     * Méthode permettant d'exporter les formulaires
     *
     * @param formulaires Les formulaires
     * @return Le chemin du fichier
     */
    public static String export(Collection<Formulaire> formulaires) {
        //Création du fichier
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH+mm-ss!");
        String dateStr = date.format(formatter);
        dateStr = dateStr.replace("+", "h");
        dateStr = dateStr.replace("!", "s");

        // Get parameters from config file
        String pathwayFichier = getInfo(Util.UTILEXPORT);
        String fileName = pathwayFichier + File.separator + "/export" + dateStr + ".csv";
        BufferedWriter bufferedWriter;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            //Ecriture dans le fichier
            bufferedWriter.write("\uFEFF");
            //bufferedWriter.newLine();
            //Colonnes
            bufferedWriter.write("Nom;");
            bufferedWriter.write("Prénom;");
            bufferedWriter.write("Date de naissance;");
            bufferedWriter.write("Numéro SCEI;");
            bufferedWriter.write("Genre;");
            bufferedWriter.write("Ville;");
            bufferedWriter.write("Code Postal;");
            bufferedWriter.write("Pays;");
            bufferedWriter.write("Mail;");
            bufferedWriter.write("Numéro de téléphone;");
            bufferedWriter.write("Souhait;");
            bufferedWriter.write("Est boursier;");
            bufferedWriter.write("A besoin de dispositions;");
            bufferedWriter.write("Date de soumission;");
            bufferedWriter.write("Commentaire;");
            bufferedWriter.write("Commentaire Mission logement");
            bufferedWriter.newLine();

            for (Formulaire formulaire : formulaires) {
                //Ajout d'un formulaire
                exportFormulaire(formulaire, bufferedWriter);
            }
            bufferedWriter.close();
            return fileName;
        } catch (FileNotFoundException exc) {
            return null;

        } catch (IOException ex) {
            return null;
        }
    }

    public static int getIntFromString(String value) {
        int intValue = -1;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException exc) {
            Logger.getLogger(StudentController.class.getName()).log(Level.WARNING, null, exc);
        }
        return intValue;
    }

    private static void ensureValidPath(String pathwayFichier) {
        if (pathwayFichier != null) {
            File check = new File(pathwayFichier);
            if (!check.exists()) {
                check.mkdirs();
            }
        }
    }

    /**
     * Get String from request
     *
     * @param request
     * @param value
     * @return
     */
    private static void parseRequest(HttpServletRequest request) {
        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        if ((isMultipart) && (request.getAttribute("MULTIPART") == null)) {
            String pathwayFichier = getInfo(Util.UTILTEMP);
            ensureValidPath(pathwayFichier);
            File directoryFile = new File(pathwayFichier);

            DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
            JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);

            boolean hasFile = false;
            File file = null;
            // File file = temp.getFile();
            try {
                List items = upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (item.isFormField()) {
                        request.setAttribute(item.getFieldName(), item.getString(Charset.forName("UTF-8")));
                    } else if (!item.getName().isEmpty()) {
                        file = new File(directoryFile.getAbsolutePath() + "/" + item.getName());
                        if ((file.exists()) && (file.isFile())) {
                            file.delete();
                        }
                        if (!file.exists()) {
                            file.createNewFile();
                            try (
                                    InputStream uploadedStream = item.getInputStream(); OutputStream out = new FileOutputStream(file);) {
                                IOUtils.copy(uploadedStream, out);
                            } catch (IOException ex) {
                                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!hasFile) {
                                request.setAttribute(MULTIPART, new ArrayList<File>());
                            }
                            ArrayList<File> tempFiles = (ArrayList<File>) (request.getAttribute(MULTIPART));
                            tempFiles.add(file);
                            hasFile = true;
                        }
                    }
                }
            } catch (FileUploadException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static boolean hasRequestParameter(HttpServletRequest request, String value) {
        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            parseRequest(request);
            return (request.getAttribute(value) != null);
        } else {
            // Standart request
            return (request.getParameter(value) != null);
        }
    }

    /**
     * Get String from request
     *
     * @param request
     * @param value
     * @return
     */
    public static File getFileFromRequest(HttpServletRequest request, String value) {
        parseRequest(request);

        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            File file = null;
            if (request.getAttribute("MULTIPART") != null) {
                ArrayList<File> tempFiles = (ArrayList<File>) (request.getAttribute(MULTIPART));
                file = tempFiles.getFirst();
            }
            return file;
        }
        return null;
    }

    private static String genericStringFromRequest(HttpServletRequest request, String value) {
        parseRequest(request);

        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            return (String) (request.getAttribute(value));
        } else {
            // Standart request
            return request.getParameter(value);
        }
    }

    public static String getStringFromRequest(HttpServletRequest request, String value) {
        return genericStringFromRequest(request, value);
    }

    public static String getStringFromRequest(HttpServletRequest request, String value, int maxLength) {
        String res = genericStringFromRequest(request, value);
        return res.substring(0, maxLength);
    }

    /*
    private static void parseRequest(HttpServletRequest request) {
        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            // MULTIPART request
            if (request.getAttribute("MULTIPART") == null) {
                // Not parsed
                HashMap<String, File> tempFiles = new HashMap<>();
                request.setAttribute(MULTIPARTFILES, tempFiles);
                HashMap<String, String> tempFilesName = new HashMap<>();
                request.setAttribute(MULTIPARTFILESNAME, tempFilesName);

                try {
                    DiskFileItemFactory factory = DiskFileItemFactory.builder().setFileCleaningTracker(null).get();
                    ServletContext servletContext = request.getServletContext();
                    File repository = (File) servletContext.getAttribute("jakarta.servlet.context.tempdir");

                    // factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
                    // factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
                    JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);
                    upload.setSizeMax(UPLOADMAX);

                    FileItemInputIterator iterator = upload.getItemIterator(request);
                    System.out.println(iterator);
                    System.out.println(iterator.hasNext());
                    while (iterator.hasNext()) {
                        FileItemInput item = iterator.next();
                        InputStream stream = item.getInputStream();
                        String itemName = item.getFieldName();
                        if (!item.isFormField()) {
                            // File
                            Path tempFile = Files.createTempFile("logement", ".tmp");
                            InputStream is = item.getInputStream();
                            File osFile = tempFile.toFile();
                            OutputStream os = new FileOutputStream(osFile);
                            try {
                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = is.read(buffer)) > 0) {
                                    os.write(buffer, 0, length);
                                }
                            } finally {
                                is.close();
                                os.close();
                            }
                            tempFiles.put(itemName, osFile);
                            tempFilesName.put(itemName, item.getName());
                        }
                    }
                    request.setAttribute(MULTIPARTFILES, tempFiles);
                } catch (FileUploadException ex) {
                    Logger.getLogger(FormulaireController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FormulaireController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
     */
    /**
     * Save Formulaire from request data
     *
     * @param request
     * @param formulaireId
     * @param validation
     * @param formulaireRepository
     */
    public static void enregistrementFormulaire(HttpServletRequest request, int formulaireId, Boolean validation, FormulaireRepository formulaireRepository) {
        //Récupération des attributs
        Formulaire formulaire = formulaireRepository.getReferenceById(formulaireId);

        String nom = Util.getStringFromRequest(request, "nom"); // Obligatoire tous formulaires

        String prenom = Util.getStringFromRequest(request, "prenom"); // Obligatoire tous formulaires

        String dateNaissanceStr = Util.getStringFromRequest(request, "dateDeNaissance"); // Obligatoire tous formulaires
        Date dateNaissance = Util.isDate(dateNaissanceStr);

        String ville = Util.getStringFromRequest(request, "ville"); // Obligatoire tous formulaires

        String codePostal = Util.getStringFromRequest(request, "codePostal"); // Obligatoire tous formulaires

        String paysStr = Util.getStringFromRequest(request, "pays"); // Obligatoire tous formulaires
        int pays = getIntFromString(paysStr);

        String mail = Util.getStringFromRequest(request, "mail"); // Obligatoire tous formulaires

        String commentaireVe = formulaire.getCommentairesVe();
        if (Util.hasRequestParameter(request, "commentairesVe")) {
            commentaireVe = Util.getStringFromRequest(request, "commentairesVe");
            commentaireVe = StringEscapeUtils.escapeHtml4(commentaireVe);
        }

        String commentairesEleve = formulaire.getCommentairesEleve();
        if (Util.hasRequestParameter(request, "commentairesEleve")) {
            commentairesEleve = Util.getStringFromRequest(request, "commentairesEleve");
            commentairesEleve = StringEscapeUtils.escapeHtml4(commentairesEleve);
        } else if (Util.hasRequestParameter(request, "infoSupplementaires")) {
            commentairesEleve = Util.getStringFromRequest(request, "infoSupplementaires");
            commentairesEleve = StringEscapeUtils.escapeHtml4(commentairesEleve);
        }

        int genre = formulaire.getGenreId().getGenreId();
        if (Util.hasRequestParameter(request, "Genre")) {
            String genreStr = Util.getStringFromRequest(request, "Genre");
            genre = getIntFromString(genreStr);
        }

        String numTelephone = formulaire.getNumeroTel();
        if (Util.hasRequestParameter(request, "tel")) {
            numTelephone = Util.getStringFromRequest(request, "tel");
        }

        int souhait = -1;
        if (formulaire.getSouhaitId() != null) {
            souhait = formulaire.getSouhaitId().getSouhaitId();
        }
        if (Util.hasRequestParameter(request, "Souhait")) {
            String souhaitStr = Util.getStringFromRequest(request, "Souhait");
            souhait = getIntFromString(souhaitStr);
        }

        String pmr = "null";
        if (formulaire.getEstPmr() != null) {
            pmr = formulaire.getEstPmr().toString().toLowerCase();
        }
        if (Util.hasRequestParameter(request, "pmr")) {
            pmr = Util.getStringFromRequest(request, "pmr");
        }

        String boursier = "null";
        if (formulaire.getEstBoursier() != null) {
            boursier = formulaire.getEstBoursier().toString().toLowerCase();
        }
        if (Util.hasRequestParameter(request, "bourse")) {
            String bourse = Util.getStringFromRequest(request, "bourse");
            boursier = Boolean.valueOf(Boolean.parseBoolean(bourse)).toString().toLowerCase();
        } else if (Util.hasRequestParameter(request, "boursier")) {
            boursier = Util.getStringFromRequest(request, "boursier");
        }

        formulaireRepository.update(formulaireId,
                nom, prenom, dateNaissance, ville, codePostal, pays, mail, genre, numTelephone,
                boursier, souhait, pmr, commentaireVe, commentairesEleve, validation);
    }

    public static ResponseEntity<InputStreamResource> sendFile(String fileName, File theFile, MediaType mediaType) {
        if ((fileName != null) && (!fileName.isEmpty()) && (theFile != null) && (mediaType != null)) {
            try {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"")
                        .contentType(mediaType)
                        .body(new InputStreamResource(new FileInputStream(theFile)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ResponseEntity.unprocessableEntity().body((InputStreamResource) null);
    }
}
