/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.infosi.pappl.logement.util;

/**
 *
 * @author samer
 */
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe d'utilitaires liés à la gestion des mots de passe (hachage)
 * @author clesp
 * 
 */
public class PasswordUtils {
    
    //On utilise BCrypt de SpringSecurity
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(15);


    /**
     * Méthode permettant d'hasher un mot de passe
     * @param plainPassword Le mot de passe à hasher
     * @return Le mot de passe hasher
     */
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }


    /**
     * Vérifie si un mot de passe est égal à un mot de passe hashé
     * @param plainPassword Le mot de passe en clair à vérifier
     * @param hashedPassword Le mot de passe hashé à comparer
     * @return Si les mots de passe sont égaux
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
    
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Méthode permettant de générer un token aléatoire
     * @return Un token généré aléatoirement
     */
    public static String generateToken() {
        byte[] randomBytes = new byte[32]; // 32 bytes for a 256-bit token
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes); // URL-safe base64 encoding
    }
}

