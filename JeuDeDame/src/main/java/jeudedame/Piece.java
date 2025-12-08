/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jeudedame;

/**
 *
 * @author lenovo
 */
public class Piece {
    private Point2D pos;
    private String couleur;
    private Boolean isKing;
    
    public Piece(Point2D pos,String couleur){
        this.pos = pos;
        this.couleur= couleur;
        isKing=false;
    }
    public enum Direction {
    HAUT_GAUCHE,
    HAUT_DROIT,
    BAS_GAUCHE,
    BAS_DROIT
}


    public Point2D getPos() {
        return pos;
    }

    public String getCouleur() {
        return couleur;
    }

    public Boolean getIsKing() {
        return isKing;
    }

    public void setPos(Point2D pos) {
        this.pos = pos;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public void setIsKing(Boolean isKing) {
        this.isKing = isKing;
    }
    
    public void passerDame() {
        this.isKing = true;
    }
public void deplacer(Direction direction, int distance) {

    // ---- CAS DU PION ----
    if (!isKing) {
        if (distance != 1) {
            distance =1;
        }
    }

    // ---- CAS DE LA DAME ----
    else {
        if (distance <= 0) {
            throw new IllegalArgumentException(
                "La distance doit être positive."
            );
        }    }

    // Mouvement final (exécuté pour les deux)
    int x = pos.getX();
    int y = pos.getY();

    switch (direction) {
        case HAUT_GAUCHE -> pos = new Point2D(x - distance, y - distance);
        case HAUT_DROIT  -> pos = new Point2D(x + distance, y - distance);
        case BAS_GAUCHE  -> pos = new Point2D(x - distance, y + distance);
        case BAS_DROIT   -> pos = new Point2D(x + distance, y + distance);
    }
} 
public void manger(Direction direction, int distance) {

    // ----- RÈGLE POUR LE PION -----
    if (!isKing) {
        if (distance != 2) {
            throw new IllegalArgumentException(
                "Un pion doit sauter exactement 2 cases pour manger."
            );
        }
    }

    // ----- RÈGLE POUR LA DAME -----
    else {
        if (distance < 2) {
            throw new IllegalArgumentException(
                "Une dame doit sauter au moins 2 cases pour manger."
            );
        }
        // Une dame peut sauter 2, 3, 4... cases.
    }

    // ----- DÉPLACEMENT FINAL -----
    int x = pos.getX();
    int y = pos.getY();

    switch (direction) {
        case HAUT_GAUCHE -> pos = new Point2D(x - distance, y - distance);
        case HAUT_DROIT  -> pos = new Point2D(x + distance, y - distance);
        case BAS_GAUCHE  -> pos = new Point2D(x - distance, y + distance);
        case BAS_DROIT   -> pos = new Point2D(x + distance, y + distance);
    }
}

}
