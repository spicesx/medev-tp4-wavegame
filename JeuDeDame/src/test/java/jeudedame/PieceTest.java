package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {

    // --- TESTS DE DÉPLACEMENT ---

    @Test
    public void testDeplacementPionSimple() {
        Plateau plateau = new Plateau();
        // Setup : Pion Blanc en (1, 6)
        Piece p = plateau.getPiece(1, 6);
        assertNotNull(p, "La pièce en (1,6) devrait exister");
        assertEquals("BLANC", p.getCouleur());

        // Action : Avancer de 1 case vers la droite (Haut-Droit pour les blancs)
        // (1, 6) -> (2, 5)
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_DROIT, 1);

        // Vérification
        assertTrue(res, "Le déplacement devrait être valide");
        assertEquals(2, p.getPos().getX());
        assertEquals(5, p.getPos().getY());
        assertNull(plateau.getPiece(1, 6), "L'ancienne case doit être vide");
        assertSame(p, plateau.getPiece(2, 5), "La pièce doit être sur la nouvelle case");
    }

    @Test
    public void testDeplacementDame() {
        Plateau plateau = new Plateau();
        // Setup : Pion Blanc en (1, 6) qu'on transforme en Dame
        Piece p = plateau.getPiece(1, 6);
        p.passerDame();
        assertTrue(p.getIsKing());

        // Action : Déplacement de 2 cases (Autorisé pour une Dame)
        // (1, 6) -> (3, 4) en passant par (2, 5) qui est vide
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_DROIT, 2);

        // Vérification
        assertTrue(res, "Une dame devrait pouvoir bouger de 2 cases");
        assertEquals(3, p.getPos().getX());
        assertEquals(4, p.getPos().getY());
    }

    @Test
    public void testDeplacementImpossibleCollision() {
        Plateau plateau = new Plateau();
        // Setup : Pion Noir en (0, 1)
        // Il a un ami Noir en (1, 2) (Diagonale Bas-Droit)
        Piece p = plateau.getPiece(0, 1);
        assertNotNull(p);
        assertEquals("NOIR", p.getCouleur());

        // Action : Essayer d'aller sur la case occupée par l'ami
        boolean res = p.deplacer(plateau, Piece.Direction.BAS_DROIT, 1);

        // Vérification
        assertFalse(res, "Le déplacement sur une case occupée doit échouer");
        assertEquals(0, p.getPos().getX()); // La pièce n'a pas bougé
        assertEquals(1, p.getPos().getY());
    }

    @Test
    public void testDeplacementHorsPlateau() {
        Plateau plateau = new Plateau();
        // Setup : Pion Noir en (1, 0) (Bord haut)
        Piece p = plateau.getPiece(1, 0);

        // Action : Essayer de sortir vers le haut
        boolean res = p.deplacer(plateau, Piece.Direction.HAUT_GAUCHE, 1);

        assertFalse(res, "Le déplacement hors plateau doit échouer");
    }

    // --- TESTS DE PRISE (MANGER) ---

    @Test
    public void testMangerErreurDistancePion() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(1, 6); // Pion Blanc

        // Un pion DOIT sauter de 2 cases pour manger.
        // Essai avec 1 case
        assertThrows(IllegalArgumentException.class, () -> {
            p.manger(plateau, Piece.Direction.HAUT_DROIT, 1);
        });
    }

    @Test
    public void testMangerErreurDistanceDame() {
        Plateau plateau = new Plateau();
        Piece p = plateau.getPiece(1, 6);
        p.passerDame();

        // Une dame DOIT sauter au moins 2 cases pour manger.
        // Essai avec 1 case
        assertThrows(IllegalArgumentException.class, () -> {
            p.manger(plateau, Piece.Direction.HAUT_DROIT, 1);
        });
    }

    @Test
    public void testMangerSansVictime() {
        Plateau plateau = new Plateau();
        // Setup : Pion Blanc en (1, 6)
        Piece p = plateau.getPiece(1, 6);

        // Action : Essayer de manger vers (3, 4)
        // La case intermédiaire est (2, 5). Elle est VIDE au début du jeu.
        // Donc il n'y a rien à manger.
        boolean res = p.manger(plateau, Piece.Direction.HAUT_DROIT, 2);

        // Vérification
        assertFalse(res, "On ne peut pas manger s'il n'y a pas de pièce adverse");
        assertEquals(1, p.getPos().getX(), "La pièce ne doit pas bouger si l'attaque échoue");
    }
    
    @Test
    public void testMangerAmi() {
        Plateau plateau = new Plateau();
        // Setup : Noir en (0, 1). Ami Noir en (1, 2).
        // On imagine un saut vers (2, 3).
        // La case milieu (1, 2) contient un AMI.
        Piece p = plateau.getPiece(0, 1);
        
        // Note: manger cherche la victime sur le chemin.
        // Pour un pion, il regarde le milieu.
        // (0,1) -> (2,3). Milieu = (1,2).
        
        boolean res = p.manger(plateau, Piece.Direction.BAS_DROIT, 2);
        
        assertFalse(res, "On ne peut pas manger ses propres pions");
    }
    
    // --- AUTRES TESTS ---

    @Test
    public void testPasserDame() {
        Piece p = new Piece(new Point2D(0, 0), "BLANC");
        assertFalse(p.getIsKing());
        p.passerDame();
        assertTrue(p.getIsKing());
    }

    @Test
    public void testArgumentsNull() {
        Piece p = new Piece(new Point2D(0,0), "BLANC");
        assertThrows(IllegalArgumentException.class, () -> p.deplacer(null, Piece.Direction.BAS_DROIT, 1));
        assertThrows(IllegalArgumentException.class, () -> p.deplacer(new Plateau(), null, 1));
    }
}
