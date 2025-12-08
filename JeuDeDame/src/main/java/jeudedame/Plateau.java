/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jeudedame;

/**
 *
 * @author michel
 */
import java.io.Serializable;

public class Plateau implements Serializable {
    private Piece[][] grille;
    private final int TAILLE = 10;

    public Plateau() {
        grille = new Piece[TAILLE][TAILLE];
        initialiser();
    }

    private void initialiser() {
        for (int y = 0; y < TAILLE; y++) {
            for (int x = 0; x < TAILLE; x++) {
                if ((x + y) % 2 != 0) {
                    if (y < 4) {
                        grille[x][y] = new Piece(new Point2D(x, y), "NOIR");
                    } else if (y > 5) {
                        grille[x][y] = new Piece(new Point2D(x, y), "BLANC");
                    }
                }
            }
        }
    }

    public Piece getPiece(int x, int y) {
        if (x >= 0 && x < TAILLE && y >= 0 && y < TAILLE) {
            return grille[x][y];
        }
        return null;
    }

    public void afficher() {
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int y = 0; y < TAILLE; y++) {
            System.out.print(y + " ");
            for (int x = 0; x < TAILLE; x++) {
                Piece p = grille[x][y];
                if (p == null) {
                    System.out.print(((x + y) % 2 == 0) ? ". " : "_ ");
                } else {
                    String sym = p.getCouleur().equals("BLANC") ? "O " : "X ";
                    if (p.getIsKing()) sym = p.getCouleur().equals("BLANC") ? "# " : "@ ";
                    System.out.print(sym);
                }
            }
            System.out.println();
        }
    }
    
    public void bougerPiece(int x1, int y1, int x2, int y2) {
        Piece p = grille[x1][y1];
        if (p != null) {
            p.setPos(new Point2D(x2, y2));
            grille[x2][y2] = p;
            grille[x1][y1] = null;
        }
    }
}
