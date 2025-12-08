package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {
    
    public PieceTest() {
    }

    /**
     * Test de setCouleur() : la couleur doit bien être modifiée.
     */
    @Test
    public void testSetCouleur() {
        System.out.println("setCouleur");
        
        // Arrange
        Piece piece = new Piece(new Point2D(2, 3), "blanc");

        // Act
        piece.setCouleur("noir");

        // Assert
        assertEquals("noir", piece.getCouleur());
    }

    /**
     * Test de setIsKing() : le statut dame doit bien être modifié.
     */
    @Test
    public void testSetIsKing() {
        System.out.println("setIsKing");
        
        // Arrange
        Piece piece = new Piece(new Point2D(1, 1), "blanc");
        assertFalse(piece.getIsKing());   // au départ c'est un pion

        // Act
        piece.setIsKing(true);

        // Assert
        assertTrue(piece.getIsKing());
    }

    /**
     * Test de passerDame() : la pièce devient dame.
     */
    @Test
    public void testPasserDame() {
        System.out.println("passerDame");

        // Arrange
        Piece piece = new Piece(new Point2D(4, 5), "noir");
        assertFalse(piece.getIsKing());

        // Act
        piece.passerDame();

        // Assert
        assertTrue(piece.getIsKing());
    }

    /**
     * Test de deplacer() : déplacement simple d'un pion.
     */
    @Test
    public void testDeplacer() {
        System.out.println("deplacer");

        // Arrange
        Piece piece = new Piece(new Point2D(2, 3), "blanc");
        
        // Act : on le déplace en haut à droite d'une case
        piece.deplacer(Piece.Direction.HAUT_DROIT, 1);

        // Assert
        assertEquals(3, piece.getPos().getX()); // 2 + 1
        assertEquals(2, piece.getPos().getY()); // 3 - 1
    }

    /**
     * Test de manger() : un pion qui mange saute 2 cases en diagonale.
     */
    @Test
    public void testManger() {
        System.out.println("manger");

        // Arrange
        Piece piece = new Piece(new Point2D(2, 3), "blanc");

        // Act : il mange en haut à droite en sautant 2 cases
        piece.manger(Piece.Direction.HAUT_DROIT, 2);

        // Assert
        assertEquals(4, piece.getPos().getX()); // 2 + 2
        assertEquals(1, piece.getPos().getY()); // 3 - 2
    }
}
