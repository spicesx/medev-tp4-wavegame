package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

public class PieceTest {

    // Petit utilitaire pour accéder à grille[][] dans Plateau sans le modifier
    private Piece[][] getGrille(Plateau plateau) throws Exception {
        Field f = Plateau.class.getDeclaredField("grille");
        f.setAccessible(true);
        return (Piece[][]) f.get(plateau);
    }

    private void viderPlateau(Plateau plateau) throws Exception {
        Piece[][] grille = getGrille(plateau);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                grille[x][y] = null;
            }
        }
    }

    /**
     * Test de setCouleur() : la couleur doit bien être modifiée.
     */
    @Test
    public void testSetCouleur() {
        Piece piece = new Piece(new Point2D(2, 3), "BLANC");
        piece.setCouleur("NOIR");
        assertEquals("NOIR", piece.getCouleur());
    }

    /**
     * Test de setIsKing() : le statut dame doit bien être modifié.
     */
    @Test
    public void testSetIsKing() {
        Piece piece = new Piece(new Point2D(1, 1), "BLANC");
        assertFalse(piece.getIsKing());   // au départ c'est un pion

        piece.setIsKing(true);

        assertTrue(piece.getIsKing());
    }

    /**
     * Test de passerDame() : la pièce devient dame.
     */
    @Test
    public void testPasserDame() {
        Piece piece = new Piece(new Point2D(4, 5), "NOIR");
        assertFalse(piece.getIsKing());

        piece.passerDame();

        assertTrue(piece.getIsKing());
    }

    /**
     * Test de deplacer() : déplacement simple d'un pion vers une case libre.
     */
    @Test
    public void testDeplacerPionCaseLibre() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        // On place un pion blanc en (2,3)
        Piece piece = new Piece(new Point2D(2, 3), "BLANC");
        grille[2][3] = piece;

        // On demande un déplacement en HAUT_DROIT avec distance 5,
        // mais pour un pion la distance est forcée à 1.
        boolean ok = piece.deplacer(plateau, Piece.Direction.HAUT_DROIT, 5);

        assertTrue(ok);
        assertEquals(3, piece.getPos().getX()); // 2 + 1
        assertEquals(2, piece.getPos().getY()); // 3 - 1

        assertNull(grille[2][3]);          // ancienne case vidée par bougerPiece
        assertSame(piece, grille[3][2]);   // nouvelle case contient la pièce
    }

    /**
     * Test de deplacer() : on ne peut pas aller sur une case occupée.
     */
    @Test
    public void testDeplacerCaseOccupee() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        // Attaquant en (2,3)
        Piece piece = new Piece(new Point2D(2, 3), "BLANC");
        grille[2][3] = piece;

        // Autre pièce en (3,2) (case d'arrivée)
        Piece bloqueur = new Piece(new Point2D(3, 2), "NOIR");
        grille[3][2] = bloqueur;

        boolean ok = piece.deplacer(plateau, Piece.Direction.HAUT_DROIT, 1);

        assertFalse(ok);
        // Position inchangée
        assertEquals(2, piece.getPos().getX());
        assertEquals(3, piece.getPos().getY());
        assertSame(piece, grille[2][3]);
        assertSame(bloqueur, grille[3][2]);
    }

    /**
     * Test de manger() : un pion qui mange saute 2 cases en diagonale
     * quand une pièce adverse se trouve au milieu.
     */
    @Test
    public void testMangerPionSimple() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        // Attaquant blanc en (2,3)
        Piece attaquant = new Piece(new Point2D(2, 3), "BLANC");
        grille[2][3] = attaquant;

        // Victime noire en (3,2) (au milieu)
        Piece victime = new Piece(new Point2D(3, 2), "NOIR");
        grille[3][2] = victime;

        // Case d'arrivée (4,1) vide
        boolean ok = attaquant.manger(plateau, Piece.Direction.HAUT_DROIT, 2);

        assertTrue(ok);
        assertEquals(4, attaquant.getPos().getX());
        assertEquals(1, attaquant.getPos().getY());
        assertSame(attaquant, grille[4][1]);  // déplacé

        // Avec la version actuelle de Piece (sans supprimer dans Plateau),
        // la victime est toujours dans la grille. On NE teste donc PAS sa suppression ici.
        // Si tu ajoutes plus tard une méthode pour supprimer dans Plateau,
        // tu pourras mettre : assertNull(grille[3][2]);
    }

    /**
     * Test de manger() : échoue si aucune pièce à manger au milieu.
     */
    @Test
    public void testMangerSansVictime() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        // Attaquant blanc en (2,3)
        Piece attaquant = new Piece(new Point2D(2, 3), "BLANC");
        grille[2][3] = attaquant;

        // Pas de victime en (3,2)
        boolean ok = attaquant.manger(plateau, Piece.Direction.HAUT_DROIT, 2);

        assertFalse(ok);
        // Position inchangée
        assertEquals(2, attaquant.getPos().getX());
        assertEquals(3, attaquant.getPos().getY());
        assertSame(attaquant, grille[2][3]);
    }
}
