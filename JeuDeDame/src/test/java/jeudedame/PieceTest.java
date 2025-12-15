package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {

    // --- TESTS DE DÉPLACEMENT ---

    @Test
    public void testDeplacementPionSimple() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(1, 6); // Pion Blanc
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_DROIT, 1);
        assertTrue(res);
        assertEquals(2, p.getPos().getX());
        assertEquals(5, p.getPos().getY());
    }

    @Test
    public void testDeplacementDame() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(1, 6);
        p.passerDame();
        // (1,6) -> (3,4) (2 cases HAUT_DROIT)
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_DROIT, 2);
        assertTrue(res);
        assertEquals(3, p.getPos().getX());
    }

    @Test
    public void testDeplacementImpossibleCollision() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(0, 1); // Noir
        // (1, 2) est occupé par un ami Noir
        boolean res = p.deplacer(plateau, Piece.Direction.BAS_DROIT, 1);
        assertFalse(res);
    }

    @Test
    public void testDeplacementHorsPlateau() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(1, 0); // Bord haut
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_GAUCHE, 1);
        assertFalse(res);
    }

    // --- TESTS DE PRISE (MANGER) ---

    @Test
    public void testMangerPionSucces() {
        Plateau plateau = new Plateau();
        
        // 1. Préparation du terrain pour avoir une prise possible
        // On bouge un Pion Noir de (0,3) vers (1,4) (case vide)
        Piece noir = plateau.getPiece(0, 3);
        assertNotNull(noir, "Pion noir en (0,3) introuvable");
        assertTrue(noir.deplacer(plateau, Piece.Direction.BAS_DROIT, 1)); // Noir en (1,4)
        
        // On bouge un Pion Blanc de (1,6) vers (2,5)
        Piece blanc = plateau.getPiece(1, 6);
        assertNotNull(blanc, "Pion blanc en (1,6) introuvable");
        assertTrue(blanc.deplacer(plateau, Piece.Direction.HAUT_DROIT, 1)); // Blanc en (2,5)
        
        // 2. Action : Le Blanc (2,5) mange le Noir (1,4) et atterrit en (0,3)
        // La case (0,3) est libre car on a bougé le noir au début.
        boolean res = blanc.manger(plateau, Piece.Direction.HAUT_GAUCHE, 2);
        
        // 3. Vérifications
        assertTrue(res, "Le pion blanc devrait manger le noir");
        assertEquals(0, blanc.getPos().getX()); // Atterrissage
        assertEquals(3, blanc.getPos().getY());
    }

    @Test
    public void testMangerDameSucces() {
        Plateau plateau = new Plateau();
        
        // 1. Setup : Noir en (1,4), Blanc (Dame) en (3,6)
        // Bouger Noir (0,3) -> (1,4)
        Piece noir = plateau.getPiece(0, 3);
        noir.deplacer(plateau, Piece.Direction.BAS_DROIT, 1);
        
        // Prendre un blanc en (3,6) et le faire Dame
        Piece blanc = plateau.getPiece(3, 6); 
        blanc.passerDame();
        
        // 2. Action : Dame Blanc (3,6) mange Noir (1,4) -> Atterrit en (0,3)
        // Distance = 3 cases (3,6 -> 2,5 -> 1,4 -> 0,3)
        boolean res = blanc.manger(plateau, Piece.Direction.HAUT_GAUCHE, 3);
        
        // 3. Vérifications
        assertTrue(res, "La dame devrait manger à distance 3");
        assertEquals(0, blanc.getPos().getX());
        assertEquals(3, blanc.getPos().getY());
    }
    
    @Test
    public void testMangerDameEchecDeuxPieces() {
        Plateau plateau = new Plateau();
        
        // Setup : On veut 2 pièces noires alignées.
        // On a déjà (0,3) et (2,1) alignés sur la diagonale (4,7) -> (0,3) ? Non.
        // Créons une situation simple : Noir en (1,4) et Noir en (0,3).
        
        // 1. Noir1 (2,3) -> (1,4)
        Piece noir1 = plateau.getPiece(2, 3);
        noir1.deplacer(plateau, Piece.Direction.BAS_GAUCHE, 1); // Noir en (1,4)
        
        // 2. Noir2 est déjà en (0,3)
        assertNotNull(plateau.getPiece(0, 3));
        
        // 3. Blanc Dame en (3,6) tente de sauter par dessus les deux
        Piece blanc = plateau.getPiece(3, 6);
        blanc.passerDame();
        
        // Trajet (3,6) -> (2,5)[vide] -> (1,4)[Noir] -> (0,3)[Noir] -> Hop ?
        // On vise une case imaginaire (-1, 2) qui est hors plateau de toute façon, 
        // mais le test d'obstruction doit se déclencher AVANT le test de limite si possible,
        // ou essayons une distance plus courte qui atterrit SUR la 2eme pièce.
        
        // Essayons simplement de manger le Noir en (0,3) depuis (3,6).
        // Le Noir en (1,4) est sur le chemin.
        // La méthode devrait refuser car il y a DEUX pièces (ou atterrissage sur pièce).
        
        boolean res = blanc.manger(plateau, Piece.Direction.HAUT_GAUCHE, 3);
        assertFalse(res, "Interdit de manger 2 pièces ou d'atterrir sur une pièce");
    }

    @Test
    public void testMangerErreurDistance() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(1, 6);
        assertThrows(IllegalArgumentException.class, () -> p.manger(plateau, Piece.Direction.HAUT_DROIT, 1));
        
        p.passerDame();
        assertThrows(IllegalArgumentException.class, () -> p.manger(plateau, Piece.Direction.HAUT_DROIT, 1));
    }

    @Test
    public void testMangerSansVictime() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(1, 6);
        // Case (2,5) vide
        boolean res = p.manger(plateau, Piece.Direction.HAUT_DROIT, 2);
        assertFalse(res);
    }
    
    @Test
    public void testMangerAmi() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(0, 1); // Noir
        // Tente de manger ami en (1,2)
        boolean res = p.manger(plateau, Piece.Direction.BAS_DROIT, 2);
        assertFalse(res);
    }
    
    @Test
    public void testPasserDame() {
        Piece p = new Piece(new Point2D(0, 0), "BLANC");
        p.passerDame();
        assertTrue(p.getIsKing());
    }

    @Test
    public void testArgumentsNull() {
        Piece p = new Piece(new Point2D(0,0), "BLANC");
        assertThrows(IllegalArgumentException.class, () -> p.deplacer(null, Piece.Direction.BAS_DROIT, 1));
        assertThrows(IllegalArgumentException.class, () -> p.deplacer(new Plateau(), null, 1));
        assertThrows(IllegalArgumentException.class, () -> p.manger(null, Piece.Direction.BAS_DROIT, 2));
    }
}
