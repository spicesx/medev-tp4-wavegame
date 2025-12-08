package jeudedame;

import java.io.Serializable;

public class Piece implements Serializable {

    private Point2D pos;
    private String couleur;
    private Boolean isKing;

    // Taille du plateau (même valeur que dans Plateau)
    private static final int TAILLE = 10;

    public enum Direction {
        HAUT_GAUCHE,
        HAUT_DROIT,
        BAS_GAUCHE,
        BAS_DROIT
    }

    public Piece(Point2D pos, String couleur) {
        this.pos = pos;
        this.couleur = couleur;
        this.isKing = false;
    }

    // --- Getters / Setters ---
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

    // -----------------------------------------------------------
    // DEPLACER : ne pas déplacer si la case d'arrivée est occupée
    // -----------------------------------------------------------
    public boolean deplacer(Plateau plateau, Direction direction, int distance) {
        if (plateau == null || direction == null) {
            throw new IllegalArgumentException("Plateau ou direction null.");
        }

        int x1 = pos.getX();
        int y1 = pos.getY();

        // Règles de distance : pion vs dame
        if (!Boolean.TRUE.equals(isKing)) {
            // pion : toujours 1 case, on force
            distance = 1;
        } else {
            // dame : au moins 1 case
            if (distance <= 0) {
                throw new IllegalArgumentException("La distance doit être positive pour une dame.");
            }
        }

        int dx = 0, dy = 0;
        switch (direction) {
            case HAUT_GAUCHE -> { dx = -distance; dy = -distance; }
            case HAUT_DROIT  -> { dx =  distance; dy = -distance; }
            case BAS_GAUCHE  -> { dx = -distance; dy =  distance; }
            case BAS_DROIT   -> { dx =  distance; dy =  distance; }
        }

        int x2 = x1 + dx;
        int y2 = y1 + dy;

        // Vérifier les bords du plateau (on ne touche pas à Plateau)
        if (x2 < 0 || x2 >= TAILLE || y2 < 0 || y2 >= TAILLE) {
            return false;
        }

        // Si il y a déjà un pion ou une dame à l'arrivée → on ne bouge pas
        if (plateau.getPiece(x2, y2) != null) {
            return false;
        }

        // Déplacement autorisé : on utilise bougerPiece du Plateau
        plateau.bougerPiece(x1, y1, x2, y2);
        // bougerPiece met déjà pos à jour via setPos(new Point2D(x2,y2))

        return true;
    }

    // -----------------------------------------------------------
    // MANGER : vérifier s'il y a une pièce à manger
    //         puis déplacer l'attaquant
    //  ⚠️ La suppression réelle de la victime dans la grille
    //     nécessite une méthode de Plateau qui met la case à null.
    // -----------------------------------------------------------
    public boolean manger(Plateau plateau, Direction direction, int distance) {
        if (plateau == null || direction == null) {
            throw new IllegalArgumentException("Plateau ou direction null.");
        }

        int x1 = pos.getX();
        int y1 = pos.getY();

        // Règle pion / dame sur la distance
        if (!Boolean.TRUE.equals(isKing)) {
            // pion : doit sauter exactement 2 cases
            if (distance != 2) {
                throw new IllegalArgumentException("Un pion doit sauter exactement 2 cases pour manger.");
            }
        } else {
            // dame : distance >= 2
            if (distance < 2) {
                throw new IllegalArgumentException("Une dame doit sauter au moins 2 cases pour manger.");
            }
        }

        int dx = 0, dy = 0;
        switch (direction) {
            case HAUT_GAUCHE -> { dx = -distance; dy = -distance; }
            case HAUT_DROIT  -> { dx =  distance; dy = -distance; }
            case BAS_GAUCHE  -> { dx = -distance; dy =  distance; }
            case BAS_DROIT   -> { dx =  distance; dy =  distance; }
        }

        int x2 = x1 + dx;
        int y2 = y1 + dy;

        // Vérifier les bords
        if (x2 < 0 || x2 >= TAILLE || y2 < 0 || y2 >= TAILLE) {
            return false;
        }

        // La case d'arrivée doit être vide
        if (plateau.getPiece(x2, y2) != null) {
            return false;
        }

        // --------- CAS PION : une seule pièce à manger, au milieu ----------
        if (!Boolean.TRUE.equals(isKing)) {
            int mx = (x1 + x2) / 2;
            int my = (y1 + y2) / 2;

            Piece victime = plateau.getPiece(mx, my);
            if (victime == null) {
                return false; // rien à manger
            }
            if (victime.getCouleur().equals(this.couleur)) {
                return false; // même couleur → interdit
            }

            // Ici, on devrait supprimer la victime de la grille,
            // mais Plateau ne propose pas de méthode pour mettre null.

            // On déplace au moins l'attaquant :
            plateau.bougerPiece(x1, y1, x2, y2);
            return true;
        }

        // --------- CAS DAME : trouver une (et une seule) pièce sur la diagonale ----------
        int stepX = dx / Math.abs(dx); // +1 ou -1
        int stepY = dy / Math.abs(dy); // +1 ou -1

        int cx = x1 + stepX;
        int cy = y1 + stepY;

        Piece victime = null;
        int vx = -1, vy = -1;

        while (cx != x2 && cy != y2) {
            Piece p = plateau.getPiece(cx, cy);
            if (p != null) {
                if (victime != null) {
                    // plus d'une pièce sur le chemin → prise illégale
                    return false;
                }
                victime = p;
                vx = cx;
                vy = cy;
            }
            cx += stepX;
            cy += stepY;
        }

        if (victime == null) {
            return false; // aucune pièce à manger
        }
        if (victime.getCouleur().equals(this.couleur)) {
            return false; // pièce de la même couleur
        }

        // Ici aussi, on devrait supprimer la victime (vx, vy) de la grille,
        // mais on ne peut pas sans une méthode dans Plateau.

        // On déplace au moins la dame :
        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }
}
