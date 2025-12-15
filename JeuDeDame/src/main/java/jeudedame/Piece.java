package jeudedame;

import java.io.Serializable;

public class Piece implements Serializable {

    private Point2D pos;
    private String couleur;
    private Boolean isKing;

    private static final int TAILLE = 10;

    public enum Direction {
        HAUT_GAUCHE, HAUT_DROIT, BAS_GAUCHE, BAS_DROIT
    }

    public Piece(Point2D pos, String couleur) {
        this.pos = pos;
        this.couleur = couleur;
        this.isKing = false;
    }

    // --- Getters / Setters ---
    public Point2D getPos() { return pos; }
    public String getCouleur() { return couleur; }
    public Boolean getIsKing() { return isKing; }

    public void setPos(Point2D pos) { this.pos = pos; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public void setIsKing(Boolean isKing) { this.isKing = isKing; }

    public void passerDame() { this.isKing = true; }

    // --- Méthodes Utilitaires (Pour éviter la duplication) ---

    /**
     * Retourne le vecteur de direction unitaire (-1 ou 1) pour X et Y.
     */
    private int[] getDirectionStep(Direction direction) {
        return switch (direction) {
            case HAUT_GAUCHE -> new int[]{-1, -1};
            case HAUT_DROIT  -> new int[]{ 1, -1};
            case BAS_GAUCHE  -> new int[]{-1,  1};
            case BAS_DROIT   -> new int[]{ 1,  1};
        };
    }

    /**
     * Vérifie si la case cible est dans le plateau et vide.
     */
    private boolean isCibleValide(Plateau plateau, int x, int y) {
        // Hors limites
        if (x < 0 || x >= TAILLE || y < 0 || y >= TAILLE) {
            return false;
        }
        // Case occupée
        return plateau.getPiece(x, y) == null;
    }

    // -----------------------------------------------------------
    // DEPLACER
    // -----------------------------------------------------------
    public boolean deplacer(Plateau plateau, Direction direction, int distance) {
        if (plateau == null || direction == null) throw new IllegalArgumentException("Plateau ou direction null.");

        // Règles de distance
        if (!Boolean.TRUE.equals(isKing)) {
            distance = 1; // Pion forcé à 1
        } else if (distance <= 0) {
            throw new IllegalArgumentException("La distance doit être positive pour une dame.");
        }

        // Calcul des coordonnées
        int[] step = getDirectionStep(direction); // step[0]=dx, step[1]=dy unitaires
        int x2 = pos.getX() + (step[0] * distance);
        int y2 = pos.getY() + (step[1] * distance);

        // Vérification validité du mouvement
        if (!isCibleValide(plateau, x2, y2)) {
            return false;
        }

        // Action
        plateau.bougerPiece(pos.getX(), pos.getY(), x2, y2);
        return true;
    }

    // -----------------------------------------------------------
    // MANGER
    // -----------------------------------------------------------
    public boolean manger(Plateau plateau, Direction direction, int distance) {
        if (plateau == null || direction == null) throw new IllegalArgumentException("Plateau ou direction null.");

        // Règles de distance spécifiques à la prise
        if (!Boolean.TRUE.equals(isKing)) {
            if (distance != 2) throw new IllegalArgumentException("Un pion doit sauter exactement 2 cases pour manger.");
        } else {
            if (distance < 2) throw new IllegalArgumentException("Une dame doit sauter au moins 2 cases pour manger.");
        }

        // Calcul des coordonnées
        int[] step = getDirectionStep(direction);
        int x1 = pos.getX();
        int y1 = pos.getY();
        int x2 = x1 + (step[0] * distance);
        int y2 = y1 + (step[1] * distance);

        // Vérification validité case arrivée
        if (!isCibleValide(plateau, x2, y2)) {
            return false;
        }

        // Logique de détection de la victime
        Piece victime = null;

        if (!Boolean.TRUE.equals(isKing)) {
            // --- CAS PION ---
            int mx = (x1 + x2) / 2;
            int my = (y1 + y2) / 2;
            victime = plateau.getPiece(mx, my);
        } else {
            // --- CAS DAME ---
            int cx = x1 + step[0];
            int cy = y1 + step[1];

            while (cx != x2 && cy != y2) {
                Piece p = plateau.getPiece(cx, cy);
                if (p != null) {
                    if (victime != null) return false; // Plus d'une pièce sur le chemin
                    victime = p;
                }
                cx += step[0];
                cy += step[1];
            }
        }

        // Vérification de la victime trouvée
        if (victime == null || victime.getCouleur().equals(this.couleur)) {
            return false; // Pas de victime ou tir ami
        }

        // TODO: Ajouter ici la suppression réelle de la victime dans le plateau (ex: plateau.supprimerPiece(victime))
        
        // Action
        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }
}
