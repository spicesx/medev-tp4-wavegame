package jeudedame;

import java.io.*;

public class Jeu implements Serializable {
    
    private Plateau plateau;
    private boolean tourAuNoir;

    public Jeu() {
        this.plateau = new Plateau();
        this.tourAuNoir = false;
    }
    
    public Plateau getPlateau() { return plateau; }
    
    public String getJoueurCourant() {
        return tourAuNoir ? "NOIR" : "BLANC";
    }

    public void changerTour() {
        tourAuNoir = !tourAuNoir;
    }
    
    public boolean estMonTour(Piece p) {
        if (p == null) return false;
        String couleurPiece = p.getCouleur();
        if (tourAuNoir && couleurPiece.equals("NOIR")) return true;
        if (!tourAuNoir && couleurPiece.equals("BLANC")) return true;
        return false;
    }

    public void sauvegarder(String nomFichier) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            out.writeObject(this);
            System.out.println(">> Partie sauvegardée dans : " + nomFichier);
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    public static Jeu charger(String nomFichier) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomFichier))) {
            return (Jeu) in.readObject();
        } catch (Exception e) {
            System.out.println("Erreur chargement : " + e.getMessage());
            return null;
        }
    }
}