package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JoueurTest {

    @Test
    public void testJoueur() {
        Joueur j1 = new Joueur("Alice", false);
        assertEquals("Alice", j1.getNom());
        assertFalse(j1.isNoir());
        assertTrue(j1.toString().contains("Alice"));
        assertTrue(j1.toString().contains("Blanc"));

        Joueur j2 = new Joueur("Bob", true);
        assertEquals("Bob", j2.getNom());
        assertTrue(j2.isNoir());
        assertTrue(j2.toString().contains("Noir"));
    }
}
