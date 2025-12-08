/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package jeudedame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author bachirmasroua
 */


public class JoueurTest {
    public static void main(String[] args) {
        System.out.println("--- Démarrage des tests Joueur ---");

        // 1. Test Creation
        Joueur j1 = new Joueur("Alice", false); // Blanc
        Joueur j2 = new Joueur("Bob", true);    // Noir

        if (j1.getNom().equals("Alice") && j1.isNoir() == false) {
            System.out.println("Test 1 (Création Blanc): OK");
        } else {
            System.out.println("Test 1: FAIL");
        }

        if (j2.getNom().equals("Bob") && j2.isNoir() == true) {
            System.out.println("Test 2 (Création Noir): OK");
        } else {
            System.out.println("Test 2: FAIL");
        }

        // 2. Test Display (toString)
        String affichage = j2.toString();
        if (affichage.equals("Bob [Noir]")) {
             System.out.println("Test 3 (Affichage): OK");
        } else {
             System.out.println("Test 3: FAIL -> " + affichage);
        }

        // 3. Test Serializable (Simulation)
        if (j1 instanceof java.io.Serializable) {
            System.out.println("Test 4 (Prêt pour sauvegarde): OK");
        } else {
            System.out.println("Test 4: FAIL");
        }
        
        System.out.println("--- Fin des tests ---");
    }
}