package jeudedame;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class JeuTest {

    @Test
    public void testInitialisation() {
        Jeu jeu = new Jeu();
        assertNotNull(jeu.getPlateau());
        assertEquals("BLANC", jeu.getJoueurCourant());
    }

    @Test
    public void testChangerTour() {
        Jeu jeu = new Jeu();
        jeu.changerTour();
        assertEquals("NOIR", jeu.getJoueurCourant());
        jeu.changerTour();
        assertEquals("BLANC", jeu.getJoueurCourant());
    }

    @Test
    public void testEstMonTour() {
        Jeu jeu = new Jeu();
        Piece pBlanc = new Piece(new Point2D(0,0), "BLANC");
        Piece pNoir = new Piece(new Point2D(0,0), "NOIR");

        // Tour Blanc
        assertTrue(jeu.estMonTour(pBlanc));
        assertFalse(jeu.estMonTour(pNoir));
        assertFalse(jeu.estMonTour(null));

        // Tour Noir
        jeu.changerTour();
        assertFalse(jeu.estMonTour(pBlanc));
        assertTrue(jeu.estMonTour(pNoir));
    }

    @Test
    public void testSauvegardeChargement() {
        Jeu jeu = new Jeu();
        String filename = "test_save.dat";
        
        // Sauvegarder
        jeu.sauvegarder(filename);
        File f = new File(filename);
        assertTrue(f.exists());

        // Charger
        Jeu charge = Jeu.charger(filename);
        assertNotNull(charge);
        assertEquals("BLANC", charge.getJoueurCourant());

        // Nettoyage
        f.delete();
        
        // Test erreur chargement
        assertNull(Jeu.charger("fichier_inexistant.dat"));
    }
}
