package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {

    @Test
    public void testDeplacementSimple() {
        Plateau plateau = new Plateau();
        
        // RÉCUPÉRATION D'UNE VRAIE PIÈCE DU PLATEAU
        // Les Blancs sont en bas (y=6 à 9). Prenons un pion en (1, 6).
        // Il doit aller vers le haut (y diminue).
        // Attention : Vérifiez bien le sens de vos pièces.
        // Si (1,6) est BLANC, il peut aller en (0,5) ou (2,5).
        
        Piece p = plateau.getPiece(1, 6); 
        assertNotNull(p, "Il devrait y avoir une pièce en (1,6)");
        assertEquals("BLANC", p.getCouleur());

        // On le déplace vers HAUT_DROIT (x+1, y-1) => vers (2, 5)
        // Note: Dans votre code Piece.java:
        // HAUT_DROIT -> dx=1, dy=-1.
        // Donc (1, 6) + (1, -1) = (2, 5).
        
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_DROIT, 1);
        
        assertTrue(res, "Le déplacement devrait être valide");
        
        // Vérification que la pièce a bien bougé
        assertEquals(2, p.getPos().getX());
        assertEquals(5, p.getPos().getY());
        
        // Vérification que le plateau est à jour
        assertNull(plateau.getPiece(1, 6)); // L'ancienne case est vide
        assertSame(p, plateau.getPiece(2, 5)); // La nouvelle case contient la pièce
    }

    @Test
    public void testDeplacementInvalide() {
        Plateau plateau = new Plateau();
        // Un pion Noir en (0, 1) est bloqué devant par un pion Noir en (1, 2) ?
        // Non, essayons de sortir du plateau.
        // Pion Noir en (0,0) (Coin haut gauche).
        Piece p = plateau.getPiece(1, 0); // Case noire en 1,0
        assertNotNull(p);
        
        // Essai de sortir vers le haut (y négatif)
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_GAUCHE, 1);
        assertFalse(res, "Ne doit pas sortir du plateau");
        assertEquals(1, p.getPos().getX()); // N'a pas bougé
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
        // Test manuel difficile sans setup complexe du plateau via setPiece (inexistant).
        // On teste au moins les exceptions d'arguments.
        
        Plateau plateau = new Plateau();
        Piece p = new Piece(new Point2D(0,0), "NOIR");
        
        assertThrows(IllegalArgumentException.class, () -> {
            p.manger(null, Piece.Direction.BAS_DROIT, 2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            p.manger(plateau, Piece.Direction.BAS_DROIT, 1); // Distance incorrecte pour un pion (doit être 2)
        });
    }
}
