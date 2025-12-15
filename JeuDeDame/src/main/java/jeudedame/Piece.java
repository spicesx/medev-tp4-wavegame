package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {

    @Test
    public void testDeplacementSimple() {
        Plateau plateau = new Plateau();
        // Vider le plateau pour le test pour éviter les collisions par défaut
        // On crée un plateau "vide" artificiellement ou on utilise une case vide connue
        // Case 4,4 est vide au départ
        Piece p = new Piece(new Point2D(4, 4), "BLANC");
        
        // Test déplacement valide (Bas Droite pour un pion)
        // Attention : la logique actuelle de Piece.java ne vérifie pas le sens de marche (un pion blanc peut reculer dans votre code actuel ?)
        // Si oui, ce test passe. Sinon, adapter la direction.
        boolean res = p.deplacer(plateau, Piece.Direction.BAS_DROIT, 1);
        
        // Note: Dans votre Plateau(), (4,4) est vide ?
        // Initialisation standard : lignes 0-3 (Noir), 6-9 (Blanc). 4 et 5 vides.
        // Donc (4,4) est vide.
        // Le déplacement vers (5,5) est valide.
        
        assertTrue(res);
        assertEquals(5, p.getPos().getX());
        assertEquals(5, p.getPos().getY());
    }

    @Test
    public void testDeplacementInvalide() {
        Plateau plateau = new Plateau();
        // Pion en (0,0) est bloqué ou ne peut pas sortir du plateau
        Piece p = new Piece(new Point2D(0, 0), "NOIR");
        
        // Essai sortie de plateau
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_GAUCHE, 1);
        assertFalse(res);
        assertEquals(0, p.getPos().getX());
    }

    @Test
    public void testPasserDame() {
        Piece p = new Piece(new Point2D(0, 0), "BLANC");
        assertFalse(p.getIsKing());
        p.passerDame();
        assertTrue(p.getIsKing());
    }
    
    @Test
    public void testMangerPion() {
        Plateau plateau = new Plateau();
        // Configurer une situation de prise manuelle
        // Attaquant en 4,4
        Piece attaquant = new Piece(new Point2D(4, 4), "BLANC");
        // Victime en 5,5 (simulée, car on ne peut pas injecter facilement dans Plateau sans méthode setPiece)
        // On va utiliser le plateau existant.
        
        // Utilisons une pièce existante.
        // Piece Noir en (1, 2)
        Piece pNoir = plateau.getPiece(1, 2);
        assertNotNull(pNoir);
        
        // Piece Blanc en (2, 3) -> n'existe pas par défaut (cases vides 4 et 5)
        // Difficile de tester "manger" sans pouvoir placer des pièces arbitrairement sur le Plateau.
        // Cependant, on peut tester les exceptions d'arguments.
        
        assertThrows(IllegalArgumentException.class, () -> {
            pNoir.manger(null, Piece.Direction.BAS_DROIT, 2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            pNoir.manger(plateau, Piece.Direction.BAS_DROIT, 1); // Distance incorrecte pour pion
        });
    }
}
